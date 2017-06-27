package oreilly.jent.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Account: A simple RMI interface that represents a banking account
 *          of some kind.
 *
 * Example 3-1, Java Enterprise in a Nutshell, 2nd ed.
 * Author: Jim Farley
 */

public interface Account extends java.rmi.Remote {
  // Get the name on the account
  public String getName() throws RemoteException;
  // Get current balance
  public float getBalance() throws RemoteException;
  // Take some money away
  public void withdraw(float amt)
    throws RemoteException, InsufficientFundsException;
  // Put some money in
  public void deposit(float amt) throws RemoteException;
  // Move some money from one account into this one
  public void transfer(float amt, Account src)
    throws RemoteException, InsufficientFundsException;
  // Make a number of transfers from other accounts into this one
  public void transfer(List amts, List srcs)
    throws RemoteException, InsufficientFundsException;
}
