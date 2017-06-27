package oreilly.jent.jdbc;
// Example 2-2

import java.sql.*;
import java.util.StringTokenizer;

public class DBViewer {
	final static String jdbcURL = "jdbc:odbc:customerdsn";
	final static String jdbcDriver = "sun.jdbc.odbc.JdbcOdbcDriver";
	
	public static void main(java.lang.String[] args) {
	
		System.out.println("--- Database Viewer ---");
		try {
		
			Class.forName(jdbcDriver);
			
			Connection con = DriverManager.getConnection(jdbcURL, "", "");
			DatabaseMetaData dbmd = con.getMetaData();
			
			System.out.println("Driver Name: " + dbmd.getDriverName());
			System.out.println("Database Product: " + dbmd.getDatabaseProductName());
			System.out.println("SQL Keywords Supported:");
			
			StringTokenizer st = new StringTokenizer(dbmd.getSQLKeywords(), ",");
			
			while(st.hasMoreTokens())
				System.out.println(" " + st.nextToken());
			
			// Get a ResultSet that contains all of the tables in this database
			// We specify a table_type of "TABLE" to prevent seeing system tables,
			// views and so forth
			
			String[] tableTypes = { "TABLE" };
			ResultSet allTables = dbmd.getTables(null,null,null,tableTypes);
			
			while(allTables.next()) {
				String table_name = allTables.getString("TABLE_NAME");
				System.out.println("Table Name: " + table_name);
				System.out.println("Table Type: " + allTables.getString("TABLE_TYPE"));
				
				System.out.println("Indexes: ");
				// Get a list of all the indexes for this table
				ResultSet indexList = dbmd.getIndexInfo(null,null,table_name,false,false);
				
				while(indexList.next()) {
					System.out.println(" Index Name: "+indexList.getString("INDEX_NAME"));
					System.out.println(" Column Name:"+indexList.getString("COLUMN_NAME"));
	
				}
				
				indexList.close();
			}
			allTables.close();
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