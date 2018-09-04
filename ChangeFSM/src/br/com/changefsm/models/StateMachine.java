package br.com.changefsm.models;

import java.util.ArrayList;
import java.util.List;

public class StateMachine {
	
	private List<State> states;
	private List<Transition> transitions;
	private String name;
	
	public StateMachine() {
		states = new ArrayList<State>();
		transitions = new ArrayList<Transition>();
	}
	
	public StateMachine(String name) {
		this.name = name;
		states = new ArrayList<State>();
		transitions = new ArrayList<Transition>();
	}
	
	public StateMachine(String name, List<State> states, List<Transition> transitions) {
		this.name = name;
		this.states = states;
		this.transitions = transitions;
	}
	
	//Getters and Setters
	public List<State> getStates() {
		return states;
	}

	public void setStates(List<State> states) {
		this.states = states;
	}

	public List<Transition> getTransitions() {
		return transitions;
	}

	public void setTransitions(List<Transition> transitions) {
		this.transitions = transitions;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}

}
