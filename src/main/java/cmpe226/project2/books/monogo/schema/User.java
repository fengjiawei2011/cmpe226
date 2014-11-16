package cmpe226.project2.books.monogo.schema;

import java.util.ArrayList;

public class User {
	private String id;
	private String name;
	private ArrayList<String> writeBooks;
	private ArrayList<String> readBooks;
	
	
	public static String MATEDATA_ID = "_id";
	public static String MATEDATA_NAME = "name";
	public static String MATEDATA_WRITE_BOOKS = "write_books";
	public static String MATEDATA_READ_BOOKS ="read_books";
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public ArrayList<String> getWriteBooks() {
		return writeBooks;
	}
	public void setWriteBooks(ArrayList<String> writeBooks) {
		this.writeBooks = writeBooks;
	}
	public ArrayList<String> getReadBooks() {
		return readBooks;
	}
	public void setReadBooks(ArrayList<String> readBooks) {
		this.readBooks = readBooks;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
