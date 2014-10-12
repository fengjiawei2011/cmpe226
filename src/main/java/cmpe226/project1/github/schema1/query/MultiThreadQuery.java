package cmpe226.project1.github.schema1.query;

import java.util.ArrayList;

import cmpe226.project1.util.MongoUtil;

public class MultiThreadQuery {
	
	private static int numberOfSubThread=50;

	
	public static void main(String[] args) throws Exception {
	    System.out.println("Main ThreadId: " + Thread.currentThread().getId());
		long start = System.currentTimeMillis();
		System.out.println("Start at:"+start);
			    
	    ArrayList<Thread> threadpool= new ArrayList<Thread>();
	    for (int i =0; i<numberOfSubThread; i++){
	    	Thread thread=new Thread(new SingleQuery()); 
	    	threadpool.add(thread);	    	
	    }
	    
	    for(Thread thread: threadpool){
	    	thread.start();
	    }
	    
	    for(Thread thread: threadpool){
	    	thread.join();
	    }
	    
	    long end = System.currentTimeMillis();
		System.out.println("End at:"+end);
		System.out.println("many small query finished, time taken:" + (end-start)+" milliseconds");
		System.out.println("Number of sub threads : "+ numberOfSubThread);
		MongoUtil.printStat(start, end);
		
	}

}
