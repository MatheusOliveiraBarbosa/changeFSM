package br.com.changefsm.models;

import java.io.File;

import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;

public class UpdateSM {
	
	private StateMachine stateMachine;
	private SourceCodeChange codeChange;
	private File classJava;
	private UpdateSMType updateSMType;
	
	public UpdateSM() {}
	
	public UpdateSM(File classJava, SourceCodeChange codeChange, StateMachine stateMachine) {
		this.classJava = classJava;
		this.codeChange = codeChange;
		this.stateMachine = stateMachine;
	}
	
	public StateMachine getStateMachine() {
		return stateMachine;
	}
	public void setStateMachine(StateMachine stateMachine) {
		this.stateMachine = stateMachine;
	}
	public SourceCodeChange getCodeChange() {
		return codeChange;
	}
	public void setCodeChange(SourceCodeChange codeChange) {
		this.codeChange = codeChange;
	}
	public File getClassJava() {
		return classJava;
	}
	public void setClassJava(File classJava) {
		this.classJava = classJava;
	}
	public UpdateSMType getUpdateSMType() {
		return updateSMType;
	}
	public void setUpdateSMType(UpdateSMType updateSMType) {
		this.updateSMType = updateSMType;
	}
	
	@Override
	public String toString() {
		return "The update in SM is: " + this.updateSMType + 
				" || the State Machine is: " + stateMachine  + 
				" || the Source Code Class is: " + this.classJava + 
				" || the Source Code Change is: " + this.codeChange ;
	}

}
