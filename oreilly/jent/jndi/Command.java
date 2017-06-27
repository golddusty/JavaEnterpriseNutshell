package oreilly.jent.jndi;

/**
 * Command: Defines the behavior of a NamingShell command.
 * 
 * The method execute(Context, Vector) is how the NamingShell executes the
 * command. The Context parameter represents the Context the command operates
 * upon, and the Vector packages up any data necessary. Any data that the command 
 * returns to the NamingShell is required to use a NamingShell accessor method.  
 * 
 * Any unrecoverable error that a Command encounters should cause the Command
 * to throw a CommandException, and the shell will handle it gracefully.
 *  
 * Example 7-3, Java Enterprise in a Nutshell, 2nd ed.
 * Author: Kris Magnusson
 */

import java.util.Vector;
import javax.naming.Context;

public interface Command {
  public void execute(Context c, Vector v)
    throws CommandException;
        
  public void help();  
}
