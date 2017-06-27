package oreilly.jent.corba;

import org.omg.CORBA.*;
import org.omg.CORBA.ORBPackage.*;
import org.omg.CosNaming.*;
import java.util.*;

/**
 * AccountClient: A client that looks up the account "named" on the
 *    command-line, then performs the requested transaction on the account.
 *    The supported transactions are "deposit", "withdrawal" and "balance".
 *    The "name" of the account can either be a stringified object reference
 *    (in which case the ORB string_to_object() method is used to locate the
 *    referenced object), or a simple String, in which case the Naming Service
 *    accessible from the ORB is accessed and a lookup made directly with the
 *    command-line argument.
 *
 *    USAGE: java AccountClient
 *              [corbaloc:...|corbaname:...|IOR:...|<simple name>]
 *              [deposit <amount> | withdrawal <amount> | balance]
 *              [other Java IDL arguments]
 * 
 * Example 4-8, Java Enterprise in a Nutshell, 2nd ed.
 * Author: Jim Farley
 */

public class AccountClient {
  public static void main(String args[]) {
    // Initialize the ORB
    ORB orb = ORB.init(args, null);
    org.omg.CORBA.Object ref = null;
    // The object name passed in on the command line
    String name = args[0];
    Account acct = null;
    // See if a stringified object reference/URL was provided
    if (name.startsWith("corbaname") || name.startsWith("corbaloc") ||
        name.startsWith("IOR")) {
      System.out.println("Attempting to lookup " + args[0]);
      ref = orb.string_to_object(args[0]);
      acct = AccountHelper.narrow(ref);
    }
    // Otherwise, do a traditional Naming Service lookup using the
    // services being referenced by our local ORB
    else {
      try {
        ref = orb.resolve_initial_references("NameService");
      }
      catch (InvalidName invN) {
        System.out.println("Couldn't locate a Naming Service");
        System.exit(1);
      }
      NamingContext nameContext = NamingContextHelper.narrow(ref);
      NameComponent comp = new NameComponent(args[0], "");
      NameComponent path[] = {comp};
      try {
        ref = nameContext.resolve(path);
        System.out.println("ref = " + ref);
        acct = AccountHelper.narrow(ref);
      }
      catch (Exception e) {
        System.out.println("Error resolving name against Naming Service");
        e.printStackTrace();
      }
    }

    if (acct != null) {
      // We managed to get a reference to the named account, now check the
      // requested transaction from the command-line
      String action = args[1];
      float amt = 0.0f;
      if (action.equals("deposit") || action.equals("withdrawal")) {
        amt = Float.parseFloat(args[2]);
      }
      System.out.println("Got account, performing transaction...");
      try {
        // Did user ask to do a deposit?
        if (action.equals("deposit")) {
          acct.deposit(amt);
          System.out.println("Deposited " + amt + " to account.");
          System.out.println("New balance = " + acct.getBalance());
        }
        // Did user ask to do a withdrawal?
        else if (action.equals("withdrawal")) {
          acct.withdraw(amt);
          System.out.println("Withdrew " + amt + " from account.");
          System.out.println("New balance = " + acct.getBalance());
        }
        // Assume a balance inquiry if neither deposit nor withdrawal was given
        else {
          System.out.println("Current account balance = " + acct.getBalance());
        }
      }
      catch (InsufficientFundsException ife) {
        System.out.println("Insufficient funds for transaction.");
        System.out.println("Current account balance = " + acct.getBalance());
      }
      catch (Exception e) {
        System.out.println("Error occurred while performing transaction:");
        e.printStackTrace();
      }
    }
    else {
      System.out.println("Null account returned.");
      System.exit(1);
    }
  }
}
