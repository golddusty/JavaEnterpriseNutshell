package oreilly.jent.xml;

// Example 9-4: Transforming a Document into a SAXResult

import javax.xml.transform.*;
import javax.xml.transform.sax.*;
import javax.xml.transform.stream.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

public class SAXTransformTarget {
  public static void main(String[] args) {
    try {
      StreamSource ss = new StreamSource("orders.xml");
      SAXResult sr = new SAXResult(new OrderHandler());
      TransformerFactory tf = TransformerFactory.newInstance();
      Transformer t = tf.newTransformer();
      t.transform(ss, sr);
      } catch (TransformerConfigurationException e) {
      e.printStackTrace();
    } catch (TransformerException e) {
      e.printStackTrace();
    }
  }
}