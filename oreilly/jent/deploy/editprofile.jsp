<html>
<head>
<title>Edit Your Profile</title>
</head>

<!-- Import the packages we need. -->
<%@ page import="oreilly.jent.ejb.*" %>
<%@ page import="oreilly.jent.ejb.beanManaged.*" %>
<%@ page import="javax.ejb.*" %>
<%@ page import="javax.naming.*" %>

  <!-- Create a Profile Bean, using the name provided as a URL argument -->
  <%! Profile profile = null; %>
  <%
    Context cntx = null;
    try {
      String userName = request.getParameter("name");
      // Get a JNDI context for our J2EE server
      System.out.println("Getting EJB context.");
//       Properties p = new Properties();
//       p.put(Context.INITIAL_CONTEXT_FACTORY,
// 	    "weblogic.jndi.T3InitialContextFactory");
//       p.put(Context.PROVIDER_URL, "t3://localhost:7001");
      cntx = new InitialContext();
      System.out.println("Looking up EJB home.");
      ProfileHome profileHome = (ProfileHome)cntx.lookup("java:comp/env/ejb/Profile");
      System.out.println("Creating profile");
      try {
	profile = profileHome.findByPrimaryKey(userName);
      }
      catch (FinderException fe) {
	profile = profileHome.create(userName);
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  %>
</jsp:useBean>

<!-- Start the body of the page -->
<body bgcolor="#FFFFFF" text="#000000" link="#330099" vlink="#666666">
<table width="100%" border="0">
  <tr bgcolor="#000055"> 
    <td> 
      <h3 align="right"><font color="#FFFF00"><b><font face="Arial, Helvetica, sans-serif">Advanced 
        Topics in Java Distributed Computing&nbsp;&nbsp;&nbsp;<br>
        (CSCI E-162) </font></b></font><font color="#FFFF00"><b>&nbsp;&nbsp;&nbsp;</b></font></h3>
      <h4 align="left">&nbsp;&nbsp;&nbsp;<font color="#8080FF" face="Arial, Helvetica, sans-serif" size="4"><!-- #BeginEditable "Title" -->EJB Stock Server Example<!-- #EndEditable --></font><font size="-1" color="#FFFF00"> 
        </font></h4>
    </td>
  </tr>
  <tr> 
    <td> 
      <div align="right"><font size="-1" color="#FFFF00">&nbsp;<font face="Arial, Helvetica, sans-serif"><a href="http://lab.dce.harvard.edu/extension/cscie162-spring/">CSCI 
        E-162 home</a></font></font> </div>
    </td>
  </tr>
  <tr> 
    <td>
      <!-- If we received any profile update information, update the profile
           before proceeding -->
<% if (request.getParameter("update") != null) {
     try {
       String color = request.getParameter("favColor");
       profile.setEntry("favColor", color);
       String language = request.getParameter("language");
       profile.setEntry("language", language);
       String timeZone = request.getParameter("timeZone");
       profile.setEntry("timeZone", timeZone);
       out.println("<br><center><h3>" +
		   "Updated profile sucessfully.</h3></center><br>");
     }
     catch (Exception e) {
       out.println("<br><center><h3><font color=\"#FF0000\">"
		   + "Error updating profile.</font></h3><center><br>");
     }
   } %>

<!-- Print out a form asking for updated profile data -->
  <form method="post" action="<%= request.getRequestURI() %>">
  <b>Update your profile:</b><br>
  Favorite color: <input type="text" name="favColor"
                         value=<%= profile.getEntry("favColor") %> ><br>
  Preferred language: <input type="text" name="language"
                             value=<%= profile.getEntry("language") %> ><br>
  Time zone: <input type="text" name="timeZone"
                    value=<%= profile.getEntry("timeZone") %> ><br>
  <input type="submit" name="update">
  </form>



    </td>
  </tr>
</table>
</body>
</html>
