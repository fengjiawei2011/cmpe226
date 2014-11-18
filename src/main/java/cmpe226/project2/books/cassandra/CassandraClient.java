package cmpe226.project2.books.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

/*
 * Cassandra 2.1.2
 */

public class CassandraClient {
	private final Cluster cluster;
	private final Session session;

	public CassandraClient(String node) {
		System.out.println("[Build connection]");
		cluster = Cluster.builder().addContactPoint(node).build();
		session = cluster.connect();
	}

	public Session getSession() {
		return this.session;
	}

	public void close() {
		System.out.println("[Close connection]");
		session.close();
		cluster.close();
	}
	
	public static void main(String[] args) {
		CassandraClient client = new CassandraClient("localhost");
		client.close();
	}
}
