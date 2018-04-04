package br.com.changefsm.models;

public class Transition {
	
	private String id;
	private String name;
	private State source;
	private State target;
	
	
	public Transition () {	}
	
	public Transition(String id, String name, State source, State target) {
		this.id= id;
		this.name = name;
		this.source = source;
		this.target = target;
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
	public State getTarget() {
		return target;
	}
	public void setTarget(State target) {
		this.target = target;
	}
	public State getSource() {
		return source;
	}
	public void setSource(State source) {
		this.source = source;
	}
	
	@Override
	public String toString() {
		return this.name;
	}

}
