package oreilly.jent.jndi;

/**
 * list: Command to list the contents of a JNDI context.
 * 
 * Example 7-6, Java Enterprise in a Nutshell, 2nd ed.
 * Authors: Kris Magnusson, Jim Farley.
 */

import java.util.Vector;
import javax.naming.*;

public class list implements Command {
  public void execute(Context c, Vector v) throws CommandException {
        
    String name = "";
        
    // An empty string is OK for a list operation as it means
    // list children of the current context.
    if (!(v.isEmpty()))
      name = (String)v.firstElement();
        
    // Check for current context; throw an exception if there isn&rsquo;t one
    if (NamingShell.getCurrentContext() == null)
      throw new CommandException(new Exception(), 
        "Error: no current context.");

    // Call list() and then loop through the results, printing the names
    // and class names of the children
    try {
      NamingEnumeration enum = c.list(name);                
      while (enum.hasMore()) {
        NameClassPair ncPair = (NameClassPair)enum.next();
        System.out.print(ncPair.getName() + " (type ");
        System.out.println(ncPair.getClassName() + ")");
      }
    }
    catch (NamingException e) {
      throw new CommandException(e, "Couldn't list " + name);
    }
  }

  public void help() { System.out.println("Usage: list [name]"); }
}
