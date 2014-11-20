package cmpe226.project2.books.mongo.gridfs;

import java.io.IOException;

import cmpe226.project2.util.MongoDB;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;

public class MongoQueryTest {

	public void loadBooksTest () throws IOException {
		long begin = System.currentTimeMillis();
		
		LoaderWithGridFSV2.load(System.getProperty("user.dir") + "/books/");
		
		long end = System.currentTimeMillis();
		
		System.out.println("Total used " + (end - begin) + " msec");
	}
	
	
	public void downloadByMd5Test() throws IOException{
		long begin = System.currentTimeMillis();
		
//		MongoDB mdb=new MongoDB("test");
//		mdb.download(fileId, destPath);
//		
		Mongo mongo = new Mongo("localhost", 27017);
		DB db = mongo.getDB("test");
		
		String destPath=System.getProperty("user.dir") + "/booksCache/";
		String md5="testmd5"; ///need to find proper id later!!!
		
		downloadByMd5(db , md5, destPath, "testsavebook");
		long end = System.currentTimeMillis();
		
		System.out.println("Total used " + (end - begin) + " msec");
	}
	
	public void searchByTitleTest(){
		long begin = System.currentTimeMillis();
		
		MongoDB mdb=new MongoDB("test");

		searchByTitle(mdb.getCollection("books_meta"), "Peter Pan in Kensington Gardens");
		
		long end = System.currentTimeMillis();
		
		System.out.println("Total used " + (end - begin) + " msec");
		
	}
	
	//can be put into MongoDB class
	public String searchByTitle(DBCollection metacollection, String bookTitle ){
		DBObject obj = metacollection.findOne(new BasicDBObject("title", bookTitle));
		String resultmd5=(String)obj.get("md5");
		return resultmd5;
	
	}
	
	//can be put into MongoDB class
	public void downloadByMd5(DB db, String bookmd5, String savePath, String saveName) throws IOException{
		GridFS gfsBooks = new GridFS(db, "books");
		GridFSDBFile imageForOutput = gfsBooks.findOne(new BasicDBObject("md5",bookmd5));
		String newFile=savePath+saveName;
		imageForOutput.writeTo(newFile); //output to new file
	}
}
