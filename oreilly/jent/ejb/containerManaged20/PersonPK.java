package oreilly.jent.ejb.containerManaged20;

/*
 * This example is from the book "Java Enterprise in a Nutshell", 2nd ed.
 * Copyright (c) 1999,2001 by O'Reilly & Associates.  
 * You may distribute this source code for non-commercial purposes only.
 * You may study, modify, and use this example for any purpose, as long as
 * this notice is retained.  Note that this example is provided "as is",
 * WITHOUT WARRANTY of any kind either expressed or implied.
 */

public class PersonPK implements java.io.Serializable {
  public String firstName;
  public String lastName;

  public PersonPK() {
    firstName = null;
    lastName = null;
  }
  public PersonPK(String fname, String lname) {
    firstName = fname;
    lastName = lname;
  }

  public boolean equals(Object other) {
    if (other == this) return true;

    if (!(other instanceof PersonPK))
      return false;

    PersonPK otherPPK = (PersonPK)other;

    return ((hashCode() == otherPPK.hashCode()) &&
	    (firstName.equals(otherPPK.firstName)) &&
	    (lastName.equals(otherPPK.lastName)));
  }
  
  public int hashCode() {
    return firstName.hashCode() ^ lastName.hashCode();
  }
}
