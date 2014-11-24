package cmpe226.project2.books.cassandra;

import java.io.IOException;
import java.util.HashMap;

public class QueryTest {
	
	private DAO dao;
	private HashMap<String, Long> result;
	
	public QueryTest() {
		CassandraClient client = new CassandraClient("localhost");
		dao = new DAO(client.getSession());
		result  = new HashMap<String, Long>();
	}
	
	// Load books
	public void loadBooks (int num,QueryTest test) throws IOException {
		dao.recreateKeyspace();		
		
		long begin = System.currentTimeMillis();
		for(int i = 0; i < num; i++){
			dao.loadData(System.getProperty("user.dir") + "/books/");
		}
		long end = System.currentTimeMillis();
		
		System.out.println("Total used " + (end - begin) + " msec");
		result.put("loading_"+num+"_times_data", (end - begin));
		
		Long searchByTitle = test.querySearchByTitle();
		Long searchByAuthor = test.querySearchByAuthor();
		Long searchByLanguage = test.querySearchByLanguage();
		
		result.put("querySearchByTitle_"+num+"_times_data",  searchByTitle);
		result.put("querySearchByAuthor_"+num+"_times_data",  searchByAuthor);
		result.put("querySearchByLanguage_"+num+"_times_data",  searchByLanguage);
		
		System.out.println("*************************************");
	}
	
	// Query 1
	public Long querySearchByTitle() {
		
		long begin = System.currentTimeMillis();
		
		dao.searchByTitle();
		
		long end = System.currentTimeMillis();
		
		System.out.println("QuerySearchByTitle - total used " + (end - begin) + " msec");
		return (end - begin);
	}
	
	// Query 2
	public Long querySearchByAuthor() {

		long begin = System.currentTimeMillis();
		
		dao.searchByAuthor();
		
		long end = System.currentTimeMillis();
		
		System.out.println("QuerySearchByAuthor - total used " + (end - begin) + " msec");
		return (end - begin);
	}
	
	// Query 3
	public Long querySearchByLanguage() {

		dao.addIndex("books", "language");

		long begin = System.currentTimeMillis();
		
		dao.searchByLanguage();
		
		long end = System.currentTimeMillis();
		
		System.out.println("querySearchByLanguage - total used " + (end - begin) + " msec");
		return (end - begin);
	}
		
	public static void main(String[] args) throws IOException {
		QueryTest test = new QueryTest();
		
		test.loadBooks(1,test);
		
		test.loadBooks(5,test);
		
		test.loadBooks(9,test);
		
		
		System.out.println("*************results******************");
		
		for(String str : test.result.keySet()){
			System.out.println(str+" : "+ test.result.get(str));
		}
	}
	
}
