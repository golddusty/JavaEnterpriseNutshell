package oreilly.jent.jndi;

/**
 * create: Command to create a new context within the current JNDI context.
 * 
 * Example 7-8, Java Enterprise in a Nutshell, 2nd ed.
 * Author: Kris Magnusson
 */

import java.util.Vector;
import javax.naming.*;

public class create implements Command {
    
  public void execute(Context c, Vector v) throws CommandException {
            
    // Check to see if we have the name we need to create a context
    if (v.isEmpty())
      throw new CommandException(new Exception(), "No name specified");
                
    String name = (String)v.firstElement();
    try {
      c.createSubcontext(name);
      System.out.println("Created " + name);
    }
    catch (NoPermissionException npe) {
      throw new CommandException(npe,
        "You don't have permission to create " + name + " at this context");
    }
    catch (NamingException ne) {
      throw new CommandException(ne, "Couldn't create " + name);
    }
  }

  public void help() {  System.out.println("Usage: create [name]"); }
}
