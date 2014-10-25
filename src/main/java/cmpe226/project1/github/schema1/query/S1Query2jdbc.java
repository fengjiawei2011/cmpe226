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
 * Query2: find the most popular language (the language that largest number of actor works on)
 * 
 * SQLs query:
 * select r.language, count(distinct(a.login)) as number_of_users
 * from repository r, event e, actor a
 * where r.repo_id=e.repo_id and e.actor_id=a.actor_id
 * group by r.language
 * order by number_of_users desc
 * limit 1;
 */
public class S1Query2jdbc {
	
	public static void query2jdbc() throws Exception{
    	
		System.out.println("Connecting to ProsgreSQL database...");
	    Connection conn = PostgresJdbcUtil.getDBconnection();
	    System.out.println("Start query2 for shema1(3Normal formal)...");	    	    
	    String sql;
	    sql = "select r.language as repo_language, count(distinct(a.login)) as number_of_users "
	    		+ "from repository r, event e, actor a "
	    		+ "where r.repo_id=e.repo_id and e.actor_id=a.actor_id "
	    		+ "group by r.language "
	    		+ "order by number_of_users desc "
	    		+ "limit 1";
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
