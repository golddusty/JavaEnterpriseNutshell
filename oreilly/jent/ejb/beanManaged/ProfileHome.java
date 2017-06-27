package oreilly.jent.ejb.beanManaged;

/**
 * ProfileHome: A remote home interface for the entity Profile EJB.
 * 
 * Example 8-11, Java Enterprise in a Nutshell, 2nd ed.
 * Author: Jim Farley
 */

import javax.ejb.*;
import java.rmi.RemoteException;
import java.util.Collection;
import oreilly.jent.ejb.*;

public interface ProfileHome extends EJBHome {
  // Create a profile for a named person.
  public Profile create(String name)
    throws CreateException, RemoteException, DuplicateProfileException;

  // Lookup a Profile by name (the primary key)
  public Profile findByPrimaryKey(String key)
    throws RemoteException, FinderException;

  // Lookup a Profile by the value of a particular entry in the profile.
  public Collection findByEntryValue(String key, String value)
    throws RemoteException, FinderException;
}
