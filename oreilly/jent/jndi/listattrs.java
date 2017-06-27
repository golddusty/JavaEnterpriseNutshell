package oreilly.jent.jndi;

/**
 * listattrs: Command to list the attributes of a given directory context.
 * 
 * Example 7-10, Java Enterprise in a Nutshell, 2nd ed.
 * Authors: Kris Magnusson
 */

import java.util.Vector;
import javax.naming.*;
import javax.naming.directory.*;

class listattrs implements Command {
  public void execute(Context c, Vector v) throws CommandException {
        
    String name = "";
        
    // An empty string is OK for a listattrs operation
    // as it means list attributes of the current context
    if (!(v.isEmpty()))
      name = (String)v.firstElement();
        
    if (NamingShell.getCurrentContext() == null)
      throw new CommandException(new Exception(), "No current context");       

    try {
      // Get the Attributes and then get enumeration of Attribute objects
      Attributes attrs = ((DirContext)c).getAttributes(name);
      NamingEnumeration allAttr = attrs.getAll();
      while (allAttr.hasMore()) {
        Attribute attr = (Attribute)allAttr.next();
        System.out.println("Attribute: " + attr.getID());
                
        // Note that this can return human-unreadable garbage
        NamingEnumeration values = attr.getAll();
        while (values.hasMore())
          System.out.println("Value: " + values.next());
      }
    }
    catch (NamingException e) {
      throw new CommandException(e, "Couldn't list attributes of " + name);
    }
    catch (ClassCastException cce) {
      throw new CommandException(cce, "Not a directory context");
    }
  }

  public void help() { System.out.println("Usage: listattrs [name]"); }
}
