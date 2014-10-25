package cmpe226.project1.github.schema2.query;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import cmpe226.project1.util.MongoUtil;
import cmpe226.project1.util.PostgresJdbcUtil;


/**
 * @author lingzhang
 * JDBC version
 * Schema2--0 Normal Form :There are only 1 table : event_single_table
 * Query1: find the actor that works on largest number of repository
 * 
 * SQLs query:
 * select actor_login, count(distinct(repo_id)) as number_of_worked_repo 
 * from event_single_table
 * group by actor_login
 * order by number_of_worked_repo desc
 * limit 1;
 */

public class S2Query1jdbc {
	
    public static void query1jdbc() throws Exception{
    	
    			System.out.println("Connecting to PostgreSQL database...");
    		    Connection conn = PostgresJdbcUtil.getDBconnection();
    		    System.out.println("Start query1 for shema2(0Normal formal)...");	    	    
    		    String sql;
    		    sql = "select actor_login,count(distinct(repo_id)) as number_of_worked_repo "
    		    		+ "from event_single_table "
    		    		+ "group by actor_login "
    		    		+ "order by number_of_worked_repo desc "
    		    		+ "limit 1";
    		    PreparedStatement ps=conn.prepareStatement(sql);
    		    ResultSet rs = ps.executeQuery();
    		    while (rs.next()) {
    		    	String actorlogin = rs.getString("actor_login");
    		    	long count=rs.getLong("number_of_worked_repo");
    		    	System.out.println("result is "+ actorlogin + ", "+count);
    		    }
    		    
    		    conn.close();
    		    
    }
    
    public static void main(String[] args) throws Exception{
    	long begin = System.currentTimeMillis();
    	query1jdbc();
		long end = System.currentTimeMillis();
		MongoUtil.printStat(begin, end);
    	
    }

}
