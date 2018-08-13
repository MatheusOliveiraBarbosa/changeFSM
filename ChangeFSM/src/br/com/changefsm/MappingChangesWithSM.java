package br.com.changefsm;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import br.com.changefsm.models.ClassChanged;
import br.com.changefsm.models.State;
import br.com.changefsm.models.StateType;
import br.com.changefsm.models.Transition;

public class MappingChangesWithSM {

	private List<ClassChanged> candidateCodeClasses;
	private String[] stopWords = { "of", "the", "set", "a", "an", "with", "to" };

	public MappingChangesWithSM() {
		this.candidateCodeClasses = new ArrayList<ClassChanged>();
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
						if(state.getStateType() == StateType.NORMAL) {
							identifyRelatedClasses(classChange, line, state);
						}
					}
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	private void identifyRelatedClasses(ClassChanged classChange, String line, State state) {
		String[] wordsInState = state.getName().split(" ");
		ArrayList<String> wos = new ArrayList<String>();
		boolean hasStop = false;
		for (int i = 0; i < wordsInState.length; i++) {
			for (int j = 0; j < stopWords.length; j++) {
				if (wordsInState[i].toLowerCase().equals(stopWords[j].toLowerCase())) {
					hasStop = true;
				}
			}
			if (!hasStop) {
				wos.add(wordsInState[i]);
			}
			hasStop = false;
		}
		for (String word : wos) {
			if (line.contains(word) && !this.candidateCodeClasses.contains(classChange)) {
				System.out.println("Estados: " + word +" || " + line);
				this.candidateCodeClasses.add(classChange);
			}
		}
	}

	// Getters and Setters
	public List<ClassChanged> getCandidateCodeClasses() {
		return this.candidateCodeClasses;
	}

	public void setCandidateCodeClasses(List<ClassChanged> candidateCodeClasses) {
		this.candidateCodeClasses = candidateCodeClasses;
	}

}
