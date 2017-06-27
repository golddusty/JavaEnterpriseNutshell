package oreilly.jent.servlet;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

public class HelloServlet extends HttpServlet {
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {
	
		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();
		
		out.println("<HTML>");
		out.println("<HEAD><TITLE>Finally, interaction!</TITLE></HEAD>");
		out.println("<BODY><H1>Hello, " + req.getParameter("username") + "!</H1>");
		out.println("</BODY></HTML>");
		
	}
}