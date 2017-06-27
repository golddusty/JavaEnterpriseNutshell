package oreilly.jent.jndi;

/**
 * help: The help command displays help for NamingShell. If the user types
 * 
 *   help [command]
 * 
 * help invokes that command's help method.
 * 
 * Java Enterprise in a Nutshell, 2nd ed.
 * Author: Kris Magnusson
 */

import java.lang.reflect.Method;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.naming.Context;

public class help implements Command {
    public void execute(Context currentContext, Vector args) 
        throws CommandException {
        
        if (args.isEmpty())
            help();
        else {
            String commandName = (String)args.firstElement();
            Command command = NamingShell.loadCommand(commandName);
            if (command != null)
                command.help();
            else 
                throw new CommandException(new Exception(), 
                    "No help available for this command");
        }
    }
    
    public void help() { System.out.print("Usage: help [command]"); }
}
