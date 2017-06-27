package oreilly.jent.ejb.beanManaged;

/**
 * ProfileBean: A bean-managed entity implementation of the Profile EJB.
 * 
 * Example 8-12, Java Enterprise in a Nutshell, 2nd ed.
 * Author: Jim Farley
 */

import javax.ejb.*;
import java.util.*;
import java.sql.*;
import javax.sql.*;
import javax.naming.*;
import java.io.*;
import oreilly.jent.ejb.*;

public class ProfileBean implements EntityBean {
  // Entries in the profile (name/value pairs)
  public Properties mEntries;

  // Our EJB context (non-persistent)
  public EntityContext mContext = null;

  //--------------------------------------------------
  // Entity bean methods
  //--------------------------------------------------

  // EJB activation method.  During activation, create our entry lookup table.
  public void ejbActivate() {
    mEntries = new Properties();
    System.out.println("ProfileBean activated.");
  }

  // Load bean from persistent store.  In this case, we're managing the dbase
  // storage, so we store our profile entries as independent records in a
  // separate "PROFILE_ENTRY" table.
  public void ejbLoad() {
    try {
      String key = (String)mContext.getPrimaryKey();
      loadFromDB(key);
    }
    catch (Exception e) {
      System.out.println("Failed to load ProfileBean: ");
      e.printStackTrace();
      throw new EJBException("ejbLoad failed: ", e);
    }
    System.out.println("ProfileBean load finished.");
  }

  protected void loadFromDB(String key) throws FinderException {
    boolean found = false;
    try {
      Connection conn = getConnection();
      Statement s = conn.createStatement();
      s.executeQuery("select name from profile where name = '" + key + "'");
      ResultSet rs = s.getResultSet();
      if (rs.next()) {
        found = true;
        s.executeQuery("select key, value from profile_entry where name = '" +
                       key + "'");
        rs = s.getResultSet();
        while (rs.next()) {
          String pKey = rs.getString(1);
          String pValue = rs.getString(2);
          mEntries.put(pKey, pValue);
        }
      }
    }
    catch (SQLException e) {
      throw new FinderException("Failed to load profile entries from DB: " +
                                e.toString());
    }
    catch (NamingException ne) {
      throw new FinderException("Failed to access DataSource from server: " +
                                ne.toString());
    }
    if (!found) {
      throw new FinderException("No profile found for " + key);
    }
  }
  
  // Store bean to persistent store.  Properties are stored as records in the
  // PROFILE_ENTRY table.
  public void ejbStore() {
    String key = (String)mContext.getPrimaryKey();
    try {
      Connection conn = getConnection();
      // Clear out old profile entries and replace with the current ones
      Statement s = conn.createStatement();
      s.executeUpdate("delete from profile_entry where name = '" + key + "'");
      Enumeration pKeys = mEntries.propertyNames();
      while (pKeys.hasMoreElements()) {
        String pKey = (String)pKeys.nextElement();
        String pValue = mEntries.getProperty(pKey);
        s.executeUpdate("insert into profile_entry (name, key, value) values "
                        + "('" + key + "', '" + pKey + "', '" + pValue + "')");
      }
      // Close the statement and the connection, just to be tidy...
      s.close();
      conn.close();
    }
    catch (Exception e) {
      System.out.println("Failed to store ProfileBean: ");
      e.printStackTrace();
      throw new EJBException("ejbStore failed: ", e);
    }
    System.out.println("ProfileBean store finished.");
  }
  
  // Remove this named profile from the database.
  public void ejbRemove() {
    // Get this profile's name
    String key = (String)mContext.getPrimaryKey();
    try {
      Connection conn = getConnection();
      // Clear out any profile entries
      Statement s = conn.createStatement();
      s.executeUpdate("delete from profile_entry where name = '" + key + "'");
      // Clear out the profile itself
      s.executeUpdate("delete from profile where name = '" + key + "'");

      s.close();
      conn.close();
      System.out.println("ProfileBean removed.");
    }
    catch (SQLException se) {
      System.out.println("Error removing profile for " + key);
      se.printStackTrace();
    }
    catch (NamingException ne) {
      System.out.println("Error accessing DataSource");
      ne.printStackTrace();
    }
  }

  // When we're passivated, release our entry table.
  public void ejbPassivate() {
    mEntries = null;
    System.out.println("ProfileBean passivated.");
  }

  // Get context from container.
  public void setEntityContext(EntityContext context) {
    mContext = context;
    System.out.println("ProfileBean context set.");
  }

  // Container is removing our context.
  public void unsetEntityContext() {
    mContext = null;
    System.out.println("ProfileBean context unset.");
  }

  //  Since we're managing persistence here in the bean, we need to
  //  implement the finder methods.
  public String ejbFindByPrimaryKey(String key) throws FinderException {
    loadFromDB(key);
    return key;
  }

  public Collection ejbFindByEntryValue(String key, String value)
    throws FinderException {
    LinkedList userList = new LinkedList();
    // Get a new connection from the EJB server
    try {
      Connection conn = getConnection();
      Statement s = conn.createStatement();
      // Issue a query for matching profile entries, grabbing just the name
      s.executeQuery("select distinct(name) from profile_entry where " +
                     " key = '" + key + "' and value = '" + value + "'");
      // Convert the results in primary keys and return an enumeration
      ResultSet results = s.getResultSet();
      while (results.next()) {
        String name = results.getString(1);
        userList.add(name);
      }
    }
    catch (SQLException se) {
      // Failed to do database lookup
      throw new FinderException();
    }
    catch (NamingException ne) {
      // Failed to access DataSource
      throw new FinderException();
    }
    return userList;
  }

  // Create method with name of profile owner.
  public String ejbCreate(String name)
    throws DuplicateProfileException, CreateException {
    try {
      Connection conn = getConnection();
      Statement s = conn.createStatement();
      s.executeUpdate("insert into profile (name) values ('" + name + "')");
      s.close();
      conn.close();
    }
    catch (SQLException se) {
      System.out.println("Error creating profile, assuming duplicate.");
      try {
        StringWriter strw = new StringWriter();
        PrintWriter prntw = new PrintWriter(strw);
        se.printStackTrace(prntw);
        throw new DuplicateProfileException("SQL error creating profile for " +
                                            name + ": " + se.toString() +
                                            "\n" + strw.toString());
      }
      catch (Exception e) {}
    }
    catch (NamingException ne) {
      System.out.println("Error accessing DataSource");
      throw new CreateException("Error accessing DataSource.");
    }
    System.out.println("ProfileBean created for " + name + ".");
    return name;
  }

  // Post-creation notification.  Nothing to do here, but we need
  // to provide an implementation.
  public void ejbPostCreate(String name) {
    System.out.println("ProfileBean post-create called for " + name + ".");
  }
   
  //--------------------------------------------------
  // Utility methods
  //--------------------------------------------------

  // Get a connection from our J2EE data source.
  private Connection getConnection() throws SQLException, NamingException {
    Context ctx = new InitialContext();
    DataSource profileDB =
      (DataSource)ctx.lookup("java:comp/env/jdbc/profileDB");
    return profileDB.getConnection();
  }

  //--------------------------------------------------
  // Business methods
  //--------------------------------------------------

  // Returns the name of the owner of this profile.
  public String getName() {
    return (String)mContext.getPrimaryKey();
  }

  // Returns the value of the given entry in this profile, or null
  // if the given property isn't present in this profile.
  public String getEntry(String key) {
    return mEntries.getProperty(key);
  }

  // Sets the value of the property to the given value.
  public void setEntry(String key, String value) {
    if (mEntries == null) {
      mEntries = new Properties();
    }
    mEntries.put(key, value);
  }
}
