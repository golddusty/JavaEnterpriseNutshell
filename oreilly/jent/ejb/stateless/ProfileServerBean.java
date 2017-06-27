package oreilly.jent.ejb.stateless;

/**
 * ProfileServerBean: Stateless session implementation of the 
 *   ProfileServer EJB.
 * 
 * Example 8-6, Java Enterprise in a Nutshell, 2nd ed.
 * Author: Jim Farley
 */

import javax.ejb.*;
import java.rmi.RemoteException;
import oreilly.jent.ejb.*;

public class ProfileServerBean implements SessionBean {
  // Store session context
  private SessionContext mContext = null;

  public ProfileServerBean() {}
  
  //--------------------------------------------------
  // Session bean methods
  //--------------------------------------------------

  // No need for us to activate anything in this bean, but we need to
  // provide an implementation.
  public void ejbActivate() {
    System.out.println("ProfileServerBean activated.");
  }

  // Nothing to do on a remove for this bean.
  public void ejbRemove() {
    System.out.println("ProfileServerBean removed.");
  }

  // No state to store on passivation.
  public void ejbPassivate() {
    System.out.println("ProfileServerBean passivated.");
  }

  // Get context from container, store in a member variable.
  public void setSessionContext(SessionContext context) {
    System.out.println("ProfileServerBean context set.");
    mContext = context;
  }

  // No-argument create() -- nothing to initialize in this case.
  public void ejbCreate() {
    System.out.println("ProfileServerBean created.");
  }

  //--------------------------------------------------
  // Business methods
  //--------------------------------------------------
  
  // Get a profile for a named person.  Throws NoSuchPersonException if the
  // named person cannot be found in the persistent storage used to store
  // profile information.
  public ProfileBean getProfile(String name) throws NoSuchPersonException {
    // Here, we just create one of our serializable Profile objects and return
    // it (i.e., no persistence of profile data is provided).
    ProfileBean profile = new oreilly.jent.ejb.stateless.ProfileBean(name);
    return profile;
  }
}
