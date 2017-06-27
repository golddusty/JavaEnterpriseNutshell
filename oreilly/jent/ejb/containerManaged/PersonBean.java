package oreilly.jent.ejb.containerManaged;

/*
 * This example is from the book "Java Enterprise in a Nutshell", 2nd ed.
 * Copyright (c) 1999,2001 by O'Reilly & Associates.  
 * You may distribute this source code for non-commercial purposes only.
 * You may study, modify, and use this example for any purpose, as long as
 * this notice is retained.  Note that this example is provided "as is",
 * WITHOUT WARRANTY of any kind either expressed or implied.
 */

import javax.ejb.*;
import javax.naming.*;
import java.rmi.RemoteException;
import java.util.Properties;
import oreilly.jent.ejb.*;

// A PersonBean, which represents a person stored in our database.
public class PersonBean implements EntityBean {
  // First name of person
  public String mFirstName = "";

  // Last name of person
  public String mLastName = "";

  // Store context
  public EntityContext mContext = null;

  // No action required on activation of this bean.
  public void ejbActivate() {
    System.out.println("ProfileBean activated.");
  }

  // Load bean from persistent store.  In this case, there is no action needed
  // after container performs the database load for us.
  public void ejbLoad() throws RemoteException {}

  // Store bean to persistent store.  In this case, there is no action needed
  // before the container performs the database store for us.
  public void ejbStore() throws RemoteException {}
  
  // Remove the bean from persistent storage.  No action needed here before the
  // container does the removal for us.
  public void ejbRemove() {}

  // Pre-passivation callback.  No action needed here.
  public void ejbPassivate() {}

  // Get our context from the container.
  public void setEntityContext(EntityContext context) {
    mContext = context;
  }

  // Container is removing our context.
  public void unsetEntityContext() throws RemoteException {
    mContext = null;
  }

  // Create method (corresponds to the create() method on the
  // home interface).  Nothing to initialize in this case.
  public PersonPK ejbCreate() {
    System.out.println("Nameless PersonBean created.");
    return new PersonPK("", "");
  }

  // Post-creation notification.  Nothing to do here, but we need
  // to provide an implementation.
  public void ejbPostCreate() {
    System.out.println("PersonBean post-create called.");
  }
   
  // Create method with name of person.
  public PersonPK ejbCreate(String fname, String lname) 
    throws NoSuchPersonException {
    mFirstName = fname;
    mLastName = lname;
    return new PersonPK(fname, lname);
  }

  // Post-creation notification.  Nothing to do here, what we need
  // to provide an implementation.
  public void ejbPostCreate(String fname, String lname) {}
   
  // "Business" methods
  
  public String getFirstName() {
    return mFirstName;
  }

  public String getLastName() {
    return mLastName;
  }

  // Lookup the profile for this person and return it.  In this case, our
  // profile is an entity EJB, so we do a find on its home for our named
  // profile.  If no profile is found, or if an error of some kind occurs,
  // a null profile is returned.
  public Profile getProfile() throws RemoteException {
    Profile myProfile = null;
    try {
      // Get profile home interface
      Context ctx = new InitialContext();
      ProfileHome pHome = (ProfileHome)ctx.lookup("ejb/CMP-ProfileHome");
      // Do a find based on our name (first name concatenated with last name)
      String pname = getFirstName() + getLastName();
      try {
	myProfile = pHome.findByPrimaryKey(pname);
      }
      catch (FinderException fe) {
	System.out.println("Error occurred looking up profile for " + pname);
	// Try creating a profile
	myProfile = pHome.create(pname);
      }
    }
    catch (NamingException ne) {
      System.out.println("Error occurred looking up profile home.");
      throw new RemoteException("Error looking up profile home", ne);
    }
    catch (CreateException ce) {
      System.out.println("Could neither find nor create a profile for "
			 + getFirstName() + getLastName());
      throw new RemoteException("Error accessing profile", ce);
    }
    catch (NoSuchPersonException nspe) {
      System.out.println("Could neither find nor create a profile for "
			 + getFirstName() + getLastName());
      throw new RemoteException("Error accessing profile", nspe);
    }
      
    return myProfile;
  }
}
