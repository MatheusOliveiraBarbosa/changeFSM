package br.com.changefsm.classifiersupdate;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.changefsm.models.State;
import br.com.changefsm.models.StateAction;
import br.com.changefsm.models.Transition;
import br.com.changefsm.models.TypeStateAction;
import br.com.changefsm.models.UpdateSM;
import br.com.changefsm.models.UpdateSMType;

public class ClassifierByMethodCall extends ClassifierUpdate implements InterfaceClassifierByMethodCall {

	private static final Logger log = LogManager.getLogger(ClassifierByMethodCall.class);

	@Override
	public void classifyByMethodCall(UpdateSM updateSM, List<State> statesForClassification) {
		if (updateSM.getCodeChange().toString().startsWith(getINSERT())) { // INSERT
			if (hasStateInParameters(updateSM, statesForClassification)) {
				updateSM.setUpdateSMType(UpdateSMType.ADD_TRANSITION);
				log.info("After classifyByMethodCall: " + updateSM);
			} // TO-DO INSERT STATE'S ACTIONS AND TRANSITION'S ACTIONS

		} else if (updateSM.getCodeChange().toString().startsWith(getDELETE())) { // DELETE
			if (isTransitionAction(updateSM)) {
				updateSM.setUpdateSMType(UpdateSMType.REMOVE_ACTION);
				if (hasStateInParameters(updateSM, statesForClassification)) {
					updateSM.setUpdateSMType(UpdateSMType.REMOVE_TRANSITION);
				}
			} else {
				classifyDeleteTypeStateAction(updateSM);
			}
		} else if (updateSM.getCodeChange().toString().startsWith(getUPDATE())) { // TO-DO UPDATE

		}
	}

	private void classifyDeleteTypeStateAction(UpdateSM updateSM) {
		for (State state : updateSM.getStateMachine().getStates()) {
			for (StateAction stateAction : state.getActions()) {
				String nameAction = findAndRemoveSpecialCharacter(stateAction.getName()).toLowerCase();
				String nameMethod = extractObjectWithNameMethod(updateSM).toLowerCase();
				if (nameAction.split(" ").length > 1) {
					String[] wordsSeparated = nameAction.split(" ");
					List<String> wordsSelected = removeStopWords(wordsSeparated);
					for (String word : wordsSelected) {
						if (nameMethod.contains(word)) {
							classifyByStateAction(updateSM, stateAction);
						}
					}
				} else {
					if (nameMethod.contains(nameAction)) {
						classifyByStateAction(updateSM, stateAction);
					}
				}
			}
		}

	}

	private void classifyByStateAction(UpdateSM updateSM, StateAction stateAction) {
		if (stateAction.getTypeStateAction() == TypeStateAction.DO) {
			updateSM.setUpdateSMType(UpdateSMType.REMOVE_DOACTION_STATE);
		} else if (stateAction.getTypeStateAction() == TypeStateAction.ENTRY) {
			updateSM.setUpdateSMType(UpdateSMType.REMOVE_ENTRYACTION_STATE);
		} else if (stateAction.getTypeStateAction() == TypeStateAction.EXIT) {
			updateSM.setUpdateSMType(UpdateSMType.REMOVE_EXITACTION_STATE);
		}
	}

	private String findAndRemoveSpecialCharacter(String nameAction) {
		if (nameAction.contains("(")) {
			nameAction = nameAction.substring(0, nameAction.indexOf("("));
		} else if (nameAction.contains("[")) {
			nameAction = nameAction.substring(0, nameAction.indexOf("["));
		} else if (nameAction.contains("{")) {
			nameAction = nameAction.substring(0, nameAction.indexOf("{"));
		}
		return nameAction;
	}

	private boolean isTransitionAction(UpdateSM updateSM) {
		String nameMethod = extractObjectWithNameMethod(updateSM);
		for (Transition transition : updateSM.getStateMachine().getTransitions()) {
			if (transition.getAction().toLowerCase().contains(nameMethod.toLowerCase())) {
				return true;
			}
		}
		return false;
	}

	private String extractObjectWithNameMethod(UpdateSM updateSM) {
		String nameMethod = updateSM.getCodeChange().getChangedEntity().getUniqueName();
		int index = nameMethod.indexOf("(");
		nameMethod = nameMethod.substring(0, index);
		int indexObj = nameMethod.indexOf(".");
		if (indexObj > 0) {
			nameMethod = nameMethod.substring(indexObj);
		}
		return nameMethod;
	}

	private boolean hasStateInParameters(UpdateSM updateSM, List<State> statesForClassification) {
		String param = updateSM.getCodeChange().getChangedEntity().getUniqueName();
		int index = param.indexOf("(");
		param = param.substring(index);
		for (State state : statesForClassification) {
			if (param.toLowerCase().contains(state.getName().toLowerCase())) {
				return true;
			} else {
				String[] wordsSeparated = state.getName().split(" ");
				List<String> wordsSelected = removeStopWords(wordsSeparated);
				for (String word : wordsSelected) {
					if (param.toLowerCase().contains(word)) {
						log.info(word);
						return true;
					}
				}
			}
		}
		return false;
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

}
