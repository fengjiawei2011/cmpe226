package cmpe226.project2.util;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
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

	public DBCollection getCollection(String collectionName) {
		return client.getDB(dbName).getCollection(collectionName);
	}

	public String saveBookToGridFS(File bookFile) throws IOException {
		GridFS gfsBooks = new GridFS(client.getDB(dbName), "books");
		GridFSInputFile gfsFile = gfsBooks.createFile(bookFile);
		gfsFile.save();
		return gfsFile.getMD5();
	}
	
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
}
