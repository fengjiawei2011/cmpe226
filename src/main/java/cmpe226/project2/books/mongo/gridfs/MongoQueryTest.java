package cmpe226.project2.books.mongo.gridfs;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import cmpe226.project2.util.MongoDB;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;

public class MongoQueryTest {
	
	
	public static void main(String[] args) throws IOException{
		MongoQueryTest test=new MongoQueryTest();
		test.loadBooksTest();
		//test.searchByTitleTest("Peter Pan in Kensington Gardens");
		//test.searchByAuthorTest("William Shakespeare");
		//test.searchByLanguageTest("English");
		//test.searchByLanguageTest("Gemany");
	}
	

	public void loadBooksTest () throws IOException {
		long begin = System.currentTimeMillis();
		
		LoaderWithGridFSV2.load(System.getProperty("user.dir") + "/books/");
		
		long end = System.currentTimeMillis();
		
		System.out.println("Total used " + (end - begin) + " msec");
	}
	
	
	public void downloadByMd5Test(String md5) throws IOException{
		long begin = System.currentTimeMillis();
		
		MongoClient mongoClient = new MongoClient("localhost" , 27017 );
		DB db = mongoClient.getDB("test");
		
		String destPath=System.getProperty("user.dir") + "/booksCache/";
		
		MongoQuery.downloadByMd5(db , md5, destPath, "testsavebook");
		long end = System.currentTimeMillis();
		
		System.out.println("Total used " + (end - begin) + " msec");
		
		mongoClient.close();
	}
	
	
	
	/**
	 * test for Query1
	 * 
	 * @throws UnknownHostException
	 */
	public void searchByTitleTest() throws UnknownHostException{
		long begin = System.currentTimeMillis();
		System.out.println("test for query1: search by title=========");
		MongoClient mongoClient = new MongoClient("localhost" , 27017 );
		DB db = mongoClient.getDB("test");

		MongoQuery.searchByTitle(db, "Peter Pan in Kensington Gardens");
		
		long end = System.currentTimeMillis();
		
		System.out.println("Total used " + (end - begin) + " msec");
		
		mongoClient.close();
	}
	
	
	/**
	 * test for Query2
	 * 
	 * @throws UnknownHostException
	 */
	public void searchByAuthorTest(String testauthor) throws UnknownHostException{
		long begin = System.currentTimeMillis();
		System.out.println("test for query2: search by author=========");
		MongoClient mongoClient = new MongoClient("localhost" , 27017 );
		DB db = mongoClient.getDB("test");
		ArrayList<String> result=MongoQuery.searchByAuthor(db,testauthor );
		System.out.println("the number of books writen by "+ testauthor +" is: "+ result.size());
		
		long end = System.currentTimeMillis();
		
		System.out.println("Total used " + (end - begin) + " msec");
		
		mongoClient.close();
	}
	
	/**
	 * test for Query3
	 * 
	 * @throws UnknownHostException
	 */
	public void searchByLanguageTest(String testlang) throws UnknownHostException{
		long begin = System.currentTimeMillis();
		System.out.println("test for query3: search by languqge=========");
		MongoClient mongoClient = new MongoClient("localhost" , 27017 );
		DB db = mongoClient.getDB("test");
		ArrayList<String> result=MongoQuery.searchByLanguage(db, testlang);
		System.out.println("the number of books writen in "+testlang +" is: "+ result.size());
		
		long end = System.currentTimeMillis();
		
		System.out.println("Total used " + (end - begin) + " msec");
		
		mongoClient.close();
	}
	
	
	
	
	
	
	
	

}
