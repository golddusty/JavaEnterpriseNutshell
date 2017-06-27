package oreilly.jent.servlet;

// Example 5-5: AuthenticationFilter

import javax.servlet.*;
import javax.servlet.http.*;

import java.util.Hashtable;

public class AuthenticationFilter implements Filter {

private Hashtable users = null;

	public void init(FilterConfig config) throws javax.servlet.ServletException
	{
		users = (Hashtable)config.getServletContext().getAttribute("enterprise.users");
	
		if(users == null) {
			users = new Hashtable(5);
			users.put("test", "test");
		}
	}
	
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
		throws java.io.IOException, javax.servlet.ServletException {
	
		HttpServletRequest request = (HttpServletRequest)req;
		HttpSession sess = request.getSession(true);
		
		if(sess != null) {
			Boolean loggedIn = (Boolean)sess.
			getAttribute("enterprise.login");
	
			if (loggedIn != Boolean.TRUE) {
				String login_name = request.getParameter("login_name");
				String login_pass = request.getParameter("login_pass");
				
				if((login_name != null) && (login_pass != null)) 
					if(users.get(login_name).toString().equals(login_pass)) {
						loggedIn = Boolean.TRUE;
						sess.setAttribute("enterprise.login", Boolean.TRUE);
						sess.setAttribute("enterprise.loginname", login_name);
					}
				
			}
		
			if (loggedIn == Boolean.TRUE) {
				chain.doFilter(req, res);
			} else {
				request.setAttribute("originaluri", request.getRequestURI());
				request.getRequestDispatcher("/login.jsp").
				forward(req, res);
			}
		}
	}
	
	public void destroy() {
		// Code cleanup would be here
	}
}