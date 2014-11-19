package cmpe226.project2.books.cassandra;

import java.io.IOException;

public class QueryTest {
	
	private DAO dao;
	
	public QueryTest() {
		CassandraClient client = new CassandraClient("localhost");
		dao = new DAO(client.getSession());
	}
	
	// Load books
	public void loadBooks () throws IOException {
		dao.recreateKeyspace();
		
		long begin = System.currentTimeMillis();
		
		dao.loadData(System.getProperty("user.dir") + "/books/");
		
		long end = System.currentTimeMillis();
		
		System.out.println("Total used " + (end - begin) + " msec");
	}
	
	//TODO
	public void query() {
		
	}

	
	public static void main(String[] args) throws IOException {
		QueryTest test = new QueryTest();
		
		test.loadBooks();
	}
	
}
