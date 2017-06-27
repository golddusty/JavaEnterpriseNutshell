package oreilly.jent.rmi;

import javax.naming.*;
import javax.rmi.PortableRemoteObject;
import java.rmi.RMISecurityManager;
import java.util.Hashtable;

// Simple test client that accesses a specific remote account, makes a deposit
// and reports the current balance.

public class IIOPAccountClient {
  public static void main(String argv[]) {
    try {
      // Set the RMI security manager, in case we need to load remote classes
      System.setSecurityManager(new RMISecurityManager());
      // Lookup account object
      Hashtable props = new Hashtable();
      props.put("java.naming.factory.initial",
                "com.sun.jndi.cosnaming.CNCtxFactory");
      props.put("java.naming.provider.url", "iiop://localhost:900");
      Context ctx = new InitialContext(props);
      Account jimAcct =
        (Account)PortableRemoteObject.narrow(ctx.lookup("JimF"),
                                             Account.class);
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
