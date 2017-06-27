package oreilly.jent.jndi;

/**
 * NamespaceChangeLogger: A sample NamespaceChangeListener that simply prints
 *   information about the namespace events.
 * 
 * Example 7-12, Java Enterprise in a Nutshell, 2nd ed.
 * Author: Jim Farley
 */

import javax.naming.*;
import javax.naming.event.*;

public class NamespaceChangeLogger implements NamespaceChangeListener {
  // Default constructor
  public NamespaceChangeLogger() {}

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

  // Callback for errors in the naming service
  public void namingExceptionThrown(NamingExceptionEvent ev) {
    System.out.println("--> ERROR: An error occurred in the naming service:");
    ev.getException().printStackTrace();
  }
}
      
