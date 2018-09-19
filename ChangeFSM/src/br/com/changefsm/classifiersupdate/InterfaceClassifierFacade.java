package br.com.changefsm.classifiersupdate;

import java.util.List;

import br.com.changefsm.models.State;
import br.com.changefsm.models.UpdateSM;

public interface InterfaceClassifierFacade {
	
	void classifyByIF(UpdateSM updateSM, List<State> statesForClassification);
	
	void classifyByELSE(UpdateSM updateSM, List<State> statesForClassification);
	
	void classifyByEnum(UpdateSM updateSM, List<State> statesForClassification);
	
	void classifyByScapeReturn(UpdateSM updateSM, List<State> statesForClassification);
	
	List<UpdateSM> classifyByMethod(UpdateSM updateSM, List<State> statesForClassification);
	
	void classifyByMethodCall(UpdateSM updateSM, List<State> statesForClassification);
	
	void classifyByAssignemt(UpdateSM updateSM, List<State> statesForClassification);

}
