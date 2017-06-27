package oreilly.jent.ejb.stateless;

/**
 * ProfileServer: A stateless session EJB that provides a simple lookup
 * service for named people or accounts.
 * 
 * Example 8-1, Java Enterprise in a Nutshell, 2nd ed.
 * Author: Jim Farley
 */

import javax.ejb.*;
import java.rmi.RemoteException;
import java.rmi.Remote;
import oreilly.jent.ejb.*;

public interface ProfileServer extends EJBObject {
  public ProfileBean getProfile(String acctName)
    throws NoSuchPersonException, RemoteException;
}
