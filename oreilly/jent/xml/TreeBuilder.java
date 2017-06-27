package oreilly.jent.xml;
// Example 9-3: TreeBuilder

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.w3c.dom.*;
import java.io.*;

public class TreeBuilder {

  public static void main(String[] args) {
  
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    dbf.setValidating(false);
    DocumentBuilder db = null;

    try {
      db = dbf.newDocumentBuilder();
    } catch (ParserConfigurationException pce) {
      pce.printStackTrace();
      return;
    }
    
    Document doc = db.getDOMImplementation().createDocument(null, "orders", null);
    // create the initial document element
    
    Element orderNode = doc.createElement("order");
    orderNode.setAttribute("orderno", "123433");

    Node item = doc.createElement("item");
    Node subitem = doc.createElement("number");
    subitem.appendChild(doc.createTextNode("3AGM-5"));
    item.appendChild(subitem);

    subitem = doc.createElement("handling");
    subitem.appendChild(doc.createTextNode("With Care"));
    item.appendChild(subitem);

    orderNode.appendChild(item);
    doc.getDocumentElement().appendChild(orderNode);
  }
}