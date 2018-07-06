package br.com.changefsm.models;

public class Transition {
	
	private String id;
	private String action;
	private String guard;
	private String event;
	private State source;
	private State target;
	
	
	public Transition () {	}
	
	public Transition(String id, String name, State source, State target) {
		this.id= id;
		this.action = name;
		this.source = source;
		this.target = target;
	}
	
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
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
		return this.action;
	}

	public String getGuard() {
		return guard;
	}

	public void setGuard(String guard) {
		this.guard = guard;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

}
