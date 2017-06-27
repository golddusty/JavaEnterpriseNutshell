package oreilly.jent.jndi;

/**
 * Lookup: Bind to a JNDI context and lookup a given object.
 * 
 * Example 7-1, Java Enterprise in a Nutshell, 2nd ed.
 * Authors: Kris Magnusson, Jim Farley.
 */

import java.util.Properties;
import javax.naming.*;

public class Lookup {
  public static void main(String[] args) {
    String name = "";
    if (args.length > 0)
      name = args[0];

    try {
      // Create a Properties object and set default properties
      Properties props = new Properties();

      // Optional command-line args to specify alternate factory and URL
      if (args.length > 1) {
        props.put(Context.INITIAL_CONTEXT_FACTORY, args[1]);
      }
      if (args.length > 2) {
        props.put(Context.PROVIDER_URL, args[2]);
      }

      // Create the initial context from the properties we just created
      Context initialContext = new InitialContext(props);

      // Look up the named object
      Object obj = initialContext.lookup(name);
      if (name.equals(""))
        System.out.println("Looked up the initial context");
      else {
        System.out.println(name + " is bound to: " + obj
                           + " of type " + obj.getClass().getName());
        if (obj instanceof java.io.File) {
          System.out.println("It's a file");
        }
      }
    }
    catch (NamingException nnfe) {
      System.out.println("Encountered a naming exception");
      nnfe.printStackTrace();
    }
  }
}
