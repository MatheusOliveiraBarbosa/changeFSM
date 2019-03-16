package br.com.changefsm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.itextpdf.text.DocumentException;

import br.com.changefsm.classifiersupdate.ClassifierUpdatesSM;
import br.com.changefsm.extractor.ExtractChangesInClasses;
import br.com.changefsm.extractor.ExtractME;
import br.com.changefsm.extractor.ExtractorClasses;
import br.com.changefsm.generatorpdf.GeneratorPDF;
import br.com.changefsm.mapping.MappingChangesWithSM;
import br.com.changefsm.models.ClassChanged;

public class Main {

	
	/*  DESYSTEM - STATEMACHINES AND CODE  */
//	private static final String PATH_PROJECT_OLD = "./data/DESystem-old/";
//	private static final String PATH_PROJECT_NEW = "./data/DESystem-new/";
//	private static final String PATH_SM = "./data/statemachines/carbuttoncontrol_state_diagram.xml"; // OK
//	private static final String PATH_SM = "./data/statemachines/carpositionl_state_diagram.xml";  // OK
//	private static final String PATH_SM = "./data/statemachines/dispatcherl_state_diagram.xml";   // Ok
//	private static final String PATH_SM = "./data/statemachines/doorcontrol_state_diagram.xml";   // Verify
//	private static final String PATH_SM = "./data/statemachines/driveControl_state_diagram.xml"; // Verify
//	private static final String PATH_SM = "./data/statemachines/hallbutton_state_diagram.xml";  // OK
//	private static final String PATH_SM = "./data/statemachines/lanternControl_state_diagram.xml"; // Verify

	
	/*  DESING PATTERN - STATEMACHINES AND CODE */   //Verify Lucene
//	private static final String PATH_PROJECT_OLD = "./data/design-pattern-indeepth-old/";
//	private static final String PATH_PROJECT_NEW = "./data/design-pattern-indeepth-new/";
//	private static final String PATH_SM = "./data/statemachines/gumballmachine.xml";
	
	/* SMARTHOME - STATEMACHINES AND CODE */ //Verify Classification
	private static final String PATH_PROJECT_OLD = "./data/smarthome-first-version/";
	private static final String PATH_PROJECT_NEW = "./data/smarthome-master/";
	private static final String PATH_SM = "./data/statemachines/status-smarthome.xml";

	private static ArrayList<File> classesOld = new ArrayList<File>();
	private static ArrayList<File> classesNew = new ArrayList<File>();
	
	private static final Logger log = LogManager.getLogger(Main.class);
	
	public static void main(String[] args) {
	
		// Extract the classes for the both versions
		System.out.println("Initializing...");
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
		
		/* Mapped between Class and StateMachine
		 * but keep in the array just classes and their changes
		 * where the class has some relation with state machine
		 */
		MappingChangesWithSM mcwsm = new MappingChangesWithSM();
		try {
			mcwsm.mappingClassWithLucene(scc, eme.getStateMachine());
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		
//		//Classify Changes and Updates
		ClassifierUpdatesSM cusm = new ClassifierUpdatesSM();
		cusm.searchAndClassifySMUpdates(mcwsm.getCandidateCodeClasses(), eme.getStateMachine());
		
//		try {
//		String PATH_FILE = "./output/updatesInStateMachine.xml"
//			WriterXML.writerXML(cusm.getUpdates(), PATH_FILE);
//		} catch (JAXBException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		System.out.println(cusm.getUpdates().size());
		if(cusm.getUpdates().size() > 0) {
			
			GeneratorPDF gPDf = new GeneratorPDF();
			try {
				gPDf.openPdfWriter("");
				gPDf.generatePDFUpdates(cusm.getUpdates());
				gPDf.closePdfWriter();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (DocumentException e) {
				e.printStackTrace();
			}
		}
		
		
	}
}
