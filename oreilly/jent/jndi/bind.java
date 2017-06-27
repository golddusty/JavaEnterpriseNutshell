package oreilly.jent.jndi;

/**
 * bind: Command to create a binding in a JNDI context.
 * 
 * Java Enterprise in a Nutshell, 2nd ed.
 * Author: Kris Magnusson
 */

import java.util.Vector;
import javax.naming.*;

class bind implements Command {

    public void execute(Context c, Vector v) 
        throws CommandException {

        // check to see if we have all the args we need to bind
        if (v.isEmpty())
            throw new CommandException(new Exception(),
                "No names specified");
        else if (v.elementAt(1) == null)
            throw new CommandException(new Exception(),
                "Only one name specified");
        
        String name = (String)v.firstElement();
        String objName = (String)v.elementAt(1);
        
        try {
            Object obj = c.lookup(objName);
            c.bind(name, obj);
            System.out.println("Bound " + name + " to " + objName);
        }
        catch (NameAlreadyBoundException nabe) {
            throw new CommandException(nabe, 
                "The name " + name + " is already bound");
        }
        catch (OperationNotSupportedException onse) {
            throw new CommandException(onse, 
                "Binding " + objName + " to " + name + " is not supported");
        }        
        catch (NamingException ne) {
            throw new CommandException(ne,
                "Couldn't bind " + name + " to "  + objName);
        }
    }

    public void help() { System.out.println("Usage: bind [name] [obj]"); }
}
