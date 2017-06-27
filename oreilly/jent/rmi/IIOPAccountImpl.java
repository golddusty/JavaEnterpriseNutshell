package oreilly.jent.rmi;

import javax.rmi.PortableRemoteObject;
import java.rmi.RemoteException;
import java.util.List;
import java.util.ListIterator;

// Implementation of the Account remote interface, using RMI/IIOP

public class IIOPAccountImpl extends PortableRemoteObject implements Account {
  // Our current balance
  private float mBalance = 0;
  // Name on account
  private String mName = "";
  // Create a new account with the given name
  public IIOPAccountImpl(String name) throws RemoteException {
    mName = name;
  }

  public String getName() throws RemoteException {
    return mName;
  }
  public float getBalance() throws RemoteException {
    return mBalance;
  }
  // Withdraw some funds
  public void withdraw(float amt) throws RemoteException {
    mBalance -= amt;
    // Make sure balance never drops below zero
    mBalance = Math.max(mBalance, 0);
  }
  // Deposit some funds
  public void deposit(float amt) throws RemoteException {
    mBalance += amt;
  }
  // Move some funds from another (remote) account into this one
  public void transfer(float amt, Account src)
    throws RemoteException, InsufficientFundsException {
    src.withdraw(amt);
    this.deposit(amt);
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
}
