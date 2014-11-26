package cmpe226.project2.books.mongo;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

import cmpe226.project2.util.BookParser;

import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSInputFile;

public class MongoDB {

	
	private String dbName = "books";
	private String bookCollection = "book_matedata";
	private String bookStatCollection = "book_statistic";
	private String domain = "localhost";
	private int port = 27017;
	
	private static MongoClient client;
	private DB mongoDB;

	public MongoDB() throws InterruptedException {
		try {
			client = new MongoClient(domain, port);
			this.mongoDB = client.getDB(dbName);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void load(String fileFolderPath) throws IOException {

		String inputpath = fileFolderPath;

		String[] books = new File(inputpath).list(); // {"35.txt"};
		int count = 0;

		for (String book : books) {
			File bookFile = new File(inputpath + book);
			String md5 = saveBookToGridFS(bookFile);

			BookParser parser = new BookParser(inputpath + book);
			HashMap<String, String> result = parser.processLineByLine();
			saveMeta(result, md5, mongoDB.getCollection(bookCollection));
			count++;
		}
		//System.out.println(count + " books parsed.");
	}

	public DB getDB() {
		return this.mongoDB;
	}
	
	public boolean dbExist() {
		ArrayList<String> dbs = (ArrayList<String>) client.getDatabaseNames();
		for (String db : dbs) {
			//System.out.println(db);
			if (db.equals(dbName)) {
				return true;
			}
		}
		return false;
	}
	
	public DBCollection getBookCollection(){
		return mongoDB.getCollection(bookCollection);
	}
	
	public void saveStatistic(HashMap<String, Long> map){
		CommandResult result = mongoDB.getCollection(bookCollection).getStats();
		BasicDBObject obj = new BasicDBObject()
				.append("ns", result.get("ns"))
				.append("count", result.get("count"))
				.append("size", result.get("size"))
				.append("storageSize", result.get("storageSize"));
		for(String key : map.keySet()){
			obj.append(key, map.get(key));
		}
		//mongoDB.getCollection(bookStatCollection).save(obj);
		System.out.println(obj);
	}

	public void deleteDB() throws InterruptedException {
		if (dbExist()) {
			System.out.println("deleting old db first ...");
			client.dropDatabase(dbName);
			System.out.println("delete successful...");
			return;
		}
		System.out.println("db not exist, start to crearting and loading...");
	}
	private String saveBookToGridFS(File bookFile) throws IOException {
		GridFS gfsBooks = new GridFS(mongoDB, dbName);
		GridFSInputFile gfsFile = gfsBooks.createFile(bookFile);
		gfsFile.save();
		return gfsFile.getMD5();
	}
	
	private void saveMeta(HashMap<String, String> metadata, String md5,
			DBCollection metacollection) {

		BasicDBObject bookMetaData = new BasicDBObject();
		bookMetaData.append("md5", md5);
		for (String k : metadata.keySet()) {
			bookMetaData.append(k, metadata.get(k));
		}
		// System.out.println(bookMetaData);
		metacollection.save(bookMetaData);

	}
}
