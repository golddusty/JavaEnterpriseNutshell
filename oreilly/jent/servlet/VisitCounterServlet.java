package oreilly.jent.servlet;

// Example 5-7: Counting Visits with Sessions

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

public class VisitCounterServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
		
		PrintWriter out = resp.getWriter();
		resp.setContentType("text/html");
		
		
		HttpSession thisUser = req.getSession(true);
		Integer visits;
		
		if(!thisUser.isNew()) { //Don't check newly created sessions
		
			visits = (Integer)thisUser.getValue("visitcounter.visits");
			if(visits == null)
				visits = new Integer(1);
			else
				visits = new Integer(visits.intValue() + 1);
		}
		else
			visits = new Integer(1);
		
		// Put the new count in the session
		thisUser.putValue("visitcounter.visits", visits);
		
		// Finally, display the results and give them the session ID too
		out.println("<HTML><HEAD><TITLE>Visit Counter</TITLE></HEAD>");
		out.println("<BODY>You have visited this page " + visits + " time[s]");
		out.println("since your last session expired.");
		out.println("Your Session ID is " + thisUser.getId());
		out.println("</BODY></HTML>");
	}
}