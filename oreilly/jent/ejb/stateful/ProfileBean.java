package oreilly.jent.ejb.stateful;

/**
 * ProfileBean: A stateful session implementation of the Profile EJB.
 * 
 * Example 8-9, Java Enterprise in a Nutshell, 2nd ed.
 * Author: Jim Farley
 */

import javax.ejb.*;
import java.rmi.RemoteException;
import oreilly.jent.ejb.*;
import java.util.Properties;

// A stateful session Profile, which provides profile information
// for a named person.
public class ProfileBean implements SessionBean {

  // State for this (stateful) bean

  // Name of the person owning the profile
  private String mName = "";
  // Entries in the profile (name/value pairs)
  private Properties mEntries = new Properties();
  
  // Store session context
  private SessionContext mContext = null;
  
  //--------------------------------------------------
  // Session bean methods
  //--------------------------------------------------

  // No need for us to activate anything in this bean, but we need to
  // provide an implementation.
  public void ejbActivate() {
    System.out.println("ProfileBean activated.");
  }

  // Nothing to do on a remove.
  public void ejbRemove() {
    System.out.println("ProfileBean removed.");
  }

  // No resources to release on passivation...
  public void ejbPassivate() {
    System.out.println("ProfileBean passivated.");
  }

  // Get context from container.
  public void setSessionContext(SessionContext context) {
    System.out.println("ProfileBean context set.");
    mContext = context;
  }

  // Create method (corresponds to each create() method on the
  // home interface, ProfileHome).  Nothing to initialize in this case.
  public void ejbCreate() {
    System.out.println("Nameless ProfileBean created.");
  }

  // Create method with name of profile owner.
  public void ejbCreate(String name) throws NoSuchPersonException {
    mName = name;
    System.out.println("ProfileBean created for " + mName + ".");
  }

  //--------------------------------------------------
  // Business methods
  //--------------------------------------------------

  public String getName() {
    return mName;
  }

  public String getEntry(String key) {
    return mEntries.getProperty(key);
  }

  public void setEntry(String key, String value) {
    mEntries.put(key, value);
  }
}
