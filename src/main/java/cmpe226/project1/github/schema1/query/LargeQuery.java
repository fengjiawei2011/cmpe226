package cmpe226.project1.github.schema1.query;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;

import cmpe226.project1.github.schema1.model.Actor;
import cmpe226.project1.util.HibernateUtil;
import cmpe226.project1.util.MongoUtil;

public class LargeQuery {
	
	
	@SuppressWarnings("unchecked")
	public static void findActorByLanguage(String lang) {
		//find all the actors that have worked on specific language
		
		//if (id == null)
			//return null;

		ArrayList<ActorResult> r = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			long begin = System.currentTimeMillis();
			
			session.beginTransaction();
			
			Query query = session.createQuery("select new cmpe226.project1.github.schema1.query.ActorResult"
												+ "(a.id, a.login, a.email, count(e.id) as number_of_events) "
												+"from Event e join e.repository as r join e.actor as a " 
												+"where r.language=:language "
												+"group by a.id " 
												+"order by number_of_events desc "
												//);
												).setFirstResult(0).setMaxResults(10);		
			query.setString("language", lang);
			Set<ActorResult> set = new LinkedHashSet<ActorResult>(query.list());
			r = new ArrayList<ActorResult>(set);
			System.out.println("found all the actors who have worked on " + lang + ",the result is: ");
			int count=0;
			for (ActorResult a: r){
				count++;
				System.out.println(a.toString());
			}			
			System.out.println("The total number of actors worked on " + lang + " is: " + count);
																		
			session.getTransaction().commit();
			
			long end = System.currentTimeMillis();
			MongoUtil.printStat(begin, end);
			
			
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		}
		
		//return r;
	}
	
	@SuppressWarnings("unchecked")
	public static void findAllActor() {
		//if (id == null)
			//return null;

		ArrayList<Actor> r = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			long begin = System.currentTimeMillis();
			
			session.beginTransaction();
									
			Query query = session.createQuery("from Actor ");
			Set<Actor> set = new LinkedHashSet<Actor>(query.list());
			r = new ArrayList<Actor>(set);
			System.out.println("found all the actors: ");
			int count=0;
			for (Actor a: r){
				count++;
				System.out.println(a.toString());
			}			
			System.out.println("The total number of actors is: " + count);
			
						
			session.getTransaction().commit();
			
			long end = System.currentTimeMillis();
			MongoUtil.printStat(begin, end);
			
			
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		}
		
		//return r;
	}
	
	
	public static void main(String[] args) throws Exception {
		String lang = "JavaScript";
		findActorByLanguage(lang);
		//findAllActor();

	}

}
