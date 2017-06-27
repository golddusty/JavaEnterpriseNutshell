package oreilly.jent.ejb.stateful;

/**
 * ProfileHome: A remote home interface for the Profile EJB.
 * 
 * Example 8-10, Java Enterprise in a Nutshell, 2nd ed.
 * Author: Jim Farley
 */

import oreilly.jent.ejb.*;
import javax.ejb.*;
import java.rmi.RemoteException;

public interface ProfileHome extends EJBHome {
  // Create a new (nameless) profile
  public Profile create() throws RemoteException, CreateException;

  // Create a profile for a named person.  Throws an exception if the person's
  // profile can't be found.
  public Profile create(String name)
    throws RemoteException, CreateException, NoSuchPersonException;
}
