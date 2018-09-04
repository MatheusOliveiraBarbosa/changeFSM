package br.com.changefsm.classifiersupdate;

import br.com.changefsm.models.UpdateSM;
import br.com.changefsm.models.UpdateSMType;

public class ClassifierByIF extends ClassifierUpdate implements InterfaceClassifierByIF {

	/**
	 * Classify the updates in the SM by means of changes in IFs
	 * 
	 * @param classChanged
	 * @param stateMachine
	 * @param change
	 * @param updateSM
	 */
	@Override
	public void classifyByIF(UpdateSM updateSM) {
		if (isGuard(updateSM)) {
			if (updateSM.getCodeChange().toString().startsWith(getUPDATE())) {
				updateSM.setUpdateSMType(UpdateSMType.UPDATE_GUARD);
			} else if (updateSM.getCodeChange().toString().startsWith(getDELETE())) {
				updateSM.setUpdateSMType(UpdateSMType.REMOVE_GUARD);
			} else if (updateSM.getCodeChange().toString().startsWith(getINSERT())) {
				updateSM.setUpdateSMType(UpdateSMType.ADD_GUARD);
			}
		}
	}

	@Override
	public void classifyByELSE(UpdateSM updateSM) {
		// TODO Auto-generated method stub
	}
	
	private boolean isGuard(UpdateSM updateSM) {
		// TO-DO Verify the body if looking for the states to indicate that this
		// transition is a guard
		return true;
	}

}
