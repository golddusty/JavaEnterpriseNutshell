package oreilly.jent.jndi;

/**
 * destroy: Command to destroy a given context in the JNDI namespace.
 * 
 * Example 7-9, Java Enterprise in a Nutshell, 2nd ed.
 * Author: Kris Magnusson
 */

import java.util.Vector;
import javax.naming.*;

public class destroy implements Command {
  public void execute(Context c, Vector v) throws CommandException {

    // Check to see if we have the name we need
    if (v.isEmpty())
      throw new CommandException(new Exception(), "No name specified");
        
    String name = (String)v.firstElement();
        
    try {
      c.destroySubcontext(name);
      System.out.println("Destroyed " + name);
    }
    catch (NameNotFoundException nnfe) {
      throw new CommandException(nnfe, "Couldn't find " + name);
    }
    catch (NotContextException nce) {
      throw new CommandException(nce, 
        name + " is not a Context and couldn't be destroyed");
    }        
    catch (ContextNotEmptyException cnee) {
      throw new CommandException(cnee, 
        name + " is not empty and couldn't be destroyed");
    }
    catch (NamingException ne) {
      throw new CommandException(ne, name + " couldn't be destroyed");
    }
  }

  public void help() { System.out.println("Usage: destroy [name]"); }
}
