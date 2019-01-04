package br.com.changefsm.facade;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import com.itextpdf.text.DocumentException;

import br.com.changefsm.classifiersupdate.ClassifierUpdatesSM;
import br.com.changefsm.exceptions.ChangeFSMException;
import br.com.changefsm.extractor.ExtractChangesInClasses;
import br.com.changefsm.extractor.ExtractME;
import br.com.changefsm.extractor.ExtractorClasses;
import br.com.changefsm.generatorpdf.GeneratorPDF;
import br.com.changefsm.mapping.MappingChangesWithSM;
import br.com.changefsm.models.UpdateSM;
import br.com.changefsm.writerxml.WriterXML;

/**
 * This class provides the operations to its interface GUI
 * @author mathe
 *
 */
public class ChangeFSM {
	
	private ExtractorClasses extractorClasses;
	private ExtractChangesInClasses extractorChangesInClasses;
	private ExtractME extractorME;
	private MappingChangesWithSM mcwsm;
	private ClassifierUpdatesSM cusm;
	private GeneratorPDF gPDf;
	
	public ChangeFSM() {
		this.extractorClasses = new ExtractorClasses();
		this.extractorChangesInClasses = new ExtractChangesInClasses();
		this.extractorME = new ExtractME();
		this.mcwsm = new MappingChangesWithSM();
		this.cusm = new ClassifierUpdatesSM();
		this.gPDf = new GeneratorPDF();
	}
	
	public List<UpdateSM> getUpdatesSM(){
		if(cusm.getUpdates().isEmpty()) {
			return null;
		}
		return cusm.getUpdates();
	}
	
	public void identifyAndClassifyUpdateSM(String pathOld, String pathNew, String pathSM) throws ChangeFSMException, IOException {
		extractorClasses.extractClasses(pathOld, pathNew);
		if(extractorClasses.getNewJavaClasses().isEmpty() || extractorClasses.getOldJavaClasses().isEmpty()) {
			throw new ChangeFSMException("CHANGEFSM DID NOT find classes in the selected directories!");
		}
		
		extractorChangesInClasses.extractChanges(extractorClasses.getOldJavaClasses(), extractorClasses.getNewJavaClasses());
		if(extractorChangesInClasses.getClassesChanged().isEmpty()) {
			throw new ChangeFSMException("CHANGEFSM DID NOT find any source code changes!");
		}
		
		extractorME.extractElementsSM(pathSM);
		if(extractorME.getStateMachine().getStates().isEmpty()) {
			throw new ChangeFSMException("CHANGEFSM DID NOT find any states in XML!");
		}
		
		mcwsm.mappingClassWithLucene(extractorChangesInClasses.getClassesChanged(), extractorME.getStateMachine());
		if(mcwsm.getCandidateCodeClasses().isEmpty()) {
			throw new ChangeFSMException("CHANGEFSM DID NOT find any candidate classes!");
		}
		
		cusm.searchAndClassifySMUpdates(mcwsm.getCandidateCodeClasses(), extractorME.getStateMachine());
		if(cusm.getUpdates().isEmpty()) {
			throw new ChangeFSMException("CHANGEFSM DID NOT find any possible update to do on SM");
		}
	}
	
	public void generatePDF(String path) throws FileNotFoundException, DocumentException {
		gPDf.openPdfWriter(path);
		gPDf.generatePDFUpdates( (ArrayList<UpdateSM>) cusm.getUpdates() ); //getUpdates return a <LIST> 
		gPDf.closePdfWriter();
	}
	
	public void generateXML(String path) throws JAXBException {
		WriterXML.writerXML(cusm.getUpdates(), path);
	}
}
