package br.com.changefsm.readerxml;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ReaderXML {

	private static Document xml = null;
	private static String nameFile;

	public static Document readXML(String path) {
		File xmlStateMachine = new File(path);
		nameFile = xmlStateMachine.getName();
		try {
			DocumentBuilder dBuilder;
			dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			xml = dBuilder.parse(xmlStateMachine);
			xml.getDocumentElement().normalize();
		} catch (ParserConfigurationException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		} catch (SAXException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		return xml;
	}

	public static NodeList getNodeList(String tagName) {
		return xml.getElementsByTagName(tagName);
	}

	public static String getNameFile() {
		return nameFile;
	}

}