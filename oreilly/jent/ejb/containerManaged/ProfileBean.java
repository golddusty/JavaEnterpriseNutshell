package oreilly.jent.ejb.containerManaged;

/*
 * This example is from the book "Java Enterprise in a Nutshell", 2nd ed.
 * Copyright (c) 1999,2002 by O'Reilly & Associates.  
 * You may distribute this source code for non-commercial purposes only.
 * You may study, modify, and use this example for any purpose, as long as
 * this notice is retained.  Note that this example is provided "as is",
 * WITHOUT WARRANTY of any kind either expressed or implied.
 */

import javax.ejb.*;
import java.rmi.RemoteException;
import java.util.Properties;
import java.io.*;
import oreilly.jent.ejb.*;

// A ProfileBean, which provides enterprise
// profile information for a named person.

public class ProfileBean implements EntityBean {
  //--------------------------------------------------
  // Persistent data properties
  //--------------------------------------------------

  // Name of the person owning the profile
  public String mName = "";

  // Entries in the profile (name/value pairs)
  transient private Properties mEntries = new Properties();

  // Serialized representation of profile entries, for storage in persistent
  // store, or passivated state
  public byte[] mEntriesBytes = null;

  // Store context
  private EntityContext mContext = null;

  //--------------------------------------------------
  // Utility methods
  //--------------------------------------------------

  // Transfer the list of entries from our Properties member to the byte
  // array.
  private void transferToBytes() throws IOException {
    if (!mEntries.isEmpty()) {
      // Serialize the Properties into a byte array using an ObjectOutputStream
      ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
      ObjectOutputStream objOut = new ObjectOutputStream(byteOut);
      objOut.writeObject(mEntries);
      mEntriesBytes = byteOut.toByteArray();
    }
    else {
      mEntriesBytes = null;
    }
  }

  // Convert the serialized byte array into our Properties entry list.
  private void transferToProps() throws IOException {
    // Take the raw byte array and de-serialize it
    // back into a Properties object using an ObjectInputStream
    try {
      if (mEntriesBytes != null) {
        ByteArrayInputStream byteIn = new ByteArrayInputStream(mEntriesBytes);
        ObjectInputStream objIn = new ObjectInputStream(byteIn);
        mEntries = (Properties)objIn.readObject();
      }
      // If no entries in database, set properties to new, empty collection
      else {
        mEntries = new Properties();
      }
    }
    catch (ClassNotFoundException cnfe) {
      System.out.println("Properties class not found during de-serialization");
    }
  }
  
  //--------------------------------------------------
  // Entity bean methods
  //--------------------------------------------------

  // After the container reloads our byte array from passivated state,
  // convert it back into a Properties object.
  public void ejbActivate() {
    try {
      transferToProps();
      System.out.println("ProfileBean activated.");
    }
    catch (IOException ioe) {
      System.out.println("Failed to convert entries during activation.");
      ioe.printStackTrace();
    }
  }

  // Before passivation occurs, copy the entries list from the Properties
  // member into the byte array.
  public void ejbPassivate() {
    try {
      transferToBytes();
      System.out.println("ProfileBean being passivated.");
    }
    catch (IOException ioe) {
      System.out.println("Failed to convert entries during activation.");
      ioe.printStackTrace();
    }
  }

  // Load bean from persistent store.  Since the profile entries are stored in
  // a non-primitive object (Properties), they are stored in the database as a
  // raw byte array (BLOB).  In this load method, we convert the serialized
  // bytes loaded by the container into the original Properties object.
  public void ejbLoad() throws RemoteException {
    try {
      transferToProps();
    }
    catch (IOException e) {
      System.out.println("Failed to load ProfileBean: ");
      e.printStackTrace();
      throw new RemoteException("ejbLoad failed: ", e);
    }
    System.out.println("ProfileBean load finished.");
  }

  // Store bean to persistent store.  We store our Properties object in the
  // database as a serialized byte array.  Here, we serialize the Properties
  // object so that the container can store it.
  public void ejbStore() throws RemoteException {
    try {
      transferToBytes();
    }
    catch (IOException e) {
      System.out.println("Failed to store ProfileBean: ");
      e.printStackTrace();
      throw new RemoteException("ejbStore failed: ", e);
    }
    System.out.println("ProfileBean store finished.");
  }
  
  // Nothing to do on a remove.
  public void ejbRemove() {
    System.out.println("ProfileBean removed.");
  }

  // Get context from container.
  public void setEntityContext(EntityContext context) {
    System.out.println("ProfileBean context set.");
    mContext = context;
  }

  // Container is removing our context.
  public void unsetEntityContext() throws RemoteException {
    System.out.println("ProfileBean context unset.");
    mContext = null;
  }

  // Create method (corresponds to each create() method on the
  // home interface, ProfileHome).  Nothing to initialize in this case.
  public String ejbCreate() {
    System.out.println("Nameless ProfileBean created.");
    return mName;
  }

  // Post-creation notification.  Nothing to do here, but we need
  // to provide an implementation.
  public void ejbPostCreate() {
    System.out.println("ProfileBean post-create called.");
  }
   
  // Create method with name of profile owner.
  public String ejbCreate(String name) throws NoSuchPersonException {
    mName = name;
    System.out.println("ProfileBean created for " + mName + ".");
    return mName;
  }

  // Post-creation notification.  Nothing to do here, what we need
  // to provide an implementation.
  public void ejbPostCreate(String name) {
    System.out.println("ProfileBean post-create called.");
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
