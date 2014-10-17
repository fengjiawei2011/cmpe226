package cmpe226.project1.github.schema2.dataloader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.zip.GZIPInputStream;

import org.hibernate.Session;
import org.hibernate.Transaction;

import cmpe226.project1.github.schema2.model.Actor;
import cmpe226.project1.github.schema2.model.Event;
import cmpe226.project1.github.schema2.model.EventMaper;
import cmpe226.project1.github.schema2.model.Repository;
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
		String url = "http://data.githubarchive.org/2014-10-05-22.json.gz";
		// String url = "http://data.githubarchive.org/2012-04-11-15.json.gz";
		Loader.loadArchive(url);
	}

	@SuppressWarnings("finally")
	public static void loadArchive(String url) throws IOException {
		InputStream inputStream = new URL(url).openStream();
		Gson gson = new Gson();
		InputStream gzipStream = new GZIPInputStream(inputStream);
		JsonReader reader = new JsonReader(new InputStreamReader(gzipStream,
				"UTF-8"));
		reader.setLenient(true);

		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			long begin = System.currentTimeMillis();

			Transaction tx = session.beginTransaction();
			System.out.println("Start Uploading .......");
			int n = 0;
			while (reader.hasNext() && reader.peek() != JsonToken.END_DOCUMENT) {

				EventMaper eventMapper = gson.fromJson(reader, EventMaper.class);

				Event event_1 = getNewEvent(eventMapper);
				System.out.println(event_1.toString() + " " + n);

				session.save(event_1);
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

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			reader.close();
			return;
		}
	}

	public static cmpe226.project1.github.schema2.model.Event getNewEvent(
			EventMaper oldEvent) {
		Event newEvent = new Event();

		Actor actor = oldEvent.getActor();
		if (actor != null) {
			newEvent.setActor_blog(actor.getBlog());
			newEvent.setActor_company(actor.getCompany());
			newEvent.setActor_email(actor.getEmail());
			newEvent.setActor_location(actor.getLocation());
			newEvent.setActor_login(actor.getLogin());
			newEvent.setActor_name(actor.getName());
			newEvent.setActor_type(actor.getType());
		}
		Repository rep = oldEvent.getRepository();
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
