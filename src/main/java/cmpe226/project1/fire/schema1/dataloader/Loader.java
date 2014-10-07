package cmpe226.project1.fire.schema1.dataloader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import cmpe226.project1.fire.schema1.data.Fire;
import cmpe226.project1.util.HibernateUtil;
import cmpe226.project1.util.Validator;

public class Loader {
	
	public static void main(String[] args) {
		Loader.load("/Users/frank_feng1/tmp/datadir");
	}
	
	//upload data into db
	public static void upload(ArrayList<Fire> fireList){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			Transaction tx = session.beginTransaction();
			for (int n = 0; n < fireList.size(); ++n){
				session.save(fireList.get(n));
				if (n % 100 == 0) {
					session.flush();
					session.clear();
				}
			}
			tx.commit();
		} finally {
			// session.close();
		}
	}
	
	//load data from files
	public static void load(String path){
		File dataFolder = new File(path);
		ArrayList<Fire> fireList = new ArrayList<Fire>();
		if(dataFolder.isDirectory()){
			//int i = 0;
			for(File f : dataFolder.listFiles()){
				//i++;
				if(f.getName().indexOf(".url") >= 0)
					continue;
				//System.out.print(f.getName() + "-->");
				//System.out.println(f.getName());
				try {
					BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
					String line = "";
					while((line = br.readLine()) != null){
						if(line.indexOf("latitude") >= 0 || line.trim().equals(""))
							continue;
						String[] strings = line.split(",");
						ArrayList<String> tmp = new ArrayList<String>();
						for(String str : strings){
							tmp.add(str);
						}
						if(strings.length < 10){
							
							for(int i = strings.length; i < 10; i++){
								tmp.add("");				
							}	
						}
						System.out.println(f.getName() + "-->" + strings[2]);
						Fire fire = generateFireObj(tmp);
						fireList.add(fire);
						if(fireList.size() > 2048){
							upload(fireList);
							fireList.clear();
						}
						//System.out.println(strings[0]);
					}
					
					//if(i > 3) break;
				}catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}	
		}
		
	}
	
	@SuppressWarnings("deprecation")
	public static Fire generateFireObj(ArrayList<String> strings){
		Fire fire = new Fire();
		fire.setLatitude(Validator.doubleValidate(strings.get(0)));
		fire.setLongitude(Validator.doubleValidate(strings.get(1)));
		fire.setReport_date(new Date(strings.get(2)));
		fire.setArea(Validator.doubleValidate(strings.get(3)));
		fire.setFire_name(strings.get(4));
		fire.setFire_number(strings.get(5));
		fire.setCondition(strings.get(6));
		fire.setWfu(Validator.intValidate(strings.get(7)));
		fire.setReport_age(Validator.intValidate(strings.get(8)));
		fire.setGacc(strings.get(9));
		return fire;
	}

}
