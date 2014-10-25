package cmpe226.project1.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresJdbcUtil {
	private static final String URL="jdbc:postgresql://localhost:5432/lingzhang";
	private static final String USER="lingzhang";
	private static final String PASS="";
	
	public static Connection getDBconnection() throws SQLException{
		return DriverManager.getConnection(URL,USER,PASS);
	}
	

}
