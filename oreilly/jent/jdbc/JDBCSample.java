package oreilly.jent.jdbc;
// Example 2-1
import java.sql.*;

public class JDBCSample {

	public static void main(java.lang.String[] args) {

		try {
			// This is where we load the driver
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
		}
		catch (ClassNotFoundException e) {
			System.out.println("Unable to load Driver Class");
			return;
		}
		
		try {
			// All database access is within a try/catch block. Connect to database,
			// specifying particular database, username, and password
			
			Connection con = DriverManager.getConnection("jdbc:odbc:companydb", "", "");
			// Create and execute an SQL Statement
			
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT FIRST_NAME FROM EMPLOYEES");
			
			// Display the SQL Results
			while(rs.next()) {
				System.out.println(rs.getString("FIRST_NAME"));
			}
			
			// Make sure our database resources are released
			rs.close();
			stmt.close();
			con.close();
		}
		catch (SQLException se) {
			// Inform user of any SQL errors
			System.out.println("SQL Exception: " + se.getMessage());
			se.printStackTrace(System.out);
		}
	}
}