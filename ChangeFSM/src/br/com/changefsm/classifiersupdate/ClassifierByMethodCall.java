package br.com.changefsm.classifiersupdate;

import java.util.ArrayList;
import java.util.List;

import br.com.changefsm.models.State;
import br.com.changefsm.models.StateAction;
import br.com.changefsm.models.Transition;
import br.com.changefsm.models.TypeStateAction;
import br.com.changefsm.models.UpdateSM;
import br.com.changefsm.models.UpdateSMType;
import ch.uzh.ifi.seal.changedistiller.model.entities.Delete;
import ch.uzh.ifi.seal.changedistiller.model.entities.Insert;
import ch.uzh.ifi.seal.changedistiller.model.entities.Update;

public class ClassifierByMethodCall extends ClassifierUpdate implements InterfaceClassifierByMethodCall {

//	 private static final Logger log = LogManager.getLogger(ClassifierByMethodCall.class);

	@Override
	public void classifyByMethodCall(UpdateSM updateSM, List<State> statesForClassification) {
		if (updateSM.getCodeChange() instanceof Insert) { // INSERT
			if (hasStateInParameters(updateSM.getCodeChange().getChangedEntity().getUniqueName(),
					statesForClassification)) {
				updateSM.setUpdateSMType(UpdateSMType.ADD_TRANSITION);
			} // TO-DO INSERT STATE'S ACTIONS AND TRANSITION'S ACTIONS

		} else if (updateSM.getCodeChange() instanceof Delete) { // DELETE
			if (isTransitionAction(updateSM)) {
				updateSM.setUpdateSMType(UpdateSMType.REMOVE_ACTION);
				if (hasStateInParameters(updateSM.getCodeChange().getChangedEntity().getUniqueName(),
						statesForClassification)) {
					updateSM.setUpdateSMType(UpdateSMType.REMOVE_TRANSITION);
				}
			} else {
				classifyTypeStateAction(updateSM);
			}
		} else if (updateSM.getCodeChange() instanceof Update) { // TO-DO UPDATE
			if (isTransitionAction(updateSM)) {
				if (hasStateInParameters(updateSM.getCodeChange().getChangedEntity().getUniqueName(),
						statesForClassification)) {
					if (hasStateInParameters(((Update) updateSM.getCodeChange()).getNewEntity().getUniqueName(),
							statesForClassification)) {
						updateSM.setUpdateSMType(UpdateSMType.UPDATE_TRANSITION);
					} else {
						updateSM.setUpdateSMType(UpdateSMType.REMOVE_TRANSITION);
					}
				} else if (hasStateInParameters(((Update) updateSM.getCodeChange()).getNewEntity().getUniqueName(),
						statesForClassification)) {
					updateSM.setUpdateSMType(UpdateSMType.ADD_TRANSITION);
				} else {
					updateSM.setUpdateSMType(UpdateSMType.UPDATE_ACTION);
				}

			} else if (hasStateInParameters(((Update) updateSM.getCodeChange()).getNewEntity().getUniqueName(),
					statesForClassification)) {
				updateSM.setUpdateSMType(UpdateSMType.ADD_TRANSITION);
			} else {
				classifyTypeStateAction(updateSM);
			}
		}
	}

	private void classifyTypeStateAction(UpdateSM updateSM) {
		for (State state : updateSM.getStateMachine().getStates()) {
			for (StateAction stateAction : state.getActions()) {
				String nameAction = findAndRemoveSpecialCharacter(stateAction.getName()).toLowerCase();
				String nameMethod = extractObjectWithNameMethod(updateSM).toLowerCase();
				if (nameAction.split(" ").length > 1) {
					String[] wordsSeparated = nameAction.split(" ");
					List<String> wordsSelected = removeStopWords(wordsSeparated);
					for (String word : wordsSelected) {
						if (nameMethod.contains(word)) {
							classifyByStateAction(updateSM, stateAction.getTypeStateAction());
						}
					}
				} else {
					if (nameMethod.contains(nameAction)) {
						classifyByStateAction(updateSM, stateAction.getTypeStateAction());
					}
				}
			}
		}
	}

	private void classifyByStateAction(UpdateSM updateSM, TypeStateAction stateAction) {
		if (stateAction.equals(TypeStateAction.DO)) {
			if (updateSM.getCodeChange() instanceof Update) {
				updateSM.setUpdateSMType(UpdateSMType.UPDATE_DOACTION_STATE);
			} else if (updateSM.getCodeChange() instanceof Delete) {
				updateSM.setUpdateSMType(UpdateSMType.REMOVE_DOACTION_STATE);
			}
		} else if (stateAction.equals(TypeStateAction.ENTRY)) {
			if (updateSM.getCodeChange() instanceof Update) {
				updateSM.setUpdateSMType(UpdateSMType.UPDATE_ENTRYACTION_STATE);
			} else if (updateSM.getCodeChange() instanceof Delete) {
				updateSM.setUpdateSMType(UpdateSMType.REMOVE_ENTRYACTION_STATE);
			}
		} else if (stateAction.equals(TypeStateAction.EXIT)) {
			if (updateSM.getCodeChange() instanceof Update) {
				updateSM.setUpdateSMType(UpdateSMType.UPDATE_EXITACTION_STATE);
			} else if (updateSM.getCodeChange() instanceof Delete) {
				updateSM.setUpdateSMType(UpdateSMType.REMOVE_EXITACTION_STATE);
			}
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
		nameMethod = nameMethod.replaceAll("\\([a-zA-Z0-9\\W]+\\);", " ");
		return nameMethod;
	}

	private boolean hasStateInParameters(String nameMethod, List<State> statesForClassification) {
		int index = nameMethod.indexOf("(");
		nameMethod = nameMethod.substring(index);
		for (State state : statesForClassification) {
			if (nameMethod.toLowerCase().contains(state.getName().toLowerCase())) {
				return true;
			} else {
				String[] wordsSeparated = state.getName().split(" ");
				List<String> wordsSelected = removeStopWords(wordsSeparated);
				for (String word : wordsSelected) {
					if (nameMethod.toLowerCase().contains(word)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private List<String> removeStopWords(String[] words) {
		/* TRANFORMAR EM EXPRESSÃO REGULAR */
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
