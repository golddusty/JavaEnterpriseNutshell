package oreilly.jent.servlet;

// Example 5-4: Date Filtering Servlet

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

public class DateFilter extends HttpServlet {
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
	
	PrintWriter out = resp.getWriter();
	String contentType = req.getContentType();
	if (contentType == null)
		return; // No incoming data

	// Note that if we were using MIME filtering we would have to set this to
	// something different to avoid an infinite loop
	resp.setContentType(contentType);
	BufferedReader br = new BufferedReader(req.getReader());
	String line = null;
	Date d = new Date();
	
	while ((line = br.readLine()) != null) {
		int index;
		
		while ((index=line.indexOf("<DATE>")) >= 0)
			line = line.substring(0, index) + d + line.substring(index + 6);
			out.println(line);
		}
		
		br.close();
	}
}