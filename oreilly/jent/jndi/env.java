package oreilly.jent.jndi;

/**
 * env: Command to print the environment parameters of the current context.
 * 
 * Java Enterprise in a Nutshell, 2nd ed.
 * Author: Kris Magnusson
 */


import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.NamingException;

class env implements Command {
    
    public void execute(Context c, Vector v) 
        throws CommandException {
            
        if (c == null)
            throw new CommandException(new Exception(),
                "No current context");
        try {
            Hashtable env = NamingShell.getInitialContext().getEnvironment();
            Enumeration enum = env.keys();
            String key;
            while (enum.hasMoreElements()) {
                key = (String)enum.nextElement();
                System.out.println(key + "\t" + env.get(key));
            }

            Hashtable sysProps = System.getProperties();
            Enumeration sysEnum = sysProps.keys();
            while (sysEnum.hasMoreElements()) {
                key = (String)sysEnum.nextElement();
                System.out.println(key + "\t" + sysProps.get(key));
            }
        }
        catch (NamingException ne) {
            throw new CommandException(ne, "Couldn't get the environment of " 
                + NamingShell.getCurrentContext());
        }
    }
    
    public void help() { System.out.println("Usage: env");
    }
}
