package br.com.changefsm.classifiersupdate;

import java.util.List;


import br.com.changefsm.models.State;
import br.com.changefsm.models.UpdateSM;
import br.com.changefsm.models.UpdateSMType;

public class ClassifierByAssignment extends ClassifierUpdate implements InterfaceClassifierByAssignemt {
	
//	private static final Logger log = LogManager.getLogger(ClassifierByAssignment.class);
	/**
	 * Classify the updates in the SM by means of changes in Assignment
	 * 
	 * @param updateSM
	 * @param statesForClassification
	 */
	@Override
	public void classifyByAssignemt(UpdateSM updateSM, List<State> statesForClassification) {
		if (isPossibleTransition(updateSM, statesForClassification)) {
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
		String assignemt = updateSM.getCodeChange().getChangedEntity().getUniqueName().toLowerCase();
		int indexEqual = assignemt.indexOf("=");
		assignemt = assignemt.substring(indexEqual);
		
		if(assignemt.contains("(")) {
			String parameters = extractParameters(assignemt);
			if(parameters != "") {
				isPossibleTransition = searchParamForState(parameters, statesForClassification);
			}
		}
		else {
			isPossibleTransition = hasStateInAssignemt(assignemt, statesForClassification);
		}
			
		
//		for (State state : statesForClassification) {
//			if (updateSM.getCodeChange().toString().toLowerCase().contains(state.getName().toLowerCase())) {
//				isPossibleTransition = true;
//			} else if (state.getName().split(" ").length > 1) {
//				String[] words = state.getName().split(" ");
//				List<String> wordsSeparated = removeStopWords(words);
//				for (String word : wordsSeparated) {
//					if (updateSM.getCodeChange().toString().toLowerCase().contains(word.toLowerCase())) {
//						isPossibleTransition = true;
//					}
//				}
//			}
//		}
		return isPossibleTransition;
	}

	private boolean hasStateInAssignemt(String assignemt, List<State> statesForClassification) {
		boolean hasState = false;
		for (State state : statesForClassification) {
			if(assignemt.contains(state.getName().toLowerCase())) {
				hasState = true;
			}
		}
		return hasState;
	}

	private boolean searchParamForState(String parameters, List<State> statesForClassification) {
		parameters = parameters.trim();
		String[] wordsByParam = parameters.split(",");
		for(int i = 0 ; i < wordsByParam.length ; i++) {
			for(State state : statesForClassification) {
				if (wordsByParam[i].contains(state.getName().toLowerCase())) {
					return hasStateInParam(wordsByParam[i], state.getName() );
				}
			}
		}
		return false;
	}

	private boolean hasStateInParam(String param, String name) {
		String[] words = param.split(".");
		for(int i = 0 ; i < words.length ; i++) {
			if(words[i].toLowerCase().equals(name.toLowerCase())){
				return true;
			}
		}
		return false;
	}

	private String extractParameters(String assignemt) {
		String parameters = "";
		int indexParam = assignemt.indexOf("(") + 1;
		int indexLastParam = assignemt.lastIndexOf(")");
		if(indexParam > 1 && indexLastParam > indexParam) {
			parameters = assignemt.substring(indexParam, indexLastParam);
		}
		
		return parameters;
	}

//	private List<String> removeStopWords(String[] words) {
//		String[] stopWords = { "of", "the", "set", "get", "a", "an", "with", "to", "=", "true", "false", "state", "",
//				" " };
//		List<String> separetedWords = new ArrayList<String>();
//		for (int i = 0; i < words.length; i++) {
//			boolean hasStopWord = false;
//			for (int j = 0; j < stopWords.length; j++) {
//				if (words[i].toLowerCase().equals(stopWords[j].toLowerCase())) {
//					hasStopWord = true;
//					break;
//				}
//			}
//			if (hasStopWord == false) {
//				separetedWords.add(words[i]);
//			}
//		}
//		return separetedWords;
//	}

}
