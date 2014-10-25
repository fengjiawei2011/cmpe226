package cmpe226.project1.github.schema2.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import cmpe226.project1.util.HibernateUtil;
import cmpe226.project1.util.MongoUtil;
import cmpe226.project1.util.PostgresJdbcUtil;

/**
 * @author lingzhang
 * 0rd Normal Form :There are only 1 table : event_single_table
 * Query3: find how many unique actors
 * 
 * SQLs query:
 * select count(distinct(actor_login))
 * from event_single_table;
 */
public class S2Query3jdbc {
	
	public static void query3jdbc() throws Exception{
    	
		System.out.println("Connecting to ProsgreSQL database...");
	    Connection conn = PostgresJdbcUtil.getDBconnection();
	    System.out.println("Start query3 for shema2(0Normal formal)...");	    	    
	    String sql;
	    sql = "select count(distinct(actor_login)) as number from event_single_table";
	    PreparedStatement ps=conn.prepareStatement(sql);
	    ResultSet rs = ps.executeQuery();
	    while (rs.next()) {
	    	
	    	long count=rs.getLong("number");
	    	System.out.println("result for schema2 query3 jdbc version: actor number :"+count);
	    }
	    
	    conn.close();
	    
}

	public static void main(String[] args) throws Exception{
	long begin = System.currentTimeMillis();
	query3jdbc();
	long end = System.currentTimeMillis();
	MongoUtil.printStat(begin, end);
	
	}

}
