package cmpe226.project1.github.schema2.dataloader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.zip.GZIPInputStream;

import org.hibernate.Session;
import org.hibernate.Transaction;

import cmpe226.project1.github.schema2.model.ActorSingle;
import cmpe226.project1.github.schema2.model.EventSingle;
import cmpe226.project1.github.schema2.model.EventMaper;
import cmpe226.project1.github.schema2.model.RepositorySingle;
import cmpe226.project1.util.HibernateUtil;
import cmpe226.project1.util.MongoUtil;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

class DataRecord {
	String url;
}

// schema 2 , zero normal form.
public class Loader {

	public static void main(String[] args) throws IOException {
		String domain = "http://data.githubarchive.org/";
		int rows = 0;
		Session session = null;
		long begin = System.currentTimeMillis();
		System.out.println("\n**************Schema2 0 Normal Form**************");
		
		for (int i = 0; i < 24; i++) {
			String url = "";

			url += "2014-10-05-" + i + ".json.gz";
			
			try {
				
				session = HibernateUtil.getSessionFactory().openSession();
				rows += Loader.loadArchive(domain + url, session);
			} catch (Exception e) {
				//System.out.println("no data");
			}finally{
				session.close();
			}
		}
		long end = System.currentTimeMillis();

		System.out.println("Data Uploaded.");
		System.out.println("Total records " + rows);

		MongoUtil.printStat(begin, end);

	}

	@SuppressWarnings("finally")
	public static int loadArchive(String url, Session session) throws IOException {
		int n = 0;
		JsonReader reader = null;
		try {
			InputStream inputStream = new URL(url).openStream();
			Gson gson = new Gson();
			InputStream gzipStream = new GZIPInputStream(inputStream);
			reader = new JsonReader(new InputStreamReader(
					gzipStream, "UTF-8"));
			reader.setLenient(true);			

			Transaction tx = session.beginTransaction();
			System.out.println("Start Uploading for "+ url + " to schema2-0NF");
			
			while (reader.hasNext() && reader.peek() != JsonToken.END_DOCUMENT) {

				EventMaper eventMapper = gson
						.fromJson(reader, EventMaper.class);

				EventSingle event_1 = getNewEvent(eventMapper);
				//System.out.println(event_1.toString() + " " + n);

				session.save(event_1);
				n++;

				if (n % 2048 == 0) {
					session.flush();
					session.clear();
				}
			}
			tx.commit();

		} catch (Exception e) {
			System.out.println("data file not found!");
		}finally{
			reader.close();
		}
		return n;
	}

	public static cmpe226.project1.github.schema2.model.EventSingle getNewEvent(
			EventMaper oldEvent) {
		EventSingle newEvent = new EventSingle();

		ActorSingle actor = oldEvent.getActor();
		if (actor != null) {
			newEvent.setActor_blog(actor.getBlog());
			newEvent.setActor_company(actor.getCompany());
			newEvent.setActor_email(actor.getEmail());
			newEvent.setActor_location(actor.getLocation());
			newEvent.setActor_login(actor.getLogin());
			newEvent.setActor_name(actor.getName());
			newEvent.setActor_type(actor.getType());
		}
		RepositorySingle rep = oldEvent.getRepository();
		if (rep != null) {
			newEvent.setRepo_id(rep.getId());
			newEvent.setRepo_forks(rep.getForks());
			newEvent.setRepo_has_downloads(rep.isHas_downloads());
			newEvent.setRepo_has_issues(rep.isHas_issues());
			newEvent.setRepo_has_wiki(rep.isHas_wiki());
			newEvent.setRepo_is_forked(rep.isIs_forked());
			newEvent.setRepo_is_private(rep.isIs_private());
			newEvent.setRepo_language(rep.getLanguage());
			newEvent.setRepo_master_branch(rep.getMaster_branch());
			newEvent.setRepo_name(rep.getName());
			newEvent.setRepo_open_issues(rep.getOpen_issues());
			newEvent.setRepo_owner(rep.getOwner());
			newEvent.setRepo_pushed_at(rep.getPushed_at());
			newEvent.setRepo_repoCreated_at(rep.getCreated_at());
			newEvent.setRepo_size(rep.getSize());
			newEvent.setRepo_stargazers(rep.getStargazers());
			newEvent.setRepo_updated_at(rep.getUpdated_at());
			newEvent.setRepo_Url(rep.getUrl());
			newEvent.setRepo_watchers(rep.getWatchers());
		}

		newEvent.setEvent_created_at(oldEvent.getCreated_at());
		newEvent.setEvent_is_public(oldEvent.isIs_public());
		newEvent.setEvent_type(oldEvent.getType());
		newEvent.setEvent_url(oldEvent.getUrl());
		return newEvent;
	}

}
