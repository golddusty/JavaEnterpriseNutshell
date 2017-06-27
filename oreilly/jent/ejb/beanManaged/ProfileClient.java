package oreilly.jent.ejb.beanManaged;

import javax.ejb.*;
import javax.naming.*;
import java.rmi.*;
import java.util.Properties;
import oreilly.jent.ejb.*;

public class ProfileClient {
  public static void main(String[] args) {
    String name = args[0];

    try {
      // Get JNDI context from J2EE server
      Context context = new InitialContext();

      // Create a profile server
      ProfileHome pHome = (ProfileHome) context.lookup("ejb/BMP-ProfileHome");

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
        System.out.println("Setting profile entries for " + name);
        profile.setEntry("favoriteColor", "blue");
        profile.setEntry("language", "German");
      }

      // Get the entries from the profile
      System.out.println("Getting profile entries for " + name);
      System.out.println(
        "\tFavorite color: " + profile.getEntry("favoriteColor"));
      System.out.println("\tLanguage: " + profile.getEntry("language"));

      // Change the entries
      profile.setEntry("favoriteColor", "red");
      profile.setEntry("language", "Italian");

      // Get the updated entries from the profile 
      System.out.println("Getting profile entries for " + name);
      System.out.println(
        "\tFavorite color: " + profile.getEntry("favoriteColor"));
      System.out.println("\tLanguage: " + profile.getEntry("language"));
    }
    catch (Exception e) {
      System.out.println("Error while creating/using profile.");
      e.printStackTrace();
    }
  }
}
