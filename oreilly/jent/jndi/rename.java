package oreilly.jent.jndi;

/**
 * rename: Command to rename a given context.
 * 
 * Java Enterprise in a Nutshell, 2nd ed.
 * Author: Kris Magnusson
 */

import java.util.Vector;

import javax.naming.Context;
import javax.naming.NamingException;

class rename implements Command {

    public void execute(Context c, Vector v) 
        throws CommandException {
            
        // check to see if we have all the args we need to rename
        if (v.isEmpty())
            throw new CommandException(new Exception(), "No names specified");
        else if (v.elementAt(1) == null)
            throw new CommandException(new Exception(), 
                "Only one name specified");

        String oldName = (String)v.firstElement();
        String newName = (String)v.elementAt(1);
        
        try {
            c.rename(oldName, newName);
            System.out.println("Renamed " + oldName + " to " + newName);
        }
        catch (NamingException ne) {
            throw new CommandException(ne, "Couldn't rename " 
                + oldName + " to " + newName);
        }
    }

    public void help() { System.out.println("Usage: rename [old name] [new name]"); }
}
