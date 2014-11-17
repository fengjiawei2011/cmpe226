package cmpe226.project2.books.mongo.gridfs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.UnknownHostException;

import cmpe226.project2.books.monogo.schema.Book;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

public class GridFStest3 {
	
	static Mongo mongo;
	static DB db;
	static DBCollection metacollection;
	
	
	public static void main(String[] args) throws IOException{
		dbconnection();
		
		File bookFile = new File("/Users/lingzhang/Desktop/11.txt");
		String md5= saveBookFileToGrid(bookFile);
		
		
		getImage(md5);
		findAndSaveToLocal(md5, "/Users/lingzhang/Desktop/", "test1_save.txt");
		
		parseBookMetaAndSave(bookFile, md5, metacollection);

		
	}
	
	
	
	@SuppressWarnings("deprecation")
	public static void dbconnection() throws UnknownHostException{
		 mongo = new Mongo("localhost", 27017);
		db = mongo.getDB("test");
		 metacollection = db.getCollection("books_meta");
	}
	
	
	public static String saveBookFileToGrid(File bookFile) throws IOException{
		
		
		GridFS gfsBooks = new GridFS(db, "books");
		GridFSInputFile gfsFile = gfsBooks.createFile(bookFile);
		gfsFile.save();
		return gfsFile.getMD5();
		//return gfsFile.getId();
	}
	
	public static void getImage(String bookmd5){
		GridFS gfsBooks = new GridFS(db, "books");
		GridFSDBFile imageForOutput = gfsBooks.findOne(new BasicDBObject("md5",bookmd5));
		System.out.println(imageForOutput);
	}
	
	public static void findAndSaveToLocal(String bookmd5, String savePath, String saveName) throws IOException{
		GridFS gfsBooks = new GridFS(db, "books");
		GridFSDBFile imageForOutput = gfsBooks.findOne(new BasicDBObject("md5",bookmd5));
		String newFile=savePath+saveName;
		imageForOutput.writeTo(newFile); //output to new file
	}
	

	@SuppressWarnings("resource")
	public static void parseBookMetaAndSave(File bookFile, String md5, DBCollection metacollection) throws IOException{
		BasicDBObject bookMetaData = new BasicDBObject();
		bookMetaData.append("md5", md5);
		BufferedReader reader = new BufferedReader(new FileReader(bookFile));
		String line= null;
		while((line=reader.readLine())!=null){
			if(line.startsWith("*** START OF THIS PROJECT GUTENBERG EBOOK")){
				break;
			}
			if(line.indexOf(":") < 0) 
				continue;
			
			String key = line.substring(0, line.indexOf(":")).trim();
			String value = line.substring(line.indexOf(":")+1).trim();
			
			if(line.startsWith("Title")){
				bookMetaData.append(Book.MATEDATA_TITLE, value);
				System.out.println(key+" is : "+ value);
				
			}else if (line.startsWith("Author")){
				bookMetaData.append(Book.MATEDATA_AUTHOR, value);
				System.out.println(key+" is : "+ value);
				
			}else if(line.startsWith("Language") ){
				bookMetaData.append(Book.MATEDATA_LANG, value);
				System.out.println(key+" is : "+ value);
				
			}else if(line.startsWith("Release Date")){
				bookMetaData.append(Book.MATEDATA_RELEASEDATE, value);
				System.out.println(key+" is : "+ value);
				
			}else if(line.startsWith("Translator") ){
				bookMetaData.append(Book.MATEDATA_TRANSLATOR, value);
				System.out.println(key+" is : "+ value);
			}
			//System.out.println(line);
			
		}
		System.out.println(bookMetaData);
		metacollection.save(bookMetaData);	
		
		
	}
	
}
