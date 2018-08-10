package br.com.changefsm;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import br.com.changefsm.models.ClassChanged;
import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;


public class Main {

	private static final String PATH_PROJECT_OLD = "./data/DESystem-old/";
	private static final String PATH_PROJECT_NEW = "./data/DESystem-new/";
	private static final String PATH_SM = "./data/statemachines/gumballmachine1.xml";

	private static ArrayList<File> classesOld = new ArrayList<File>();
	private static ArrayList<File> classesNew = new ArrayList<File>();

	public static void main(String[] args) {
	
		// Extract the classes for the both versions
		ExtractorClasses ec = new ExtractorClasses();
		ec.extractClasses(PATH_PROJECT_OLD, PATH_PROJECT_NEW);
		classesNew = ec.getNewJavaClasses();
		classesOld = ec.getOldJavaClasses();
		
		//Extract changes and their classes
		ExtractChangesInClasses ecc = new ExtractChangesInClasses();
		List<ClassChanged> scc = ecc.extractChanges(classesOld, classesNew);
	
		
		//Extract Elements in SM
		ExtractME eme = new ExtractME();
		eme.extractElementsSM(PATH_SM);
		System.out.println(eme.printTransitions());
		
		/* Mapped between Class and StateMachine
		 * but keep in the array just classes and their changes
		 * where the class has some relation with state machine
		 */
		MappingChangesWithSM mcwsm = new MappingChangesWithSM();
		mcwsm.mappingClassWithSM(scc, eme.getStates(), eme.getTransitions());
		
		//Classify Changes and Updates
		ClassifierUpdatesSM cusm = new ClassifierUpdatesSM();
		cusm.searchAndClassifySMUpdates(mcwsm.getCandidateCodeClasses());
		
		
	}
}
