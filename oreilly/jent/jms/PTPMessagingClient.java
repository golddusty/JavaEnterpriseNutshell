package oreilly.jent.jms;

/**
 * PTPMessagingClient: A JMS point-to-point messaging client.  This client 
 * can either send or receive messages from a given Queue.  The main method 
 * allows for running the client from the command-line.
 * 
 * Example 10-2, Java Enterprise in a Nutshell, 2nd ed.
 * Author: Jim Farley
 */

import java.util.*;
import javax.naming.*;
import javax.jms.*;
import java.io.*;

public class PTPMessagingClient implements Runnable {

  // Our connection to the JMS provider.  Only one is needed for this client.
  private QueueConnection mQueueConn = null;
  
  // The queue used for message-passing
  private Queue mQueue = null;

  // Our message receiver - only need one.
  private QueueReceiver mReceiver = null;

  // A single session for sending and receiving from all remote peers.
  private QueueSession mSession = null;

  // The message type we tag all our messages with
  private static String MSG_TYPE = "JavaEntMessage";

  // Constructor, with client name, and the JNDI locations of the JMS
  // connection factory and queue that we want to use.
  public PTPMessagingClient(String cFactoryJNDIName, String queueJNDIName) {
    init(cFactoryJNDIName, queueJNDIName);
  }
  
  // Do all the JMS-setup for this client.  Assumes that the JVM is
  // configured (perhaps using jndi.properties) so that the default JNDI
  // InitialContext points to the JMS provider's JNDI service.
  protected boolean init(String cFactoryJNDIName, String queueJNDIName) {
    boolean success = true;

    Context ctx = null;
    // Attempt to make connection to JNDI service
    try {
      ctx = new InitialContext();
    }
    catch (NamingException ne) {
      System.out.println("Failed to connect to JNDI provider:");
      ne.printStackTrace();
      success = false;
    }

    // If no JNDI context, bail out here
    if (ctx == null) {
      return success;
    }

    // Attempt to lookup JMS connection factory from JNDI service
    QueueConnectionFactory connFactory = null;
    try {
      connFactory = (QueueConnectionFactory)ctx.lookup(cFactoryJNDIName);
      System.out.println("Got JMS connection factory.");
    }
    catch (NamingException ne2) {
      System.out.println("Failed to get JMS connection factory: ");
      ne2.printStackTrace();
      success = false;
    }

    try {
      // Make a connection to the JMS provider and keep it.
      // At this point, the connection is not started, so we aren't
      // receiving any messages.
      mQueueConn = connFactory.createQueueConnection();
      // Try to find our designated queue
      mQueue = (Queue)ctx.lookup(queueJNDIName);
      // Make a session for queueing messages: no transactions,
      // auto-acknowledge
      mSession =
        mQueueConn.createQueueSession(false,
                                      javax.jms.Session.AUTO_ACKNOWLEDGE);
    }
    catch (JMSException e) {
      System.out.println("Failed to establish connection/queue:");
      e.printStackTrace();
      success = false;
    }
    catch (NamingException ne) {
      System.out.println("JNDI Error looking up factory or queue:");
      ne.printStackTrace();
      success = false;
    }

    try {
      // Make our receiver, for incoming messages.
      // Set the message selector to only receive our type of messages,
      // in case the same queue is being used for other purposes.
      mReceiver = mSession.createReceiver(mQueue,
                                          "JMSType = '" + MSG_TYPE + "'");
    }
    catch (JMSException je) {
      System.out.println("Error establishing message receiver:");
      je.printStackTrace();
    }

    return success;
  }
      
  // Send a message to the queue
  public void sendMessage(String msg) {
    try {
      // Create a JMS msg sender connected to the destination queue
      QueueSender sender = mSession.createSender(mQueue);
      // Use the session to create a text message
      TextMessage tMsg = mSession.createTextMessage();
      tMsg.setJMSType(MSG_TYPE);
      // Set the body of the message
      tMsg.setText(msg);
      // Send the message using the sender
      sender.send(tMsg);
      System.out.println("Sent the message");
    }
    catch (JMSException je) {
      System.out.println("Error sending message " + msg + " to queue");
      je.printStackTrace();
    }
  }

  // Register a MessageListener with the queue to receive
  // messages asynchronously
  public void registerListener(MessageListener listener) {
    try {
      // Set the listener on the receiver
      mReceiver.setMessageListener(listener);
      // Start the connection, in case it's still stopped
      mQueueConn.start();
    }
    catch (JMSException je) {
      System.out.println("Error registering listener: ");
      je.printStackTrace();
    }
  }
     
  // Perform an synchronous receive of a message from the queue.  If it's a
  // TextMessage, print the contents.
  public String receiveMessage() {
    String msg = "-- No message --";
    try {
      Message m = mReceiver.receive();
      if (m instanceof TextMessage) {
        msg = ((TextMessage)m).getText();
      }
      else {
        msg = "-- Unsupported message type received --";
      }
    }
    catch (JMSException je) {
    }
    return msg;
  }

  // Print the current contents of the message queue, using a QueueBrowser
  // so that we don't remove any messages from the queue
  public void printQueue() {
    try {
      QueueBrowser browser = mSession.createBrowser(mQueue);
      Enumeration msgEnum = browser.getEnumeration();
      System.out.println("Queue contents:");
      while (msgEnum.hasMoreElements()) {
        System.out.println("\t" + (Message)msgEnum.nextElement());
      }
    }
    catch (JMSException je) {
      System.out.println("Error browsing queue: " + je.getMessage());
    }
  }
  
  // When run within a thread, just wait for messages to be delivered to us
  public void run() {
    while (true) {
      try { this.wait(); } catch (Exception we) {}
    }
  }

  // Take command-line arguments and send or receive messages from the
  // named queue
  public static void main(String args[]) {
    if (args.length < 3) {
      System.out.println("Usage: PTPMessagingClient" +
                         " connFactoryName queueName" +
                         " [send|listen|recv_synch] <messageToSend>");
      System.exit(1);
    }

    // Get the JNDI names of the connection factory and
    // queue, from the command-line
    String factoryName = args[0];
    String queueName = args[1];

    // Get the command to execute (send, recv, recv_synch)
    String cmd = args[2];
    
    // Create and initialize the messaging participant
    PTPMessagingClient msger =
      new PTPMessagingClient(factoryName, queueName);

    // Run the participant in its own thread, so that it can react to
    // incoming messages
    Thread listen = new Thread(msger);
    listen.start();

    // Send a message to the queue
    if (cmd.equals("send")) {
      String msg = args[3];
      msger.sendMessage(msg);
      System.exit(0);
    }
    // Register a listener
    else if (cmd.equals("listen")) {
      MessageListener listener = new TextLogger();
      msger.registerListener(listener);
      System.out.println("Client listening to queue " + queueName
                         + ". . .");
      System.out.flush();
      try { listen.wait(); } catch (Exception we) {}
    }
    // Synchronously receive a message from the queue
    else if (cmd.equals("recv_synch")) {
      String msg = msger.receiveMessage();
      System.out.println("Received message: " + msg);
      System.exit(0);
    }
    else if (cmd.equals("browse")) {
      msger.printQueue();
      System.exit(0);
    }
  }
}
