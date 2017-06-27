package oreilly.jent.rmi;

import java.rmi.Naming;

public class AccountManagerReg {
  public static void main(String argv[]) {
    try {
      AccountManager mgr = new AccountManagerImpl();
      Account one = new AccountImpl("Fred");
      mgr.newAccount(one);
      Account two = new AccountImpl("Wilma");
      mgr.newAccount(two);
      Naming.rebind("acctManager", mgr);
    }
    catch (Exception e) {}
  }
}
