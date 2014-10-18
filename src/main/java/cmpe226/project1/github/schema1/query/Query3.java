package cmpe226.project1.github.schema1.query;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import cmpe226.project1.util.HibernateUtil;
import cmpe226.project1.util.MongoUtil;

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
public class Query3 {
	
	@SuppressWarnings("unchecked")
	public static void findNumberOfUniqueActor(){
		Session session = HibernateUtil.getSessionFactory().openSession();
		System.out.println("Trying to how many unique actors... ");
		try {			
			session.beginTransaction();			
			Query query=session.createQuery(
							"select count(actor_id)"
							+ "from Actor "
							);

			
			//put query result in with Object, so don't need to create a specific Class for the result

			List<Long> ls= (List<Long>) query.list();

			//print out the query result
			System.out.println("Result of Query3 On 3NF: The number of different actors------");
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
