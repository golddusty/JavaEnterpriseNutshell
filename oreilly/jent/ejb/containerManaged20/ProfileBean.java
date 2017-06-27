package oreilly.jent.ejb.containerManaged20;

/**
 * ProfileBean: A container-managed persistence (CMP) implementation of
 *   the Profile EJB.  This implementation uses CMP 2.0.  See the
 *   oreilly.jent.ejb.containerManaged package for a CMP 1.1 version.
 * 
 * Example 8-13, Java Enterprise in a Nutshell, 2nd ed.
 * Author: Jim Farley
 */

import javax.ejb.*;
import java.util.Properties;
import java.io.*;
import oreilly.jent.ejb.*;

abstract public class ProfileBean implements EntityBean {
  //--------------------------------------------------
  // Persistent data properties
  //--------------------------------------------------

  // EJB 2.0: Just provide abstract accessors for the properties
  
  // Name of the person owning the profile
  abstract public String getName();
  abstract public void setName(String name);
  
  // Serialized representation of profile entries, for storage in persistent
  // store, or passivated state
  abstract public byte[] getEntriesBytes();
  abstract public void setEntriesBytes(byte[] entries);

  // EJB 1.1: Define actual data members to hold persistent data
  
  // Name of the person owning the profile
  //public String mName = "";
  // Serialized representation of profile entries, for storage in persistent
  // store, or passivated state
  //public byte[] mEntriesBytes = null;

  // Entries in the profile (name/value pairs)
  transient private Properties mEntries = new Properties();

  // Our entity context
  private EntityContext mContext = null;

  //--------------------------------------------------
  // Utility methods
  //--------------------------------------------------

  // Transfer the list of entries from our Properties member to the byte
  // array.
  private void transferToBytes() throws IOException {
    // Serialize the Properties into a byte array using an ObjectOutputStream
    if (mEntries != null && !mEntries.isEmpty()) {
      ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
      ObjectOutputStream objOut = new ObjectOutputStream(byteOut);
      objOut.writeObject(mEntries);
      setEntriesBytes(byteOut.toByteArray());
    }
    else {
      setEntriesBytes(null);
    }
  }

  // Convert the serialized byte array into our Properties entry list.
  private void transferToProps() throws IOException {
    // Take the raw byte array and de-serialize it
    // back into a Properties object using an ObjectInputStream
    try {
      if (getEntriesBytes() != null) {
        ByteArrayInputStream byteIn =
          new ByteArrayInputStream(getEntriesBytes());
        ObjectInputStream objIn = new ObjectInputStream(byteIn);
        mEntries = (Properties)objIn.readObject();
      }
      // If no entries in database, set properties to a new, empty collection
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
  public void ejbLoad() {
    try {
      transferToProps();
    }
    catch (IOException e) {
      System.out.println("Failed to load ProfileBean: ");
      e.printStackTrace();
      throw new EJBException("ejbLoad failed: ", e);
    }
    System.out.println("ProfileBean load finished.");
  }

  // Store bean to persistent store.  We store our Properties object in the
  // database as a serialized byte array.  Here, we serialize the Properties
  // object so that the container can store it.
  public void ejbStore() {
    try {
      transferToBytes();
    }
    catch (IOException e) {
      System.out.println("Failed to store ProfileBean: ");
      e.printStackTrace();
      throw new EJBException("ejbStore failed: ", e);
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
  public void unsetEntityContext() {
    System.out.println("ProfileBean context unset.");
    mContext = null;
  }

  // Create method (corresponds to each create() method on the
  // home interface, ProfileHome).  Here, we initialize our persistent
  // properties to sensible starting values.
  public String ejbCreate() {
    System.out.println("Nameless ProfileBean created.");
    setName(" ");
    setEntriesBytes(null);
    return null;
  }

  // Post-creation notification.  Nothing to do here, but we need
  // to provide an implementation.
  public void ejbPostCreate() {
    System.out.println("ProfileBean post-create called.");
  }
   
  // Create method with name of profile owner.
  public String ejbCreate(String name) throws NoSuchPersonException {
    setName(name);
    setEntriesBytes(null);
    System.out.println("ProfileBean created for " + name + ".");
    return null;
  }

  // Post-creation notification.  Nothing to do here, what we need
  // to provide an implementation.
  public void ejbPostCreate(String name) {
    System.out.println("ProfileBean post-create called.");
  }

  //--------------------------------------------------
  // Business methods
  //--------------------------------------------------
  
  public String getEntry(String key) {
    System.out.println("getEntry(): principle = " +
                       mContext.getCallerPrincipal().getName());
    return mEntries.getProperty(key);
  }

  public void setEntry(String key, String value) {
    mEntries.put(key, value);
  }

  // EJB 2.0: Accessors for the relationship with the corresponding
  //          Person bean.
  
  // Get local person for this profile
//   abstract public PersonLocal getPersonLocal();
//   abstract public void setPersonLocal(PersonLocal person);
}
