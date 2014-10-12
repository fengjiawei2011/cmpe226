package cmpe226.project1.github.schema1.query;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;

import cmpe226.project1.github.schema1.model.Repository;
import cmpe226.project1.util.HibernateUtil;

public class SingleQuery implements Runnable{
	
	
	
	public void find(Long id) {
//		if (id == null)
//			return null;

		Repository r = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			r = (Repository) session.load(Repository.class, id);

			System.out.println("found the repository " + r.getName());
			session.getTransaction().commit();
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		}
		
		//return r;
	}
	
	@SuppressWarnings("unchecked")
	public void findByID(Long id) {
		//if (id == null)
			//return null;

		ArrayList<Repository> r = null;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			session.beginTransaction();
			Query query = session.createQuery("from Repository as r where r.id = :repo_id");
			query.setLong("repo_id", id);
			Set<Repository> set = new LinkedHashSet<Repository>(query.list());
			r = new ArrayList<Repository>(set);
			System.out.println("found the repository by id: " + id + ",the name is: "+ r.get(0).getName());
			
			session.getTransaction().commit();
		} catch (RuntimeException e) {
			session.getTransaction().rollback();
			throw e;
		}
		
		//return r;
	}
	
	public static void main(String[] args){
		SingleQuery q= new SingleQuery();
		//q.find(new Long(18221501));
		q.findByID(new Long(18221501));
				
	}
	
	
	public void run() {
		// TODO Auto-generated method stub
		try {
			long subthreadid=Thread.currentThread().getId();
			System.out.println("Sub ThreadId: " + subthreadid +" started");			
			SingleQuery q= new SingleQuery();
			q.findByID(new Long(18221501));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
