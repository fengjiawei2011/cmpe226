package cmpe226.project1.github.schema2.dataloader;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.zip.GZIPInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import cmpe226.project1.github.schema2.model.ActorSingle;
import cmpe226.project1.github.schema2.model.EventSingle;
import cmpe226.project1.github.schema2.model.EventMaper;
import cmpe226.project1.github.schema2.model.RepositorySingle;
import cmpe226.project1.util.MongoUtil;
import cmpe226.project1.util.PostgresJdbcUtil;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

// schema 2 , zero normal form.
public class JdbcLoader {
	
	static String dataDir = System.getProperty("user.dir") + "/data/";

	public static void main(String[] args) throws Exception {
		
		int rows = 0;
		System.out.println("\n**************Schema2 0 Normal Form**************");

		long begin = System.currentTimeMillis();
		for (int i = 0; i < 24; i++) {
			String filename ="2014-10-09-"+i+".json.gz";

			try {
				rows += JdbcLoader.loadArchive(filename);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		long end = System.currentTimeMillis();

		System.out.println("Data Uploaded.");
		System.out.println("Total records " + rows);

		MongoUtil.printStat(begin, end);

	}

	public static int loadArchive(String fn) throws Exception {
		int n = 0;
		JsonReader reader = null;
		Connection connection = PostgresJdbcUtil.getDBconnection();
		try {
			InputStream inputStream = new FileInputStream(dataDir + fn);
			Gson gson = new Gson();
			InputStream gzipStream = new GZIPInputStream(inputStream);
			reader = new JsonReader(new InputStreamReader(gzipStream, "UTF-8"));
			reader.setLenient(true);

			System.out
					.println("Start Uploading for " + fn + " to schema2-0NF");

			while (reader.hasNext() && reader.peek() != JsonToken.END_DOCUMENT) {

				EventMaper eventMapper = gson
						.fromJson(reader, EventMaper.class);

				EventSingle event_1 = getNewEvent(eventMapper);
				// System.out.println(event_1.toString() + " " + n);

				try {

					PreparedStatement stmt = connection
							.prepareStatement("insert into event_single_table (actor_blog, actor_company, "
									+ "actor_email, actor_location, actor_login, actor_name, actor_type, "
									+ "event_created_at, event_is_public, event_type, event_url, gravatar_id, "
									+ "repo_Url, repo_forks, repo_has_downloads, repo_has_issues, repo_has_wiki, "
									+ "repo_id, repo_is_forked, repo_is_private, repo_language, repo_master_branch, "
									+ "repo_name, repo_open_issues, repo_owner, repo_pushed_at, repo_repoCreated_at, "
									+ "repo_size, repo_stargazers, repo_updated_at, repo_watchers) "
									+ "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
									+ "?, ?, ?, ?, ?, ?, ?, ?, ?)");
					stmt.setString(1, event_1.getActor_blog());
					stmt.setString(2, event_1.getActor_company());
					stmt.setString(3, event_1.getActor_email());
					stmt.setString(4, event_1.getActor_location());
					stmt.setString(5, event_1.getActor_login());
					stmt.setString(6, event_1.getActor_name());
					stmt.setString(7, event_1.getActor_type());
					stmt.setString(8, event_1.getEvent_created_at());
					stmt.setBoolean(9, event_1.isEvent_is_public());
					stmt.setString(10, event_1.getEvent_type());
					stmt.setString(11, event_1.getEvent_url());
					stmt.setString(12, event_1.getGravatar_id());
					stmt.setString(13, event_1.getRepo_Url());
					stmt.setInt(14, event_1.getRepo_forks());
					stmt.setBoolean(15, event_1.isRepo_has_downloads());
					stmt.setBoolean(16, event_1.isRepo_has_issues());
					stmt.setBoolean(17, event_1.isRepo_has_wiki());
					stmt.setLong(18, event_1.getRepo_id());
					stmt.setBoolean(19, event_1.isRepo_is_forked());
					stmt.setBoolean(20, event_1.isRepo_is_private());
					stmt.setString(21, event_1.getRepo_language());
					stmt.setString(22, event_1.getRepo_master_branch());
					stmt.setString(23, event_1.getRepo_name());
					stmt.setInt(24, event_1.getRepo_open_issues());
					stmt.setString(25, event_1.getRepo_owner());
					stmt.setString(26, event_1.getRepo_pushed_at());
					stmt.setString(27, event_1.getRepo_repoCreated_at());
					stmt.setInt(28, event_1.getRepo_size());
					stmt.setInt(29, event_1.getRepo_stargazers());
					stmt.setString(30, event_1.getRepo_updated_at());
					stmt.setInt(31, event_1.getRepo_watchers());
					stmt.executeUpdate();
					stmt.close();
//					System.out.println(stmt);

				} catch (SQLException sqle) {
					System.err.println("Something exploded running the insert: "
									+ sqle.getMessage());
				}
				n++;
			}

		} catch (Exception e) {
			System.out.println("data upload fail!!" + e.getMessage());
		} finally {
			closeConnection(connection);
			reader.close();
		}
		return n;
	}

	public static EventSingle getNewEvent(
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

	private static void closeConnection(Connection connection)
			throws SQLException {
		if (connection != null && !connection.isClosed()) {
			connection.close();
		}
	}

}
