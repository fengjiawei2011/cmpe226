package cmpe226.project2.books.mongo.gridfs;

import java.io.File;
import java.io.IOException;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.WriteConcern;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSInputFile;

public class GridFStest {
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException {
		 
		//
		// Connect to MongoDB (without authentification for the time being)
		// And get a handle on the collection used to store the metadata
		//
		Mongo mongo = new Mongo("localhost", 27017);
		DB db = mongo.getDB("test");
		DBCollection collection = db.getCollection("downloads_meta");
 
		//
		// The biiiiig file to be stored to MongoDB
		//
		File file = new File("/Users/lingzhang/Desktop/test1.jpeg");
 
		//
		// Store the file to MongoDB using GRIDFS
		//
		GridFS gridfs = new GridFS(db, "downloads");
		GridFSInputFile gfsFile = gridfs.createFile(file);
		gfsFile.setFilename("test1");
		gfsFile.save();
 
		//
		// Let's create a new JSON document with some "metadata" information on the download
		//
		BasicDBObject info = new BasicDBObject();
                info.put("name", "MongoDB");
                info.put("fileName", "test1");
                info.put("rawName", "test1.jpeg");
                info.put("rawPath", "/Users/lingzhang/Desktop/");
 
                //
                // Let's store our document to MongoDB
                //
		collection.insert(info, WriteConcern.SAFE);
	}
}
