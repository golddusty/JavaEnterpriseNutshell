package oreilly.jent.servlet;

// Example 5-6: An ATM Servlet

import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import java.io.*;

public class AtmServlet extends HttpServlet {

	Account act;

	public void init(ServletConfig conf) throws ServletException {
		super.init(conf);
		act = new Account();
		act.balance = 0;
	}
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
		
		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();
		
		out.println("<HTML><BODY>");
		out.println("<H2>First Bank of Java ATM</H2>");
		out.println("Current Balance: <B>" + act.balance + "</B><BR>");
		out.println("<FORM METHOD=POST ACTION=/servlet/AtmServlet>");
		out.println("Amount: <INPUT TYPE=TEXT NAME=AMOUNT SIZE=3><BR>");
		out.println("<INPUT TYPE=SUBMIT NAME=DEPOSIT VALUE=\"Deposit\">");
		out.println("<INPUT TYPE=SUBMIT NAME=WITHDRAW VALUE=\"Withdraw\">");
		out.println("</FORM>");
		out.println("</BODY></HTML>");
	}
	
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
		
		int amt=0;
		
		try {
			amt = Integer.parseInt(req.getParameter("AMOUNT"));
		}
		catch (NullPointerException e) {
			// No Amount Parameter passed
		}
		catch (NumberFormatException e) {
		// Amount Parameter was not a number
		}
		
		synchronized(act) {
		
			if((req.getParameter("WITHDRAW") != null) && (amt < act.balance))
				act.balance = act.balance - amt;
				
			if((req.getParameter("DEPOSIT") != null) && (amt > 0))
				act.balance = act.balance + amt;
				
		} // end synchronized block
			doGet(req, resp); // Show ATM screen
		}
		
	public void destroy() {
		// This is where we would save the balance to a file
	}
	
	class Account {
		public int balance;
	}
}