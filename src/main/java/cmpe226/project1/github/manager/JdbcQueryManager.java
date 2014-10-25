package cmpe226.project1.github.manager;

import cmpe226.project1.util.MongoUtil;

public class JdbcQueryManager {
	public static void main(String[] agrs) throws Exception{
		long begin=0;
		long end=0;

		//query1
		begin = System.currentTimeMillis();		
		cmpe226.project1.github.schema1.query.S1Query1jdbc.s1query1jdbc();
		end = System.currentTimeMillis();
		MongoUtil.printStat(begin, end);
		
		begin = System.currentTimeMillis();		
		cmpe226.project1.github.schema2.query.S2Query1jdbc.query1jdbc();;
		end = System.currentTimeMillis();
		MongoUtil.printStat(begin, end);
				
		//query2
		begin = System.currentTimeMillis();		
		cmpe226.project1.github.schema1.query.S1Query2jdbc.query2jdbc();;
		end = System.currentTimeMillis();
		MongoUtil.printStat(begin, end);
		
		begin = System.currentTimeMillis();		
		cmpe226.project1.github.schema2.query.S2Query2jdbc.query2jdbc();
		end = System.currentTimeMillis();
		MongoUtil.printStat(begin, end);

		//query3
		begin = System.currentTimeMillis();		
		cmpe226.project1.github.schema1.query.S1Query3jdbc.s1query3jdbc();
		end = System.currentTimeMillis();
		MongoUtil.printStat(begin, end);
		
		begin = System.currentTimeMillis();		
		cmpe226.project1.github.schema2.query.S2Query3jdbc.query3jdbc();
		end = System.currentTimeMillis();
		MongoUtil.printStat(begin, end);
		
	}
}
