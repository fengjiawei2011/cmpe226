package cmpe226.project1.github.schema1.dataloader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.zip.GZIPInputStream;

//import org.hibernate.Query;
//import org.hibernate.Session;
//import org.hibernate.Transaction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import cmpe226.project1.github.schema1.model.Actor;
import cmpe226.project1.github.schema1.model.Event;
import cmpe226.project1.github.schema1.model.Repository;
import cmpe226.project1.util.HibernateUtil;
import cmpe226.project1.util.MongoUtil;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;


public class JdbcLoader {

	public static void main(String[] args) throws Exception {
		String url = "http://data.githubarchive.org/2014-10-05-22.json.gz";
		// String url = "http://data.githubarchive.org/2012-04-11-15.json.gz";
		Loader.loadArchive(url);
	}

	@SuppressWarnings("finally")
	public static void loadArchive(String url) throws Exception {
		InputStream inputStream = new URL(url).openStream();
		Gson gson = new Gson();
		InputStream gzipStream = new GZIPInputStream(inputStream);
		JsonReader reader = new JsonReader(new InputStreamReader(gzipStream,
				"UTF-8"));
		reader.setLenient(true);

		Connection connection = getConnection();
		try {
			long begin = System.currentTimeMillis();
			System.out.println("Start Uploading .......");
			int n = 0;
			while (reader.hasNext() && reader.peek() != JsonToken.END_DOCUMENT) {

				Event event = gson.fromJson(reader, Event.class);

				Actor actor = event.getActor();
				if (actor != null) {
					// check whether the actor is in DB by "login"
					PreparedStatement ps = connection
							.prepareStatement("select login from actor where login = ?");
					ps.setString(1, actor.getLogin());
					ResultSet rs = ps.executeQuery();
					if (!rs.next()) {
						try {
							PreparedStatement stmt = connection
									.prepareStatement("insert into actor (blog, company, email, gravatar_id, location, login, name, type) values (?, ?, ?, ?, ?, ?, ?, ?)");
							stmt.setString(1, actor.getBlog());
							stmt.setString(2, actor.getCompany());
							stmt.setString(3, actor.getEmail());
							stmt.setString(4, actor.getGravatar_id());
							stmt.setString(5, actor.getLocation());
							stmt.setString(6, actor.getLogin());
							stmt.setString(7, actor.getName());
							stmt.setString(8, actor.getType());
							// stmt.setLong(9, actor.getId());
							stmt.executeUpdate();
							stmt.close();
							System.out.println(stmt);
						} catch (SQLException sqle) {
							System.err
									.println("Something exploded running the insert: "
											+ sqle.getMessage());
						}

					}

					else {
						// TODO update actor attributes
						// event.setActor((Actor) query.list().get(0)); ??
						try {

							PreparedStatement stmt = connection
									.prepareStatement("update actor set (blog, company, email, gravatar_id, location, name, type)=(?, ?, ?, ?, ?, ?, ?) where login=?");
							stmt.setString(1, actor.getBlog());
							stmt.setString(2, actor.getCompany());
							stmt.setString(3, actor.getEmail());
							stmt.setString(4, actor.getGravatar_id());
							stmt.setString(5, actor.getLocation());
							stmt.setString(6, actor.getName());
							stmt.setString(7, actor.getType());
							stmt.setString(8, actor.getLogin());
							stmt.executeUpdate();
							stmt.close();
							System.out.println(stmt);
						} catch (SQLException sqle) {
							System.err
									.println("Something exploded running the update: "
											+ sqle.getMessage());
						}

					}

				}

				Repository rep = event.getRepository();

				if (rep != null) {
					// TODO check whether the repository is in DB by "id", then
					// save or update repository

					PreparedStatement ps = connection
							.prepareStatement("select repo_id from repository where repo_id = ?");
					ps.setLong(1, rep.getId());
					ResultSet rs = ps.executeQuery();
					if (!rs.next()) {
						try {
							PreparedStatement stmt = connection
									.prepareStatement("insert into repository (created_at, forks, has_downloads, "
											+ "has_issues, has_wiki, is_forked, is_private, language, master_branch, "
											+ "name, open_issues, owner, pushed_at, size, stargazers, updated_at, url, "
											+ "watchers, repo_id) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
							stmt.setString(1, rep.getCreated_at());
							stmt.setInt(2, rep.getForks());
							stmt.setBoolean(3, rep.isHas_downloads());
							stmt.setBoolean(4, rep.isHas_issues());
							stmt.setBoolean(5, rep.isHas_wiki());
							stmt.setBoolean(6, rep.isIs_forked());
							stmt.setBoolean(7, rep.isIs_private());
							stmt.setString(8, rep.getLanguage());
							stmt.setString(9, rep.getMaster_branch());
							stmt.setString(10, rep.getName());
							stmt.setInt(11, rep.getOpen_issues());
							stmt.setString(12, rep.getOwner());
							stmt.setString(13, rep.getPushed_at());
							stmt.setInt(14, rep.getSize());
							stmt.setInt(15, rep.getStargazers());
							stmt.setString(16, rep.getUpdated_at());
							stmt.setString(17, rep.getUrl());
							stmt.setInt(18, rep.getWatchers());
							stmt.setLong(19, rep.getId());
							stmt.executeUpdate();
							stmt.close();
							System.out.println(stmt);
						} catch (SQLException sqle) {
							System.err
									.println("Something exploded running the insert: "
											+ sqle.getMessage());
						}
					}
				}

				try {

					PreparedStatement psEvent = connection
							.prepareStatement("select actor_id from actor where login = ?");
					psEvent.setString(1, actor.getLogin());
					ResultSet rsEvent = psEvent.executeQuery();
					while (rsEvent.next()) {
						// Read values using column name
						long actor_id = rsEvent.getLong("actor_id");

						PreparedStatement stmtEvent = connection
								.prepareStatement("insert into event (actor_id, created_at, is_public, repo_id, "
										+ "type, url) values (?, ?, ?, ?, ?, ?)");
						stmtEvent.setLong(1, actor_id);
						stmtEvent.setString(2, event.getCreated_at());
						stmtEvent.setBoolean(3, event.isIs_public());
						stmtEvent.setLong(4, rep.getId());
						stmtEvent.setString(5, event.getType());
						stmtEvent.setString(6, event.getUrl());
						stmtEvent.executeUpdate();
						stmtEvent.close();
						System.out.println(stmtEvent);
					}
				} catch (SQLException sqle) {
					System.err
							.println("Something exploded running the insert: "
									+ sqle.getMessage());
				}

				n++;

			}

			long end = System.currentTimeMillis();

			System.out.println("Data Uploaded.");
			System.out.println("Total records " + n);

			MongoUtil.printStat(begin, end);

		} finally {

			closeConnection(connection);
			reader.close();
			return;
		}
	}

	private static Connection getConnection() throws Exception {
		Connection connection = null;
		Class.forName("org.postgresql.Driver");
		connection = DriverManager.getConnection(
				"jdbc:postgresql://localhost:5432/lillianj", "Lillian", "");
		return connection;
	}

	private static void closeConnection(Connection connection)
			throws SQLException {
		if (connection != null && !connection.isClosed()) {
			connection.close();
		}
	}

}
