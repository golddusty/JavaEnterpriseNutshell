package oreilly.jent.ejb;

/**
 * Profile: A remote interface for the Profile EJB.
 * 
 * Example 8-8, Java Enterprise in a Nutshell, 2nd ed.
 * Author: Jim Farley
 */

import java.rmi.Remote;
import java.rmi.RemoteException;
import javax.ejb.*;

public interface Profile extends EJBObject {
  // Get the name of the user associated with this profile
  public String getName() throws RemoteException;
  // Lookup an entry by name
  public String getEntry(String name) throws RemoteException;
  // Set an entry value
  public void setEntry(String name, String value) throws RemoteException;
}
