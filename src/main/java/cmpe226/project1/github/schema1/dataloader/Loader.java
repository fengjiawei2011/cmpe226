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

public class Loader {

	public static void main(String[] args) throws IOException {
		String url = "http://data.githubarchive.org/2014-10-05-22.json.gz";
//		String url = "http://data.githubarchive.org/2012-04-11-15.json.gz";
		Loader.loadArchive(url);
	}
	
	public static void loadArchive(String url) throws IOException{
		InputStream inputStream = new URL(url).openStream();
	    Gson gson = new Gson();
	    InputStream gzipStream = new GZIPInputStream(inputStream);
	    JsonReader reader = new JsonReader(new InputStreamReader(gzipStream, "UTF-8"));
	    reader.setLenient(true);
	    
	    Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			long begin = System.currentTimeMillis();
			
			Transaction tx = session.beginTransaction();
			System.out.println("Start Uploading .......");
			int n = 0;
		    while (reader.hasNext() && reader.peek() != JsonToken.END_DOCUMENT) {
		    	
		    	Event event = gson.fromJson(reader, Event.class);
		    	
		    	Actor actor = event.getActor();
				if(actor != null) {
					// check whether the actor is in DB by "login"
					Query query = session.createQuery("from Actor where login = :login");
					query.setString("login", actor.getLogin());
					
					if(query.list().size() == 0)
						session.save(actor);
					else {
						//TODO update actor attributes
						event.setActor((Actor) query.list().get(0));
//						System.out.println("Actor updated");
					}
				}
				
				Repository rep = event.getRepository();
				if(rep != null && session.get(Repository.class, rep.getId()) == null)
					// TODO check whether the repository is in DB by "id", then save or update actor
					session.save(rep);
				
				session.save(event); 
				n++;
				
				if (n % 2048 == 0) {
					session.flush();
					session.clear();
				}
		    }
		    tx.commit();
		    long end = System.currentTimeMillis();
		    
		    System.out.println("Data Uploaded.");
		    System.out.println("Total records " + n);
		    
		    MongoUtil.printStat(begin, end);
		    
		} finally {
			reader.close();
			return;
		}
	}

}
