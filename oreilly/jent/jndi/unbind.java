package oreilly.jent.jndi;

/**
 * unbind: Command to remove a binding from the JNDI namespace.
 * 
 * Java Enterprise in a Nutshell, 2nd ed.
 * Author: Kris Magnusson
 */

import java.util.Vector;

import javax.naming.Context;
import javax.naming.NamingException;

class unbind implements Command {
    
    public void execute(Context c, Vector v) 
        throws CommandException {
            
        // check to see if we have all the args we need to rebind
        if (v.isEmpty())
            throw new CommandException(new Exception(), "No names specified");
                
        String name = (String)v.firstElement();

        try {
            c.unbind(name);
            System.out.println("Unbound the object " + name);
        }
        catch (NamingException ne) {
            throw new CommandException (ne, "Couldn't unbind " + name);
        }
    }

    public void help() { System.out.println("Usage: unbind [name]"); }
}
