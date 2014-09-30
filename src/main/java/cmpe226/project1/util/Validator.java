package cmpe226.project1.util;

import java.util.Date;

public class Validator {
	public static void main(String[] args) {
		dateValidate("1");
	}
	
	public static double doubleValidate(String d){
		try {
			return Double.parseDouble(d);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("convert double failed ! Default convert to 0.0");
			return Double.parseDouble("0.0");
		}
	}
	
	public static int intValidate(String i){
		try {
			return Integer.parseInt(i);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("convert int failed ! Default convert to -1");
			return Integer.parseInt("-1");
		}
	}
	
	public static Date dateValidate(String date){
		Date d = new Date("09/02/2014");
		return d;
	}

}
