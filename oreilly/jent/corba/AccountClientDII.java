package oreilly.jent.corba;

/**
 * AccountClientDII: A client of our Account interface that uses DII to
 * interact with the CORBA objects.
 * 
 * Example 4-9, Java Enterprise in a Nutshell, 2nd ed.
 * Author: Jim Farley
 */

import org.omg.CORBA.*;
import org.omg.CosNaming.*;

public class AccountClientDII {
  public static void main(String args[]) {
    ORB myORB = ORB.init(args, null);
    try {
      // The object name passed in on the command line
      String name = args[0];
      org.omg.CORBA.Object acctRef = null;
      if (name.startsWith("corbaname") || name.startsWith("corbaloc") ||
          name.startsWith("IOR")) {
        System.out.println("Attempting to lookup " + args[0]);
        acctRef = myORB.string_to_object(args[0]);
      }
      else {
        System.out.println("Invalid object URL provided: " + args[0]);
        System.exit(1);
      }

      // Make a dynamic call to the doThis method
      if (acctRef != null) {
        // We managed to get a reference to the named account, now check the
        // requested transaction from the command-line
        String action = args[1];
        float amt = 0.0f;
        if (action.equals("deposit")) {
          amt = Float.parseFloat(args[2]);
        }
        System.out.println("Got account, performing transaction...");
        try {
          // Did user ask to do a deposit?
          if (action.equals("deposit")) {
            // The following DII code is equivalent to this:
            //   acct.deposit(amt);

            // First build the argument list.  In this case, there's a single
            // float argument to the method.
            NVList argList = myORB.create_list(1);
            Any arg1 = myORB.create_any();
            // Set the Any to hold a float value, and set the value to the
            // amount to be deposited.
            arg1.insert_float(amt);
            NamedValue nvArg = 
              argList.add_value("amt", arg1, org.omg.CORBA.ARG_IN.value);
            // Java IDL does not implement the get_default_context() operation
            // on the ORB, so we just set the Context to null 
            Context ctx = null;
            // Create the request to call the deposit() method, using the
	    // _create_request() method on the ORB
            Request depositReq = 
              acctRef._create_request(ctx, "deposit", argList, null);

            // Invoke the method...
            depositReq.invoke();
            System.out.println("Deposited " + amt + " to account.");
          }
          else {
            // The following DII code is equivalent to this:
            //   acct.balance();

             // No argument list is needed here, since the getBalance() method
            // has no arguments.  But we do need a result value to hold the
            // returned balance
            Any result = myORB.create_any();
            // Set the any to hold a float value
            result.insert_float(0.0f);
            NamedValue resultVal =
              myORB.create_named_value("result", result, 
                                        org.omg.CORBA.ARG_OUT.value);
            // Java IDL does not implement the get_default_context() operation
            // on the ORB, so we just set the Context to null 
            Context ctx = null;
            // Create the request to call getBalance()
            Request balanceReq = 
              acctRef._create_request(ctx, "getBalance", null, resultVal);

            // Invoke the method...
            balanceReq.invoke();
            System.out.println("Current account balance: " +
                               result.extract_float());
          }
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
    catch (Exception e) {
      e.printStackTrace();
    }
  }
}
