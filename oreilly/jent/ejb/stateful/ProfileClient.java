package oreilly.jent.ejb.stateful;

import javax.ejb.*;
import javax.naming.*;
import java.rmi.*;
import java.util.Properties;
import oreilly.jent.ejb.*;

public class ProfileClient {
  public static void main(String[] args) {
    String name = args[0];

    try {
      // Get a JNDI context for our EJB server
      Context context = new InitialContext();

      // Create a profile server
      ProfileHome pHome = (ProfileHome) context.lookup("ejb/ProfileHome");

      // Ask the server for the person's profile
      System.out.println("Creating profile for " + name);
      Profile profile = pHome.create(name);

      // Get/set some entries in the profile
      System.out.println("Setting profile entries for " + name);
      profile.setEntry("favoriteColor", "blue");
      profile.setEntry("language", "German");

      System.out.println("Getting profile entries for " + name);
      System.out.println(
        "\tFavorite color: " + profile.getEntry("favoriteColor"));
      System.out.println("\tLanguage: " + profile.getEntry("language"));
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
