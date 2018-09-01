package br.com.changefsm.writerxml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.changefsm.models.StateMachine;

@XmlRootElement(name="UpdateSMToXML")
public class UpdateSMToXML {
	
	@XmlAttribute
	private int updateSMId;
	
	private StateMachine stateMachine;
	private String codeChange;
	private String classJava;
	private String updateSMType;
	
	public UpdateSMToXML() {}
	
	public UpdateSMToXML(int id, StateMachine stateMachine, String codeChange, String classJava, String updateSMType) {
		this.updateSMId = id;
		this.stateMachine = stateMachine;
		this.codeChange = codeChange;
		this.classJava = classJava;
		this.updateSMType = updateSMType;
	}

	public StateMachine getStateMachine() {
		return stateMachine;
	}

	public void setStateMachine(StateMachine stateMachine) {
		this.stateMachine = stateMachine;
	}

	public String getCodeChange() {
		return codeChange;
	}

	public void setCodeChange(String codeChange) {
		this.codeChange = codeChange;
	}

	public String getClassJava() {
		return classJava;
	}

	public void setClassJava(String classJava) {
		this.classJava = classJava;
	}

	public String getUpdateSMType() {
		return updateSMType;
	}

	public void setUpdateSMType(String updateSMType) {
		this.updateSMType = updateSMType;
	}

}
