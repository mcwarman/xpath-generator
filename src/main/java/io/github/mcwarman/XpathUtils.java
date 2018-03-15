package io.github.mcwarman;

import org.w3c.dom.*;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mwarman
 */
public class XpathUtils {

  private XpathUtils(){
  }

  public static List<String> getXpaths(String file) throws ParserConfigurationException, IOException, SAXException {
    DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
    Document document = docBuilder.parse(new ByteArrayInputStream((file).getBytes()));
    List<Node> nodes = new ArrayList<>();
    while(true){
      Node node = getXPaths(document.getDocumentElement(), nodes);
      if(node == null){
        break;
      }
      nodes.add(node);
    }
    List<String> xpaths = new ArrayList<>();
    for (Node node : nodes){
      xpaths.add(generateXPath(node));
    }
    return xpaths;
  }

  private static Node getXPaths(Node root, List<Node> foundNodes) {
    for (int i = 0; i < root.getChildNodes().getLength(); i++) {
      Node node = root.getChildNodes().item(i);
      if(node.getAttributes() != null) {
        for (int j = 0; j < node.getAttributes().getLength(); j++) {
          Node attribute = node.getAttributes().item(j);
          if (attribute != null && !foundNodes.contains(attribute)) {
            return attribute;
          }
        }
      }

      if (node instanceof Text && !((Text) node).getWholeText().trim().equals("") && !foundNodes.contains(node.getParentNode())){
        return node.getParentNode();
      } else if (node instanceof Element && node.getChildNodes().getLength() > 0) {
        Node result = getXPaths(node, foundNodes);
        if (result != null) {
          return result;
        }
      }
    }
    return null;
  }



  private static String generateXPath(Node node){
    Node parent;
    if(node instanceof Attr){
      parent = ((Attr)node).getOwnerElement();
    } else {
      parent = node.getParentNode();
    }
    if (parent == null)
    {
      return "";
    }
    int pos  = 0;
    if(node.getParentNode()!=  null && node.getParentNode().getChildNodes() != null && node.getParentNode().getChildNodes().getLength() > 1){
      for (int i = 0; i < node.getParentNode().getChildNodes().getLength(); i++) {
        Node child = node.getParentNode().getChildNodes().item(i);
        if(child instanceof Element){
          if (child.getNodeName().equals(node.getNodeName())){
            pos++;
          }
        }
      }
    }
    return generateXPath(parent) + "/" +(node instanceof Attr ? "@" : "" ) + node.getNodeName() +
        (pos > 1 ? String.format("[%s]", pos) : "");
  }

}
