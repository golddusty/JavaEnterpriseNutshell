package oreilly.jent.ejb.containerManaged20;

/*
 * This example is from the book "Java Enterprise in a Nutshell", 2nd ed.
 * Copyright (c) 1999, 2001 by O'Reilly & Associates.  
 * You may distribute this source code for non-commercial purposes only.
 * You may study, modify, and use this example for any purpose, as long as
 * this notice is retained.  Note that this example is provided "as is",
 * WITHOUT WARRANTY of any kind either expressed or implied.
 */

import javax.ejb.*;
import java.rmi.RemoteException;
import oreilly.jent.ejb.*;
import java.util.*;

/**
 * The "home" interface for the ProfileBean.  This interface provides
 * methods used to create beans on the server.  The container provider is
 * responsible for implementing this interface, most likely using
 * auto-generated Java classes derived from the interface bytecodes.
 */

public interface ProfileHome extends EJBHome {
  // Create a new (nameless) profile
  public Profile create() throws CreateException, RemoteException;

  // Create a profile for a named person.  Throws an exception if the person's
  // profile can't be found.
  public Profile create(String name)
    throws CreateException, RemoteException, NoSuchPersonException;

  // Lookup a Profile by name (the primary key)
  public Profile findByPrimaryKey(String name)
    throws RemoteException, FinderException;

  // Lookup a Profile by a person's first and last name
  public Profile findByNameComponents(String fname, String lname)
    throws RemoteException, FinderException;

  // Find any profiles with no entries
  public Collection findEmptyProfiles()
    throws RemoteException, FinderException;
  
  // Lookup a Profile by the value of a particular entry in the profile.
//   public Enumeration findByEntryValue(String key, String value)
//     throws RemoteException, FinderException;
}
