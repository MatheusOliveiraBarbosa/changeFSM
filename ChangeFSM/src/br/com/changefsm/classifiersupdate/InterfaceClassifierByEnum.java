package br.com.changefsm.classifiersupdate;

import java.util.List;

import br.com.changefsm.models.State;
import br.com.changefsm.models.UpdateSM;

public interface InterfaceClassifierByEnum {
	
	void classifyByEnum(UpdateSM updateSM, List<State> stateForClassification);

}
