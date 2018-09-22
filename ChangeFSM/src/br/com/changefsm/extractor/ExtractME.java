package br.com.changefsm.extractor;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import br.com.changefsm.models.State;
import br.com.changefsm.models.StateAction;
import br.com.changefsm.models.StateMachine;
import br.com.changefsm.models.StateType;
import br.com.changefsm.models.Transition;
import br.com.changefsm.models.TypeStateAction;
import br.com.changefsm.readerxml.ReaderXML;

public class ExtractME {

	private List<State> states = new ArrayList<State>();
	private List<Transition> transitions = new ArrayList<Transition>();
	private StateMachine stateMachine;
	private Logger log = LogManager.getLogger(ExtractME.class);

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
		log.info("Preparing for extract elements of the State Machine");
		NodeList nlT = ReaderXML.getNodeList("transition");
		NodeList nlS = ReaderXML.getNodeList("subvertex");

		extractStates(nlS);
		extractTransitions(nlT);
		
		setStateMachine(new StateMachine(ReaderXML.getNameFile(), states, transitions));
		log.info("Extraction of State Machine's elements was success.");
		log.info("The State Machine can be represented this way: \n" + printStateMachine(getStateMachine()));
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
			Transition transition = new Transition(element.getAttribute("xmi:id"), element.getAttribute("name"), source,
					target);
			transition.setGuard(extractGuard(element));
			transition.setEvent(extractEvent(element));
			this.transitions.add(transition);
		}
	}

	/**
	 * Extract the events of the Transition and return like a String the values that
	 * exist
	 * 
	 * @param element
	 * @return events of the Transition
	 */
	private String extractEvent(Element element) {
		String events = "";
		NodeList eventList = element.getElementsByTagName("effect");
		for (int i = 0; i < eventList.getLength(); i++) {
			Element e = (Element) eventList.item(i);
			events += e.getAttribute("body") + "\n";
		}
		return events;
	}

	/**
	 * Extract the guards of the Transitions and return like a String the
	 * expressions that exist
	 * 
	 * @param element
	 * @return guards of the Transition
	 */
	private String extractGuard(Element element) {
		NodeList guardList = element.getElementsByTagName("specification");
		String guards = "";
		for (int i = 0; i < guardList.getLength(); i++) {
			Element e = (Element) guardList.item(i);
			if (e.getParentNode().toString().contains("guard")) {
				guards += e.getAttribute("body") + "\n";
			}
		}
		return guards;
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
				this.states.add(new State(element.getAttribute("xmi:id"), "FINAL_STATE", StateType.PSEUDO_FINAL));

			} else if (element.getAttribute("xmi:type").equals("uml:Pseudostate")
					&& element.getAttribute("kind").equals("initial")) {
				this.states.add(new State(element.getAttribute("xmi:id"), "INITITAL_STATE", StateType.PSEUDO_INITIAL));

			} else {
				// here it will extract the actions presents on State
				State state = new State(element.getAttribute("xmi:id"), element.getAttribute("name"), StateType.NORMAL);
				state.setActions(extractActions(element));
				this.states.add(state);
			}
		}
	}

	/**
	 * This method extract the State's behaviors searching for tags has in the XML
	 * file
	 * 
	 * @param element
	 * @return ArrayList<StateAction> the behaviors of the State
	 */
	private ArrayList<StateAction> extractActions(Element element) {
		ArrayList<StateAction> actionsState = new ArrayList<StateAction>();
		NodeList doActs = element.getElementsByTagName("ownedOperation");
		for (int i = 0; i < doActs.getLength(); i++) {
			Element e = (Element) doActs.item(i);
			if (e.getParentNode().toString().contains("doActivity")) {
				StateAction stateAction = new StateAction(TypeStateAction.DO, e.getAttribute("name"));
				actionsState.add(stateAction);
			} else if (e.getParentNode().toString().contains("exitActivity")) {
				StateAction stateAction = new StateAction(TypeStateAction.EXIT, e.getAttribute("name"));
				actionsState.add(stateAction);
			} else if (e.getParentNode().toString().contains("entryActivity")) {
				StateAction stateAction = new StateAction(TypeStateAction.ENTRY, e.getAttribute("name"));
				actionsState.add(stateAction);
			}
		}
		return actionsState;
	}

	/**
	 * Create a representation of the transitions in the StateMachine and print on
	 * console the result
	 * 
	 * @return transitionsForPrint - A string for represent the transitions and
	 *         their states
	 */
	public String printStateMachine(StateMachine stateMachine) {
		String transitionsForPrint = "";
		for (Transition transition : stateMachine.getTransitions()) {
			transitionsForPrint += transition.getSource() + "  ---------   " + transition.getAction()
					+ "   ----------->>  " + transition.getTarget() + "\n";
		}
		return transitionsForPrint;
	}

	// Getters and Setters
	
	
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

	public StateMachine getStateMachine() {
		return stateMachine;
	}

	public void setStateMachine(StateMachine stateMachine) {
		this.stateMachine = stateMachine;
	}

}
