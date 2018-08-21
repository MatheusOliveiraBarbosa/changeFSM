package br.com.changefsm.models;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;

public class ClassChanged {
	
	private File classFile;
	private List<SourceCodeChange> changes;
	
	public ClassChanged() {
		changes = new ArrayList<SourceCodeChange>();
	}
	
	public ClassChanged(File className, List<SourceCodeChange> changes) {
		this.classFile = className;
		this.changes = changes;
	}
	
	public File getClassFile() {
		return classFile;
	}
	public void setClassFile(File classFile) {
		this.classFile = classFile;
	}
	public List<SourceCodeChange> getChanges() {
		return changes;
	}
	public void setChanges(List<SourceCodeChange> changes) {
		this.changes = changes;
	}
	
	@Override
	public String toString() {
		return getClassFile().getName();
	}

}
