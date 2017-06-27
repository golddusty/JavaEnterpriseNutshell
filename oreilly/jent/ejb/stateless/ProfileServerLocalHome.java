package oreilly.jent.ejb.stateless;

/**
 * ProfileServerLocalHome: The local "home" interface for the ProfileServer EJB.  
 * This interface provides methods used by local clients (running in the same 
 * JVM as the EJB container) to create beans on the server.  
 * 
 * Example 8-5, Java Enterprise in a Nutshell, 2nd ed.
 * Author: Jim Farley
 */

import javax.ejb.*;

public interface ProfileServerLocalHome extends EJBLocalHome {
  // Only a default create() method provided here.  There must be a matching
  // ejbCreate() method on the EJB implementation class.
  public ProfileServerLocal create() throws CreateException;
}
