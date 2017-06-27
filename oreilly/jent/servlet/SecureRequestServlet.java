package oreilly.jent.servlet;

// Example 5-1: Checking Request Information to Restrict Servlet Access

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

public class SecureRequestServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
		
		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();
		
		out.println("<HTML>");
		out.println("<HEAD><TITLE>Semi-Secure Request</TITLE></HEAD>");
		out.println("<BODY>");
		
		String remoteHost = req.getRemoteHost();
		String scheme = req.getScheme();
		String authType = req.getAuthType();
		
		if((remoteHost == null) || (scheme == null) || (authType == null)) {
			out.println("Request Information Was Not Available.");
			return;
		}
		
		if(scheme.equalsIgnoreCase("https") && remoteHost.endsWith(".gov")
			&& authType.equals("Digest")) {
			out.println("Special, secret information.");
		}
		else {
			out.println("You are not authorized to view this data.");
		}
		
		out.println("</BODY></HTML>");
	}

}