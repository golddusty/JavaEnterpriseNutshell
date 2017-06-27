package oreilly.jent.jndi;

/**
 * listen: Command to listen for events in a JNDI context.
 * 
 * Example 7-15, Java Enterprise in a Nutshell, 2nd ed.
 * Author: Jim Farley
 */

import java.util.Vector;
import javax.naming.*;
import javax.naming.event.*;
import javax.naming.directory.*;

class listen implements Command {
  public void execute(Context c, Vector v) throws CommandException {
        
    if (NamingShell.getCurrentContext() == null)
      throw new CommandException(new Exception(), "No current context");
    else if (v.isEmpty())
      throw new CommandException(new Exception(), "No target specified");
    String name = (String)v.firstElement();
    String filter = null;
    if (v.size() > 1) {
      filter = (String)v.elementAt(1);
    }
    
    try {
      // Cast context to an event context
      EventContext evCtx = (EventContext)c;
      // Create our listener
      NamingListener listener = new AnyChangeLogger();
      // If no filter specified, just register a listener using EventContext
      if (filter == null) {
        evCtx.addNamingListener(name, EventContext.OBJECT_SCOPE, listener);
      }
      // If we have a filter, use the EventDirContext to specify the target
      else {
        EventDirContext evDirCtx = (EventDirContext)c;
        evDirCtx.addNamingListener(name, filter, null, listener);
      }
      System.out.println("Registered listener for " + name +
                         (filter != null ? (" and filter " + filter) : ""));
    }
    catch (ClassCastException cce) {
      cce.printStackTrace();
      throw new CommandException(cce,
                  "The current context does not support event notification.");
    }
    catch (NamingException e) {
      throw new CommandException(e, "The search for " + filter + " failed");
    }
  }

  public void help() { System.out.println("Usage: listen name [filter]"); }
}
