package oreilly.jent.rmi;

import java.rmi.Naming;
import java.rmi.RMISecurityManager;

/**
 * AccountClient: An example client for an RMI-exported Account.
 *
 * Example from ch 3, "RMI", Java Enterprise in a Nutshell, 2nd ed.
 * Author: Jim Farley
 */

public class AccountClient {
  public static void main(String argv[]) {
    try {
      System.setSecurityManager(new RMISecurityManager());

      // Lookup account object
      Account jimAcct = (Account)Naming.lookup("rmi://localhost/JimF");

      // Make deposit
      jimAcct.deposit(12000);

      // Report results and balance.
      System.out.println("Deposited 12,000 into account owned by " +
                         jimAcct.getName());
      System.out.println("Balance now totals: " + jimAcct.getBalance());
    }
    catch (Exception e) {
      System.out.println("Error while looking up account:");
      e.printStackTrace();
    }
  }
}
