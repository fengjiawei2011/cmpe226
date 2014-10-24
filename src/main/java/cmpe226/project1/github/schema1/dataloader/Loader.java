package cmpe226.project1.github.schema1.dataloader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.zip.GZIPInputStream;

import org.hibernate.Query;
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


class DataRecord{
	String url;
}

// schema 1, Third Normal form
public class Loader {

	public static void main(String[] args) throws IOException {

		long begin = System.currentTimeMillis();
		int records=0;
		Session session =null;
		System.out.println("\n**************Schema1 3rd Normal Form**************");
		
		for (int i=0; i<24; i++){
			try {
				String url ="http://data.githubarchive.org/2014-10-05-"+i+".json.gz";
				session = HibernateUtil.getSessionFactory().openSession();
				
				records += Loader.loadArchive(url,session);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				//continue;
			}finally{
				session.close();
			}	
		}
		
		long end = System.currentTimeMillis();
		
		System.out.println("Data Uploaded.");
		System.out.println("Total records " + records);
	    
	    MongoUtil.printStat(begin, end);
	}
	
	
	@SuppressWarnings("finally")
	public static int loadArchive(String url, Session session) throws IOException{
		
		int n = 0;
		JsonReader reader = null;
		try {
			InputStream inputStream = new URL(url).openStream();
		    Gson gson = new Gson();
		    InputStream gzipStream = new GZIPInputStream(inputStream);
		    reader = new JsonReader(new InputStreamReader(gzipStream, "UTF-8"));
		    reader.setLenient(true);
		    
			Transaction tx = session.beginTransaction();	
			System.out.println("Start Uploading for " +url+ " to schema1-3NF");
		    while (reader.hasNext() && reader.peek() != JsonToken.END_DOCUMENT) {
		    	
		    	Event event = gson.fromJson(reader, Event.class);
		    	
		    	Actor actor = event.getActor();
				if(actor != null) {
					// check whether the actor is in DB by "login"
					Query query = session.createQuery("from Actor where login = :login");
					query.setString("login", actor.getLogin());
					
					if(query.list().size() == 0)
						session.save(actor);
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
		      
		} catch(Exception e){ 
			System.out.println("data upload fail!!");
		}
		finally {
			reader.close();			
		}
		return n;
	}

}
