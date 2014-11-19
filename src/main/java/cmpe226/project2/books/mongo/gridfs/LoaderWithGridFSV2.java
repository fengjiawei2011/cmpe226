package cmpe226.project2.books.mongo.gridfs;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Scanner;

import cmpe226.project2.util.BookParser;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

public class LoaderWithGridFSV2 {
	
	static Mongo mongo;
	static DB db;
	static DBCollection metacollection;
	
	
	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException{
		dbconnection();
		
		String inputpath;
		if(args.length>0){
			inputpath=args[0];
		}
		else {
			Scanner reader0 = new Scanner(System.in);
	    	System.out.println(" Input the folder that contains data, like /Users/lingzhang/Desktop/data/ ");
	    	inputpath=reader0.nextLine();
		}
		
		
		String[] books = new File(inputpath).list(); //{"35.txt"};
		int count = 0;

		for (String book : books) {
			File bookFile=new File(inputpath+book);
			String md5= saveBookFileToGrid(bookFile);
			
			BookParser parser = new BookParser(inputpath + book);
			HashMap<String, String> result = parser.processLineByLine();
			saveMeta(result, md5, metacollection);
			count++;
		}
		System.out.println(count + " books parsed.");
					
	}
	
	
	public static void load(String fileFolderPath) throws IOException{
		dbconnection();
		
		String inputpath=fileFolderPath;
		
		
		String[] books = new File(inputpath).list(); //{"35.txt"};
		int count = 0;

		for (String book : books) {
			File bookFile=new File(inputpath+book);
			String md5= saveBookFileToGrid(bookFile);
			
			BookParser parser = new BookParser(inputpath + book);
			HashMap<String, String> result = parser.processLineByLine();
			saveMeta(result, md5, metacollection);
			count++;
		}
		System.out.println(count + " books parsed.");
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
	
	
	public static void findAndSaveToLocal(String bookmd5, String savePath, String saveName) throws IOException{
		GridFS gfsBooks = new GridFS(db, "books");
		GridFSDBFile imageForOutput = gfsBooks.findOne(new BasicDBObject("md5",bookmd5));
		String newFile=savePath+saveName;
		imageForOutput.writeTo(newFile); //output to new file
	}


	public static void saveMeta(HashMap<String, String> metadata,
								String md5, DBCollection metacollection){
		
		BasicDBObject bookMetaData = new BasicDBObject();
		bookMetaData.append("md5", md5);
		for (String k : metadata.keySet()) {
			bookMetaData.append(k, metadata.get(k) );
		}
		//System.out.println(bookMetaData);
		metacollection.save(bookMetaData);
		
		
	}
	
}
