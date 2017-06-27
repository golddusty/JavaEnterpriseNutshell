package oreilly.jent.jndi;

/**
 * exit: Command to exit the NamingShell.
 * 
 * Java Enterprise in a Nutshell, 2nd ed.
 * Author: Kris Magnusson
 */

import java.util.Vector;

import javax.naming.Context;

public class exit implements Command {
    
    String COMMAND_NAME = "exit";
    
    public void execute(Context currentContext, Vector args) 
        throws CommandException {
        System.out.println("Exiting");
        System.exit(0);
    }
    
    public void help() {
        System.out.println("Usage: exit");
    }
}
