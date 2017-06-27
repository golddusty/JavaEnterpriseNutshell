package oreilly.jent.servlet;

// HellowWorldServlet

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

public class HelloWorldServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
	
		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();
		
		out.println("<HTML>");
		out.println("<HEAD><TITLE>Have you seen this before?</TITLE></HEAD>");
		out.println("<BODY><H1>Hello, World!</H1><H6>Again.</H6></BODY></HTML>");
	}
}