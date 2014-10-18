package cmpe226.project1.github.schema2.query;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import cmpe226.project1.util.HibernateUtil;
import cmpe226.project1.util.MongoUtil;

/**
 * @author lingzhang
 * 0rd Normal Form :There are only 1 table : event_single_table
 * Query3: find how many unique actors
 * 
 * SQLs query:
 * select count(distinct(actor_login))
 * from event_single_table;
 */
public class Query3 {
	
	@SuppressWarnings("unchecked")
	public static void findNumberOfUniqueActor(){
		Session session = HibernateUtil.getSessionFactory().openSession();
		System.out.println("Trying to find how many unique actors... ");
		try {			
			session.beginTransaction();			
			Query query=session.createQuery(
							"select count(distinct actor_login) "
							+ "from EventSingle "
							);

			
			//put query result in with Object, so don't need to create a specific Class for the result
			List<Long> ls= (List<Long>) query.list();
			
			//print out the query result
			System.out.println("Result of Query3 On 0NF: The number of different actors------");
			for (Long row : ls) {			
				System.out.println("number_of_actors: " + row);
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
		
		findNumberOfUniqueActor();
		long end = System.currentTimeMillis();
		MongoUtil.printStat(begin, end);
		
	}


}
