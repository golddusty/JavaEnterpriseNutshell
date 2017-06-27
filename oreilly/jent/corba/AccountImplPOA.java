package oreilly.jent.corba;

/**
 * AccountImplPOA: Implementation of the Account remote interface, based on the
 * CORBA 2.3 (POA-compliant) version of the IDL-to-Java mapping (introduced
 * in JDK 1.4).
 *
 * Example 4-4, Java Enterprise in a Nutshell, 2nd ed.
 * Author: Jim Farley
 */

public class AccountImplPOA extends AccountPOA {
  // Our current balance
  private float mBalance = 0;
  // Name on account
  private String mName = "";
  // Create a new account with the given name
  public AccountImplPOA(String name) {
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
      throw new InsufficientFundsException("Withdrawal request of " + amt +
                                           " exceeds balance of " + mBalance);
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
      throw new InsufficientFundsException("Source account balance is less " +
                                           "than the requested transfer.");
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
