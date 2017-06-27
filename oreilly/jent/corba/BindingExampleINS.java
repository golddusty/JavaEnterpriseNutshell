package oreilly.jent.corba;

import org.omg.CosNaming.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.POA;

public class BindingExampleINS {
  static public void main(String argv[]) {
    try {
      // Initialize our ORB reference
      ORB myORB = ORB.init(argv, null);
      // Get the root Portable Object Adapter (POA)
      POA rootPOA = (POA) myORB.resolve_initial_references("RootPOA");
      // Activate the POA manager
      rootPOA.the_POAManager().activate();

      // Get the root naming context, using the INS interface
      org.omg.CORBA.Object ref =
        myORB.resolve_initial_references("NameService");
      NamingContextExt rootNC = NamingContextExtHelper.narrow(ref);

      String currURL = null;

      // Create a new context
      NamingContext ctx = rootNC.new_context();
      // Construct the name URL for the new context, and convert it to a
      // Name (a NameComponent array) using the INS utility method to_name()
      currURL = "bankBranches";
      NameComponent path[] = rootNC.to_name(currURL);
      // Bind the context to the name
      rootNC.rebind_context(path, ctx);
      System.out.println("Bound new context to " + currURL);

      // Create another context, bind it to the path "bankBranches, Cambridge" 
      NamingContext cambridgeCtx = rootNC.new_context();
      currURL = "bankBranches/Cambridge";
      path = rootNC.to_name(currURL);
      rootNC.rebind_context(path, cambridgeCtx);
      System.out.println("Bound new context to " + currURL);

      // Create another context, bind it to the path "bankBranches, Boston" 
      NamingContext bostonCtx = rootNC.new_context();
      currURL = "bankBranches/Boston";
      path = rootNC.to_name(currURL);
      rootNC.rebind_context(path, bostonCtx);
      System.out.println("Bound new context to " + currURL);

      // Now we can bind Accounts to a name within any of the new sub-contexts
      // Create a few Account server objects, and get usable client references
      // from the POA
      AccountImplPOA johnAcct = new AccountImplPOA("JohnSmith");
      Account johnRef =
        AccountHelper.narrow(rootPOA.servant_to_reference(johnAcct));
      AccountImplPOA maryAcct = new AccountImplPOA("MarkJones");
      Account maryRef =
        AccountHelper.narrow(rootPOA.servant_to_reference(maryAcct));
      // Bind each Account to a name in the appropriate branch path.  Assume
      // that John has his account out of the Cambridge branch, Mary has hers
      // out of the Boston branch.
      currURL = "bankBranches/Cambridge/Smith,J";
      NameComponent johnPath[] = rootNC.to_name(currURL);
      rootNC.rebind(johnPath, johnRef);
      System.out.println("Bound new account to " + currURL);
      currURL = "bankBranches/Boston/Jones,M";
      NameComponent maryPath[] = rootNC.to_name(currURL);
      rootNC.rebind(maryPath, maryRef);
      System.out.println("Bound new account to " + currURL);

      // Wait for client requests
      myORB.run();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
}
