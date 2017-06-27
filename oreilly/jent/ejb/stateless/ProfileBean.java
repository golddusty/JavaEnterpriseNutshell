package oreilly.jent.ejb.stateless;

/**
 * ProfileBean: A serializable Profile JavaBean.
 * 
 * Example 8-2, Java Enterprise in a Nutshell, 2nd ed.
 * Author: Jim Farley
 */

import java.util.*;
import java.io.Serializable;

public class ProfileBean implements Serializable {
  protected Properties mPEntries = new Properties();

  public ProfileBean() {}
    
  public ProfileBean(String name) {
    mPEntries.put("Name", name);
  }

  public Hashtable getEntries() {
    return (Hashtable)mPEntries.clone();
  }
  
  public String getEntry(String name) {
    return mPEntries.getProperty(name);
  }
  
  public void setEntry(String name, String value) {
    mPEntries.put(name, value);
  }
}
