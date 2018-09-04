package br.com.changefsm.classifiersupdate;

import br.com.changefsm.models.UpdateSM;
import br.com.changefsm.models.UpdateSMType;

public class ClassifierByMethodCall extends ClassifierUpdate implements InterfaceClassifierByMethodCall {
	

	@Override
	public void classifyByMethodCall(UpdateSM updateSM) {
		if (updateSM.getCodeChange().toString().startsWith(getUPDATE())) {
			updateSM.setUpdateSMType(UpdateSMType.UPDATE_ACTION_STATE);
		} else if (updateSM.getCodeChange().toString().startsWith(getDELETE())) {
			updateSM.setUpdateSMType(UpdateSMType.REMOVE_ACTION_STATE);
		} else if (updateSM.getCodeChange().toString().startsWith(getINSERT())) {
			updateSM.setUpdateSMType(UpdateSMType.ADD_ACTION_STATE);
		}
	}

}
