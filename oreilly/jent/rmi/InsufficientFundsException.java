package oreilly.jent.rmi;

/**
 * An exception indicating that anthe account funds did not support
 * a particular Account operation.
 */

public class InsufficientFundsException extends Exception {
  public InsufficientFundsException(String msg) {
    super(msg);
  }

  public InsufficientFundsException() {
    super();
  }
}
