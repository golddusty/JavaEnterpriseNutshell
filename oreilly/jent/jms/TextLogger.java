package oreilly.jent.jms;

/**
 * TextLogger: A simple message listener that logs text messages.
 * 
 * Example 10-1, Java Enterprise in a Nutshell, 2nd ed.
 * Author: Jim Farley
 */

import javax.jms.*;

public class TextLogger implements MessageListener {
  // Default constructor
  public TextLogger() {}

  // Message handler
  public void onMessage(Message msg) {
    // If it's a text message, print it to stdout
    if (msg instanceof TextMessage) {
      TextMessage tMsg = (TextMessage)msg;
      try {
        System.out.println("Received message: " + tMsg.getText());
      }
      catch (JMSException je) {
        System.out.println("Error retrieving message text: " +
                           je.getMessage());
      }
    }
    // For other types of messages, print an error
    else {
      System.out.println("Unsupported message type encountered.");
    }
  }
}
