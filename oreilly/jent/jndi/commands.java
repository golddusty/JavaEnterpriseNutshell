package oreilly.jent.jndi;

/**
 * commands: The commands command prints out the known commands for NamingShell
 * 
 * Java Enterprise in a Nutshell, 2nd ed.
 * Authors: Kris Magnusson
 */

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.naming.Context;

public class commands implements Command {
    
    String COMMAND_NAME = "commands";
    Hashtable commands = NamingShell.getCommands();
    
    public void execute(Context currentContext, Vector args) 
        throws CommandException {
        
        System.out.println("Known commands: ");
        Enumeration names = commands.elements();
        while (names.hasMoreElements())
            System.out.println(((Command)
                names.nextElement()).getClass().getName());
        System.out.println();
    }
    
    public void help() { System.out.print("Usage: commands"); }
}
