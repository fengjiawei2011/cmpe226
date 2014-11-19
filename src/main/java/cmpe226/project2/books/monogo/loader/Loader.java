package cmpe226.project2.books.monogo.loader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import cmpe226.project2.books.monogo.schema.Book;
import cmpe226.project2.util.MongoDB;

import com.mongodb.BasicDBObject;

public class Loader {
	private final static String FILES_PATH = "/Users/frank/Documents/projects/java/cmpe226/books/";
	private final static int READ_LINES_THRESHOLD = 40;
	private MongoDB db;
	
	public static void main(String[] args) {
		File directory = new File(FILES_PATH);
		File[] files = directory.listFiles();
		Loader loader = new Loader();
		MongoDB db = new MongoDB("books");
		try {
			for (File f : files) {
				BasicDBObject book = loader.read(f);
				String id = db.saveBookToGridFS(f);
				((BasicDBObject)book.get(Book.MATEDATA)).put(Book.FILE_MATEDATA, id);
				
				System.out.println(book);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public BasicDBObject read(File file) throws FileNotFoundException {
		BasicDBObject book = new BasicDBObject();
		BasicDBObject bookMateData = new BasicDBObject();
		//BasicDBObject fileMateData = new BasicDBObject();

		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = null;
		//Boolean contentStart = false;
		int i = 0;
		try {
			//String bContent = "";
			while ((line = reader.readLine()) != null) {
				if((i++) <= READ_LINES_THRESHOLD) {
					parseMatedata(line, bookMateData);
				}else{
					break;
				}
					
//				line = line.toLowerCase().trim();
//				
//				if(contentStart)
//					bContent += line;
//
//				if(line.equals("contents")) 
//					contentStart = true;					
			}
			
//			if( bContent.length() == 0 )
//				System.out.println("The file parse fails to parse content --->"+file.getName());
		
			book.put(Book.MATEDATA, bookMateData);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return book;
	}
	
	public void parseMatedata(String line,BasicDBObject matedata ){
		line = line.toLowerCase();
		
		if(line.indexOf(":") < 0) 
			return;
	
		String key = line.substring(0, line.indexOf(":")).trim();
		String value = line.substring(line.indexOf(":")+1).trim();
		
		BasicDBObject book = null;
		if (key.indexOf("title") >= 0) {
			matedata.append(Book.MATEDATA_TITLE, value);
			//System.out.println(line);
		}else if(key.indexOf("author") >= 0){
			matedata.append(Book.MATEDATA_AUTHOR, value);
			//System.out.println(line);
		}else if(key.indexOf("release") >= 0 && key.indexOf("date") >= 0){
			matedata.append(Book.MATEDATA_RELEASEDATE, value);
			//System.out.println(line);
		}else if(key.indexOf("language") >= 0){
			matedata.append(Book.MATEDATA_LANG, value);
			//System.out.println(line);
		}else if(key.indexOf("last") >=0 && key.indexOf("update") >= 0){
			matedata.append(Book.MATEDATA_LASTUPDATE, value);
			//System.out.println(line);
		}else if(key.indexOf("genre") >= 0){
			matedata.append(Book.MATEDATA_GENRE, value);
			//System.out.println(line);
		}else if(key.indexOf("translator") >= 0){
			matedata.append(Book.MATEDATA_TRANSLATOR, value);
			//System.out.println(line);
		}
		
		//System.out.println(book);
	}
}
