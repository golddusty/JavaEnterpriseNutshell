package oreilly.jent.xml;
// Example 9-1: Parsing XML with SAX

import javax.xml.parsers.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

public class OrderHandler extends org.xml.sax.helpers.DefaultHandler {

  public static void main(String[] args) {
    SAXParserFactory spf = SAXParserFactory.newInstance();
    spf.setValidating(true); //request a validating parser
    XMLReader xmlReader = null;

    try {
      SAXParser saxParser = spf.newSAXParser();
      /* We need an XMLReader to use an ErrorHandler
      We could just pass the DataHandler to the parser if we wanted
      to use the default error handler. */
      xmlReader = saxParser.getXMLReader();
      xmlReader.setContentHandler(new OrderHandler());
      xmlReader.setErrorHandler(new OrderErrorHandler());
      xmlReader.parse("orders.xml");
      } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // The startDocument() method is called at the beginning of parsing
  public void startDocument() throws SAXException {
    System.out.println("Incoming Orders:");
  }

  // The startElement() method is called at the start of each element
  public void startElement(String namespaceURI, String localName,
    String rawName, Attributes atts) throws SAXException {
    
    if(localName.equals("order")) {
      System.out.print("\nNew Order Number " + atts.getValue("idnumber") +
        " for Customer Number " + atts.getValue("custno"));
    } else if (localName.equals("item")) {
      System.out.print("\nLine Item: " + atts.getValue("idnumber") + " (Qty " +
        atts.getValue("quantity") + ")");
    } else if (localName.equals("shippingaddr")) {
      System.out.println("\nShip by " + atts.getValue("method") + " to:");
    } else if (localName.equals("handling")) {
      System.out.print("\n\tHandling Instructions: ");
    }
  }

  // Print Characters within a tag
  // This will print the contents of the <shippingaddr> and <handling> tags
  // There is no guarantee that all characters will be delivered in a single call

  public void characters(char[] ch, int start, int length)
  throws SAXException {
    System.out.print(new String(ch, start, length));
  }
  
  /* A custom error handling class, although DefaultHandler actually
  implements both interfaces. 
  Here we just throw the exception back to the user.*/

  private static class OrderErrorHandler implements ErrorHandler {

    public void error(SAXParseException spe) throws SAXException {
      throw new SAXException(spe);
    }

    public void warning(SAXParseException spe) throws SAXException {
      System.out.println("\nParse Warning: " + spe.getMessage());
    }

    public void fatalError(SAXParseException spe) throws SAXException {
      throw new SAXException(spe);
    }
  }
}