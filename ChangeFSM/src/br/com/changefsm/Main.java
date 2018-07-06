package br.com.changefsm;

import java.io.File;
import java.util.ArrayList;


public class Main {

	private static final String PATH_PROJECT_OLD = "./data/smarthome-first-version/";
	private static final String PATH_PROJECT_NEW = "./data/smarthome-master/";

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
//		ecc.extractChanges(classesOld, classesNew);
		
		//Extract Elements in SM
		String pathME = "./data/status-smarthome.xml";
		ExtractME eme = new ExtractME();
		eme.extractElementsSM(pathME);
		System.out.println(eme.printTransitions());
		
		/* Mapped between Class and StateMachine
		 * but keep in the array just classes and their changes
		 * where the class has some relation with state machine
		 */
		MappingChangesWithSM mcwsm = new MappingChangesWithSM();
		mcwsm.mappingClassWithSM(ecc.extractChanges(classesOld, classesNew), eme.getStates(), eme.getTransitions());
		
	}
}
