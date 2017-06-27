package oreilly.jent.ejb.messageDriven;

/**
 * ProfileProxyBean: A message-driven EJB proxy for our Profile EJB.
 * 
 * Example 8-15, Java Enterprise in a Nutshell, 2nd ed.
 * Author: Jim Farley
 */

import javax.ejb.*;
import javax.rmi.*;
import java.rmi.*;
import javax.jms.*;
import javax.naming.*;
import java.util.*;
import oreilly.jent.ejb.containerManaged20.*;
import oreilly.jent.ejb.*;

public class ProfileProxyBean implements MessageDrivenBean, MessageListener {
  private MessageDrivenContext mContext;
  private ProfileHome mProfileHome;

  // Required create method.  Here, we lookup the home interface for the
  // Profile EJB we are acting as a proxy for.  We use the Profile's local
  // home interface, assuming that we're running in the same container.
  public void ejbCreate () throws CreateException {
    System.out.println("Create called on ProfileProxyBean.");
    try {
      Context ctx = new InitialContext();
      mProfileHome = 
        (ProfileHome)PortableRemoteObject.narrow(ctx.lookup("ejb/CMP20-ProfileHome"), ProfileHome.class);
    }
    catch (NamingException ne) {
      throw new CreateException("Failed to locate Profile home interface: " +
                                ne.getMessage());
    }
  }

  // Receive context from the container.
  public void setMessageDrivenContext(MessageDrivenContext ctx) {
    mContext = ctx;
  }

  // Required remove method - no action needed in our case.
  public void ejbRemove() {
    System.out.println("Remove called on ProfileProxyBean.");
  }

  // Implementation of message listener.  Here, we check the type of the
  // incoming message.  If it's other than a MapMessage, we ignore it.  If it
  // is a MapMessage, we interpret it as a set of new entry values for a
  // profile named by the "OWNER" field in the map.  We lookup the
  // corresponding Profile EJB, and set the entries according to the contents
  // of the MapMessage.
  public void onMessage(Message msg) {
    if (msg instanceof MapMessage) {
      MapMessage mMsg = (MapMessage)msg;
      try {
        String name = mMsg.getString("OWNER");
        if (name != null) {
          Profile prof = mProfileHome.findByPrimaryKey(name);
          Enumeration eNames = mMsg.getMapNames();
          while (eNames.hasMoreElements()) {
            String eName = (String)eNames.nextElement();
            String eVal = mMsg.getString(eName);
            if (!eName.equals("OWNER")) {
              prof.setEntry(eName, eVal);
            }
          }
        }
      }
      catch (JMSException je) {
        System.out.println("JMS error processing message to ProfileProxy: " +
                           je.getMessage());
      }
      catch (RemoteException re) {
        System.out.println("Remote exception while accessing profile: " +
                           re.getMessage());
      } 
      catch (FinderException fe) {
        System.out.println("Failed to find Profile named in message: " +
                           fe.getMessage());
      }
    }
    else {
      System.out.println("Non-MapMessage received by ProfileProxy, type = " +
                         msg.getClass().getName());
    }
  }
}
  
