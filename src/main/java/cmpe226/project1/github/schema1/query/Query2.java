package cmpe226.project1.github.schema1.query;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import cmpe226.project1.util.HibernateUtil;
import cmpe226.project1.util.MongoUtil;

/**
 * @author lingzhang
 * 3rd Normal Form :There are 3 tables: event, actor, repository
 * Query2: find the most popular language (the language that largest number of actor works on)
 * SQLs query:
 * select r.language, count(distinct(a.login)) as number_of_users
 * from repository r, event e, actor a
 * where r.repo_id=e.repo_id and e.actor_id=a.actor_id
 * group by r.language
 * order by number_of_users desc
 * limit 1;
 */
public class Query2 {
	
	@SuppressWarnings("unchecked")
	public static void findMostPopularLang(){
		Session session = HibernateUtil.getSessionFactory().openSession();
		System.out.println("Trying to find the most popular language... ");
		try {			
			session.beginTransaction();			
			Query query=session.createQuery(
							"select r.language, count(distinct a.login) as number_of_users "
							+ "from Event e join e.actor as a join e.repository as r "
							+ "group by r.language "
							+ "order by number_of_users desc "
							).setFirstResult(0).setMaxResults(2);

			
			//put query result in with Object, so don't need to create a specific Class for the result
			List<Object[]> ls= (List<Object[]>) query.list();
			
			//print out the query result
			System.out.println("Result of Query2 --------The most popular language: ");
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
