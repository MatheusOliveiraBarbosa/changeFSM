package br.com.changefsm;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import br.com.changefsm.models.State;

public class Main {

	private static final String PATH_PROJECT_OLD = "./data/design-pattern-indeepth-old/";
	private static final String PATH_PROJECT_NEW = "./data/design-pattern-indeepth-new/";
	private static final String PATH_XMI = "./data/status-smarthome.xml";
	private static final String TAG_STATE = "subvertex";
	private static final String TYPE_STATE = "uml:State";

	private static ArrayList<State> states = new ArrayList<State>();
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
		ecc.extractChanges(classesOld, classesNew);

		//Extract Elements in SM
		String pathME = "./data/status-smarthome.xml";
		ExtractME eme = new ExtractME();
		eme.extractElementsSM(pathME);
		System.out.println(eme.printTransitions());
		
//		filterClasses();

	}

	/**
	 * This method is responsible for filter the classes that has in its body the
	 * word equals the name of the states in the state machine, and for last, update
	 * 
	 * @classesNew and @classesOld with those files.
	 * 
	 */
	private static void filterClasses() {
		ArrayList<File> clNew = new ArrayList<File>();
		ArrayList<File> clOld = new ArrayList<File>();
		Scanner scanner;
		for (File file : classesOld) {
			try {
				scanner = new Scanner(file);
				while (scanner.hasNext()) {
					String line = scanner.nextLine();
					for (State state : states) {
						if (line.contains( state.getName() ) && !clOld.contains(file) ) {
							clOld.add(file);
							break;
						}
					}
				}

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

		}

		for (File file : classesNew) {
			try {
				scanner = new Scanner(file);
				while (scanner.hasNext()) {
					String line = scanner.nextLine();
					for (State state : states) {
						if (line.contains( state.getName() ) && !clNew.contains(file) ) {
							clNew.add(file);
							break;
						}
					}
				}

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

		}

		classesNew = clNew;
		classesOld = clOld;

	}


}
