package br.com.changefsm;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import br.com.changefsm.models.State;
import br.com.changefsm.models.Transition;
import br.com.changefsm.readerxml.ReaderXML;

public class ExtractME {

	private List<State> states = new ArrayList<State>();
	private List<Transition> transitions = new ArrayList<Transition>();

	public ExtractME() {}
	
	/**
	 * Read a XML file and extract the values used in StateMachine like a State and
	 * Transitions
	 * 
	 * The XML model is Enterprise Architecture
	 * 
	 * @author Matheus de Oliveira
	 */
	public void extractElementsSM(String path) {
		ReaderXML.readXML(path);
		NodeList nlT = ReaderXML.getNodeList("transition");
		NodeList nlS = ReaderXML.getNodeList("subvertex");

		extractStates(nlS);

		extractTransitions(nlT);

	}

	/**
	 * Extract the transitions in StateMachine For each element in nodelist mark
	 * with "transition" this method extract the values and search for the Target
	 * and Source state for add in Transition object
	 * 
	 * @param nlT
	 */
	private void extractTransitions(NodeList nlT) {
		State source;
		State target;
		for (int j = 0; j < nlT.getLength(); j++) {
			Element element = (Element) nlT.item(j);
			source = null;
			target = null;
			for (State state : getStates()) {
				if (element.getAttribute("source").equals(state.getId())) {
					source = state;
				}
				if (element.getAttribute("target").equals(state.getId())) {
					target = state;
				}
			}
			getTransitions()
					.add(new Transition(element.getAttribute("xmi:id"), element.getAttribute("name"), source, target));
		}
	}

	/**
	 * Extract the States in the StateMachine For each element in nodelist mark with
	 * "subvertex" (this is a state) this method extract the values the states and
	 * pseudostates like "initialState" and "finalState", and add to the list of the
	 * States
	 * 
	 * @param nlS
	 */
	private void extractStates(NodeList nlS) {
		for (int i = 0; i < nlS.getLength(); i++) {
			Element element = (Element) nlS.item(i);
			if (element.getAttribute("xmi:type").equals("uml:FinalState")) {
				getStates().add(new State(element.getAttribute("xmi:id"), "FINAL_STATE"));

			} else if (element.getAttribute("xmi:type").equals("uml:Pseudostate")
					&& element.getAttribute("kind").equals("initial")) {
				getStates().add(new State(element.getAttribute("xmi:id"), "INITITAL_STATE"));

			} else {
				getStates().add(new State(element.getAttribute("xmi:id"), element.getAttribute("name")));
			}
		}
	}

	/**
	 * Create a representation of the transitions in the StateMachine and print on
	 * console the result
	 * 
	 * @return transitionsForPrint - A string for represent the transitions and
	 *         their states
	 */
	public String printTransitions() {
		String transitionsForPrint = "";
		for (Transition transition : getTransitions()) {
			transitionsForPrint += transition.getSource() + "  ---------   " + transition.getAction()
					+ "   ----------->>  " + transition.getTarget() + "\n";
		}
		return transitionsForPrint;
	}

	public List<State> getStates() {
		return states;
	}

	public void setStates(List<State> states) {
		this.states = states;
	}

	public List<Transition> getTransitions() {
		return transitions;
	}

	public void setTransitions(List<Transition> transitions) {
		this.transitions = transitions;
	}

}
