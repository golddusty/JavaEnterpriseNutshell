package oreilly.jent.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;

/**
 * AccountManagerImpl: Sample implementation of the AccountManager interface.
 * This simple example simply registers each account with the local registry, 
 * and does the lookup for the remote client when getAccount() is called.
 *
 * Example from ch 3, "RMI", Java Enterprise in a Nutshell, 2nd ed.
 * Author: Jim Farley
 */

public class AccountManagerImpl
  extends UnicastRemoteObject implements AccountManager {
  public AccountManagerImpl() throws RemoteException {}
  public Account getAccount(String name) throws RemoteException {
    Registry reg = LocateRegistry.getRegistry();
    Account a = null;
    try {
      a = (Account)reg.lookup(name);
    }
    catch (Exception e) {}
    return a;
  }
  
  public boolean newAccount(Account a) throws RemoteException {
    Registry reg = LocateRegistry.getRegistry();
    System.out.print("Registering new account...");
    reg.rebind(a.getName(), a);
    System.out.println("done.");
    return true;
  }
}  
