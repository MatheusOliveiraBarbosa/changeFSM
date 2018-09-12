package br.com.changefsm.classifiersupdate;

import java.util.List;

import br.com.changefsm.models.State;
import br.com.changefsm.models.UpdateSM;

public interface InterfaceClassifierByMethod {
	
	public List<UpdateSM> classifyByMethod(UpdateSM updateSM, List<State> statesForClassification);

}
