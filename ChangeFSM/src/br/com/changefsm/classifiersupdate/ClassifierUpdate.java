package br.com.changefsm.classifiersupdate;

/**
 * This class export Strings to ClassifiersClasses for identify 
 * if changes is a UPDATE or DELETE or INSERT
 * @author mathe
 *
 */
public class ClassifierUpdate {
	
	private final String UPDATE = "Update";
	private final String DELETE = "Delete";
	private final String INSERT = "Insert";
	
	public String getUPDATE() {
		return UPDATE;
	}

	public String getDELETE() {
		return DELETE;
	}

	public String getINSERT() {
		return INSERT;
	}

}
