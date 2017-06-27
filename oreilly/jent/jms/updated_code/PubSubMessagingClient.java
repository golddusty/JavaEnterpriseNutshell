package oreilly.jent.jms;

/**
 * PubSubMessagingClient: A JMS publish-subscribe messaging client.  
 * This client can either send or receive messages from a given Topic.  The 
 * main method allows for running the client from the command-line.
 * 
 * Example 10-3, Java Enterprise in a Nutshell, 2nd ed.
 * Author: Jim Farley
 */

import java.util.*;
import javax.naming.*;
import javax.jms.*;
import java.io.*;

public class PubSubMessagingClient implements Runnable {

  // Our connection to the JMS provider.  Only one is needed for this client.
  private TopicConnection mTopicConn = null;
  
  // The topic used for message-passing
  private Topic mTopic = null;

  // Our message subscriber - only need one.
  private TopicSubscriber mSubscriber = null;

  // A single session for sending and receiving from all remote peers.
  private TopicSession mSession = null;

  // The message type we tag all our messages with
  private static String MSG_TYPE = "JavaEntMessage";

  // Constructor, with client name, and the JNDI location of the JMS
  // connection factory and topic that we want to use.
  public PubSubMessagingClient(String cFactJNDIName, String topicJNDIName) {
    init(cFactJNDIName, topicJNDIName);
  }
  
  // Do all the JMS-setup for this client.  Assumes that the JVM is
  // configured (perhaps using jndi.properties) so that the default JNDI
  // InitialContext points to the JMS provider's JNDI service.
  protected boolean init(String cFactoryJNDIName, String topicJNDIName) {
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
    TopicConnectionFactory connFactory = null;
    try {
      connFactory = (TopicConnectionFactory)ctx.lookup(cFactoryJNDIName);
      System.out.println("Got JMS connection factory.");
    }
    catch (NamingException ne2) {
      System.out.println("Failed to get JMS connection factory: ");
      ne2.printStackTrace();
      success = false;
    }

    try {
      // Make a connection to the JMS provider and keep it
      // At this point, the connection is not started, so we aren't
      // receiving any messages.
      mTopicConn = connFactory.createTopicConnection();
      // Try to find our designated topic
      mTopic = (Topic)ctx.lookup(topicJNDIName);
      // Make a session for topicing messages
      // no transactions, auto-acknowledge
      mSession =
        mTopicConn.createTopicSession(false,
                                      javax.jms.Session.AUTO_ACKNOWLEDGE);
    }
    catch (JMSException e) {
      System.out.println("Failed to establish connection/topic:");
      e.printStackTrace();
      success = false;
    }
    catch (NamingException ne) {
      System.out.println("JNDI Error looking up factory or topic:");
      ne.printStackTrace();
      success = false;
    }

    try {
      // Make our subscriber, for incoming messages
      // Set the message selector to only receive our type of messages,
      // in case the same topic is being used for other purposes
      // Also indicate that we don't want any message sent from this connection
      mSubscriber =
        mSession.createSubscriber(mTopic, "JMSType = '" + MSG_TYPE + "'",
                                  true);
    }
    catch (JMSException je) {
      System.out.println("Error establishing message subscriber:");
      je.printStackTrace();
    }

    return success;
  }
      
  // Send a message to the topic
  public void publishMessage(String msg) {
    try {
      // Create a JMS msg publisher connected to the destination topic
      TopicPublisher publisher = mSession.createPublisher(mTopic);
      // Use the session to create a text message
      TextMessage tMsg = mSession.createTextMessage();
      tMsg.setJMSType(MSG_TYPE);
      // Set the body of the message
      tMsg.setText(msg);
      // Send the message using the publisher
      publisher.publish(tMsg);
      System.out.println("Published the message");
    }
    catch (JMSException je) {
      System.out.println("Error sending message " + msg + " to topic");
      je.printStackTrace();
    }
  }

  // Register a MessageListener with the topic to receive
  // messages asynchronously
  public void registerListener(MessageListener listener) {
    try {
      // Set the listener on the subscriber
      mSubscriber.setMessageListener(listener);
      // Start the connection, in case it's still stopped
      mTopicConn.start();
    }
    catch (JMSException je) {
      System.out.println("Error registering listener: ");
      je.printStackTrace();
    }
  }
     
  // Perform an synchronous receive of a message from the topic.  If it's a
  // TextMessage, print the contents.
  public String receiveMessage() {
    String msg = "-- No message --";
    try {
      // (Re)start the connection, in case it's not already started
      mTopicConn.start();
      // Check for a message
      Message m = mSubscriber.receive();
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
  
  // When run within a thread, just wait for messages to be delivered to us
  public void run() {
    while (true) {
      try { this.wait(); } catch (Exception we) {}
    }
  }

  // Take command-line arguments and send or receive messages from the
  // named topic
  public static void main(String args[]) {
    if (args.length < 3) {
      System.out.println("Usage: PubSubMessagingClient" +
                         " connFactoryName topicName" +
                         " [publish|subscribe|recv_synch] <messageToSend>");
      System.exit(1);
    }

    // Get our client name, and the JNDI name of the connection factory and
    // topic, from the command-line
    String factoryName = args[0];
    String topicName = args[1];

    // Get the command to execute (publish, subscribe, recv_synch)
    String cmd = args[2];
    
    // Create and initialize the messaging participant
    PubSubMessagingClient msger =
      new PubSubMessagingClient(factoryName, topicName);

    // Run the participant in its own thread, so that it can react to
    // incoming messages
    Thread listen = new Thread(msger);
    listen.start();

    // Send a message to the topic
    if (cmd.equals("publish")) {
      String msg = args[3];
      msger.publishMessage(msg);
      System.exit(0);
    }
    // Register a listener
    else if (cmd.equals("subscribe")) {
      MessageListener listener = new TextLogger();
      msger.registerListener(listener);
      System.out.println("Client listening to topic " + topicName
                         + ". . .");
      try { listen.wait(); } catch (Exception we) {}
    }
    // Synchronously receive a message from the topic
    else if (cmd.equals("recv_synch")) {
      String msg = msger.receiveMessage();
      System.out.println("Received message: " + msg);
      System.exit(0);
    }
  }
}
