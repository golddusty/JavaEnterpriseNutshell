package oreilly.jent.jndi;

/**
 * initctx: Command to initialize the JNDI context.
 * 
 * Example 7-5, Java Enterprise in a Nutshell, 2nd ed.
 * Authors: Kris Magnusson, Jim Farley.
 */

import java.io.*;
import java.util.*;
import javax.naming.*;

public class initctx implements Command {
    
  public void execute(Context c, Vector v) {
    String jndiPropsFilename = null;

    // Check for a properties filename
    if (!v.isEmpty())
      jndiPropsFilename = (String)v.firstElement();

    System.out.println("file = " + jndiPropsFilename);
            
    try {
      // If no properties file is specified, let JNDI get its properties from
      // the default jndi.properties file on the CLASSPATH.  Otherwise, use the
      // specified properties file.
      if (jndiPropsFilename != null) {
        Properties props = new Properties();
        File jndiProps = new File(jndiPropsFilename);
        props.load(new FileInputStream(jndiProps));
           
        NamingShell.setInitialContext(new InitialContext(props));
      }
      else {
        NamingShell.setInitialContext(new InitialContext());
      }
      NamingShell.setInitialName("/");
      NamingShell.setCurrentContext(NamingShell.getInitialContext());
      NamingShell.setCurrentName(NamingShell.getInitialName());
      System.out.print("Created initial context using ");
      if (jndiPropsFilename != null) {
        System.out.println(jndiPropsFilename);
      }
      else {
        System.out.println("jndi.properties.");
      }
    }
    catch (NamingException ne) {
      System.out.println("Couldn't create the initial context");
    }
    catch (FileNotFoundException fnfe) {
      System.out.print("Couldn't find properties file: ");
      System.out.println(jndiPropsFilename);
    }
    catch (IOException ioe) {
      System.out.print("Problem loading the properties file: ");
      System.out.println(jndiPropsFilename);
    }
    catch (Exception e) {
      System.out.println("There was a problem starting the shell");
    }
  }
    
  public void help() { System.out.println("Usage: initctx [filename]"); }
}
