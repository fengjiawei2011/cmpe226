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
 * Query1: find the actor that works on largest number of repository
 * 
 * SQLs query:
 * select a.login, count(distinct(e.repo_id)) as number_of_worked_repo 
 * from event e, actor a
 * where e.actor_id=a.actor_id
 * group by a.login
 * order by number_of_worked_repo desc
 * limit 1;
 */
public class S1Query1jdbc {
	
    public static void s1query1jdbc() throws Exception{
    	
		System.out.println("Connecting to PostgreSQL database...");
	    Connection conn = PostgresJdbcUtil.getDBconnection();
	    System.out.println("Start query1 for shema1(3Normal formal)...");	    	    
	    String sql;
	    sql = "select a.login as login, count(distinct(e.repo_id)) as number_of_worked_repo "
	    		+ "from event e, actor a "
	    		+ "where e.actor_id=a.actor_id "
	    		+ "group by a.login "
	    		+ "order by number_of_worked_repo desc "
	    		+ "limit 1";
	    PreparedStatement ps=conn.prepareStatement(sql);
	    ResultSet rs = ps.executeQuery();
	    while (rs.next()) {
	    	String actorlogin = rs.getString("login");
	    	long count=rs.getLong("number_of_worked_repo");
	    	System.out.println("result is "+ actorlogin + ", "+count);
	    }
	    
	    conn.close();
	    
    }

	public static void main(String[] args) throws Exception{
	long begin = System.currentTimeMillis();
	s1query1jdbc();
	long end = System.currentTimeMillis();
	MongoUtil.printStat(begin, end);
	
	}


}
