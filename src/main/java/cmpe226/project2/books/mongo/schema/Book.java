package cmpe226.project2.books.mongo.schema;

import java.util.ArrayList;

public class Book {
	private String id;
	private String title;
	private String language;
	private String releaseDate;
	private String lastUpdate;
	private String genre;
	
	private User author;
	private User translator;
	private File file;
	private ArrayList<Comment> comments;
	private Rate rate;
	
	public static String MATEDATA_ID = "_id";
	public static String MATEDATA_TITLE = "title";
	public static String MATEDATA_LANG = "language";
	public static String MATEDATA_RELEASEDATE = "release_date";
	public static String MATEDATA_LASTUPDATE = "last_update";
	public static String MATEDATA_GENRE = "genre";
	public static String MATEDATA_AUTHOR = "author";
	public static String MATEDATA_FILE = "file";
	public static String MATEDATA_COMMENTS = "comments";
	public static String MATEDATA_RATE = "rate";
	public static String MATEDATA_TRANSLATOR = "translator";
	public static String MATEDATA = "matedata";
	public static String BOOk_MATEDATA = "book";
	public static String FILE_MATEDATA = "file";
	public static String MATEDATA_CONTENT = "content";
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getReleaseDate() {
		return releaseDate;
	}
	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}
	public String getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	public String getGenre() {
		return genre;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}
	public User getAuthor() {
		return author;
	}
	public void setAuthor(User author) {
		this.author = author;
	}
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public ArrayList<Comment> getComments() {
		return comments;
	}
	public void setComments(ArrayList<Comment> comments) {
		this.comments = comments;
	}
	public Rate getRate() {
		return rate;
	}
	public void setRate(Rate rate) {
		this.rate = rate;
	}
	
}

class Comment {
	private String uid;
	private String description;
	private String postDate;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPostDate() {
		return postDate;
	}

	public void setPostDate(String postDate) {
		this.postDate = postDate;
	}

}

class Rate {
	private float avgRate;
	private int rates;
	
	public float getAvgRate() {
		return avgRate;
	}
	public void setAvgRate(float avgRate) {
		this.avgRate = avgRate;
	}
	public int getRates() {
		return rates;
	}
	public void setRates(int rates) {
		this.rates = rates;
	}
}
