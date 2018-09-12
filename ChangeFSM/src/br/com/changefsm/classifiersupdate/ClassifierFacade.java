package br.com.changefsm.classifiersupdate;

import java.util.List;

import br.com.changefsm.models.State;
import br.com.changefsm.models.UpdateSM;

public class ClassifierFacade implements InterfaceClassifierFacade {
	
	private InterfaceClassifierByEnum classifierEnum;
	private InterfaceClassifierByIF classifierIF;
	private InterfaceClassifierByMethod classifierMethod;
	private InterfaceClassifierByMethodCall classifierMethodCall;
	private InterfaceClassifierByScapeReturn classifierScapeReturn;
	private InterfaceClassifierByAssignemt classifierAssignemt;
	
	public ClassifierFacade() {
		this.classifierEnum = new ClassifierByEnum();
		this.classifierIF = new ClassifierByIF();
		this.classifierAssignemt = new ClassifierByAssignment();
		this.classifierMethod = new ClassifierByMethod();
		this.classifierMethodCall = new ClassifierByMethodCall();
		this.classifierScapeReturn = new ClassifierByScapeReturn();
	}

	@Override
	public void classifyByIF(UpdateSM updateSM) {
		this.classifierIF.classifyByIF(updateSM);
	}

	@Override
	public void classifyByELSE(UpdateSM updateSM) {
		this.classifierIF.classifyByELSE(updateSM);
	}

	@Override
	public void classifyByEnum(UpdateSM updateSM, List<State> statesForClassification) {
		this.classifierEnum.classifyByEnum(updateSM, statesForClassification);
	}

	@Override
	public void classifyByScapeReturn(UpdateSM updateSM, List<State> statesForClassification) {
		this.classifierScapeReturn.classifyByScapeReturn(updateSM, statesForClassification);
	}

	@Override
	public List<UpdateSM> classifyByMethod(UpdateSM updateSM, List<State> statesForClassification) {
		return classifierMethod.classifyByMethod(updateSM, statesForClassification);
	}

	@Override
	public void classifyByMethodCall(UpdateSM updateSM, List<State> statesForClassification) {
		this.classifierMethodCall.classifyByMethodCall(updateSM, statesForClassification);
	}

	@Override
	public void classifyByAssignemt(UpdateSM updateSM, List<State> statesForClassification) {
		this.classifierAssignemt.classifyByAssignemt(updateSM, statesForClassification);
	}

}
