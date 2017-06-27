package oreilly.jent.ejb.containerManaged20;

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
import java.util.*;
import oreilly.jent.ejb.*;

public class ProfileClient {
  public static void main(String[] args) {
    String name = null;
    if (args.length > 0) {
      name = args[0];
    }

    try {
      // Get a JNDI context from our EJB server
      Properties props = new Properties();
      props.put(Context.INITIAL_CONTEXT_FACTORY,
		"weblogic.jndi.WLInitialContextFactory");
      props.put(Context.PROVIDER_URL, "t3://localhost:7011");

      Context context = new InitialContext(props);

      // Create a profile server
      ProfileHome pHome =
	(ProfileHome)context.lookup("ejb/CMP20-ProfileHome");

      // If a name is specified, look for that person's profile
      if (name != null) {
	// Ask the server for the person's profile
	// First, see if this person already has a profile
	System.out.println("Looking for profile for " + name);
	Profile profile = null;
	try {
	  profile = pHome.findByPrimaryKey(name);
	}
	catch (Exception e) {
	  System.out.println("Exception while looking up profile.");
	}

	if (profile == null) {
	  System.out.println("Creating profile for " + name);
	  profile = pHome.create(name);
	  // Set some entries in the profile
	  if (args.length < 2 || !args[1].equals("empty")) {
	    System.out.println("Setting profile entries for " + name);
	    profile.setEntry("favoriteColor", "blue");
	    profile.setEntry("language", "German");
	  }
	  else {
	    System.out.println("Empty profile created for " + name);
	  }
	}

	// Get the entries from the profile
	System.out.println("Getting profile entries for " + name);
	System.out.println("\tFavorite color: " +
			   profile.getEntry("favoriteColor"));
	System.out.println("\tLanguage: " + profile.getEntry("language"));
      }
      // If no name given, get all emtpy profiles and print their names
      else {
	Collection eProfs = pHome.findEmptyProfiles();
	System.out.println("Empty profiles:");
	Iterator iter = eProfs.iterator();
	while (iter.hasNext()) {
	  Profile p = (Profile)iter.next();
	  System.out.println("\t" + p.getName());
	}
      }
    }
    catch (NoSuchPersonException nspe) {
      System.out.println("Invalid person: " + name);
    } 
    catch (Exception e) {
      System.out.println("Error while creating/using profile.");
      e.printStackTrace();
    }
  }
}
