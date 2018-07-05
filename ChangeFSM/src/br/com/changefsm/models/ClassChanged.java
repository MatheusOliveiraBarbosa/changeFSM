package br.com.changefsm.models;

import java.util.ArrayList;
import java.util.List;

import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;

public class ClassChanged {
	
	private String className;
	private List<SourceCodeChange> changes;
	
	public ClassChanged() {
		changes = new ArrayList<SourceCodeChange>();
	}
	
	public ClassChanged(String className, List<SourceCodeChange> changes) {
		this.className = className;
		this.changes = changes;
	}
	
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public List<SourceCodeChange> getChanges() {
		return changes;
	}
	public void setChanges(List<SourceCodeChange> changes) {
		this.changes = changes;
	}
	
	@Override
	public String toString() {
		return getClassName() +" --- Changes: " + getChanges();
	}

}
