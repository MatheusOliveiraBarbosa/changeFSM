package br.com.changefsm.writerxml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement (name="UpdatesSMToXML")
public class UpdatesSMToXML {
	
	private List<UpdateSMToXML> updatesSMToXML;
	

	public UpdatesSMToXML() {
		updatesSMToXML = new ArrayList<UpdateSMToXML>();
	}
	
	public List<UpdateSMToXML> getUpdatesSMToXML() {
		return updatesSMToXML;
	}

	@XmlElement(name="updateSMToXML")
	public void setUpdatesSMToXML(List<UpdateSMToXML> updatesSMToXML) {
		this.updatesSMToXML = updatesSMToXML;
	}
	
	public void add(UpdateSMToXML updateSMToXML) {
		this.updatesSMToXML.add(updateSMToXML);
	}
	

}
