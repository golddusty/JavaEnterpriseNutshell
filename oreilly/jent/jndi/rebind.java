package oreilly.jent.jndi;

/**
 * rebind: Command to rebind a given name in the JNDI context.
 * 
 * Java Enterprise in a Nutshell, 2nd ed.
 * Author: Kris Magnusson
 */

import java.util.Vector;

import javax.naming.Context;
import javax.naming.NamingException;

class rebind implements Command {
    
    public void execute(Context c, Vector v) 
        throws CommandException {
            
        // check to see if we have all the args we need to rebind
        if (v.isEmpty())
            throw new CommandException(new Exception(), "No names specified");
        else if (v.elementAt(1) == null)
            throw new CommandException(new Exception(), 
                "Only one name specified");
                
        String name = (String)v.firstElement();
        String objName = (String)v.elementAt(1);

        try {
            Object target = c.lookup(objName);
            c.rebind(name, target);
            System.out.println("Rebound the object " 
                + objName + " to the name " + name);
        }
        catch (NamingException ne) {
            throw new CommandException(ne, 
                "Couldn't rebind " + objName + " to the name " + name);
        }
    }

    public void help() { System.out.println("Usage: rebind [name] [obj]"); }
}
