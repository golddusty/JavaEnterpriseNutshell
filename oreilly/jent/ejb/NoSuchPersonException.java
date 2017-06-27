package oreilly.jent.ejb;

// Thrown if data is requested for a person that cannot be not found.

public class NoSuchPersonException extends Exception {
  public NoSuchPersonException() {}
  public NoSuchPersonException(String message) {super(message);}
}
