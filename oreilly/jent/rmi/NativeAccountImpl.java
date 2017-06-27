package oreilly.jent.rmi;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.List;
import java.util.ListIterator;

/**
 * NativeAccountImpl: Implementation of the Account remote interface using
 *   JNI native methods for the account transactions.
 *
 * Example 3-4, Java Enterprise in a Nutshell, 2nd ed.
 * Author: Jim Farley
 */

public class NativeAccountImpl extends UnicastRemoteObject implements Account {
  // Our current balance
  private float mBalance = 0;
  // Name on account
  private String mName = "";
  // Create a new account with the given name
  public NativeAccountImpl(String name) throws RemoteException {
    mName = name;
  }

  public String getName() throws RemoteException {
    return mName;
  }
  public float getBalance() throws RemoteException {
    return mBalance;
  }

  // Withdraw some funds
  native public void withdraw(float amt)
    throws RemoteException, InsufficientFundsException;
  // Deposit some funds
  native public void deposit(float amt) throws RemoteException;
  // Move some funds from another (remote) account into this one
  native public void transfer(float amt, Account src)
    throws RemoteException, InsufficientFundsException;

  // Make several transfers from other (remote) accounts into this one
  public void transfer(List amts, List srcs)
    throws RemoteException, InsufficientFundsException {
    ListIterator amtCurs = amts.listIterator();
    ListIterator srcCurs = srcs.listIterator();
    // Iterate through the accounts and the amounts to be
    // transferred from each (assumes amounts are given as Float
    // objects)
    while (amtCurs.hasNext() && srcCurs.hasNext()) {
      Float amt = (Float)amtCurs.next();
      Account src = (Account)srcCurs.next();
      // Make the transaction
      this.transfer(amt.floatValue(), src);
    }
  }
  // Check to see if the transfer is possible, given the source account 
  private boolean checkTransfer(Account src, float amt) {
    boolean approved = false;
    try {
      if (src.getBalance() >= amt) {
        approved = true;
      }
    }
    catch (RemoteException re) {
      // If some remote exception occurred, then the transfer is still
      // compromised, so return false
      approved = false;
    }
    return approved;
  }
}
