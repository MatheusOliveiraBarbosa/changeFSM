package br.com.changefsm;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.changefsm.models.ClassChanged;
import br.com.changefsm.models.State;
import br.com.changefsm.models.StateMachine;
import br.com.changefsm.models.UpdateSM;
import br.com.changefsm.models.UpdateSMType;
import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;

public class ClassifierUpdatesSM {

	private final String METHOD_CALL = "METHOD_INVOCATION";
	private final String ASSIGNMENT = "ASSIGNMENT";
	private final String IF_INSTANCE = "IF_STATEMENT";
	private final String ELSE_INSTANCE = "ELSE_STATEMENT";
	private final String ENUM_ENTITY = "FIELD";
	private final String SCAPE_RETURN = "RETURN_STATEMENT";
	private final String METHOD = "METHOD";

	private final String UPDATE = "Update";
	private final String DELETE = "Delete";
	private final String INSERT = "Insert";
	private final String ENUM = "ENUM";

	private List<UpdateSM> updates;
	private List<State> smStates;

	private final Logger log = LogManager.getLogger(ClassifierUpdatesSM.class);

	public ClassifierUpdatesSM() {
		updates = new ArrayList<UpdateSM>();
	}

	public void searchAndClassifySMUpdates(List<ClassChanged> candidateCodeClasses, StateMachine stateMachine) {
		for (ClassChanged classChanged : candidateCodeClasses) {
			log.info("Looking for updates in STATES in the class: " + classChanged.getClassFile().getName());
			analyseChangesToStates(classChanged, stateMachine);
		}
		for (ClassChanged classChanged : candidateCodeClasses) {
			log.info("Looking for updates in another elements in the class: " + classChanged.getClassFile().getName());
			analyseChanges(classChanged, stateMachine);
		}
		log.info("The updates to realize are: " + updates);
	}

	private void analyseChanges(ClassChanged classChanged, StateMachine stateMachine) {

		for (SourceCodeChange change : classChanged.getChanges()) {
			UpdateSM updateSM = new UpdateSM(classChanged.getClassFile(), change, stateMachine);
			switch (change.getChangedEntity().getLabel()) {
			case METHOD_CALL:
				classifyByChangesMethodCall(updateSM);
				break;
			case ASSIGNMENT:
				classifyByChangeAssignment(updateSM);
				break;
			case IF_INSTANCE:
				classifyByChangesIF(updateSM);
				break;
			case ELSE_INSTANCE:
				classifyByChangesIF(updateSM);
				break;
			case SCAPE_RETURN:
				classifyByScapeReturn(updateSM);
				break;
			case METHOD:
				classifyByMethod(updateSM);
				break;
			default:
				break;
			}
		}
	}

	private void classifyByMethod(UpdateSM updateSM) {
		if(updateSM.getCodeChange().toString().startsWith(INSERT) &&
				updateSM.getCodeChange().getLabel().equals("ADDITIONAL_FUNCTIONALITY") ) {
			updateSM.setUpdateSMType(UpdateSMType.ADD_EVENT);
		}else if(updateSM.getCodeChange().toString().startsWith(DELETE) &&
				updateSM.getCodeChange().getLabel().equals("REMOVED_FUNCTIONALITY") ) {
			updateSM.setUpdateSMType(UpdateSMType.REMOVE_EVENT);
		}

	}

	//BEGIN SCAPE RETURN
	/**
	 * Analysis whether scape return has relation with the states to indicate when is a composite state or not.
	 * @param updateSM
	 */
	private void classifyByScapeReturn(UpdateSM updateSM) {
		int indicateComposite = 0;
		for (State state : this.smStates) {
			if (updateSM.getCodeChange().toString().toLowerCase().contains(state.getName().toLowerCase())) {
				indicateComposite++;
				if (indicateComposite > 1 && updateSM.getCodeChange().toString().startsWith(INSERT)) {
					updateSM.setUpdateSMType(UpdateSMType.ADD_COMPOSITE_STATE);
				} else if (indicateComposite > 1 && updateSM.getCodeChange().toString().startsWith(DELETE)) {
					updateSM.setUpdateSMType(UpdateSMType.REMOVE_COMPOSITE_STATE);
				}else if (indicateComposite > 1 && updateSM.getCodeChange().toString().startsWith(UPDATE)) {
					updateSM.setUpdateSMType(UpdateSMType.ALTER_BODY_COMPOSITE_STATE);
				}
			}
		}

	}
	//END SCAPE RETURN
	
	/**
	 * Analysis only have to updates in State of the StateMachine
	 * 
	 * @param classChanged
	 * @param stateMachine
	 */
	private void analyseChangesToStates(ClassChanged classChanged, StateMachine stateMachine) {
		for (SourceCodeChange change : classChanged.getChanges()) {
			UpdateSM updateSM = new UpdateSM(classChanged.getClassFile(), change, stateMachine);
			if (change.getChangeType().toString().contains(ENUM)
					&& change.getChangedEntity().getLabel().contains(ENUM_ENTITY)) {
				this.smStates = stateMachine.getStates();
				classifyByChangesEnum(updateSM, this.smStates);
			}
		}
	}

	// BEGIN CLASSIFY BY ENUM
	/**
	 * Classify the update in the SM by means of changes in Enum, and add the new
	 * states to the list of the extracted states to after provide aid to find
	 * transitions and others updating.
	 * 
	 * @param updateSM
	 * @param stateMachine
	 */
	private void classifyByChangesEnum(UpdateSM updateSM, List<State> states) {

		String nameEnum = extractNameOfEnum(updateSM.getCodeChange().toString());
		boolean exist = false;
		for (State state : states) {
			if (state.getName().contains(nameEnum)) {
				exist = true;
				break;
			}
		}
		if (updateSM.getCodeChange().getChangeType().toString().contains("ADD") && exist == false) {
			updateSM.setUpdateSMType(UpdateSMType.ADD_STATE);
			states.add(new State(nameEnum));
			updates.add(updateSM);
		} else if (updateSM.getCodeChange().getChangeType().toString().contains("REMOVE") && exist == true) {
			updateSM.setUpdateSMType(UpdateSMType.REMOVE_STATE);
			updates.add(updateSM);
		}
	}

	private String extractNameOfEnum(String changeToString) {
		int indexLast2PointsAndSpace = changeToString.lastIndexOf(" :");
		int indexLastPoint = changeToString.lastIndexOf(".");
		return changeToString.substring(indexLastPoint + 1, indexLast2PointsAndSpace);
	}

	// END CLASSIFY BY ENUM

	// BEGIN CLASSIFY BY ASSIGNMENT
	/**
	 * Classify the updates in the SM by means of changes in Assignment
	 * 
	 * @param classChanged
	 * @param stateMachine
	 * @param change
	 * @param updateSM
	 */
	private void classifyByChangeAssignment(UpdateSM updateSM) {
		// TODO Verify that is indication of a switch State
		boolean isPossibleTransition = isPossibleTransition(updateSM);
		if (isPossibleTransition) {
			if (updateSM.getCodeChange().toString().startsWith(UPDATE)) {
				log.info(updateSM.getCodeChange());
				updateSM.setUpdateSMType(UpdateSMType.UPDATE_TRANSITION);
				updates.add(updateSM);
				log.info("Assignment: " + updateSM);
			} else if (updateSM.getCodeChange().toString().startsWith(DELETE)) {
				log.info(updateSM.getCodeChange());
				updateSM.setUpdateSMType(UpdateSMType.REMOVE_TRANSITION);
				updates.add(updateSM);
				log.info("Assignment: " + updateSM);
			} else if (updateSM.getCodeChange().toString().startsWith(INSERT)) {
				log.info(updateSM.getCodeChange());
				updateSM.setUpdateSMType(UpdateSMType.ADD_TRANSITION);
				updates.add(updateSM);
				log.info("Assignment: " + updateSM);
			}
		}

	}

	private boolean isPossibleTransition(UpdateSM updateSM) {
		boolean isPossibleTransition = false;
		for (State state : updateSM.getStateMachine().getStates()) {
			if (updateSM.getCodeChange().toString().contains(state.getName())) {
				isPossibleTransition = true;
			} else if (state.getName().split(" ").length > 1) {
				String[] words = state.getName().split(" ");
				List<String> wordsSeparated = removeStopWords(words);
				for (String word : wordsSeparated) {
					if (updateSM.getCodeChange().toString().toLowerCase().contains(word.toLowerCase())) {
						isPossibleTransition = true;
					}
				}
			}
		}
		return isPossibleTransition;
	}

	private List<String> removeStopWords(String[] words) {
		String[] stopWords = { "of", "the", "set", "get", "a", "an", "with", "to", "=", "true", "false", "state", "",
				" " };
		List<String> separetedWords = new ArrayList<String>();
		for (int i = 0; i < words.length; i++) {
			boolean hasStopWord = false;
			for (int j = 0; j < stopWords.length; j++) {
				if (words[i].toLowerCase().equals(stopWords[j].toLowerCase())) {
					hasStopWord = true;
					break;
				}
			}
			if (hasStopWord == false) {
				separetedWords.add(words[i]);
			}
		}
		return separetedWords;
	}

	// END CLASSIFY BY ASSIGNMENT

	// BEGIN CLASSIFY BY METHOD CALL
	/**
	 * Classify the updates in the SM by means of changes in Method Call
	 * 
	 * @param classChanged
	 * @param stateMachine
	 * @param change
	 * @param updateSM
	 */
	private void classifyByChangesMethodCall(UpdateSM updateSM) {
		// TO-DO Compare to classify if is transition or action of the state
		if (updateSM.getCodeChange().toString().startsWith(UPDATE)) {
			updateSM.setUpdateSMType(UpdateSMType.UPDATE_ACTION_STATE);
			updates.add(updateSM);
			log.info("Declare that have to update a action of the state");
		} else if (updateSM.getCodeChange().toString().startsWith(DELETE)) {
			updateSM.setUpdateSMType(UpdateSMType.REMOVE_ACTION_STATE);
			updates.add(updateSM);
			log.info("Declare that have to remove a action of the state");
		} else if (updateSM.getCodeChange().toString().startsWith(INSERT)) {
			updateSM.setUpdateSMType(UpdateSMType.ADD_ACTION_STATE);
			updates.add(updateSM);
			log.info("Declare that have to add a action of the state");
		}

	}
	// END CLASSIFY BY METHOD CALL

	// BEGIN CLASSIFY BY IF
	/**
	 * Classify the updates in the SM by means of changes in IFs
	 * 
	 * @param classChanged
	 * @param stateMachine
	 * @param change
	 * @param updateSM
	 */
	private void classifyByChangesIF(UpdateSM updateSM) {
		if (isGuard(updateSM)) {
			if (updateSM.getCodeChange().toString().startsWith(UPDATE)) {
				updateSM.setUpdateSMType(UpdateSMType.UPDATE_GUARD);
				updates.add(updateSM);
				log.info("Declare that have to update a guard");
			} else if (updateSM.getCodeChange().toString().startsWith(DELETE)) {
				updateSM.setUpdateSMType(UpdateSMType.REMOVE_GUARD);
				updates.add(updateSM);
				log.info("Declare that have to remove a guard");
			} else if (updateSM.getCodeChange().toString().startsWith(INSERT)) {
				updateSM.setUpdateSMType(UpdateSMType.ADD_GUARD);
				updates.add(updateSM);
				log.info("Declare that have to add a guard");
			}
		}
	}

	private boolean isGuard(UpdateSM updateSM) {
		// TO-DO Verify the body if looking for the states to indicate that this
		// transition is a guard
		return true;
	}
	// END CLASSIFY BY IF

	/**
	 * Get a list do UpdateSM and return this
	 * 
	 * @return <code> List of UpdateSM </code> : list of the possibles updates to do
	 *         in StateMachine
	 */
	public List<UpdateSM> getUpdates() {
		return this.updates;
	}

}
