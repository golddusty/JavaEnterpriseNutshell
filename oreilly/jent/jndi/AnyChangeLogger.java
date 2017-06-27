package oreilly.jent.jndi;

/**
 * AnyChangeLogger: A sample listener that acts as both a NamespaceChangeListener
 *   and an ObjectChangeListener, and simply prints out information about
 *   the events it encounters.
 * 
 * Example 7-14, Java Enterprise in a Nutshell, 2nd ed.
 * Author: Jim Farley
 */

import javax.naming.*;
import javax.naming.event.*;

public class AnyChangeLogger
  implements NamespaceChangeListener, ObjectChangeListener {
  // Default constructor
  public AnyChangeLogger() {}

  //
  // NamespaceChangeListener methods
  //
  
  // Callback for object addition events
  public void objectAdded(NamingEvent ev) {
    Binding b = ev.getNewBinding();
    System.out.println("--> ADD: Object of type " + b.getClassName() +
                       " added at binding \"" + b.toString() + "\"");
  }
  
  // Callback for object removal events
  public void objectRemoved(NamingEvent ev) {
    Binding b = ev.getOldBinding();
    System.out.println("--> REMOVE: Object of type " + b.getClassName() +
                       " removed from binding \"" + b.toString() + "\"");
  }

  // Callback for object rename events
  public void objectRenamed(NamingEvent ev) {
    Binding bNew = ev.getNewBinding();
    Binding bOld = ev.getOldBinding();
    System.out.println("--> RENAME: Object of type " + bNew.getClassName() +
                       " renamed from binding \"" + bOld.toString() +
                       "\" to binding \"" + bNew.toString() + "\"");
  }

  //
  // ObjectChangeListener methods
  //
  
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
      
