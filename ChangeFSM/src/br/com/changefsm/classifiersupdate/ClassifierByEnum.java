package br.com.changefsm.classifiersupdate;

import java.util.List;

import br.com.changefsm.models.State;
import br.com.changefsm.models.UpdateSM;
import br.com.changefsm.models.UpdateSMType;

public class ClassifierByEnum extends ClassifierUpdate implements InterfaceClassifierByEnum {
	
	/**
	 * Classify the update in the SM by means of changes in Enum, and add the new
	 * states to the list of the extracted states to after provide aid to find
	 * transitions and others updating.
	 * 
	 * @param updateSM
	 * @param statesForClassification
	 */
	@Override
	public void classifyByEnum(UpdateSM updateSM, List<State> statesForClassification) {

		String nameEnum = extractNameOfEnum(updateSM.getCodeChange().toString());
		boolean exist = false;
		for (State state : statesForClassification) {
			if (state.getName().contains(nameEnum)) {
				exist = true;
				break;
			}
		}
		if (updateSM.getCodeChange().getChangeType().toString().contains("ADD") && exist == false) {
			updateSM.setUpdateSMType(UpdateSMType.ADD_STATE);
			statesForClassification.add(new State(nameEnum));
		} else if (updateSM.getCodeChange().getChangeType().toString().contains("REMOVE") && exist == true) {
			updateSM.setUpdateSMType(UpdateSMType.REMOVE_STATE);
		}
	}

	/**
	 * Extracts the name of the Enum into a change
	 * 
	 * @param changeToString
	 * @return
	 */
	private String extractNameOfEnum(String changeToString) {
		int indexLast2PointsAndSpace = changeToString.lastIndexOf(" :");
		int indexLastPoint = changeToString.lastIndexOf(".");
		return changeToString.substring(indexLastPoint + 1, indexLast2PointsAndSpace);
	}

}
