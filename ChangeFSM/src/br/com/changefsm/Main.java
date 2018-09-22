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
import br.com.changefsm.models.UpdateSM;

public class Main {

	private static final String PATH_PROJECT_OLD = "./data/DESystem-old/";
	private static final String PATH_PROJECT_NEW = "./data/DESystem-new/";
//	private static final String PATH_SM = "./data/statemachines/carbuttoncontrol_state_diagram.xml";
//	private static final String PATH_SM = "./data/statemachines/gumballmachine1.xml";
	private static final String PATH_SM = "./data/statemachines/dispatcherl_state_diagram.xml";

	private static ArrayList<File> classesOld = new ArrayList<File>();
	private static ArrayList<File> classesNew = new ArrayList<File>();
	
	private static final Logger log = LogManager.getLogger(Main.class);
	
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
//			WriterXML.writerXML(cusm.getUpdates());
//		} catch (JAXBException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		GeneratorPDF gPDf = new GeneratorPDF();
		try {
			ArrayList<UpdateSM> updates = (ArrayList<UpdateSM>) cusm.getUpdates();
			gPDf.generatePDFUpdates(updates);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
		
	}
}
