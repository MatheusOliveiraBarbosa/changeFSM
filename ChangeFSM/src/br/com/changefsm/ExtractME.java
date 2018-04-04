package br.com.changefsm;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import br.com.changefsm.models.State;
import br.com.changefsm.models.Transition;
import br.com.changefsm.readerxml.ReaderXML;

public class ExtractME {

	public static void main(String[] args) {

		ArrayList<State> states = new ArrayList<State>();
		ArrayList<Transition> transitions = new ArrayList<Transition>();
		
		String path = "./data/new/new.xml";
		ReaderXML.readXML(path);
		NodeList nlT = ReaderXML.getNodeList("transition");
		NodeList nlS = ReaderXML.getNodeList("subvertex");

		for (int i = 0; i < nlS.getLength(); i++) {
			Element element = (Element) nlS.item(i);
			states.add(new State(element.getAttribute("xmi:id"), element.getAttribute("name")));
		}

		State source;
		State target; 
		for (int j = 0; j < nlT.getLength(); j++) {
			Element element = (Element) nlT.item(j);
			source = null;
			target = null;
			for (State state : states) {
				if (element.getAttribute("source").equals(state.getId())) {
					source = state;
				}
				if (element.getAttribute("target").equals(state.getId())) {
					target = state;
				}
			}
			transitions.add(new Transition(element.getAttribute("xmi:id"), element.getAttribute("name"), source, target));
		}

		for (Transition transition : transitions) {
			System.out.println(transition.getSource() + "  ---------   " + transition.getName() + "   ----------->>  "
					+ transition.getTarget());
		}

	}

}
