package oreilly.jent.jdbc;
// Example 2-3: TableViewer program

import java.sql.*;
import java.util.StringTokenizer;

public class TableViewer {

	final static String jdbcURL = "jdbc:oracle:customerdb";
	final static String jdbcDriver = "oracle.jdbc.driver.OracleDriver";
	final static String table = "CUSTOMERS";

	public static void main(java.lang.String[] args) {
		System.out.println("--- Table Viewer ---");

		try {
			Class.forName(jdbcDriver);
			Connection con = DriverManager.getConnection(jdbcURL, "", "");
			
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM "+ table);
			
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			
			for(int col = 1; col <= columnCount; col++) {
				System.out.print(rsmd.getColumnLabel(col));
				System.out.print(" (" + rsmd.getColumnTypeName(col)+")");
				
				if(col < columnCount)
					System.out.print(", ");
				}
				
				System.out.println();
		
				while(rs.next()) {
					for(int col = 1; col <= columnCount; col++) {
						System.out.print(rs.getString(col));
						
						if(col < columnCount)
							System.out.print(", ");
					}
					
					System.out.println();
				}
				
			rs.close();
			stmt.close();
			con.close();
		}
		catch (ClassNotFoundException e) {
			System.out.println("Unable to load database driver class");
		}
		catch (SQLException e) {
			System.out.println("SQL Exception: " + e.getMessage());
		}
	}
}