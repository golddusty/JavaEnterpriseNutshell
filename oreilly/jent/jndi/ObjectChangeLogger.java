package oreilly.jent.jndi;

/**
 * ObjectChangeLogger: A sample ObjectChangeListener that simply prints
 *   information about the object change events it encounters.
 * 
 * Example 7-13, Java Enterprise in a Nutshell, 2nd ed.
 * Author: Jim Farley
 */

import javax.naming.*;
import javax.naming.event.*;

public class ObjectChangeLogger implements ObjectChangeListener {
  // Default constructor
  public ObjectChangeLogger() {}

  // Callback for object change events
  public void objectChanged(NamingEvent ev) {
    Binding bNew = ev.getNewBinding();
    Binding bOld = ev.getOldBinding();
    System.out.println("--> CHANGE: Object of type " + bNew.getClassName() +
                       " changed, previous binding = \"" + bOld.toString() +
                       "\" post-change binding = \"" + bNew.toString() + "\"");
  }

  // Callback for errors in the naming service
  public void namingExceptionThrown(NamingExceptionEvent ev) {
    System.out.println("--> ERROR: An error occurred in the naming service:");
    ev.getException().printStackTrace();
  }
}
      
