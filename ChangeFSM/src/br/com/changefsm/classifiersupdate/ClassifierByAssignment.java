package br.com.changefsm.classifiersupdate;

import java.util.ArrayList;
import java.util.List;

import br.com.changefsm.models.State;
import br.com.changefsm.models.UpdateSM;
import br.com.changefsm.models.UpdateSMType;

public class ClassifierByAssignment extends ClassifierUpdate implements InterfaceClassifierByAssignemt {
	

	/**
	 * Classify the updates in the SM by means of changes in Assignment
	 * 
	 * @param updateSM
	 * @param statesForClassification
	 */
	@Override
	public void classifyByAssignemt(UpdateSM updateSM, List<State> statesForClassification) {
		boolean isPossibleTransition = isPossibleTransition(updateSM, statesForClassification);
		if (isPossibleTransition) {
			if (updateSM.getCodeChange().toString().startsWith(getUPDATE())) {
				updateSM.setUpdateSMType(UpdateSMType.UPDATE_TRANSITION);
			} else if (updateSM.getCodeChange().toString().startsWith(getDELETE())) {
				updateSM.setUpdateSMType(UpdateSMType.REMOVE_TRANSITION);
			} else if (updateSM.getCodeChange().toString().startsWith(getINSERT())) {
				updateSM.setUpdateSMType(UpdateSMType.ADD_TRANSITION);
			}
		}
	}
	
	private boolean isPossibleTransition(UpdateSM updateSM, List<State> statesForClassification) {
		boolean isPossibleTransition = false;
		for (State state : statesForClassification) {
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

}
