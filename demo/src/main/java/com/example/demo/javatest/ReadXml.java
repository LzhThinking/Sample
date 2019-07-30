package com.example.demo.javatest;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class ReadXml {
    public static void main(String [] args) {
        readXml("C:/workspace_android/Sample/demo/src/main/res/values/strings.xml");
    }

    public static void readXml(String file) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try{
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(file);
            NodeList resources = document.getElementsByTagName("resources");
            for(int i = 0; i < resources.getLength(); i++){
                System.out.println("--------第" + (i+1) + "本书----------");
                Element ele = (Element) resources.item(i);
                System.out.println("ele name: " + ele.getTagName());
                NodeList childNodes= ele.getChildNodes();
                for(int j = 0; j < childNodes.getLength(); j++){
                    Node n = childNodes.item(j);
                    System.out.println("node name: " + n.getNodeName() + ", node value = " + n.getNodeValue());
                }
                System.out.println("---------------------------------");
            }
        }catch (ParserConfigurationException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }catch (SAXException e){
            e.printStackTrace();
        }
    }
}
