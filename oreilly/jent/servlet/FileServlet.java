package oreilly.jent.servlet;

// Example 5-2: Serving Files

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

public class FileServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
		
		File r;
		FileReader fr;
		BufferedReader br;
		
		try {
			r = new File(req.getParameter("filename"));
			fr = new FileReader(r);
			br = new BufferedReader(fr);
			
			if(!r.isFile()) { // Must be a directory or something else
				resp.sendError(resp.SC_NOT_FOUND);
				return;
			}
		}
			catch (FileNotFoundException e) {
			resp.sendError(resp.SC_NOT_FOUND);
			return;
		}
		catch (SecurityException se) { // Be unavailable permanently
			throw(new UnavailableException(this,
			"Servlet lacks appropriate privileges."));
		}
		
		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();
		String text;
		
		while( (text = br.readLine()) != null)
			out.println(text);
			
		br.close();
	}
}