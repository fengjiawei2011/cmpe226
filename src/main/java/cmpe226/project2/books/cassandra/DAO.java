package cmpe226.project2.books.cassandra;

import cmpe226.project2.util.BookParser;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.UUID;

import com.datastax.driver.core.BatchStatement;
import com.datastax.driver.core.KeyspaceMetadata;
import com.datastax.driver.core.ResultSet;
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
 * Create schema:				new DAO(seesion)
 * Recreate shcema: 			recreateKeyspace()
 * Load data from file to db: 	loadData(folder_path)
 * Download file from db: 		getBook(UUID id)
 * Add user:					insertUser(String name)
 * Add comment: 				insertComment(UUID book_id, UUID user_id, String comment)
 * Add rating: 					insertRate(UUID book_id, UUID user_id, Double rating)
 */
public class DAO {

	private final static String keyspace = "cmpe226";
	private final static String bookTable = "books";
	private final static String userTable = "users";
	private final static String commentType = "comment";
	private final static String rateType = "rate";
	private UserType commentT;
	private UserType rateT;

	private Session session;

	public DAO(Session session) {
		this.session = session;
		init();
	}

	public void recreateKeyspace() {
		session.execute("DROP KEYSPACE " + keyspace);
		init();
	}
	
	public void dropDB(){
		session.execute("DROP KEYSPACE " + keyspace);
	}

	// create keyspace and table
	private void init() {
		String createKeyspace = String
				.format("CREATE KEYSPACE IF NOT EXISTS %s WITH replication = {'class':'SimpleStrategy', 'replication_factor':1};",
						keyspace);

		// create comment type
		String createCommentType = String
				.format("CREATE TYPE IF NOT EXISTS %s.%s (user_id uuid, comment text, post_date timestamp);",
						keyspace, commentType);

		// create rate type
		String createRateType = String
				.format("CREATE TYPE IF NOT EXISTS %s.%s (book_id uuid, rating double, rate_date timestamp);",
						keyspace, rateType);

		// create book table with basic information
		String createBookTable = String
				.format("CREATE TABLE IF NOT EXISTS %s.%s (id uuid PRIMARY KEY, title text, author text, first_author text, release_date text, language text, avg_rate double, count_rate int, comments set<frozen<%s>>, content blob);",
						keyspace, bookTable, commentType);

		String createUserTable = String
				.format("CREATE TABLE IF NOT EXISTS %s.%s (id uuid PRIMARY KEY, name text, read_books set<frozen<%s>>, write_books set<uuid>);",
						keyspace, userTable, rateType);

		String createUserNameIndex = String.format(
				"CREATE INDEX IF NOT EXISTS user_name ON %s.%s (name);",
				keyspace, userTable);

		String createTitleIndex = String.format(
				"CREATE INDEX IF NOT EXISTS title ON %s.%s (title);", keyspace,
				bookTable);

		String createAuthorIndex = String.format(
				"CREATE INDEX IF NOT EXISTS first_author ON %s.%s (first_author);",
				keyspace, bookTable);

		String createLanguageIndex = String.format(
				"CREATE INDEX IF NOT EXISTS language ON %s.%s (language);",
				keyspace, bookTable);

		session.execute(createKeyspace);
		session.execute(createCommentType);
		session.execute(createRateType);
		session.execute(createBookTable);
		session.execute(createUserTable);
		session.execute(createUserNameIndex);
		session.execute(createTitleIndex);
		session.execute(createAuthorIndex);
		session.execute(createLanguageIndex);

		KeyspaceMetadata km = session.getCluster().getMetadata()
				.getKeyspace(keyspace);
		commentT = km.getUserType(commentType);
		rateT = km.getUserType(rateType);
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

	public void addIndex(String table, String column) {
		try {
			String addIndex = String.format(
					"CREATE INDEX IF NOT EXISTS %s ON %s.%s (%s);", column,
					keyspace, table, column);
			session.execute(addIndex);
			System.out.println("[Add Index] " + column);
		} catch (InvalidQueryException e) {
			// ignore exception if the column already exists
			return;
		}
	}

	private Statement updateOrInsertAuthor(String name, UUID book_id) {
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

	private void insertBook(String file_path) throws IOException {
		HashMap<String, String> data = new BookParser(file_path)
				.processLineByLine();

		UUID book_id = UUIDs.random();

		Insert insertBook = QueryBuilder
				.insertInto(keyspace, bookTable)
				.value("id", book_id)
				.value("content",
						ByteBuffer.wrap(Files.readAllBytes(Paths.get(file_path))));
		for (Entry<String, String> item : data.entrySet()) {
			insertBook = insertBook.value(item.getKey(), item.getValue());
		}

		String name = data.get("first_author");
		Statement userUpdate = updateOrInsertAuthor(name, book_id);

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

	public void getBook(UUID id) throws IOException {
		Statement readFile = QueryBuilder.select("content", "title")
				.from(keyspace, bookTable).where(QueryBuilder.eq("id", id));
		Row file = session.execute(readFile).one();

		if (file != null) {
			ByteBuffer fileBytes = file.getBytes("content");
			Files.write(Paths.get("download.txt"), fileBytes.array());

			System.out.println("[Download Book] " + file.getString("title"));
		} else {

			System.out.println("[Book NOT Found]");
		}
	}

	// load book data
	public int loadData(String bookDir) throws IOException {
		String[] books = new File(bookDir).list(); // { "1010.txt" };
		int count = 0;

		for (String book : books) {
			insertBook(bookDir + book);
			count++;
		}
		return count;
	}

	// add user
	public void insertUser(String name) {
		Row user = session.execute(
				QueryBuilder.select("id").from(keyspace, userTable)
						.where(QueryBuilder.eq("name", name)).limit(1)).one();

		if (user == null) {
			// insert user
			Statement statement = QueryBuilder.insertInto(keyspace, userTable)
					.value("id", UUIDs.random()).value("name", name);
			session.execute(statement);

		} else {
			System.out.println("User Already Exists");
		}
	}

	// insert comments
	public void insertComment(UUID book_id, UUID user_id, String comment)
			throws InterruptedException {
		UDTValue c = commentT.newValue().setUUID("user_id", user_id)
				.setString("comment", comment).setDate("post_date", new Date());

		Statement statement = QueryBuilder.update(keyspace, bookTable)
				.with(QueryBuilder.add("comments", c))
				.where(QueryBuilder.eq("id", book_id));

		session.execute(statement);
		System.out.println("[Comment added]");
	}

	// insert book rating
	public void insertRate(UUID book_id, UUID user_id, Double rating)
			throws InterruptedException {
		// update book rating
		Statement getRate = QueryBuilder.select("avg_rate", "count_rate")
				.from(keyspace, bookTable)
				.where(QueryBuilder.eq("id", book_id));
		Row file = session.execute(getRate).one();

		if (file != null) {
			Double rate = file.getDouble("avg_rate");
			int count = file.getInt("count_rate");

			rate = (rate * count + rating) / (++count);
			Statement updateRate = QueryBuilder.update(keyspace, bookTable)
					.with(QueryBuilder.set("count_rate", count))
					.and(QueryBuilder.set("avg_rate", rate))
					.where(QueryBuilder.eq("id", book_id));

			UDTValue r = rateT.newValue().setUUID("book_id", book_id)
					.setDouble("rating", rating)
					.setDate("rate_date", new Date());

			Statement addComment = QueryBuilder.update(keyspace, userTable)
					.with(QueryBuilder.add("read_books", r))
					.where(QueryBuilder.eq("id", user_id));

			BatchStatement bs = new BatchStatement(BatchStatement.Type.UNLOGGED)
					.add(addComment).add(updateRate);

			session.execute(bs);
			System.out.println("[Rating added]");

		} else {

			System.out.println("[Book NOT Found]");
		}
	}

	/**
	 * query1: search by title , get all data -- 'Peter Pan in Kensington
	 * Gardens'
	 */
	public ResultSet searchByTitle(String title) {
		ResultSet results = session
				.execute("select * from cmpe226.books where title='" + title
						+ "';");

//		System.out
//				.println(String
//						.format("%-25s\t%-30s\t%-20s\n%s",
//								"author",
//								"title",
//								"release_date",
//								"-------------------------+-----------------------------------------+--------------------"));
//		for (Row row : results) {
//			System.out.println(String.format("%-25s\t%-30s\t%-20s",
//					row.getString("author"), row.getString("title"),
//					row.getString("release_date")));
//		}
//		System.out.println();
		return results;
	}

	/**
	 * Query2 search by author, return book id of all books written by this
	 * author -- 'Walt Whitman'
	 */
	public ArrayList<String> searchByAuthor(String author) {
		ArrayList<String> result = new ArrayList<String>();
		ResultSet results = session
				.execute("select id from cmpe226.books where first_author ='"
						+ author + "';");
		for (Row row : results) 
			result.add(row.getUUID("id").toString());
//		System.out.println(String.format("%-20s\n%s", "id",
//				"-------------------------"));
//		for (Row row : results) {
//			System.out.println(String.format("%-20s", row.getUUID("id")));
//		}
//		System.out.println();
		return result;
	}

	/**
	 * Query3 search book by specific language or group by language -- English
	 * return bookid of all found books
	 */
	public ArrayList<String> searchByLanguage(String language) {
		ArrayList<String> result = new ArrayList<String>();
		ResultSet results = session
				.execute("select id from cmpe226.books where language ='"
						+ language + "';");

		for (Row row : results) 
			result.add(row.getUUID("id").toString());
//		System.out.println(String.format("%-20s\n%s", "id",
//				"-------------------------"));
//		for (Row row : results) {
//			System.out.println(String.format("%-20s", row.getUUID("id")));
//		}
//		System.out.println();
		return result;
	}

	public static void main(String[] args) throws IOException,
			InterruptedException {
		CassandraClient client = new CassandraClient("localhost");

		try {
			DAO dao = new DAO(client.getSession());

			// dao.insertUser("Bugy");

			// recreate schema
			// dao.recreateKeyspace();

			// load all files under folder
			// dao.loadData(System.getProperty("user.dir") + "/books/");

			// add comment
			// dao.insertComment(UUID.fromString("ce0c57af-f563-4ecb-ab8a-76f3bd5fdd6b"),
			// UUID.fromString("3c0afa81-5ba8-4c29-99a8-366445f12d7a"),
			// "comment test");

			// add rating
			// dao.insertRate(UUID.fromString("ce0c57af-f563-4ecb-ab8a-76f3bd5fdd6b"),
			// UUID.fromString("3c0afa81-5ba8-4c29-99a8-366445f12d7a"), 4.5);

			// dao.getBook(UUID.fromString("372d532e-20f2-456c-ab1d-217b8ead54c6"));

		} finally {
			client.close();
		}
	}

}
