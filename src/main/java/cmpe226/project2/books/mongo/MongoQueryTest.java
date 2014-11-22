package cmpe226.project2.books.mongo;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

import cmpe226.project2.books.mongo.gridfs.MongoQuery;

import com.mongodb.DB;
import com.mongodb.MongoClient;

public class MongoQueryTest {

	static String FILES_PATH = "/Users/frank/Documents/projects/java/cmpe226/books/";
	static MongoDB mongoDB = null;
	static BookDAO dao = null;

	public MongoQueryTest() {
		try {
			mongoDB = new MongoDB();
			dao = new BookDAO(mongoDB.getDB());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException {
		
		
		System.out.println("program start...");
		MongoQueryTest test = new MongoQueryTest();
		try {
			mongoDB.deleteDB();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		HashMap<String, Long> map = null;

		// load one time
		map = testByDataSize(mongoDB, 1);
		mongoDB.saveStatistic(map);

		// load five time
		map = testByDataSize(mongoDB, 5);
		mongoDB.saveStatistic(map);

		// load ten time
		map = testByDataSize(mongoDB, 9);
		mongoDB.saveStatistic(map);
		
		System.out.println("program end...");
	}

	public static HashMap<String, Long> testByDataSize(MongoDB db, int num)
			throws UnknownHostException {
		mongoDB.getBookCollection().drop();
		Long loadData = loadData(db, num);
		Long queryByTitle = searchByTitleTest("Peter Pan in Kensington Gardens");
		Long queryByAuthor = searchByAuthorTest("William Shakespeare");
		Long queryByLang = searchByLanguageTest("English");
		HashMap<String, Long> map = new HashMap<String, Long>();
		map.put("loading_" + num + "_times_data", loadData);
		map.put("query_by_title", queryByTitle);
		map.put("query_by_Author", queryByAuthor);
		map.put("query_by_Lanuage", queryByLang);
		return map;
	}

	private static long loadData(MongoDB db, int num) {
		System.out.println("loading "+num+" times data...");
		long begin = System.currentTimeMillis();
		try {
			for (int i = 0; i < num; i++) {
				db.load(FILES_PATH);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		return end - begin;
	}

	public static void downloadByMd5Test(String md5) throws IOException {
		long begin = System.currentTimeMillis();

		MongoClient mongoClient = new MongoClient("localhost", 27017);
		DB db = mongoClient.getDB("test");

		String destPath = System.getProperty("user.dir") + "/booksCache/";

		MongoQuery.downloadByMd5(db, md5, destPath, "testsavebook");
		long end = System.currentTimeMillis();

		//System.out.println("Total used " + (end - begin) + " msec");

		mongoClient.close();
	}

	/**
	 * test for Query1
	 * 
	 * @throws UnknownHostException
	 */
	public static long searchByTitleTest(String title)
			throws UnknownHostException {
		long begin = System.currentTimeMillis();
		//System.out.println("test for query1: search by title=========");

		// MongoQuery.searchByTitle(db, "Peter Pan in Kensington Gardens");
		dao.searchByTitle(title);

		long end = System.currentTimeMillis();

		//System.out.println("Total used " + (end - begin) + " msec");
		return (end - begin);

	}

	/**
	 * test for Query2
	 * 
	 * @throws UnknownHostException
	 */
	public static long searchByAuthorTest(String testauthor)
			throws UnknownHostException {
		long begin = System.currentTimeMillis();
		// ArrayList<String> result=MongoQuery.searchByAuthor(db,testauthor );
		ArrayList<String> result = dao.searchByAuthor(testauthor);
		long end = System.currentTimeMillis();

		//System.out.println("Total used " + (end - begin) + " msec");
		return (end - begin);
	}

	/**
	 * test for Query3
	 * 
	 * @throws UnknownHostException
	 */
	public static long searchByLanguageTest(String testlang)
			throws UnknownHostException {
		long begin = System.currentTimeMillis();
		// ArrayList<String> result=MongoQuery.searchByLanguage(db, testlang);

		ArrayList<String> result = dao.searchByLanguage(testlang);

		long end = System.currentTimeMillis();

		//System.out.println("Total used " + (end - begin) + " msec");
		return (end - begin);
	}

}
