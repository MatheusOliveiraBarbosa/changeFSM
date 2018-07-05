package br.com.changefsm;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import br.com.changefsm.utils.FileUtils;

public class ExtractorClasses {
	
	private ArrayList<File> OldJavaClasses;
	private ArrayList<File> NewJavaClasses;
	private final String FILE_EXTENSION = ".java";
	
	public ExtractorClasses() {
		OldJavaClasses = new ArrayList<File>();
		NewJavaClasses = new ArrayList<File>();
	}
	
	/**
	 * Call the two methods that will extract the Java files of the path; 
	 * @param pathOld
	 * @param pathNew
	 */
	public void extractClasses(String pathOld, String pathNew) {
		extractOldJavaClasses(pathOld);
		extractNewJavaClasses(pathNew);
	}
	
	private void extractOldJavaClasses(String pathOld) {
		List<String> files = FileUtils.listNames(pathOld, "", FILE_EXTENSION);
		for (int i = 0; i < files.size(); i++) {
			String path = files.get(i);
			path = pathOld + path;
			File java = new File(path);
			OldJavaClasses.add(java);
		}
	}
	
	private void extractNewJavaClasses(String pathNew) {
		List<String> files = FileUtils.listNames(pathNew, "", FILE_EXTENSION);
		for (int i = 0; i < files.size(); i++) {
			String path = files.get(i);
			path = pathNew + path;
			File java = new File(path);
			NewJavaClasses.add(java);
		}
	}
	
	public ArrayList<File> getOldJavaClasses() {
		return OldJavaClasses;
	}
	public ArrayList<File> getNewJavaClasses() {
		return NewJavaClasses;
	}

}
