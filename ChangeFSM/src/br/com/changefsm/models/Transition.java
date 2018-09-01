package br.com.changefsm.models;

public class Transition {
	
	private String id;
	private String action;
	private String guard;
	private String event;
	private State sourceState;
	private State targetState;
	
	
	public Transition () {
		this.event = "";
		this.guard = "";
		this.action = "";
	}
	
	public Transition(String action, State source, State target) {
		this.action = action;
		this.sourceState = source;
		this.targetState = target;
		this.event = "";
		this.guard = "";
	}
	
	public Transition(String id, String action, State source, State target) {
		this.id= id;
		this.event = "";
		this.guard = "";
		this.action = action;
		this.sourceState = source;
		this.targetState = target;
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
		return targetState;
	}
	public void setTarget(State target) {
		this.targetState = target;
	}
	public State getSource() {
		return sourceState;
	}
	public void setSource(State source) {
		this.sourceState = source;
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
