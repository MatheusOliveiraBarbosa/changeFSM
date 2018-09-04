package br.com.changefsm.classifiersupdate;

import br.com.changefsm.models.UpdateSM;
import br.com.changefsm.models.UpdateSMType;

public class ClassifierByMethod extends ClassifierUpdate implements InterfaceClassifierByMethod {

	@Override
	public void classifyByMethod(UpdateSM updateSM) {
		if (updateSM.getCodeChange().toString().startsWith(getINSERT())
				&& updateSM.getCodeChange().getLabel().equals("ADDITIONAL_FUNCTIONALITY")) {
			updateSM.setUpdateSMType(UpdateSMType.ADD_EVENT);
		} else if (updateSM.getCodeChange().toString().startsWith(getDELETE())
				&& updateSM.getCodeChange().getLabel().equals("REMOVED_FUNCTIONALITY")) {
			updateSM.setUpdateSMType(UpdateSMType.REMOVE_EVENT);
		}
	}

}
