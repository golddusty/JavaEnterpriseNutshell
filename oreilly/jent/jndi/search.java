package oreilly.jent.jndi;

/**
 * search: Command to search a given directory context.
 * 
 * Example 7-11, Java Enterprise in a Nutshell, 2nd ed.
 * Authors: Kris Magnusson
 */

import java.util.Vector;
import javax.naming.*;
import javax.naming.directory.*;

class search implements Command {
  public void execute(Context c, Vector v) throws CommandException {
        
    if (NamingShell.getCurrentContext() == null)
      throw new CommandException(new Exception(), "No current context");
    else if (v.isEmpty())
      throw new CommandException(new Exception(), "No filter specified");
    String filter = (String)v.firstElement();
    try {
      SearchControls cons = new SearchControls();
      cons.setSearchScope(SearchControls.SUBTREE_SCOPE);
      NamingEnumeration results = ((DirContext)c).search("", filter, cons);
      while (results.hasMore()) {
        SearchResult result = (SearchResult)results.next();
        System.out.println(result.getName());
      }
    }
    catch (InvalidSearchFilterException isfe) {
      throw new CommandException(isfe, 
      "The filter [" + filter + "] is invalid");
    }
    catch (NamingException e) {
      throw new CommandException(e, "The search for " + filter + " failed");
    }
    catch (ClassCastException cce) {
      throw new CommandException(cce, "Not a directory context");
    }
  }

  public void help() { System.out.println("Usage: search filter"); }
}
