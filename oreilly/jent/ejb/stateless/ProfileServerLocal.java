package oreilly.jent.ejb.stateless;

/**
 * ProfileServerLocal: A local interface for the ProfileServer EJB.
 * 
 * Example 8-3, Java Enterprise in a Nutshell, 2nd ed.
 * Author: Jim Farley
 */

import javax.ejb.*;
import oreilly.jent.ejb.*;

public interface ProfileServerLocal extends EJBLocalObject {
  public ProfileBean getProfile(String acctName)
    throws NoSuchPersonException;
}
