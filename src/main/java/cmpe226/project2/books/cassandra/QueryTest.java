package cmpe226.project2.books.cassandra;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.datastax.driver.core.ResultSet;

public class QueryTest {
	
	private DAO dao;
	private HashMap<String, Long> result;
	
	public QueryTest(CassandraClient client) {
		dao = new DAO(client.getSession());
		result  = new HashMap<String, Long>();
	}
	
	// Load books
	public void loadBooks (int num) throws IOException {
		dao.recreateKeyspace();		
		
		int count = 0;
		long begin = System.currentTimeMillis();
		for(int i = 0; i < num; i++){
			count += dao.loadData(System.getProperty("user.dir") + "/books/");
		}
		long end = System.currentTimeMillis();
		
		System.out.printf("%d books loaded.", count);
		System.out.println("Total used " + (end - begin) + " msec");
		result.put("loading_"+num+"_times_data", (end - begin));
		
		Long searchByTitle = querySearchByTitle();
		Long searchByAuthor = querySearchByAuthor();
		Long searchByLanguage = querySearchByLanguage();
		
		result.put("querySearchByTitle_"+num+"_times_data",  searchByTitle);
		result.put("querySearchByAuthor_"+num+"_times_data",  searchByAuthor);
		result.put("querySearchByLanguage_"+num+"_times_data",  searchByLanguage);
		
		System.out.println("*************************************");
	}
	
	// Query 1
	public Long querySearchByTitle() {
		
		long begin = System.currentTimeMillis();
		
		ResultSet results = dao.searchByTitle("Peter Pan in Kensington Gardens");
		
		long end = System.currentTimeMillis();
		
		System.out.println("QuerySearchByTitle - Find " + results.all().size() + " books");
		System.out.println("QuerySearchByTitle - total used " + (end - begin) + " msec");
		return (end - begin);
	}
	
	// Query 2
	public Long querySearchByAuthor() {

		long begin = System.currentTimeMillis();
		
		ArrayList<String> result = dao.searchByAuthor("William Shakespeare");
		
		long end = System.currentTimeMillis();
		
		System.out.println("QuerySearchByAuthor - Find " + result.size() + " books");
		System.out.println("QuerySearchByAuthor - total used " + (end - begin) + " msec");
		return (end - begin);
	}
	
	// Query 3
	public Long querySearchByLanguage() {

		long begin = System.currentTimeMillis();
		
		ArrayList<String> result = dao.searchByLanguage("English");
		
		long end = System.currentTimeMillis();
		
		System.out.println("QuerySearchByLanguage - Find " + result.size() + " books");
		System.out.println("QuerySearchByLanguage - total used " + (end - begin) + " msec");
		return (end - begin);
	}
		
	public static void main(String[] args) throws IOException {

		CassandraClient client = new CassandraClient("localhost");
		
		QueryTest test = new QueryTest(client);
		
		test.loadBooks(1);
		
		test.loadBooks(5);
		
		test.loadBooks(9);

		test.loadBooks(13);
		
		System.out.println("*************results******************");
		
		for(String str : test.result.keySet()){
			System.out.println(str+" : "+ test.result.get(str));
		}
		
		client.close();
	}
	
}
