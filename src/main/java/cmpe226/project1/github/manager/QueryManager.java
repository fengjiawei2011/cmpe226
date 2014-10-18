package cmpe226.project1.github.manager;

import cmpe226.project1.util.MongoUtil;

public class QueryManager {
	public static void main(String[] agrs){
		long begin=0;
		long end=0;

		begin = System.currentTimeMillis();		
		cmpe226.project1.github.schema1.query.Query1.findActorWithMostRepo();
		end = System.currentTimeMillis();
		MongoUtil.printStat(begin, end);
		
		begin = System.currentTimeMillis();		
		cmpe226.project1.github.schema2.query.Query1.findActorWithMostRepo();
		end = System.currentTimeMillis();
		MongoUtil.printStat(begin, end);
				
		
		begin = System.currentTimeMillis();		
		cmpe226.project1.github.schema1.query.Query2.findMostPopularLang();
		end = System.currentTimeMillis();
		MongoUtil.printStat(begin, end);
		
		begin = System.currentTimeMillis();		
		cmpe226.project1.github.schema2.query.Query2.findMostPopularLang();
		end = System.currentTimeMillis();
		MongoUtil.printStat(begin, end);

		begin = System.currentTimeMillis();		
		cmpe226.project1.github.schema1.query.Query3.findNumberOfUniqueActor();
		end = System.currentTimeMillis();
		MongoUtil.printStat(begin, end);
		
		begin = System.currentTimeMillis();		
		cmpe226.project1.github.schema2.query.Query3.findNumberOfUniqueActor();
		end = System.currentTimeMillis();
		MongoUtil.printStat(begin, end);
		
	}
}
