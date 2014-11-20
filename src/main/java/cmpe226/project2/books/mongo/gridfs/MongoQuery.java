package cmpe226.project2.books.mongo.gridfs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;

public class MongoQuery {
	
	/**
	 * query1: 
	 * search by title ,  
	 * return all meta data and/or book file---  mongo is better
	 * 
	 * @param metacollection
	 * @param bookTitle
	 * @return
	 */
	public static HashMap<String, String> searchByTitle(DBCollection metacollection, String bookTitle ){
		//TODO
		DBObject obj = metacollection.findOne(new BasicDBObject("title", bookTitle));
		
		
		HashMap<String, String> result =new HashMap<String, String>();
		
		return result;
	
	}
	
	/**
	 * Query2
	 * search by author, 
	 * return book id of all books written by this author --- cassandra is better
	 * 
	 * @param metacollection
	 * @param authorName
	 * @return
	 */
	public static ArrayList<String> searchByAuthor(DBCollection metacollection, String authorName ){
		//TODO
		ArrayList<String> result=new ArrayList<String>();
		return result;
	}
	
	/**
	 * Query3
	 * search book by specific language or group by language
	 * 
	 * @param metacollection
	 * @param language
	 * @return
	 */
	public static ArrayList<String> searchByLanguage(DBCollection metacollection, String language ){
		//TODO
		ArrayList<String> result=new ArrayList<String>();
		return result;
	}
	
	
	
	
	
	public static void downloadByMd5(DB db, String bookmd5, String savePath, String saveName) throws IOException{
		GridFS gfsBooks = new GridFS(db, "books");
		GridFSDBFile imageForOutput = gfsBooks.findOne(new BasicDBObject("md5",bookmd5));
		String newFile=savePath+saveName;
		imageForOutput.writeTo(newFile); //output to new file
	}
	
	
	
	public static void insertUser(DB db, String username){		
		
	}
	
	public static void addWriteBookForUser(DB db, String username, String bookid){
		
	}
	
	public static void addReadBookForUser(DB db, String username, String bookid, int rate){
		
	}
	
	public static void insertComment(DB db, String bookid, String userid, String comment){
		
	}
}
