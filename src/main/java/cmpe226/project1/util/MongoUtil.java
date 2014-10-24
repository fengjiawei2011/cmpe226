package cmpe226.project1.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class MongoUtil {
	
	private static final DBCollection logCollection = connectMongo();
	
	private static DBCollection connectMongo() {
		try {
			
			System.out.println("Connection MongoDB ......");
			final MongoClient mongoClient = new MongoClient("localhost" , 27017 );			
			return mongoClient.getDB("logstash").getCollection("postgresql");
			
		} catch (Throwable ex) {
			System.err.println("Initial MonogDB connection failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}
	
	public static DBCollection getMongoCollection(){
		return logCollection;
	}
	
	public static double avg(List<Double> data){
		double sum = 0;
		int n = 0;
		
		for (Double num : data){
			sum += num;
			n++;
		}
		if (n ==0) return -1;
		else return sum/n;
	}
	
	public static void printStat (long begin, long end){
		System.out.println("Start at " + new Date(begin));
	    System.out.println("End at " + new Date(end));
	    System.out.println("Total used " + (end - begin) + " msec");
	    
	    List<Double> cpuSample = new ArrayList<Double>();
	    List<Double> memSample = new ArrayList<Double>();
	    List<Double> rtpsSample = new ArrayList<Double>();
	    List<Double> wtpsSample = new ArrayList<Double>();

	    // get performance data out of MongoDB
	    DBCursor cursor = logCollection.find(
	    		new BasicDBObject("@timestamp", new BasicDBObject("$gt", new Date(begin)).append("$lte", new Date(end))));
	    
	    try {
	        while (cursor.hasNext()) {
	        	DBObject item = cursor.next();
	            
	            if (item.get("type").equals("io")){
	            	rtpsSample.add((Double) item.get("rtps"));
	            	wtpsSample.add((Double) item.get("wtps"));
	            } else {
	            	cpuSample.add((Double) item.get("cpu-usage"));
	            	memSample.add((Double) item.get("memory-usage"));
	            }
	        }
	        System.out.println("CPU: " + cpuSample);
	        System.out.println("Memory: " + memSample);
	        System.out.println("RTPS: " + rtpsSample);
	        System.out.println("WTPS: " + wtpsSample);
	        System.out.println(String.format("Avg(CPU, Memory, RTPS, WTPS): %.2f %.2f %.2f %.2f" , avg(cpuSample), avg(memSample), avg(rtpsSample), avg(wtpsSample)));
	        
	    } finally {
	        cursor.close();
	    }
	}
}
