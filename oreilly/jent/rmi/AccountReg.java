package oreilly.jent.rmi;

import java.rmi.*;

/**
 * RegAccount: A utility class that registers an account with the local
 * RMI registry for remote access
 *
 * Example from ch. 3, "RMI", Java Enterprise in a Nutshell, 2nd ed.
 * Author: Jim Farley
 */

public class AccountReg {
  public static void main(String argv[]) {
    try {
      System.setSecurityManager(new RMISecurityManager());
      // Make an Account with a given name
      AccountImpl acct = new AccountImpl("JimF");

      // Register it with the local naming registry
      Naming.rebind("JimF", acct);
      System.out.println("Registered account as JimF.");
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
}
