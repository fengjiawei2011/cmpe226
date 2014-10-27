package cmpe226.project1.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresJdbcUtil {
	private static final String URL="jdbc:postgresql://10.211.55.5:5432/project1";
	private static final String USER="cmpe226";
	private static final String PASS="password";
	
	public static Connection getDBconnection() throws SQLException{
		return DriverManager.getConnection(URL,USER,PASS);
	}
	

}
