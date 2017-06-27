package oreilly.jent.rmi;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;

public class AccountManagerTest {
  public static void main(String argv[]) {
    try {
      String host = argv[0];
      String name = argv[1];
      Registry rmtReg = LocateRegistry.getRegistry(host);
      AccountManager mgr = (AccountManager)rmtReg.lookup(name);
      Account acct = mgr.getAccount("Fred");
      System.out.println("Got account, name = " + acct.getName());
      acct.deposit(1000);
      System.out.println("New account balance = " + acct.getBalance());
    }
    catch (Exception e) { e.printStackTrace(); }
  }
}
