package oreilly.jent.servlet;

// Example 5-8: A Servlet That Serves PDF Files from a Database

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class DBPDFReader extends HttpServlet {
	Connection con;

	public void init(ServletConfig config) throws ServletException {
		
		super.init(config);
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection("jdbc:oracle:oci8:@DBHOST",
			"user", "passwd");
		}
		catch (ClassNotFoundException e) {
			throw new UnavailableException(this, "Couldn't load OracleDriver");
		}
		catch (SQLException e) {
			throw new UnavailableException(this, "Couldn't get db connection");
		}
	}
		
	public void doGet(HttpServletRequest req, HttpServletResponse res)
		throws ServletException, IOException {
	
		try {
			res.setContentType("application/pdf");
			ServletOutputStream out = res.getOutputStream();
			
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(
			"SELECT PDF FROM PDF WHERE PDFID = " + req.getParameter("PDFID"));
			
			if (rs.next()) {
			
				BufferedInputStream pdfData =
					new BufferedInputStream(rs.getBinaryStream("PDF"));
				byte[] buf = new byte[4 * 1024]; // 4K buffer
				int len;
				
				while ((len = pdfData.read(buf, 0, buf.length)) != -1) {
					out.write(buf, 0, len);
				}
			}
			else {
				res.sendError(res.SC_NOT_FOUND);
			}
		}
			catch(SQLException e) {
			// Report it
		}
	}
}