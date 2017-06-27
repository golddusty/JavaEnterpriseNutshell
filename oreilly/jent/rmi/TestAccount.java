package oreilly.jent.rmi;

import java.rmi.Naming;

public class TestAccount {
  public static void main(String argv[]) {
    try {
      AccountImpl acct = new AccountImpl("JimF");
      Naming.rebind("JimF", acct);
      System.out.println("Registered account.");
      System.out.println("Waiting...");
      acct.wait();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
}
