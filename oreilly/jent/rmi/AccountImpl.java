package oreilly.jent.rmi;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.List;
import java.util.ListIterator;

/**
 * AccountImpl: Implementation of the Account remote interface.
 *
 * Example 3-2, Java Enterprise in a Nutshell, 2nd ed.
 * Author: Jim Farley
 */

public class AccountImpl extends UnicastRemoteObject implements Account {
  // Our current balance
  private float mBalance = 0;
  // Name on account
  private String mName = "";
  // Create a new account with the given name
  public AccountImpl(String name) throws RemoteException {
    mName = name;
  }

  public String getName() throws RemoteException {
    return mName;
  }
  public float getBalance() throws RemoteException {
    return mBalance;
  }
  // Withdraw some funds
  public void withdraw(float amt)
    throws RemoteException, InsufficientFundsException {
    if (mBalance >= amt) {
      mBalance -= amt;
      // Log transaction...
      System.out.println("--> Withdrew " + amt + " from account " + getName());
      System.out.println("    New balance: " + getBalance());
    }
    else {
      throw new InsufficientFundsException("Withdrawal request of " + amt +
					   " exceeds balance of " + mBalance);
    }
  }   
  // Deposit some funds
  public void deposit(float amt) throws RemoteException {
    mBalance += amt;
    // Log transaction...
    System.out.println("--> Deposited " + amt + " into account " + getName());
    System.out.println("    New balance: " + getBalance());
  }
  // Move some funds from another (remote) account into this one
  public void transfer(float amt, Account src)
    throws RemoteException, InsufficientFundsException {
    if (checkTransfer(src, amt)) {
      src.withdraw(amt);
      this.deposit(amt);
      // Log transaction...
      System.out.println("--> Transferred " + amt + " from account " + getName());
      System.out.println("    New balance: " + getBalance());
    }
    else {
      throw new InsufficientFundsException("Source account balance is less " +
                                           "than the requested transfer.");
    }
  }
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
