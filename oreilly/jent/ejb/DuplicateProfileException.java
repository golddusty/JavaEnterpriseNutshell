package oreilly.jent.ejb;

// This exception is thrown if an attempt is made to create another profile
// for the same person.

public class DuplicateProfileException extends Exception {
  public DuplicateProfileException() {}
  public DuplicateProfileException(String message) {super(message);}
}
