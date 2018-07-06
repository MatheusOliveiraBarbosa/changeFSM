package br.com.changefsm;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import br.com.changefsm.models.ClassChanged;
import br.com.changefsm.models.State;
import br.com.changefsm.models.Transition;

public class MappingChangesWithSM {
	
	private List<ClassChanged> candidateCodeClasses;

	public MappingChangesWithSM() {
		candidateCodeClasses = new ArrayList<ClassChanged>();
	}

	/**
	 * This method is responsible for filter the classes that has in its body the
	 * word equals the name of the states in the state machine. 
	 */
	public void mappingClassWithSM(List<ClassChanged> classesChanged, List<State> states,
			List<Transition> transitions) {
		Scanner scanner;
		for (ClassChanged classChange : classesChanged) {
			try {
				scanner = new Scanner(classChange.getClassFile());
				while (scanner.hasNext()) {
					String line = scanner.nextLine();
					for (State state : states) {
						if (line.contains(state.getName()) && !getCandidateCodeClasses().contains(classChange)) {
							getCandidateCodeClasses().add(classChange);
						}
					}
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	public List<ClassChanged> getCandidateCodeClasses() {
		return candidateCodeClasses;
	}

	public void setCandidateCodeClasses(List<ClassChanged> candidateCodeClasses) {
		this.candidateCodeClasses = candidateCodeClasses;
	}
	
	

}
