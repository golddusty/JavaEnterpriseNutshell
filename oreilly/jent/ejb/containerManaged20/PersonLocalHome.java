package oreilly.jent.ejb.containerManaged20;

/*
 * This example is from the book "Java Enterprise in a Nutshell", 2nd ed.
 * Copyright (c) 1999,2001 by O'Reilly & Associates.  
 * You may distribute this source code for non-commercial purposes only.
 * You may study, modify, and use this example for any purpose, as long as
 * this notice is retained.  Note that this example is provided "as is",
 * WITHOUT WARRANTY of any kind either expressed or implied.
 */

import javax.ejb.*;
import java.rmi.RemoteException;
import oreilly.jent.ejb.*;

/**
 * The "home" interface for the PersonBean.  This interface provides
 * methods used to create beans on the server.  The container provider is
 * responsible for implementing this interface, most likely using
 * auto-generated Java classes derived from the interface bytecodes.
 */
public interface PersonLocalHome extends EJBLocalHome {
  // Create a new (nameless) person
  public PersonLocal create() throws CreateException;

  // Create a named person.
  public PersonLocal create(String name, String fname, String lname)
    throws NoSuchPersonException, CreateException;

  // Lookup a Person by name (the primary key)
  public PersonLocal findByPrimaryKey(String name)
    throws FinderException;
}
