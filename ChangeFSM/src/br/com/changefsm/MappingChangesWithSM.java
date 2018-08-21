package br.com.changefsm;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.changefsm.lucene.IFLucene;
import br.com.changefsm.lucene.Lucene;
import br.com.changefsm.models.ClassChanged;
import br.com.changefsm.models.State;
import br.com.changefsm.models.StateAction;
import br.com.changefsm.models.StateType;
import br.com.changefsm.models.Transition;

public class MappingChangesWithSM {

	private List<ClassChanged> candidateCodeClasses;
	private String[] stopWords = { "of", "the", "set", "get", "a", "an", "with", "to", "=", "true", "false", "state", "", " " };
	private final Logger log = LogManager.getLogger(MappingChangesWithSM.class);
	
	private final String SPACE = " ";

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
						if (state.getStateType() == StateType.NORMAL) {
							identifyRelatedClasses(classChange, line, state);
						}
					}
				}
				scanner.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	private void identifyRelatedClasses(ClassChanged classChange, String line, State state) {
		String[] wordsInState = state.getName().split(SPACE);
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
				System.out.println("Estados: " + word + " || " + line);
				this.candidateCodeClasses.add(classChange);
			}
		}
	}

	/**
	 * Make a relation between Changed classes and States and Transitions, using Lucene.
	 * 
	 * @param classesChanged
	 * @param states
	 * @param transitions
	 * @throws IOException
	 */
	public void mappingClassWithLucene(List<ClassChanged> classesChanged, List<State> states,
			List<Transition> transitions) throws IOException {
		IFLucene lucene = new Lucene();
		lucene.createInderWriter();
		lucene.indexerClass(classesChanged);

		ArrayList<String> keyWords = new ArrayList<String>();
		keyWords.addAll(extractNamesByStates(states));
		keyWords.addAll(extractNamesByTransitions(transitions));
		log.info("The keywords separate are: " + keyWords);

		List<ClassChanged> relatedClasses = lucene.searchFilesRelated(keyWords);
		log.info("The classes find by Lucene: "+ relatedClasses);
		for (ClassChanged relatedClassesByLucene : relatedClasses) {
			for (ClassChanged classChanged : classesChanged) {
				if(classChanged.getClassFile().getPath().equals(relatedClassesByLucene.getClassFile().getPath())) {
					this.candidateCodeClasses.add(classChanged);
					break;
				}
			}
		}
		

	}
	/**
	 * Make a relation between Changed classes and States, using Lucene.
	 * @param classesChanged
	 * @param states
	 * @throws IOException
	 */
	public void mappingClassWithLucene(List<ClassChanged> classesChanged, List<State> states) throws IOException {
		IFLucene lucene = new Lucene();
		lucene.createInderWriter();
		lucene.indexerClass(classesChanged);

		ArrayList<String> keyWords = new ArrayList<String>();
		keyWords.addAll(extractNamesByStates(states));
		log.info("The keywords separate are: " + keyWords);

		List<ClassChanged> relatedClasses = lucene.searchFilesRelated(keyWords);
		log.info("The classes find by Lucene: "+ relatedClasses);
		for (ClassChanged relatedClassesByLucene : relatedClasses) {
			for (ClassChanged classChanged : classesChanged) {
				if(classChanged.getClassFile().getPath().equals(relatedClassesByLucene.getClassFile().getPath())) {
					this.candidateCodeClasses.add(classChanged);
					break;
				}
			}
		}
		

	}


	/**
	 * 
	 * @param transitions
	 * @return
	 */
	private List<String> extractNamesByTransitions(List<Transition> transitions) {
		ArrayList<String> keyWordsTransition = new ArrayList<String>();
		
		for (Transition transition : transitions) {
			keyWordsTransition.addAll(removeStopWords(transition.getAction().split(SPACE)));
			keyWordsTransition.addAll(removeStopWords(transition.getGuard().split(SPACE)));
			keyWordsTransition.addAll(removeStopWords(transition.getEvent().split(SPACE)));
		}
		log.info("The keywords by transitions are: " + keyWordsTransition);

		return keyWordsTransition;
	}

	/**
	 * Extract the words has in States of the StateMachine
	 * 
	 * @param states
	 * @return List of String that are words in States
	 */
	private List<String> extractNamesByStates(List<State> states) {
		ArrayList<String> keyWordsState = new ArrayList<String>();
		for (State state : states) {
			if(state.getStateType() == StateType.NORMAL) {
				keyWordsState.addAll(removeStopWordsByState(state));
			}
		}
		log.info("The keywords by states are: " + keyWordsState);

		return keyWordsState;
	}

	/**
	 * Selected a state to remove the stopWords presents in the name
	 * and the actions of the State
	 * 
	 * @param state
	 * @return List of String that are the words are in one state
	 */
	private ArrayList<String> removeStopWordsByState(State state) {
		ArrayList<String> wordsSelected = new ArrayList<String>();
		String[] wordToWordByState = state.getName().split(SPACE);
		wordsSelected.addAll(removeStopWords(wordToWordByState));
		
		for (StateAction stateAction : state.getActions()) {
			String[] wordToWordBySAction = stateAction.getName().split(SPACE);
			wordsSelected.addAll(removeStopWords(wordToWordBySAction));
		}
		return wordsSelected;
	}

	/**
	 * Remove the stopwords presents in Array, and return
	 * a list with only selected words.
	 * 
	 * @param wordToWord
	 * @return List of String without stopwords
	 */
	private ArrayList<String> removeStopWords(String[] wordToWord) {
		ArrayList<String> wordsSelected = new ArrayList<String>();
		for (String word : wordToWord) {
			boolean hasStopWord = false;
			log.info("Verifying weather is a stopword: " + word);
			for (String stopWord : this.stopWords) {
				if(word.toLowerCase().equals(stopWord.toLowerCase())) {
					hasStopWord = true;
					break;
				}
			}
			if(!hasStopWord) {
				wordsSelected.add(word);
			}
		}
		return wordsSelected;
	}

	// Getters and Setters
	public List<ClassChanged> getCandidateCodeClasses() {
		return this.candidateCodeClasses;
	}

	public void setCandidateCodeClasses(List<ClassChanged> candidateCodeClasses) {
		this.candidateCodeClasses = candidateCodeClasses;
	}

}
