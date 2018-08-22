package br.com.changefsm.utils ;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Alysson Milanez
 *
 */

public class FileUtils{
	
	/**
	 * Method to list all names into the project.
	 * 
	 * @param path
	 *            - base directory of the project.
	 * @param base
	 *            - used to indicate the base name of directory, utilized for
	 *            purposes of recursion.
	 * @return - The names of all files presents into the current project.
	 */
	public static List<String> listNames(String path, String base,
			String fileExtension) {
		List<String> result = new ArrayList<String>();
		try {
			File dir = new File(path);

			if (!dir.exists()) {
				throw new RuntimeException("Directory " + dir.getAbsolutePath()
						+ " does not exist.");
			}
			File[] arquivos = dir.listFiles();
			int tam = arquivos.length;
			for (int i = 0; i < tam; i++) {
				if (arquivos[i].isDirectory()) {
					String baseTemp = base + arquivos[i].getName() + "/";
					result.addAll(listNames(arquivos[i].getAbsolutePath(),
							baseTemp, fileExtension));
				} else {
					if (arquivos[i].getName().endsWith(fileExtension)) {
						String temp = base + arquivos[i].getName();
						if (!result.contains(temp))
							result.add(temp);
					}
				}
			}
		} catch (Exception e) {
			System.err.println("Error in listNames()");
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * Method used to list all classes present into the directory received as
	 * parameter.
	 * 
	 * @param sourceFolder
	 *            = the directory source of the files.
	 * @return - the file containing all classes.
	 */
	public File getClassListFile(String sourceFolder) {
		List<String> listClassNames = listNames(sourceFolder, "", ".java");
		StringBuffer lines = new StringBuffer();
		for (String className : listClassNames) {
			className = className + "\n";
			lines.append(className);
		}
		return makeFile("pathToFile.txt", lines.toString());
	}
	
	/**
	 * Method to creates a new file with the name and content received as
	 * parameter.
	 * 
	 * @param name
	 *            - the name of the new file.
	 * @param text
	 *            - the content of the file.
	 * @return - the new file created.
	 */
	public static File makeFile(String name, String text) {
		File result = new File(name);
		try {
			result.createNewFile();
			FileWriter fw;
			while (!result.canWrite()) {
				result.createNewFile();
			}
			fw = new FileWriter(result);
			fw.write(text);
			fw.close();
		} catch (IOException e) {
			System.err.println("Error in method makeFile()");
		}
		return result;
	}

}