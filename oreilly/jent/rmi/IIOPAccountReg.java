package oreilly.jent.rmi;

import javax.naming.*;
import java.rmi.*;
import java.util.Hashtable;

// Utility class that registers an account with the local
// RMI registry for remote access

public class IIOPAccountReg {
  public static void main(String argv[]) {
    try {
      // Make an Account with a given name
      IIOPAccountImpl acct = new IIOPAccountImpl("JimF");
      // Get a reference to CORBA naming service using JNDI
      Hashtable props = new Hashtable();
      props.put("java.naming.factory.initial",
                "com.sun.jndi.cosnaming.CNCtxFactory");
      props.put("java.naming.provider.url", "iiop://localhost:900");
      Context ctx = new InitialContext(props);
      // Register our Account with the CORBA naming service
      ctx.rebind("JimF", acct);
      System.out.println("Registered account.");
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
}
