package cmpe226.project1.github.schema1.query;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import cmpe226.project1.util.HibernateUtil;
import cmpe226.project1.util.MongoUtil;

/**
 * @author lingzhang
 * 3rd Normal Form :There are 3 tables: event, actor, repository
 * Query1: find the actor that works on largest number of repository
 * SQLs query:
 * select a.login, count(distinct(e.repo_id)) as number_of_worked_repo 
 * from event e, actor a
 * where e.actor_id=a.actor_id
 * group by a.login
 * order by number_of_worked_repo desc
 * limit 1;
 */
public class Query1 {
	
	@SuppressWarnings("unchecked")
	public static void findActorWithMostRepo(){
		Session session = HibernateUtil.getSessionFactory().openSession();
		System.out.println("Trying to find the actor with most repository... ");
		try {			
			session.beginTransaction();			
			Query query=session.createQuery(
					"select a.login, count(distinct e.repository) as number_of_worked_repo "
					+ "from Event e join e.actor as a "
					+ "group by a.login "
					+ "order by number_of_worked_repo desc "
					).setFirstResult(0).setMaxResults(1);
			
			//put query result in with Object, so don't need to create a specific Class for the result
			List<Object[]> ls= (List<Object[]>) query.list();
			
			//print out the query result
			System.out.println("Result of Query1 --------The actors with most repository: ");
			for (Object[] row : ls) {			
				System.out.println("actor_login: " + (String)row[0] +", number_of_worked_repo: " + (Long)row[1] );
			}
			session.getTransaction().commit();
			
		}catch (RuntimeException e){
			session.getTransaction().rollback();
			throw e;
		}finally{
			session.close();
		}
		
	}
	
	public static void main(String[] args) throws Exception{
		long begin = System.currentTimeMillis();
		findActorWithMostRepo();
		long end = System.currentTimeMillis();
		MongoUtil.printStat(begin, end);
		
	}


}
