package oreilly.jent.jndi;

/**
 * cd: Command to re-position the NamingShell to a new context within the 
 *   JNDI name space.
 * 
 * Example 7-7, Java Enterprise in a Nutshell, 2nd ed.
 * Authors: Kris Magnusson, Jim Farley.
 */

import java.util.Vector;

import javax.naming.Context;
import javax.naming.NamingException;

class cd implements Command {

  public void execute(Context ctx, Vector v) throws CommandException {
    if (NamingShell.getCurrentContext() == null)
      throw new CommandException(new Exception(), "No current context");
    else if (v.isEmpty())
      throw new CommandException(new Exception(), "No name specified");

    // get args[0] and throw away the other args
    else {
      String name = (String)v.firstElement();
      try {
        if (name.equals("..")) {
          throw new CommandException(new Exception(), 
            "Contexts don't know about their parents");
        }
        else if (((name.equals("/")) || (name.equals("\\"))) ) {
          NamingShell.setCurrentContext(NamingShell.getInitialContext());
          NamingShell.setCurrentName(NamingShell.getInitialName());
          System.out.println("Returning to initial context");
        }
        else {
          Context c = (Context)NamingShell.getCurrentContext().lookup(name);
          NamingShell.setCurrentContext(c);
          NamingShell.setCurrentName(name);
          System.out.println("Current context now " + name);
        }
      }
      catch (NamingException ne) {
        throw new CommandException(ne, "Couldn't change to context " + name);
      }
      catch (ClassCastException cce) {
        throw new CommandException(cce, name + " not a Context");
      }
    }
  }
    
  public void help() { System.out.println("Usage: cd [name]"); }
}
