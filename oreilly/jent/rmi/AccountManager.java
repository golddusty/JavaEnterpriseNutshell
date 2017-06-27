package oreilly.jent.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * AccountManager: A manager for remote Accounts.
 *
 * Example from ch 3, "RMI", Java Enterprise in a Nutshell, 2nd ed.
 * Author: Jim Farley
 */

public interface AccountManager extends Remote {
  public Account getAccount(String name) throws RemoteException;
  public boolean newAccount(Account a) throws RemoteException;
}
