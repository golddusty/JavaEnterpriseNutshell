package oreilly.jent.xml;

// Example 9-2: DocumentCondenser
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.w3c.dom.*;
import java.io.*;

public class DocumentCondenser {

  public static void main(String[] args) throws Exception {

    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

    // For HTML, we don't want to validate without a DTD
    dbf.setValidating(false);
    // Ignore text elements that are completely empty:
    dbf.setIgnoringElementContentWhitespace(false);
    dbf.setExpandEntityReferences(true);
    dbf.setCoalescing(true);

    DocumentBuilder db = null;
    try {
      db = dbf.newDocumentBuilder();
    } catch (ParserConfigurationException pce) {
      pce.printStackTrace();
      return;
    }
    
    Document html = null;
    try {
      html = db.parse("enterprisexml.html");
      process(html);

      // Use the XSLT Transformer to see the output
      TransformerFactory tf = TransformerFactory.newInstance();
      Transformer output = tf.newTransformer();
      output.transform(new DOMSource(html), new StreamResult(System.out));
    } catch (Exception ex) {
      ex.printStackTrace();
      return;
    }
  }
  
  /* We want to keep text if the parent is <em>, <title>, <b>, <li>, <th>
  or <h1>..<h6>. We also want to keep text if it is in a <font> tag with
  a size attribute set to a larger than normal size */

  private static boolean keepText(Node parentNode) {
    if(parentNode == null) return true; // top level

    String parentName = parentNode.getLocalName();

    if((parentName.equalsIgnoreCase("em")) ||
    (parentName.equalsIgnoreCase("title")) ||
    (parentName.equalsIgnoreCase("b")) ||
    (parentName.equalsIgnoreCase("li")) ||
    (parentName.equalsIgnoreCase("th")) ||
    ((parentName.toLowerCase().startsWith("h")) &&
    (parentName.length() == 2))
    ) return true;

    if((parentNode.getNodeType() == Node.ELEMENT_NODE) &&
       (parentName.equalsIgnoreCase("font"))) {

      NamedNodeMap atts = parentNode.getAttributes();

      if(atts != null) {
        Node sizeNode = atts.getNamedItem("size"); //get an attribue Node
        if(sizeNode != null)
          if(sizeNode.getNodeValue().startsWith("+"))
            return true;
      }
    }

    return false;
  }
  
  private static void process(Node node) {
    Node c = null;
    Node delNode = null;
    for (c = node.getFirstChild(); c != null; c = c.getNextSibling()) {

      if(delNode != null)
        delNode.getParentNode().removeChild(delNode);
        delNode = null;

      if(( c.getNodeType() == Node.TEXT_NODE ) && (!keepText(c.getParentNode()))) {
          delNode = c;
      } else if(c.getNodeType() != Node.TEXT_NODE) {
        process(c);
      }
    } // End For

    if(delNode != null) // Delete, if the last child was text
      delNode.getParentNode().removeChild(delNode);
  }
}