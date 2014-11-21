package cmpe226.project2.books.mongo.gridfs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;

public class MongoQuery {
	
	/**
	 * query1: 
	 * search by title ,  
	 * print all meta data, including comments---  mongo is better
	 * 
	 * @param metacollection
	 * @param bookTitle
	 * @return
	 */
	public static void searchByTitle(DB db, String bookTitle ){
		//TODO
		DBCollection col = db.getCollection("books_meta");
		DBObject obj = col.findOne(new BasicDBObject("title", bookTitle));
		System.out.println(obj);
		
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
	public static ArrayList<String> searchByAuthor(DB db, String authorName ){
		ArrayList<String> result=new ArrayList<String>();
		
		DBCollection col = db.getCollection("books_meta");
		BasicDBObject query = new BasicDBObject("author", authorName);

		DBCursor cursor = col.find(query);

		while(cursor.hasNext())
		{
			DBObject obj=cursor.next();
		   //System.out.println(obj);
		   result.add(  (String)obj.get("md5")  ) ;
		}
		cursor.close();
		
		return result;
	}
	
	/**
	 * Query3
	 * search book by specific language or group by language
	 * return bookid of all found books
	 * 
	 * @param metacollection
	 * @param language
	 * @return
	 */
	public static ArrayList<String> searchByLanguage(DB db, String language ){
		ArrayList<String> result=new ArrayList<String>();
		DBCollection col = db.getCollection("books_meta");
		BasicDBObject query = new BasicDBObject("language", language);

		DBCursor cursor = col.find(query);

		while(cursor.hasNext())
		{
			DBObject obj=cursor.next();
		   //System.out.println(obj);
		   result.add(  (String)obj.get("md5")  ) ;
		}
		cursor.close();
		
		return result;
	}
	
	
	
	
	
	public static void downloadByMd5(DB db, String bookmd5, String savePath, String saveName) throws IOException{
		GridFS gfsBooks = new GridFS(db, "books");
		GridFSDBFile imageForOutput = gfsBooks.findOne(new BasicDBObject("md5",bookmd5));
		String newFile=savePath+saveName;
		imageForOutput.writeTo(newFile); //output to new file
	}
	
	
	
	public static void insertUser(DB db, String username){		
		DBCollection users=db.getCollection("users");
		BasicDBObject o = new BasicDBObject();
		o.append("username", username);
		users.insert(o);
	}
	
	public static void addWriteBookForUser(DB db, String username, String bookid){
		
		
		DBCollection col = db.getCollection("users");

		BasicDBObject query = new BasicDBObject();
		query.put("username", username); //search for the user

		BasicDBObject newDocument = new BasicDBObject();
		newDocument.put("WriteBook", bookid);

		BasicDBObject updateObj = new BasicDBObject();
		updateObj.put("$push", newDocument);

		col.update(query, updateObj);
		
		
		
	}
	
	public static void addReadBookForUser(DB db, String username, String bookid, int rate){
		DBCollection col = db.getCollection("users");

		BasicDBObject query = new BasicDBObject();
		query.put("username", username); //search for the user

		BasicDBObject newDocument = new BasicDBObject();
		newDocument.put("ReadBook", new BasicDBObject("Book", bookid)
											.append("Rating", rate) );

		BasicDBObject updateObj = new BasicDBObject();
		updateObj.put("$push", newDocument);

		col.update(query, updateObj);
		
		
	}
	
	public static void insertComment(DB db, String bookid, String userid, String comment){
		DBCollection col = db.getCollection("books_meta");

		BasicDBObject query = new BasicDBObject();
		query.put("md5", bookid); //search for the book

		BasicDBObject newDocument = new BasicDBObject();
		newDocument.put("comments", new BasicDBObject("User", userid)
											.append("Comment", comment)
											.append("PostDate", new Date(System.currentTimeMillis()) )
											);

		BasicDBObject updateObj = new BasicDBObject();
		updateObj.put("$push", newDocument);

		col.update(query, updateObj);
	}
}
