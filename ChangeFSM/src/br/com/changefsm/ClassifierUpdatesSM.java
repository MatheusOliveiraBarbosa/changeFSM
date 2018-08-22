package br.com.changefsm;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.changefsm.models.ClassChanged;
import br.com.changefsm.models.StateMachine;
import br.com.changefsm.models.UpdateSM;
import br.com.changefsm.models.UpdateSMType;
import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;

public class ClassifierUpdatesSM {

	private final String METHOD_CALL = "METHOD_INVOCATION";
	private final String ASSIGNMENT = "ASSIGNMENT";
	private final String IF_INSTANCE = "IF_STATEMENT";
	private final String ELSE_INSTANCE = "ELSE_STATEMENT";
//	private final String ENUM_VALUE = "ENUM_VALUE";
//	private final String SCAPE_RETURN = "SCAPE_RETURN";
	
	private final String UPDATE = "Update";
	private final String DELETE = "Delete";
	private final String INSERT = "Insert";
	
	private List<UpdateSM> updates;
	
	
	private final Logger log = LogManager.getLogger(ClassifierUpdatesSM.class);

	public ClassifierUpdatesSM() {
		updates = new ArrayList<UpdateSM>();
	}

	public void searchAndClassifySMUpdates(List<ClassChanged> candidateCodeClasses, StateMachine stateMachine) {
		for (ClassChanged classChanged : candidateCodeClasses) {
			log.info("Looking for updates in the class: " + classChanged.getClassFile().getName());
			analyseChanges(classChanged, stateMachine);
		}
	}

	private void analyseChanges(ClassChanged classChanged , StateMachine stateMachine) {
		for (SourceCodeChange change : classChanged.getChanges()) {
			
			UpdateSM updateSM = new UpdateSM(classChanged.getClassFile().getName(), change, stateMachine);
			
			switch (change.getChangedEntity().getLabel()) {
			case METHOD_CALL:
				classifyByChangesMethodCall(classChanged, stateMachine, change, updateSM);
				break;
			case ASSIGNMENT:
				classifyByChangeAssignment(classChanged, stateMachine, change, updateSM);
				break;
			case IF_INSTANCE:
				classifyByChangesIF(classChanged, stateMachine, change, updateSM);
				break;
			case ELSE_INSTANCE:
				classifyByChangesIF(classChanged, stateMachine, change, updateSM);
				break;
//			case ENUM_VALUE:
//				log.info("This change is in Enum");
//				break;
//			case SCAPE_RETURN:
//				log.info("This change is in Scape Return");
//				break;
			default:
				log.info("The change : ( " + change +" ) does not make changes in the SM.");
				break;

			}
		}
		log.info("The updates to realize are: " + updates);
	}

	//BEGIN CLASSIFY BY ASSIGNMENT
	/**
	 * Classify the updates in the SM by means of changes in Assignment
	 * 
	 * @param classChanged
	 * @param stateMachine
	 * @param change
	 * @param updateSM
	 */
	private void classifyByChangeAssignment(ClassChanged classChanged, StateMachine stateMachine,
			SourceCodeChange change, UpdateSM updateSM) {
		// TODO Verify that is indication of a switch State
		if(change.toString().startsWith(UPDATE)) {
			updateSM.setUpdateSMType(UpdateSMType.UPDATE_TRANSITION);
			updates.add(updateSM);
			log.info("Declare that have to update a Transition");
		}
		else if(change.toString().startsWith(DELETE)) {
			updateSM.setUpdateSMType(UpdateSMType.REMOVE_TRANSITION);
			updates.add(updateSM);
			log.info("Declare that have to remove a Transition");
		}
		else if(change.toString().startsWith(INSERT)) {
			updateSM.setUpdateSMType(UpdateSMType.ADD_TRANSITION);
			updates.add(updateSM);
			log.info("Declare that have to add a Transition");
		}
		
	}
	//END CLASSIFY BY ASSIGNMENT
	
	
	//BEGIN CLASSIFY BY METHOD CALL
	/**
	 * Classify the updates in the SM by means of changes in Method Call
	 * 
	 * @param classChanged
	 * @param stateMachine
	 * @param change
	 * @param updateSM
	 */
	private void classifyByChangesMethodCall(ClassChanged classChanged, StateMachine stateMachine,
			SourceCodeChange change, UpdateSM updateSM) {
		//TO-DO Compare to classify if is transition or action of the state
		if(change.toString().startsWith(UPDATE)) {
			updateSM.setUpdateSMType(UpdateSMType.UPDATE_ACTION_STATE);
			updates.add(updateSM);
			log.info("Declare that have to update a action of the state");
		}
		else if(change.toString().startsWith(DELETE)) {
			updateSM.setUpdateSMType(UpdateSMType.REMOVE_ACTION_STATE);
			updates.add(updateSM);
			log.info("Declare that have to remove a action of the state");
		}
		else if(change.toString().startsWith(INSERT)) {
			updateSM.setUpdateSMType(UpdateSMType.ADD_ACTION_STATE);
			updates.add(updateSM);
			log.info("Declare that have to add a action of the state");
		}
		
	}
	//END CLASSIFY BY METHOD CALL
	
	//BEGIN CLASSIFY BY IF
	/**
	 * Classify the updates in the SM by means of changes in IFs
	 * 
	 * @param classChanged
	 * @param stateMachine
	 * @param change
	 * @param updateSM
	 */
	private void classifyByChangesIF(ClassChanged classChanged, StateMachine stateMachine, SourceCodeChange change,
			UpdateSM updateSM) {
		if (isGuard(classChanged.getClassFile(), change, stateMachine)){
			if(change.toString().startsWith(UPDATE)) {
				updateSM.setUpdateSMType(UpdateSMType.UPDATE_GUARD);
				updates.add(updateSM);
				log.info("Declare that have to update a guard");
			}
			else if(change.toString().startsWith(DELETE)) {
				updateSM.setUpdateSMType(UpdateSMType.REMOVE_GUARD);
				updates.add(updateSM);
				log.info("Declare that have to remove a guard");
			}
			else if(change.toString().startsWith(INSERT)) {
				updateSM.setUpdateSMType(UpdateSMType.ADD_GUARD);
				updates.add(updateSM);
				log.info("Declare that have to add a guard");
			}
		}
	}

	private boolean isGuard(File classFile, SourceCodeChange change, StateMachine stateMachine) {
		//TO-DO Verify the body if looking for the states to indicate that this transition is a guard
		return true;
	}
	// END CLASSIFY BY IF
	
	
	/**
	 * Get a list do UpdateSM and return this 
	 * 
	 * @return <code> List of UpdateSM </code> : list of the possibles updates to do in StateMachine
	 */
	public List<UpdateSM> getUpdates(){
		return this.updates;
	}

}
