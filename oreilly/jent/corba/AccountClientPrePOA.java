package oreilly.jent.corba;

import org.omg.CORBA.*;
import org.omg.CORBA.ORBPackage.*;
import org.omg.CosNaming.*;
import java.util.*;

public class AccountClientPrePOA {
  public static void main(String args[]) {
    ORB orb = ORB.init(args, null);
    org.omg.CORBA.Object ref = null;
    try {
      ref = orb.resolve_initial_references("NameService");
    }
    catch (InvalidName invN) {
      System.out.println("No primary NameService available.");
      System.exit(1);
    }
    NamingContext nameContext = NamingContextHelper.narrow(ref);
    NameComponent comp = new NameComponent(args[0], "");
    NameComponent path[] = {comp};
    try {
      org.omg.CORBA.Object ref2 = nameContext.resolve(path);
      Account acct = AccountHelper.narrow(ref2);
      System.out.println("Got account, calling methods...");
      acct.deposit(1000);
      System.out.println("New balance = " + acct.getBalance());
    }
    catch (Exception e) {
      System.out.println("Failed to access account.");
      e.printStackTrace();
    }
  }
}
