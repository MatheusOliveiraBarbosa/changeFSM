package br.com.changefsm.classifiersupdate;

import java.util.List;

import br.com.changefsm.models.State;
import br.com.changefsm.models.UpdateSM;
import br.com.changefsm.models.UpdateSMType;

public class ClassifierByScapeReturn extends ClassifierUpdate implements InterfaceClassifierByScapeReturn {

	/**
	 * Analysis whether scape return has relation with the states to indicate when
	 * is a composite state or not.
	 * 
	 * @param updateSM
	 * @param statesForClassification
	 */
	@Override
	public void classifyByScapeReturn(UpdateSM updateSM, List<State> statesForClassification) {
		int indicateComposite = 0;
		for (State state : statesForClassification) {
			if (updateSM.getCodeChange().toString().toLowerCase().contains(state.getName().toLowerCase())) {
				indicateComposite++;
				if (indicateComposite > 1 && updateSM.getCodeChange().toString().startsWith(getINSERT())) {
					updateSM.setUpdateSMType(UpdateSMType.ADD_COMPOSITE_STATE);
				} else if (indicateComposite > 1 && updateSM.getCodeChange().toString().startsWith(getDELETE())) {
					updateSM.setUpdateSMType(UpdateSMType.REMOVE_COMPOSITE_STATE);
				} else if (indicateComposite > 1 && updateSM.getCodeChange().toString().startsWith(getUPDATE())) {
					updateSM.setUpdateSMType(UpdateSMType.ALTER_BODY_COMPOSITE_STATE);
				}
			}
		}
	}

}
