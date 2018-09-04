package br.com.changefsm.classifiersupdate;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.changefsm.models.ClassChanged;
import br.com.changefsm.models.State;
import br.com.changefsm.models.StateMachine;
import br.com.changefsm.models.UpdateSM;
import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;

public class ClassifierUpdatesSM {

	private final String METHOD_CALL = "METHOD_INVOCATION";
	private final String ASSIGNMENT = "ASSIGNMENT";
	private final String IF_INSTANCE = "IF_STATEMENT";
	private final String ELSE_INSTANCE = "ELSE_STATEMENT";
	private final String ENUM_ENTITY = "FIELD";
	private final String SCAPE_RETURN = "RETURN_STATEMENT";
	private final String METHOD = "METHOD";
	private final String ENUM = "ENUM";

	private InterfaceClassifierFacade classifierUpdatesSM;

	private List<UpdateSM> updates;
	private List<State> statesForClassification; // This list will used to provide aid on classification update SM time.

	private final Logger log = LogManager.getLogger(ClassifierUpdatesSM.class);

	public ClassifierUpdatesSM() {
		updates = new ArrayList<UpdateSM>();
		classifierUpdatesSM = new ClassifierFacade();
		statesForClassification = new ArrayList<State>();
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
		// log.info("The updates to realize are: " + updates);
	}

	private void analyseChanges(ClassChanged classChanged, StateMachine stateMachine) {

		for (SourceCodeChange change : classChanged.getChanges()) {
			UpdateSM updateSM = new UpdateSM(classChanged.getClassFile(), change, stateMachine);
			switch (change.getChangedEntity().getLabel()) {
			case METHOD_CALL:
				classifierUpdatesSM.classifyByMethodCall(updateSM);
				break;
			case ASSIGNMENT:
				classifierUpdatesSM.classifyByAssignemt(updateSM, statesForClassification);
				break;
			case IF_INSTANCE:
				classifierUpdatesSM.classifyByIF(updateSM);
				break;
			case ELSE_INSTANCE:
				classifierUpdatesSM.classifyByELSE(updateSM);
				break;
			case SCAPE_RETURN:
				classifierUpdatesSM.classifyByScapeReturn(updateSM, statesForClassification);
				break;
			case METHOD:
				classifierUpdatesSM.classifyByMethod(updateSM);
				break;
			default:
				break;
			}

			if (updateSM.getUpdateSMType() != null) {
				log.info("The update to realize is: " + updateSM);
				updates.add(updateSM);
			}
		}
	}

	/**
	 * Analysis only have to updates in State of the StateMachine
	 * 
	 * @param classChanged
	 * @param stateMachine
	 */
	private void analyseChangesToStates(ClassChanged classChanged, StateMachine stateMachine) {

		this.statesForClassification.addAll(stateMachine.getStates());

		for (SourceCodeChange change : classChanged.getChanges()) {
			UpdateSM updateSM = new UpdateSM(classChanged.getClassFile(), change, stateMachine);
			if (change.getChangeType().toString().contains(ENUM)
					&& change.getChangedEntity().getLabel().contains(ENUM_ENTITY)) {
				classifierUpdatesSM.classifyByEnum(updateSM, statesForClassification);
				if (updateSM.getUpdateSMType() != null) {
					updates.add(updateSM);
				}
			}
		}
	}

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
