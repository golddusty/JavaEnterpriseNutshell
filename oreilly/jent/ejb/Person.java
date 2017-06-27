package oreilly.jent.ejb;

import javax.ejb.*;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Person extends EJBObject {
  // Access the person's name
  public String getFirstName() throws RemoteException;
  public String getLastName() throws RemoteException;
  // Get this person's profile
  public Profile getProfile() throws RemoteException;
}
