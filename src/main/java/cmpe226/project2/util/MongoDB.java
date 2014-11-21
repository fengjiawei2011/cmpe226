package cmpe226.project2.util;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

public class MongoDB {

	private MongoClient client;
	private String dbName;

	public MongoDB(String dbName) {
		this.dbName = dbName;
		try {
			client = new MongoClient("localhost", 27017);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void deleteDB(){
		client.dropDatabase(dbName);
		System.out.println("drop db:" + dbName + " success");
	}

	public DBCollection getCollection(String collectionName) {
		return client.getDB(dbName).getCollection(collectionName);
	}

	public String saveBookToGridFS(File bookFile) throws IOException {
		GridFS gfsBooks = new GridFS(client.getDB(dbName), "books");
		GridFSInputFile gfsFile = gfsBooks.createFile(bookFile);
		gfsFile.save();
		return gfsFile.getMD5();
	}
	
	// query by id
	public boolean download(String fileId, String destPath) throws IOException{
		GridFS gfsBooks = new GridFS(client.getDB(dbName), "books");
		//gfsBooks.get
		GridFSDBFile file = gfsBooks.findOne(new BasicDBObject("id",fileId));
		if(file != null){
			file.writeTo(destPath);
			return true;
		}
		return false;
	}
	
	public ArrayList<DBObject> searchByYears(DBCollection metacollection, String d){
		DBCursor cursor = metacollection.find(new BasicDBObject("release_date", d));
		ArrayList<DBObject> list = new ArrayList<DBObject>();
		while(cursor.hasNext()){
			list.add(cursor.next());
		}
		return list;
	}
	
	public String searchByTitle(DBCollection metacollection, String bookTitle ){
		DBObject obj = metacollection.findOne(new BasicDBObject("title", bookTitle));
		String resultmd5=(String)obj.get("md5");
		return resultmd5;
	
	}
	
}
