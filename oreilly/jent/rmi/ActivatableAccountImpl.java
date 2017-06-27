package oreilly.jent.rmi;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.activation.*;
import java.rmi.MarshalledObject;
import java.rmi.RemoteException;
import java.util.List;
import java.util.ListIterator;
import java.io.IOException;

/**
 * ActivatableAccountImpl: Activatable implementation of the Account remote
 * interface.
 *
 * Example 3-3, Java Enterprise in a Nutshell, 2nd ed.
 * Author: Jim Farley
 */

public class ActivatableAccountImpl extends Activatable implements Account {
  // Our current balance
  private float mBalance = 0;
  // Name on account
  private String mName = "";

  // "Regular" constructor used to create a "pre-activated" server
  public ActivatableAccountImpl(String name)
    throws RemoteException, ActivationException, IOException {
    // Register and export object (on random open port)
    // Note that we're 
    super(null, new MarshalledObject(new AccountState(name, 0f)), false, 0);
    mName = name;
  }
  // Constructor called by the activation runtime to (re)activate
  // and export the server
  protected ActivatableAccountImpl(ActivationID id, MarshalledObject arg)
      throws RemoteException, ActivationException {
    // Export this object with the given activation id, on random port
    super(id, 0);
    System.out.println("Activating an account");
    // Check incoming data (account state) passed in with activation request
    try {
      Object oarg = arg.get();
      if (oarg instanceof AccountState) {
        AccountState s = (AccountState)oarg;
        // Set our name and balance based on incoming state
        mName = s.name;
        mBalance = s.balance;
      }
      else {
        System.out.println("Unknown argument type received on activation: " +
                           oarg.getClass().getName());
      }
    }
    catch(Exception e) {
      System.out.println("Error retrieving argument to activation");
    }
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

// Define a structure to hold our state information in a single marshalled
// object, so that we can register it with the activation system
class AccountState {
  public float balance = 0f;
  public String name = null;
  public AccountState(String n, float b) {
    name = n;
    balance = b;
  }
}
