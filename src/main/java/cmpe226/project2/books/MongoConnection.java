package cmpe226.project2.books;

import java.net.UnknownHostException;
import java.util.Set;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.WriteConcern;

public class MongoConnection {

	public static void main(String[] args) throws UnknownHostException {
		// TODO Auto-generated method stub
		MongoClient mongo = new MongoClient("localhost", 27017);
		DB db = mongo.getDB("test");
		DBCollection coll = db.getCollection("others");
	
		mongo.setWriteConcern(WriteConcern.JOURNALED);
		insert(coll);

	}

	public static void insert(DBCollection coll) {
		BasicDBObject doc = new BasicDBObject("name", "MongoDB")
				.append("type", "database").append("count", 1)
				.append("info", new BasicDBObject("x", 203).append("y", 102));
		coll.insert(doc);
	}
	
	public static void find(DBCollection collection){
		DBCursor cursor = collection.find();
	}
	
	public static void getCollections(DB db){
		Set<String> colls = db.getCollectionNames();

		for (String s : colls) {
			System.out.println(s);
		}

	}
	
	public static void findFirst(){
		//DBObject myDoc = coll.findOne();
		//System.out.println(myDoc);
	}

}
