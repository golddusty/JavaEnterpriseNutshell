package oreilly.jent.ejb.stateless;

/**
 * ProfileServerHome: The remote "home" interface for the ProfileServer EJB.  
 * This interface provides methods used to create beans on the server.  The 
 * container provider is responsible for generating an implemention of this 
 * interface, most likely using auto-generated Java classes derived from the 
 * interface bytecodes.
 * 
 * Example 8-4, Java Enterprise in a Nutshell, 2nd ed.
 * Author: Jim Farley
 */

import javax.ejb.*;
import java.rmi.RemoteException;

public interface ProfileServerHome extends EJBHome {
  // Only a default create() method provided here.  There must be a matching
  // ejbCreate() method on the EJB implementation class.
  public ProfileServer create() throws CreateException, RemoteException;
}
