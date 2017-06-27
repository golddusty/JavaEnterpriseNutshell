package oreilly.jent.corba;

/**
 * AccountImpl: Implementation of the Account remote interface, based on the
 * pre-POA version of the IDL-to-Java mapping (used in JDK 1.2 and 1.3).
 *
 * Author: Jim Farley
 */

public class AccountImplPrePOA extends _AccountImplBase {
  // Our current balance
  private float mBalance = 0;
  // Name on account
  private String mName = "";
  // Create a new account with the given name
  public AccountImplPrePOA(String name) {
    mName = name;
  }

  public String getName() {
    return mName;
  }
  public float getBalance() {
    return mBalance;
  }
  // Withdraw some funds
  public void withdraw(float amt) throws InsufficientFundsException {
    if (mBalance >= amt) {
      mBalance -= amt;
      // Log transaction...
      System.out.println("--> Withdrew " + amt + " from account " + getName());
      System.out.println("    New balance: " + getBalance());
    }
    else {
      throw new InsufficientFundsException();
    }
  }   
  // Deposit some funds
  public void deposit(float amt) {
    mBalance += amt;
    // Log transaction...
    System.out.println("--> Deposited " + amt + " into account " + getName());
    System.out.println("    New balance: " + getBalance());
  }
  // Move some funds from another (remote) account into this one
  public void transfer(float amt, Account src)
    throws InsufficientFundsException {
    if (checkTransfer(src, amt)) {
      src.withdraw(amt);
      this.deposit(amt);
      // Log transaction...
      System.out.println("--> Transferred " + amt + " from account " + getName());
      System.out.println("    New balance: " + getBalance());
    }
    else {
      throw new InsufficientFundsException();
    }
  }
  // Make several transfers from other (remote) accounts into this one
  public void transferBatch(float[] amts, Account[] srcs)
    throws InsufficientFundsException {
    // Iterate through the accounts and the amounts to be
    // transferred from each
    for (int i = 0; i < amts.length; i++) {
      float amt = amts[i];
      Account src = srcs[i];
      // Make the transaction
      this.transfer(amt, src);
    }
  }
  // Check to see if the transfer is possible, given the source account 
  private boolean checkTransfer(Account src, float amt) {
    boolean approved = false;
    if (src.getBalance() >= amt) {
      approved = true;
    }
    return approved;
  }
}
