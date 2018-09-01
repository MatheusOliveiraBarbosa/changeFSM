package br.com.changefsm.writerxml;

import java.io.File;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import br.com.changefsm.models.UpdateSM;

public class WriterXML {
	
	private static final String PATH_FILE = "./output/updatesInStateMachine.xml"; 
	
	public static void writerXML(List<UpdateSM> updatesSM) throws JAXBException {
		JAXBContext jaxbContext;
		jaxbContext = JAXBContext.newInstance(UpdatesSMToXML.class);
		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		UpdatesSMToXML updatesSMToXML = new UpdatesSMToXML();
		int idUpdate = 0;
		for (UpdateSM update : updatesSM) {
			UpdateSMToXML updateToXml = new UpdateSMToXML(idUpdate, update.getStateMachine(),
					update.getCodeChange().toString(), update.getClass().getName(), update.getUpdateSMType().name());
			idUpdate++;
			updatesSMToXML.add(updateToXml);
		}
		marshaller.marshal(updatesSMToXML, new File(PATH_FILE));
	}

}
