package cmpe226.project1.github.schema2.query;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import cmpe226.project1.util.HibernateUtil;
import cmpe226.project1.util.MongoUtil;

/**
 * @author lingzhang
 * 0rd Normal Form :There are only 1 table : event_single_table
 * Query2: find the most popular language (the language that largest number of actor works on)
 * 
 * SQLs query:
 * select repo_language, count(distinct(actor_login)) as number_of_users
 * from event_single_table
 * group by repo_language
 * order by number_of_users desc
 * limit 2;
 */
public class Query2 {
	
	@SuppressWarnings("unchecked")
	public static void findMostPopularLang(){
		Session session = HibernateUtil.getSessionFactory().openSession();
		System.out.println("Trying to find the most popular language... ");
		try {			
			session.beginTransaction();			
			Query query=session.createQuery(
							"select repo_language, count(distinct actor_login) as number_of_users "
							+ "from EventSingle "
							+ "group by repo_language "
							+ "order by number_of_users desc "
							).setFirstResult(0).setMaxResults(2);

			
			//put query result in with Object, so don't need to create a specific Class for the result
			List<Object[]> ls= (List<Object[]>) query.list();
			
			//print out the query result
			System.out.println("Result of Query2 On 0NF: The most popular language------");
			for (Object[] row : ls) {			
				System.out.println("language: " + (String)row[0] +", number_of_users: " + (Long)row[1] );
			}
			session.getTransaction().commit();
			
		}catch (RuntimeException e){
			session.getTransaction().rollback();
			throw e;
		}finally{
			session.close();
		}
		

	}
	
	public static void main(String[] args){
		long begin = System.currentTimeMillis();
		
		findMostPopularLang();
		long end = System.currentTimeMillis();
		MongoUtil.printStat(begin, end);
		
	}


}
