package br.com.changefsm.models;

public class StateAction {
	
	private TypeStateAction typeStateAction;
	private String name;
	
	public StateAction() {}
	
	public StateAction(TypeStateAction typeStateAction, String name) {
		this.typeStateAction = typeStateAction;
		this.name = name;
	}
	
	public StateAction(String name) {
		this.name = name;
	}
	
	public TypeStateAction getTypeStateAction() {
		return typeStateAction;
	}
	public void setType(TypeStateAction typeStateAction) {
		this.typeStateAction = typeStateAction;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return "Action: " + this.name + " / Type: " + this.typeStateAction;
	}

}
