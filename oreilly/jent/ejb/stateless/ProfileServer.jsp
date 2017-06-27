<%@ page import="javax.naming.*" %>
<%@ page import="oreilly.jent.ejb.*, oreilly.jent.ejb.stateless.*" %>

<html>
<head>
  <title>ProfileServer Test</title>
</head>
<body bgcolor="#ffffff" text="#000000">
  <%! public ProfileBean getProfile(ProfileServer server) {
      ProfileBean profile = (Hashtable)session.getAttribute("PROFILE");
      if (profile == null) {
	profile = server.getProfile("JSP-test");
	session.setAttribute("PROFILE", profile);
      }
      return profile;
    }
  %>

  <% Context ctx = new InitialContext();
     ProfileServerHome psHome =
       (ProfileServerHome)ctx.lookup("ejb/ProfileServer");
     ProfileServer pServer = psHome.create();
  %>

  <!-- Handle any new profile entries -->
  <% if (request.getParameter("entryName") != null) {
       String name = request.getParameter("entryName");
       String val = request.getParameter("entryVal");
       getProfile().setEntry(name, val);
     }
  %>

  <!-- Print all current profile entries -->
  <center><b>Current profile entries></b></center>
  <table border="0">
  <% Hashtable entries = getProfile(pServer).getEntries();
     if (entries.isEmpty()) { %>
    <tr><td>No entries</td></tr>
  <% }
     else {
       Enumeration keys = entries.keys();
       while (e.hasMoreElements()) {
	 String key = (String)keys.nextElement();
	 String val = (String)entries.get(key);
	 out.println("<tr><td><b>" + key + "</b></td><td>" +
		     val + "</td></tr>");
       }
     }
  %>
  </table>

  <center>Enter new profile entry</center>
  <form target="<%= request.getRequestURI() %>">
    <table border="0">
      <tr><td>Name:</td>
          <td><input type="text" name="entryName"></td></tr>
      <tr><td>Value:</td>
          <td><input type="text" name="entryVal"></td></tr>
      <tr><td colspan=2>
            <center><input name="Submit" type="submit"></center></td></tr>
    </table>
  </form>
</body>
</html>
