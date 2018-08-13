package br.com.changefsm.models;

import java.util.ArrayList;

public class State {
	
	private String name;
	private String id;
	private StateType stateType;
	private ArrayList<StateAction> actions;

	public State() {}
	
	public State(String name) {
		this.name = name;
	}
	
	public State(String id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public State(String id, String name, StateType stateType) {
		this.id = id;
		this.name = name;
		this.stateType = stateType;
	}
	
	public State (String id, String name, ArrayList<StateAction> actions) {
		this.id = id;
		this.name = name;
		this.actions = actions;
	}
	
	public State (String id, String name, ArrayList<StateAction> actions, StateType stateType) {
		this.id = id;
		this.name = name;
		this.actions = actions;
		this.stateType = stateType;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return this.name;
	}

	public ArrayList<StateAction> getActions() {
		return actions;
	}

	public void setActions(ArrayList<StateAction> actions) {
		this.actions = actions;
	}

	public StateType getStateType() {
		return stateType;
	}

	public void setStateType(StateType stateType) {
		this.stateType = stateType;
	}
	
	
}
