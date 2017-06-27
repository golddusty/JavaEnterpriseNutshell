package oreilly.jent.servlet;

// Example 5-3: A Persistent Counter Servlet

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

public class LifeCycleServlet extends HttpServlet {

	int timesAccessed;
	public void init(ServletConfig conf) throws ServletException {
		super.init(conf);
	
		// Get initial value
		try {
			timesAccessed = Integer.parseInt(getInitParameter("defaultStart"));
		}
		catch(NullPointerException e) {
			timesAccessed = 0;
		}
		catch(NumberFormatException e) {
			timesAccessed = 0;
		}
		
		// Try loading from the disk
		DataInputStream ds = null;
		try {
			File r = new File("./data/counter.dat");
			ds = new DataInputStream(new FileInputStream(r));
			timesAccessed = ds.readInt();
		}
		catch (FileNotFoundException e) {
			// Handle error
		}
		catch (IOException e) {
			// This should be logged
		}
		finally {
			try { ds.close(); } catch (IOException e) {}
		}
	}
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
		
		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();
		
		timesAccessed++;
		
		out.println("<HTML>");
		out.println("<HEAD>");
		out.println("<TITLE>Life Cycle Servlet</TITLE>");
		out.println("</HEAD><BODY>");
		out.println("I have been accessed " + timesAccessed + " time[s]");
		out.println("</BODY></HTML>");
		}
		
	public void destroy() {
		// Write the Integer to a file
		File r = new File("./data/counter.dat");
		DataOutputStream dout = null;
		
		try {
			dout = new DataOutputStream(new FileOutputStream(r));
			dout.writeInt(timesAccessed);
		}
		catch(IOException e) {
			// This should be logged
		}
		finally {
			try { dout.close(); } catch (IOException e) {}
		}
	}
}