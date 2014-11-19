package cmpe226.project2.books.cassandra;

import java.util.Collection;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ProtocolVersion;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.UserType;

/*
 * Cassandra 2.1.2
 */

public class CassandraClient {
	private final Cluster cluster;
	private final Session session;

	public CassandraClient(String node) {
		System.out.println("[Build connection]");
		cluster = Cluster.builder().withProtocolVersion(ProtocolVersion.V3)
				.addContactPoint(node).build();
		session = cluster.connect();
		
//		Collection<UserType> types = cluster.getMetadata().getKeyspace("cmpe226").getUserTypes();
//		System.out.println(types);
//		for (UserType type : types) {
//			System.out.println(type.getFieldNames());
//		}
		
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
