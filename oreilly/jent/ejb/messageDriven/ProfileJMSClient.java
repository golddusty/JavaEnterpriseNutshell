package oreilly.jent.ejb.messageDriven;

import javax.jms.*;
import javax.naming.*;
import java.util.*;

public class ProfileJMSClient {
  public static void main(String args[]) {
    String name = null;
    if (args.length > 0) {
      name = args[0];
    }

    // If there are two more arguments, then continue
    if (args.length > 2) {
      try {
        // Get a JNDI context from our EJB server
        Properties props = new Properties();
        props.put(
          Context.INITIAL_CONTEXT_FACTORY,
          "weblogic.jndi.WLInitialContextFactory");
        props.put(Context.PROVIDER_URL, "t3://localhost:7011");

        Context context = new InitialContext(props);

        // Lookup the JMS queue connection factory
        QueueConnectionFactory qFactory =
          (QueueConnectionFactory) context.lookup("jms/jent-EJB-connFactory");
        QueueConnection qConn = qFactory.createQueueConnection();
        qConn.start();

        // Lookup the JMS message queue for the message-driven bean
        Queue profQueue = (Queue) context.lookup("jms/ProfileProxyQueue");

        // Create a session with the JMS server
        QueueSession qSession =
          qConn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);

        // Create a sender
        QueueSender sender = qSession.createSender(profQueue);

        // Create a MapMessage
        MapMessage msg = qSession.createMapMessage();

        // Set the name for the target profile
        msg.setString("OWNER", name);

        // Set the entry value given on the command-line
        msg.setString(args[1], args[2]);

        // Send the message
        sender.send(msg);
      }
      catch (Exception e) {
        System.out.println("Error while creating/using message-driven bean.");
        e.printStackTrace();
      }
    }
    else {
      System.out.println("Invalid command-line args.");
    }
  }
}
