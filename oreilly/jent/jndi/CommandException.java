package oreilly.jent.jndi;

/**
 * CommandException: An exception thrown by a Command, wrapping the root
 *   NamingException.
 * 
 * Example 7-4, Java Enterprise in a Nutshell, 2nd ed.
 * Authors: Kris Magnusson.
 */

public class CommandException extends Exception {
    
    Exception e; // root exception
    
    CommandException(Exception e, String message) {
        super(message);
        this.e = e;
    }
    
    public Exception getRootException() {
        return e;
    }
}
