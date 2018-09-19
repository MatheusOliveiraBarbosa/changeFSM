package br.com.changefsm.classifiersupdate;

import java.util.List;

import br.com.changefsm.models.State;
import br.com.changefsm.models.UpdateSM;

public interface InterfaceClassifierByIF {
	
	void classifyByIF(UpdateSM updateSM, List<State> statesForClassification);
	
	void classifyByELSE(UpdateSM updateSM, List<State> statesForClassification);

}
