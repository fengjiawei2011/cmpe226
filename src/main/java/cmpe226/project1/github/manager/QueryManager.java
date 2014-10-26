package cmpe226.project1.github.manager;

import cmpe226.project1.util.HibernateUtil;
import cmpe226.project1.util.MongoUtil;

public class QueryManager {
	public static void main(String[] agrs){
		long begin=0;
		long end=0;
		int repeat = 3;

		begin = System.currentTimeMillis();	
		for (int i = 0; i < repeat; i++) {
			cmpe226.project1.github.schema1.query.Query1.findActorWithMostRepo();
		}
		end = System.currentTimeMillis();
		MongoUtil.printStat(begin, end);
		
		begin = System.currentTimeMillis();		
		for (int i = 0; i < repeat; i++) {
			cmpe226.project1.github.schema2.query.Query1.findActorWithMostRepo();
		}
		end = System.currentTimeMillis();
		MongoUtil.printStat(begin, end);
				
		System.out.println("=================================");
		
		begin = System.currentTimeMillis();		
		for (int i = 0; i < repeat; i++) {
			cmpe226.project1.github.schema1.query.Query2.findMostPopularLang();
		}
		end = System.currentTimeMillis();
		MongoUtil.printStat(begin, end);
		
		begin = System.currentTimeMillis();		
		for (int i = 0; i < repeat; i++) {
			cmpe226.project1.github.schema2.query.Query2.findMostPopularLang();
		}
		end = System.currentTimeMillis();
		MongoUtil.printStat(begin, end);

		System.out.println("=================================");
		begin = System.currentTimeMillis();		
		for (int i = 0; i < repeat; i++) {
			cmpe226.project1.github.schema1.query.Query3.findNumberOfUniqueActor();
		}
		end = System.currentTimeMillis();
		MongoUtil.printStat(begin, end);
		
		begin = System.currentTimeMillis();		
		for (int i = 0; i < repeat; i++) {
			cmpe226.project1.github.schema2.query.Query3.findNumberOfUniqueActor();
		}
		end = System.currentTimeMillis();
		
		HibernateUtil.getSessionFactory().close();
		MongoUtil.printStat(begin, end);
	}
	
}
