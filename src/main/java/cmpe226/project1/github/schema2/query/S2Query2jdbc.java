package cmpe226.project1.github.schema2.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import cmpe226.project1.util.MongoUtil;
import cmpe226.project1.util.PostgresJdbcUtil;

/**
 * @author lingzhang
 * JDBC version
 * Schema2 -- 0rd Normal Form :There are only 1 table : event_single_table
 * Query2: find the most popular language (the language that largest number of actor works on)
 * 
 * SQLs query:
 * select repo_language, count(distinct(actor_login)) as number_of_users
 * from event_single_table
 * group by repo_language
 * order by number_of_users desc
 * limit 2;
 */
public class S2Query2jdbc {
	
	public static void query2jdbc() throws Exception{
    	
			System.out.println("Connecting to ProsgreSQL database...");
		    Connection conn = PostgresJdbcUtil.getDBconnection();
		    System.out.println("Start query2 for shema2(0Normal formal)...");	    	    
		    String sql;
		    sql = "select repo_language, count(distinct(actor_login)) as number_of_users "
		    		+ "from event_single_table "
		    		+ "group by repo_language "
		    		+ "order by number_of_users desc "
		    		+ "limit 2";
		    PreparedStatement ps=conn.prepareStatement(sql);
		    ResultSet rs = ps.executeQuery();
		    while (rs.next()) {
		    	String repolang = rs.getString("repo_language");
		    	long count=rs.getLong("number_of_users");
		    	System.out.println("result for schema2 query2 jdbc version is: "+ repolang+ ", "+count);
		    }
		    
		    conn.close();
		    
	}

	public static void main(String[] args) throws Exception{
	long begin = System.currentTimeMillis();
	query2jdbc();
	long end = System.currentTimeMillis();
	MongoUtil.printStat(begin, end);

	}
}
