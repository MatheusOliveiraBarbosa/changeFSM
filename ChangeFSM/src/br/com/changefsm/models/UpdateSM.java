package br.com.changefsm.models;

import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;

public class UpdateSM {
	
	private StateMachine stateMachine;
	private SourceCodeChange changeType;
	private String className;
	private UpdateSMType updateSMType;
	
	public UpdateSM() {}
	
	public UpdateSM(String className, SourceCodeChange change, StateMachine stateMachine) {
		this.className = className;
		this.changeType = change;
		this.stateMachine = stateMachine;
	}
	
	public StateMachine getStateMachine() {
		return stateMachine;
	}
	public void setStateMachine(StateMachine stateMachine) {
		this.stateMachine = stateMachine;
	}
	public SourceCodeChange getChangeType() {
		return changeType;
	}
	public void setChangeType(SourceCodeChange changeType) {
		this.changeType = changeType;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
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
				" || the Source Code Class is: " + this.className + 
				" || the Source Code Change is: " + this.changeType ;
	}

}
