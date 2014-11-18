package cmpe226.project2.books.cassandra;

import com.datastax.driver.core.Session;

public class DataLoader {

	String keyspace = "cmpe226";
	String bookTable = "books";
	String userTable = "users";
	String commentType = "comment";
	String readType = "read";
	private Session session;

	public DataLoader(Session session) {
		this.session = session;
	}

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
				.format("CREATE TABLE IF NOT EXISTS %s.%s (id uuid PRIMARY KEY, title text, author text, first_author text, release_date text, avg_rate double, count_rate int, comments frozen<%s>, content blob);",
						keyspace, bookTable, commentType);

		String createUserTable = String
				.format("CREATE TABLE IF NOT EXISTS %s.%s (id uuid PRIMARY KEY, name text, read_books frozen<%s>, write_books set<uuid>);",
						keyspace, userTable, readType);

		session.execute(createKeyspace);
		session.execute(createCommentType);
		session.execute(createReadType);
		session.execute(createBookTable);
		session.execute(createUserTable);
	}
	
	public void addColumn(String table, String column, String type) {
		String addCol = String.format("ALTER TABLE %s.%s ADD %s %s", keyspace, table, column, type);
		session.execute(addCol);
	}
	
	//TODO
	public void loadData() {
		
	}

	public static void main(String[] args) {
		CassandraClient client = new CassandraClient("localhost");
		DataLoader loader = new DataLoader(client.getSession());
		loader.init();
		loader.addColumn("books", "language", "text");
		client.close();
	}

}
