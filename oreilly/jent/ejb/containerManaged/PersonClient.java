package oreilly.jent.ejb.containerManaged;

/*
 * This example is from the book "Java Enterprise in a Nutshell", 2nd ed.
 * Copyright (c) 1999, 2001 by O'Reilly & Associates.  
 * You may distribute this source code for non-commercial purposes only.
 * You may study, modify, and use this example for any purpose, as long as
 * this notice is retained.  Note that this example is provided "as is",
 * WITHOUT WARRANTY of any kind either expressed or implied.
 */


import javax.ejb.*;
import javax.naming.*;
import java.rmi.*;
import java.util.Properties;
import oreilly.jent.ejb.*;

public class PersonClient {
  public static void main(String[] args) {
    String fname = args[0];
    String lname = args[1];

    try {
      // Get a JNDI context from our EJB server
      Properties props = new Properties();
      props.put(Context.INITIAL_CONTEXT_FACTORY,
		"weblogic.jndi.WLInitialContextFactory");
      props.put(Context.PROVIDER_URL, "t3://localhost:7011");

      Context context = new InitialContext(props);

      // Get the home interface for Person beans
      PersonHome pHome = (PersonHome)context.lookup("ejb/CMP-PersonHome");

      // First, see if this person already exists
      System.out.println("Looking for " + fname + " " + lname);
      Person person = null;
      try {
	person = pHome.findByPrimaryKey(new PersonPK(fname, lname));
      }
      catch (Exception e) {
	System.out.println("Exception while looking up person.");
      }

      if (person == null) {
	System.out.println("Creating person " + fname + " " + lname);
	person = pHome.create(fname, lname);
      }

      // Get the person's profile
      Profile profile = person.getProfile();
      // Set some entries in the profile
      System.out.println("Setting profile entries for " + fname + " " + lname);
      profile.setEntry("favoriteColor", "blue");
      profile.setEntry("language", "German");

      // Get the entries from the profile
      System.out.println("Getting profile entries for " + fname + " " + lname);
      System.out.println("\tFavorite color: " +
			 profile.getEntry("favoriteColor"));
      System.out.println("\tLanguage: " + profile.getEntry("language"));
    }
    catch (Exception e) {
      System.out.println("Error while creating/using person");
      e.printStackTrace();
    }
  }
}
