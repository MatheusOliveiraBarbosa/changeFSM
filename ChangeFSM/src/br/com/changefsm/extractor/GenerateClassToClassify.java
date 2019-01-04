package br.com.changefsm.extractor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * This class is responsible to generate a "empty" class to be used in
 * changedistiller when class was removed or added
 * 
 * @author Matheus Oliveira
 *
 */
public class GenerateClassToClassify {

	private static final String HEADER_CLASS = "public class";
	private static final String EXTENSION_FILE = ".java";

	public static File createEmptyClass(String nameFile) {
		String nameClass = nameFile.replace(EXTENSION_FILE, "");
		String content = HEADER_CLASS + " " + nameClass + " " + " { \n}";
		
		File newClass = new File(nameFile);
		try {
			FileWriter writer = new FileWriter(newClass);
			writer.write(content);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return newClass;
	}

}
