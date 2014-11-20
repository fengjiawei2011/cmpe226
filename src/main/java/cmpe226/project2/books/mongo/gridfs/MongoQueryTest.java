package cmpe226.project2.books.mongo.gridfs;

import java.io.IOException;

import cmpe226.project2.util.MongoDB;

import com.mongodb.DB;
import com.mongodb.Mongo;

public class MongoQueryTest {

	public void loadBooksTest () throws IOException {
		long begin = System.currentTimeMillis();
		
		LoaderWithGridFSV2.load(System.getProperty("user.dir") + "/books/");
		
		long end = System.currentTimeMillis();
		
		System.out.println("Total used " + (end - begin) + " msec");
	}
	
	
	@SuppressWarnings("deprecation")
	public void downloadByMd5Test() throws IOException{
		long begin = System.currentTimeMillis();
		
//		MongoDB mdb=new MongoDB("test");
//		mdb.download(fileId, destPath);
//		
		Mongo mongo = new Mongo("localhost", 27017);
		DB db = mongo.getDB("test");
		
		String destPath=System.getProperty("user.dir") + "/booksCache/";
		String md5="testmd5"; ///need to find proper id later!!!
		
		MongoQuery.downloadByMd5(db , md5, destPath, "testsavebook");
		long end = System.currentTimeMillis();
		
		System.out.println("Total used " + (end - begin) + " msec");
	}
	
	public void searchByTitleTest(){
		long begin = System.currentTimeMillis();
		
		MongoDB mdb=new MongoDB("test");

		MongoQuery.searchByTitle(mdb.getCollection("books_meta"), "Peter Pan in Kensington Gardens");
		
		long end = System.currentTimeMillis();
		
		System.out.println("Total used " + (end - begin) + " msec");
		
	}
	

}
