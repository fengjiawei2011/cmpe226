package cmpe226.project1.github.schema1.dataloader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;

import org.hibernate.Session;
import org.hibernate.Transaction;

import cmpe226.project1.github.schema1.model.Actor;
import cmpe226.project1.github.schema1.model.Event;
import cmpe226.project1.github.schema1.model.Repository;
import cmpe226.project1.util.HibernateUtil;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

class DataRecord{
	String url;
}

public class Loader {

	public static void main(String[] args) throws IOException {
		// Loader.load("/Users/frank_feng1/tmp/datadir");
		Loader.loadArchive("http://data.githubarchive.org/2012-04-11-15.json.gz");
	}
	
	
	public static void loadArchive(String url) throws IOException{
		InputStream inputStream = new URL(url).openStream();
	    Gson gson = new Gson();
	    InputStream gzipStream = new GZIPInputStream(inputStream);
	    JsonReader reader = new JsonReader(new InputStreamReader(gzipStream, "UTF-8"));
	    reader.setLenient(true);
	    ArrayList<Event> events = new ArrayList<Event>();
	   
	    while (reader.hasNext() && reader.peek() != JsonToken.END_DOCUMENT) {
	    	Event event = gson.fromJson(reader, Event.class);
	    	//System.out.println(event.getRepository().toString());
	    	events.add(event);
//	    	if(events.size() >= 2048){
//	    		
//	    		events.clear();
//	    	}
	    }
	    
	    reader.close();
	    Loader.upload(events);
	    
	}
	
	// upload data into db
		private static void upload(ArrayList<Event> list) {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			try {
				Transaction tx = session.beginTransaction();
				for (int n = 0; n < list.size(); ++n) {
					System.out.println("uploading......." + list.get(n).toString());
					
					Actor actor = list.get(n).getActor();
					if(actor != null && session.get(Actor.class, actor.getGravatar_id()) == null)
						session.save(actor);
					
					
					Repository rep = list.get(n).getRepository();
					if(rep != null && session.get(Repository.class, rep.getId()) == null)
						session.save(rep);
					
					session.save(list.get(n)); 
					if (n % 2048 == 0) {
						session.flush();
						session.clear();
					}
				}
				tx.commit();
			} finally {
				session.close();
			}
		}
}
