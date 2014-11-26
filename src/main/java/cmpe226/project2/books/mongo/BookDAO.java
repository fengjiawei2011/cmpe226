package cmpe226.project2.books.mongo;

import java.util.ArrayList;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;


public class BookDAO {
	private final static String dbName = "books";
	private final static String tableName = "book_matedata";
	//private DB db;
	private DBCollection bookMatedata;
	
	public BookDAO(DB db) {
		//this.db = db;
		bookMatedata = db.getCollection(tableName);
	}
	
	/**
	 * query1: 
	 * search by title ,  
	 * print all meta data, including comments---  mongo is better
	 * 
	 * @param metacollection
	 * @param bookTitle
	 * @return
	 */
	public void searchByTitle( String bookTitle ){
		//TODO
		int count = 0;
		DBCursor cursor = bookMatedata.find(new BasicDBObject("title", bookTitle));
		while(cursor.hasNext()){
			cursor.next();
			count++;
		}
		System.out.println("searchByTitle has rows ----  " + count );
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
	public  ArrayList<String> searchByAuthor(String authorName ){
		ArrayList<String> result=new ArrayList<String>();

		BasicDBObject query = new BasicDBObject("author", authorName);

		DBCursor cursor = bookMatedata.find(query);
		
	
		while(cursor.hasNext())
		{
			DBObject obj=cursor.next();
		   //System.out.println(obj);
		   result.add(  (String)obj.get("md5")  ) ;
		}
		cursor.close();
		System.out.println("searchByAuthor has rows ---  " + result.size());
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
	public ArrayList<String> searchByLanguage(String language ){
		ArrayList<String> result=new ArrayList<String>();
		BasicDBObject query = new BasicDBObject("language", language);

		DBCursor cursor = bookMatedata.find(query);

		while(cursor.hasNext())
		{
			DBObject obj=cursor.next();
		   //System.out.println(obj);
		   result.add(  (String)obj.get("md5")  ) ;
		}
		cursor.close();
		System.out.println("searchByLanguage has rows ---  " + result.size());
		return result;
	}	
	
	public ArrayList<DBObject> searchByYears(String d){
		DBCursor cursor = this.bookMatedata.find(new BasicDBObject("release_date", d));
		ArrayList<DBObject> list = new ArrayList<DBObject>();
		while(cursor.hasNext()){
			list.add(cursor.next());
		}
		cursor.close();
		return list;
	}
}
