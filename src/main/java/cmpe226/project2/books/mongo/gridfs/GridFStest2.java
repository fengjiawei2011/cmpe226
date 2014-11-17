package cmpe226.project2.books.mongo.gridfs;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

public class GridFStest2 {
	
	static Mongo mongo;
	static DB db;
	
	@SuppressWarnings("deprecation")
	public static void dbconnection() throws UnknownHostException{
		 mongo = new Mongo("localhost", 27017);
		db = mongo.getDB("test");
	}
	
	
	public static void saveImage() throws IOException{
		
		String newFileName = "ling_test_image";
		File imageFile = new File("/Users/lingzhang/Desktop/test1.jpeg");
		GridFS gfsPhoto = new GridFS(db, "photo");
		GridFSInputFile gfsFile = gfsPhoto.createFile(imageFile);
		gfsFile.setFilename(newFileName);
		gfsFile.save();
	}
	
	public static void getImage(){
		String newFileName = "ling_test_image";
		GridFS gfsPhoto = new GridFS(db, "photo");
		GridFSDBFile imageForOutput = gfsPhoto.findOne(newFileName);
		System.out.println(imageForOutput);
	}
	
	public static void saveToLocal() throws IOException{
		String newFileName = "ling_test_image";
		GridFS gfsPhoto = new GridFS(db, "photo");
		GridFSDBFile imageForOutput = gfsPhoto.findOne(newFileName);
		imageForOutput.writeTo("/Users/lingzhang/Desktop/test1_save.jpeg"); //output to new file
	}
	
	public static void main(String[] args) throws IOException{
		dbconnection();
		saveImage();
		getImage();
		saveToLocal();
	}
}
