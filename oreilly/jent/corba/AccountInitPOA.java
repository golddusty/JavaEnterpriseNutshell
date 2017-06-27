package oreilly.jent.corba;

/**
 * AccountInitPOA: Initialize a remote object, register it with a naming
 * service, and generate a stringified IOR.
 * Example 4-6, Java Enterprise in a Nutshell, 2nd ed.
 * Author: Jim Farley
 */

import org.omg.CORBA.*;
import org.omg.CosNaming.*;
import org.omg.PortableServer.POA;
import java.net.InetAddress;

public class AccountInitPOA {
  public static void shutdown(org.omg.CORBA.ORB orb) {
    System.out.println("Shutting down");
  }
  public static void main(String[] args) {
    try {
      // Initialize an ORB reference
      ORB myORB = ORB.init(args, null);

      // Create an instance of an Account server implementation
      AccountImplPOA acct = new AccountImplPOA(args[0]);

      // Get the root Portable Object Adapter (POA)
      POA rootPOA = (POA) myORB.resolve_initial_references("RootPOA");
      // Activate the POA manager
      rootPOA.the_POAManager().activate();
      // Activate our servant, so that direct requests (through a corbaloc
      // or IOR URL) will work immediately
      rootPOA.activate_object(acct);

      // Get an object reference from the implementation
      org.omg.CORBA.Object obj = rootPOA.servant_to_reference(acct);
      Account acctRef = (Account) AccountHelper.narrow(obj);

      // Get the root name context (use the INS interface, so that we can use
      // the simpler name construction process)
      org.omg.CORBA.Object objRef =
        myORB.resolve_initial_references("NameService");
      NamingContextExt nc = NamingContextExtHelper.narrow(objRef);

      // Register the local object with the Name Service
      // Use the Interoperable Naming Service interface to simplify matters,
      // and to support URL-formatted names (e.g. "JohnS",
      // "corbaname://orbhost.com#JohnS", etc.)
      NameComponent[] name = nc.to_name(args[0]);
      nc.rebind(name, acctRef);

      System.out.println("Registered account under name " + args[0]);
      System.out.println("New account created and registered.  URLs are: ");
      // Get the IOR using object_to_string()
      System.out.println("\nIOR");
      System.out.println("\t" + myORB.object_to_string(acctRef));
      // Construct the corbaloc URL using local ORB parameters ???
      System.out.println("\ncorbaloc");
      System.out.println(
        "\tcorbaloc:iiop:" + InetAddress.getLocalHost().getHostName());
      System.out.println("\ncorbaname");
      // If the user gave us a corbaname URL directly on the command-line, just
      // emit that
      if (args[0].startsWith("corbaname")) {
        System.out.println("\t" + args[0]);
      }
      // Otherwise, we need to construct the corbaname URL from our ORB and
      // Naming Service parameters.
      else {
        System.out.println(
          "\tcorbaname:iiop:"
            + InetAddress.getLocalHost().getHostName()
            + "#"
            + args[0]);
      }

      // Go into a wait state, waiting for clients to connect
      myORB.run();
    }
    catch (Exception e) {
      System.out.println("An error occurred while initializing server object:");
      e.printStackTrace();
    }
  }
}
