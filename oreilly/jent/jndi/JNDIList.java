package oreilly.jent.jndi;

import javax.naming.*;
import java.util.Properties;

// A test utility that prints the contents of a given context within
// a specified JNDI server.  Useful for checking state of a JNDI server,
// without needing to use the NamingShell.
//
// Usage: java oreilly.jent.jndi.JNDIList <factoryClassname> <providerURL> 
//                                        <context>
//   e.g., > java oreilly.jent.jndi.JNDIList 
//             weblogic.jndi.WLInitialContextFactory t3://localhost:7001 
//             java:comp/env/jdbc
//
// Author: Jim Farley

public class JNDIList {
  public static void main(String[] args) {
    try {
      // Get a JNDI context from our J2EE server
      Properties props = new Properties();
      props.put(
        Context.INITIAL_CONTEXT_FACTORY,
        "weblogic.jndi.WLInitialContextFactory");
      props.put(Context.PROVIDER_URL, "t3://localhost:7011");

      Context context = new InitialContext(props);

      NamingEnumeration enum = context.list("");
      System.out.println("root context");
      while (enum.hasMore()) {
        NameClassPair p = (NameClassPair) enum.next();
        System.out.println(
          "name = " + p.getName() + ", class = " + p.getClassName());
      }
      System.out.println("Listing of context " + args[0]);
      enum = context.list(args[0]);
      while (enum.hasMore()) {
        NameClassPair p = (NameClassPair) enum.next();
        System.out.println(
          "name = " + p.getName() + ", class = " + p.getClassName());
      }
    }
    catch (Exception e) {
      System.out.println("Error while creating/using person");
      e.printStackTrace();
    }
  }
}
