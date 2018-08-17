package br.com.changefsm.lucene;

import java.io.IOException;
import java.util.List;

import br.com.changefsm.models.ClassChanged;

public interface IFLucene {
	
	/**
	 * Initialize the indexer of the Lucene.
	 */
	public void createInderWriter() throws IOException;
	
	/**
	 * Index the list of class in the Lucene
	 * 
	 * @param classesChanged
	 * @throws IOException
	 */
	public void indexerClass(List<ClassChanged> classesChanged) throws IOException;
	
	/**
	 * Search for class with terms of the State and Transition on StateMachine.
	 * 
	 * @param keyWords
	 * @return
	 * @throws IOException
	 */
	public List<ClassChanged> searchFilesRelated(List<String> keyWords) throws IOException;

}
