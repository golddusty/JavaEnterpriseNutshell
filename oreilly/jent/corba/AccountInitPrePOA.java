package oreilly.jent.corba;

/**
 * AccountInitPrePOA: Initialize a remote object and generate an IOR.
 * Example 4-5, Java Enterprise in a Nutshell, 2nd ed.
 * Author: Jim Farley
 */

import org.omg.CORBA.*;
import org.omg.CosNaming.*;

public class AccountInitPrePOA {
  public static void main(String[] args) {
    try {
      // Initialize an ORB reference
      ORB myORB = ORB.init(args, null); 

      // Create an instance of an Account server implementation
      Account acctRef = new AccountImplPrePOA(args[0]);

      // Register the local object with the ORB
      myORB.connect(acctRef);

      // Get the root name context
      org.omg.CORBA.Object objRef = 
        myORB.resolve_initial_references("NameService");
      NamingContext nc = NamingContextHelper.narrow(objRef);

      // Register the local object with the Name Service
      NameComponent ncomp = new NameComponent(args[0], "");
      NameComponent[] name = {ncomp};
      nc.rebind(name, acctRef);

      System.out.println("Registered account under name " + args[0]);
      System.out.println("New account created and registered.  URLs are: ");
      System.out.println("\nIOR");
      System.out.println("\t" + myORB.object_to_string(acctRef));

      // Go into a wait state, waiting for clients to connect
      java.lang.Object dummy = new String("I wait...");
      synchronized (dummy) {
        dummy.wait();
      }
    }
    catch (Exception e) {
      System.out.println("An error occurred while initializing server object:");
      e.printStackTrace();
    }
  }
}
    
