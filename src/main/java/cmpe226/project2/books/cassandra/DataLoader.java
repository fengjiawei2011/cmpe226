package cmpe226.project2.books.cassandra;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.UUID;

import cmpe226.project2.util.BookParser;

import com.datastax.driver.core.BatchStatement;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.UDTValue;
import com.datastax.driver.core.UserType;
import com.datastax.driver.core.exceptions.InvalidQueryException;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.utils.UUIDs;

/*
 * Create schema
 * Load data from file to db
 * Download file from db
 * 
 */
public class DataLoader {

	private String keyspace = "cmpe226";
	private String bookTable = "books";
	private String userTable = "users";
	private String commentType = "comment";
	private String readType = "read";

	private Session session;

	public DataLoader(Session session) {
		this.session = session;
	}

	// create keyspace and table
	public void init() {
		String createKeyspace = String
				.format("CREATE KEYSPACE IF NOT EXISTS %s WITH replication = {'class':'SimpleStrategy', 'replication_factor':1};",
						keyspace);

		// create comment type
		String createCommentType = String
				.format("CREATE TYPE IF NOT EXISTS %s.%s (user_id uuid, comment text, post_date timestamp);",
						keyspace, commentType);

		// create read type
		String createReadType = String
				.format("CREATE TYPE IF NOT EXISTS %s.%s (book_id uuid, rating double, rate_date timestamp);",
						keyspace, readType);

		// create book table with basic information
		String createBookTable = String
				.format("CREATE TABLE IF NOT EXISTS %s.%s (id uuid PRIMARY KEY, title text, author text, first_author text, release_date text, avg_rate double, count_rate int, comments set<frozen<%s>>, content blob);",
						keyspace, bookTable, commentType);

		String createUserTable = String
				.format("CREATE TABLE IF NOT EXISTS %s.%s (id uuid PRIMARY KEY, name text, read_books set<frozen<%s>>, write_books set<uuid>);",
						keyspace, userTable, readType);
		
		String createUserNameIndex = String
				.format("CREATE INDEX IF NOT EXISTS user_name ON %s.%s (name);", keyspace, userTable);

		session.execute(createKeyspace);
		session.execute(createCommentType);
		session.execute(createReadType);
		session.execute(createBookTable);
		session.execute(createUserTable);
		session.execute(createUserNameIndex);
	}

	public void dropKeyspace() {
		session.execute("DROP KEYSPACE " + keyspace);
	}

	private void addColumn(String table, String column, String type) {
		try {
			String addCol = String.format("ALTER TABLE %s.%s ADD %s %s",
					keyspace, table, column, type);
			session.execute(addCol);
			System.out.println("[Add Column] " + column);
		} catch (InvalidQueryException e) {
			// ignore exception if the column already exists
			return;
		}
	}

	public Statement updateOrInsertUser(String name, UUID book_id) {
		Row user = session.execute(
				QueryBuilder.select("id").from(keyspace, userTable)
						.where(QueryBuilder.eq("name", name)).limit(1)).one();
		Statement statement;

		if (user != null) {
			// update user write_books
			statement = QueryBuilder.update(keyspace, userTable)
					.with(QueryBuilder.add("write_books", book_id))
					.where(QueryBuilder.eq("id", user.getUUID("id")));
		} else {
			// insert user
			HashSet<UUID> set = new HashSet<UUID>();
			set.add(book_id);
			statement = QueryBuilder.insertInto(keyspace, userTable)
					.value("id", UUIDs.random()).value("name", name)
					.value("write_books", set);
		}

		return statement;
	}

	public void insert(String file_path) throws IOException {
		HashMap<String, String> data = new BookParser(file_path).processLineByLine();
		
		UUID book_id = UUIDs.random();
		
		Insert insertBook = QueryBuilder.insertInto(keyspace, bookTable)
				.value("id", book_id)
				.value("content", ByteBuffer.wrap(Files.readAllBytes(Paths.get(file_path))));
		for (Entry<String, String> item : data.entrySet()) {
			insertBook = insertBook.value(item.getKey(), item.getValue());
		}
		
		String name = data.get("first_author");
		Statement userUpdate = updateOrInsertUser(name, book_id);

		BatchStatement bs = new BatchStatement(BatchStatement.Type.UNLOGGED)
				.add(insertBook).add(userUpdate);

		try {
			session.execute(bs);
		} catch (Exception e) {
			for (String k : data.keySet()) {
				addColumn(bookTable, k, "text");
			}
			session.execute(bs);
		}
	}
	
	public void readBook(UUID id) throws IOException {
		Statement readFile = QueryBuilder.select("content", "title").from(keyspace, bookTable).where(QueryBuilder.eq("id", id));
		Row file = session.execute(readFile).one();

		if (file != null) {
			ByteBuffer fileBytes = file.getBytes( "content" );
			Files.write(Paths.get("download.txt"), fileBytes.array());

			System.out.println("[Download Book] " + file.getString("title"));
		} else {

			System.out.println("[Book NOT Found]");
		}
	}

	// load book data
	public void loadData(String bookDir) throws IOException {
		String[] books = new File(bookDir).list(); // { "1010.txt" }; 
		int count = 0;

		for (String book : books) {
			insert(bookDir + book);
			count++;
		}
		System.out.printf("%d books loaded.", count);
	}	
	
	// insert comments
	public void insertComment(UUID book_id, UUID user_id, String comment) throws InterruptedException {
		UserType commentT = session.getCluster().getMetadata().getKeyspace(keyspace).getUserType(commentType);
		System.out.println(commentT.getFieldNames());
		
		UDTValue c = commentT.newValue()
				.setUUID("user_id", user_id)
				.setString("comment", comment)
				.setDate("post_date", new Date());
		
		Statement statement = QueryBuilder.update(keyspace, bookTable)
				.with(QueryBuilder.add("comments", c))
				.where(QueryBuilder.eq("id", book_id));
		
		session.execute(statement);
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		CassandraClient client = new CassandraClient("localhost");
		
		try {
			DataLoader loader = new DataLoader(client.getSession());
			
//			// init schema
//			loader.dropKeyspace();
//			loader.init();
//			
//			// load all files under folder
//			loader.loadData(System.getProperty("user.dir") + "/books/");
			
			// add comment
			loader.insertComment(UUID.fromString("abd17db3-bc4f-4c5e-a096-3968d64a1bc7"), 
					UUID.fromString("415394aa-2e1f-4d75-a7d1-ed5202b51e9d"), "comment test");
			
//			loader.readBook(UUID.fromString("372d532e-20f2-456c-ab1d-217b8ead54c6"));
			
		} finally {
			client.close();
		}
	}

}
