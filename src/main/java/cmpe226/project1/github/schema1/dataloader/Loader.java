package cmpe226.project1.github.schema1.dataloader;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

import org.hibernate.Session;
import org.hibernate.Transaction;

import cmpe226.project1.github.schema1.model.Actor;
import cmpe226.project1.github.schema1.model.Event;
import cmpe226.project1.github.schema1.model.Repository;
import cmpe226.project1.util.HibernateUtil;
import cmpe226.project1.util.MongoUtil;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;


// schema 1, Third Normal form
public class Loader {

	static String dataDir = System.getProperty("user.dir") + "/data/";
	
	public static void main(String[] args) throws IOException {
		int records=0;
		System.out.println("\n**************Schema1 3rd Normal Form**************");
		
		long begin = System.currentTimeMillis();
		for (int i=0; i<24; i++){
			try {
				String filename ="2014-10-09-"+i+".json.gz";
				
				records += Loader.loadArchive(filename);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		long end = System.currentTimeMillis();
		
		HibernateUtil.getSessionFactory().close();
		
		System.out.println("Data Uploaded.");
		System.out.println("Total records " + records);
	    
	    MongoUtil.printStat(begin, end);
	}
	
	
	public static int loadArchive(String fn){
		
		int n = 0;
		JsonReader reader = null;
		Transaction tx = null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		try {
			InputStream inputStream = new FileInputStream(dataDir + fn);
		    Gson gson = new Gson();
		    InputStream gzipStream = new GZIPInputStream(inputStream);
		    reader = new JsonReader(new InputStreamReader(gzipStream, "UTF-8"));
		    reader.setLenient(true);
		    
			tx = session.beginTransaction();	
			System.out.println("Start Uploading for " +fn+ " to schema1-3NF");
		    while (reader.hasNext() && reader.peek() != JsonToken.END_DOCUMENT) {
		    	
		    	Event event = gson.fromJson(reader, Event.class);
		    	
		    	Actor actor = event.getActor();
				if(actor != null) {
					// check whether the actor is in DB by "login"
					Object search = session.createQuery("from Actor where login = :login")
							.setString("login", actor.getLogin())
							.uniqueResult();
					if(search == null) {
						session.save(actor);
					} else {
						event.setActor((Actor) search);
					}
				}
				
				Repository rep = event.getRepository();
				if(rep != null && session.get(Repository.class, rep.getId()) == null){
					session.save(rep);
				}
				
				session.save(event); 
				n++;
				
				if (n % 2048 == 0) {
					session.flush();
					session.clear();
				}
		    }
		    tx.commit();
		    reader.close();
		      
		} catch(Exception e){ 
			if (tx != null) tx.rollback();
			System.out.println("data upload fail!!" + e.getMessage());
		} finally {
			session.close();
		}
		return n;
	}

}
