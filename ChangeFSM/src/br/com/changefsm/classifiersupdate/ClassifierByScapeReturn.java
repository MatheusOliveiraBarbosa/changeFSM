package br.com.changefsm.classifiersupdate;

import java.util.List;

import br.com.changefsm.models.State;
import br.com.changefsm.models.UpdateSM;
import br.com.changefsm.models.UpdateSMType;
import ch.uzh.ifi.seal.changedistiller.model.entities.Delete;
import ch.uzh.ifi.seal.changedistiller.model.entities.Insert;
import ch.uzh.ifi.seal.changedistiller.model.entities.Update;

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
				if (indicateComposite > 1 && updateSM.getCodeChange() instanceof Insert) {
					updateSM.setUpdateSMType(UpdateSMType.ADD_COMPOSITE_STATE);
				} else if (indicateComposite > 1 && updateSM.getCodeChange() instanceof Delete) {
					updateSM.setUpdateSMType(UpdateSMType.REMOVE_COMPOSITE_STATE);
				} else if (indicateComposite > 1 && updateSM.getCodeChange() instanceof Update) {
					analyzeUpdateReturn(updateSM, statesForClassification);
					
				}
			}
		}
		if (updateSM.getCodeChange() instanceof Update) {
			createUpdateReturn(updateSM, statesForClassification);
		}
	}

	private void createUpdateReturn(UpdateSM updateSM, List<State> statesForClassification) {
		String returnsValues = ((Update)updateSM.getCodeChange()).getNewEntity().getUniqueName();
		int indicateComposite = 0;
		for (State state : statesForClassification) {
			if (returnsValues.toLowerCase().contains(state.getName().toLowerCase())) {
				indicateComposite++;
				if(indicateComposite>1) {
					updateSM.setUpdateSMType(UpdateSMType.ADD_COMPOSITE_STATE);
					break;
				}
			}
		}
		
	}

	private void analyzeUpdateReturn(UpdateSM updateSM, List<State> statesForClassification) {
		String returnsValues = ((Update)updateSM.getCodeChange()).getNewEntity().getUniqueName();
		int indicateComposite = 0;
		for (State state : statesForClassification) {
			if (returnsValues.toLowerCase().contains(state.getName().toLowerCase())) {
				indicateComposite++;
				if(indicateComposite>1) {
					updateSM.setUpdateSMType(UpdateSMType.ALTER_BODY_COMPOSITE_STATE);
				}
			}
		}
		if(indicateComposite == 0) {
			updateSM.setUpdateSMType(UpdateSMType.REMOVE_COMPOSITE_STATE);
		}
	}

}
