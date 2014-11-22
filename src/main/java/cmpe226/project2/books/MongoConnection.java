package cmpe226.project2.books;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cmpe226.project2.books.mongo.schema.Book;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.WriteConcern;


/*
 * The file is for testing the basic operations toward MongoDB
 */
public class MongoConnection {
	public static final String COLLECTION_OTHERS = "others";

	public static void main(String[] args) throws UnknownHostException {
		// TODO Auto-generated method stub
		MongoClient mongo = new MongoClient("localhost", 27017);
		DB db = mongo.getDB("test");
		DBCollection coll = db.getCollection(COLLECTION_OTHERS);
		// find(coll);
		// update(db);
		// replace(db);
		// updateByInc(db);
		//findOne(mongo, coll);
		//Query(coll);
		//findIn(coll);
		insert(mongo,coll);
		showAll(coll);
	}

	public static void insert(MongoClient mongo, DBCollection coll) {
		mongo.setWriteConcern(WriteConcern.JOURNALED);
		BasicDBObject doc = new BasicDBObject()
				.append("firstname", "jiawei")
				.append("lastname", "feng");
				
		coll.insert(doc);
	}
	
	public static void insertArray(MongoClient mongo, DBCollection coll) {
		mongo.setWriteConcern(WriteConcern.JOURNALED);
		BasicDBObject doc = new BasicDBObject()
				.append("firstname", "jiawei")
				.append("lastname", "feng");
				
		coll.insert(doc);
	}
	
	public static void insertPut(MongoClient mongo, DBCollection coll) {
		mongo.setWriteConcern(WriteConcern.JOURNALED);
		BasicDBObject doc = new BasicDBObject();
		doc.put("firstname", "jiawei");
		doc.put("lastname", "feng");
				
		coll.insert(doc);
	}

	public static void showAll(DBCollection collection) {
		DBCursor cursor = collection.find();
		while (cursor.hasNext()) {
			System.out.println(cursor.next());
		}
	}

	public static void getCollections(DB db) {
		Set<String> colls = db.getCollectionNames();

		for (String s : colls) {
			System.out.println(s);
		}

	}

	public static void replace(DB db) {
		DBCollection collection = db.getCollection(COLLECTION_OTHERS);
		showAll(collection);
		BasicDBObject obj = new BasicDBObject();
		obj.put("count", 20000);
		BasicDBObject q = new BasicDBObject();
		q.append("count", 2);
		collection.update(q, obj);
		showAll(collection);
	}

	public static void update(DB db) {
		DBCollection collection = db.getCollection(COLLECTION_OTHERS);
		showAll(collection);
		BasicDBObject obj = new BasicDBObject();
		obj.append("$set", new BasicDBObject().append("count", 2));
		collection.update(new BasicDBObject("count", 900), obj);
		showAll(collection);

		// DBCursor cursor = collection.find();
		// while(cursor.hasNext()){
		// DBObject obj1 = cursor.next();
		// System.out.println(obj);
		// obj.put("count", 10000);
		// collection.update(new BasicDBObject("count", 1), obj);
		// System.out.println(obj);
		// }

	}
	
	public static void findIn(DBCollection c){
		BasicDBObject inQuery = new BasicDBObject();
		List<Integer> list = new ArrayList<Integer>();
		list.add(1);
		list.add(40002);
		inQuery.put("count", new BasicDBObject("$in", list));
		DBCursor cursor = c.find(inQuery);
		while (cursor.hasNext()) {
			System.out.println(cursor.next());
		}
	}

	public static void Query(DBCollection c) {
		BasicDBObject obj = new BasicDBObject();
		//obj.put("count", 1);
		BasicDBObject field = new BasicDBObject();
		field.put("count", 1);
		DBCursor cursor = c.find(obj, field);
		while (cursor.hasNext()) {
			System.out.println(cursor.next());
		}

	}

	public static void updateByInc(DB db) {
		DBCollection collection = db.getCollection(COLLECTION_OTHERS);
		showAll(collection);
		BasicDBObject obj = new BasicDBObject();
		obj.append("$inc", new BasicDBObject().append("count", 20000));
		BasicDBObject q = new BasicDBObject();
		q.append("count", 20002);
		collection.update(q, obj);
		showAll(collection);
	}

	public static void findOne(MongoClient mongo, DBCollection coll) {
		// showAll(coll);
		// insert(mongo, coll);
		// System.out.println("************************");
		DBObject obj = coll.findOne();
		obj.toString();
		System.out.println(obj.toString());
	}
}
