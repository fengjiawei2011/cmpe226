package cmpe226.project1.github.schema1.query;

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
 * 3rd Normal Form :There are 3 tables: event, actor, repository
 * Query3: find how many unique actors
 * 
 * SQLs query:
 * select count(distinct(login))
 * from actor;
 * -- or  -- very fast
 * select count(actor_id)
 * from actor;
 */
public class S1Query3jdbc {
	
	public static void s1query3jdbc() throws Exception{
	    	
			System.out.println("Connecting to ProsgreSQL database...");
		    Connection conn = PostgresJdbcUtil.getDBconnection();
		    System.out.println("Start query3 for shema1(3Normal formal)...");	    	    
		    String sql;
		    sql = " select count(actor_id) as number from actor";
		    PreparedStatement ps=conn.prepareStatement(sql);
		    ResultSet rs = ps.executeQuery();
		    while (rs.next()) {
		    	
		    	long count=rs.getLong("number");
		    	System.out.println("result for schema1 query3 jdbc version: actor number :"+count);
		    }
		    
		    conn.close();
		    
	}

	public static void main(String[] args) throws Exception{
		long begin = System.currentTimeMillis();
		s1query3jdbc();
		long end = System.currentTimeMillis();
		MongoUtil.printStat(begin, end);
	
	}



}
