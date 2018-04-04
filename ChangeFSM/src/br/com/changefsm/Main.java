package br.com.changefsm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import br.com.changefsm.models.State;
import br.com.changefsm.readerxml.ReaderXML;
import br.com.changefsm.utils.FileUtils;
import ch.uzh.ifi.seal.changedistiller.ChangeDistiller;
import ch.uzh.ifi.seal.changedistiller.ChangeDistiller.Language;
import ch.uzh.ifi.seal.changedistiller.distilling.FileDistiller;
import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;

public class Main {

	private static final String FILE_EXTENSION = ".java";
	private static final String PATH_PROJECT_OLD = "./data/smarthome-first-version/bundles/core/org.eclipse.smarthome.core.thing/";
	private static final String PATH_PROJECT_NEW = "./data/smarthome-master/bundles/core/org.eclipse.smarthome.core.thing/";
	private static final String PATH_XMI = "./data/status-smarthome.xml";
	private static final String TAG_STATE = "subvertex";
	private static final String TYPE_STATE = "uml:State";

	private static ArrayList<State> states = new ArrayList<State>();
	private static ArrayList<File> classesOld = new ArrayList<File>();
	private static ArrayList<File> classesNew = new ArrayList<File>();

	public static void main(String[] args) {

		
//		extractElementsXML();

		String classesJava = extractClassesNames();

//		filterClasses();

		String results = "";
		String head = "\"Change\", \"Type Change\", \"Label Change\", \"Changed Entity\", \"Parent Entity\", \"Root Entity\", \"Class Name\"";
		results = head + "\n";
		int cont = 0;
		for (File fileNew : classesNew) {
			for (File fileOld : classesOld) {

				// This part is support for to identify the corrects classes
				// excluding classes with the same name but in different package.

				String auxNew = fileNew.getPath();
				String auxOld = fileOld.getPath();
				int indexOld = PATH_PROJECT_OLD.length();
				int indexNew = PATH_PROJECT_NEW.length();
				auxNew = auxNew.substring(indexNew);
				auxOld = auxOld.substring(indexOld);
				System.out.println(auxNew);
				System.out.println(auxOld);

				if (auxNew.equals(auxOld)) {

					FileDistiller fd = ChangeDistiller.createFileDistiller(Language.JAVA);
					fd.extractClassifiedSourceCodeChanges(fileOld, fileNew);
					if (!fd.getSourceCodeChanges().isEmpty()) {

						for (SourceCodeChange change : fd.getSourceCodeChanges()) {

							if ((!change.getChangeType().toString().equals("DOC_UPDATE"))
									&& (!change.getChangeType().toString().equals("COMMENT_UPDATE"))
									&& (!change.getChangeType().toString().equals("DOC_DELETE"))
									&& (!change.getChangeType().toString().equals("COMMENT_DELETE"))
									&& (!change.getChangeType().toString().equals("DOC_INSERT"))
									&& (!change.getChangeType().toString().equals("COMMENT_INSERT"))
									&& (!change.getChangeType().toString().equals("COMMENT_MOVE"))) {

								cont++;
								String corretor = change.toString().replace("\"", "");
								results += "\"" + corretor + "\",";

								corretor = change.getChangeType().toString().replace("\"", "");
								results += "\"" + corretor + "\",";

								results += "\"" + change.getLabel() + "\",";

								corretor = change.getChangedEntity().toString().replace("\"", "");
								results += "\"" + corretor + "\",";

								corretor = change.getParentEntity().toString().replace("\"", "");
								results += "\"" + corretor + "\",";

								results += "\"" + change.getRootEntity() + "\",";

								results += "\"" + fileOld.getName() + "\" \n";
								System.out.println("Estamos executando.... contagem: " + cont);

							}
						}
						break;
					}
				}
			}
		}
		System.out.println(results);
		System.out.println(cont);

		try {
			FileWriter writer = new FileWriter("./data/changes.csv");
			writer.write(results);
			writer.close();

			FileWriter fw = new FileWriter("./data/classes.txt");
			fw.write(classesJava);
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * This method is responsible for filter the classes that has in its body the
	 * word equals the name of the states in the state machine, and for last, update
	 * 
	 * @classesNew and @classesOld with those files.
	 * 
	 */
	private static void filterClasses() {
		ArrayList<File> clNew = new ArrayList<File>();
		ArrayList<File> clOld = new ArrayList<File>();
		Scanner scanner;
		for (File file : classesOld) {
			try {
				scanner = new Scanner(file);
				while (scanner.hasNext()) {
					String line = scanner.nextLine();
					for (State state : states) {
						if (line.contains( state.getName() ) && !clOld.contains(file) ) {
							clOld.add(file);
							break;
						}
					}
				}

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

		}

		for (File file : classesNew) {
			try {
				scanner = new Scanner(file);
				while (scanner.hasNext()) {
					String line = scanner.nextLine();
					for (State state : states) {
						if (line.contains( state.getName() ) && !clNew.contains(file) ) {
							clNew.add(file);
							break;
						}
					}
				}

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

		}

		classesNew = clNew;
		classesOld = clOld;

	}

	/**
	 * Extract all names of the new and old Java Classes, and separate in two
	 * ArrayList, each one for version.
	 * 
	 * @return string classesJava : This String return a string that contains all
	 *         names of the new and old class java.
	 */
	private static String extractClassesNames() {
		// Leitura arquivos JAVAOLD
		ArrayList<String> files = (ArrayList<String>) FileUtils.listNames(PATH_PROJECT_OLD, "", FILE_EXTENSION);
		String classesJava = "Classes Java " + files.size() + " \n";
		for (int i = 0; i < files.size(); i++) {
			String path = files.get(i);
			path = PATH_PROJECT_OLD + path;
			File java = new File(path);
			classesOld.add(java);
			classesJava += java.getPath() + "\n";
		}

		// Leitura arquivos JAVANEW
		files = (ArrayList<String>) FileUtils.listNames(PATH_PROJECT_NEW, "", FILE_EXTENSION);
		classesJava += "\n Classes Nova " + files.size() + "\n ";
		for (int i = 0; i < files.size(); i++) {
			String path = files.get(i);
			path = PATH_PROJECT_NEW + path;
			File java = new File(path);
			classesNew.add(java);
			classesJava += java.getPath() + "\n";
		}
		return classesJava;
	}

	/**
	 * Read a XML file and extract the values used in StateMachine like a State and
	 * Transitions
	 * 
	 * The XML model is Enterprise Architecture
	 * 
	 * @author Matheus de Oliveira
	 */
	private static void extractElementsXML() {
		ReaderXML.readXML(PATH_XMI);
		NodeList nodes = ReaderXML.getNodeList(TAG_STATE);
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) node;
				if (element.getAttribute("xmi:type").equals(TYPE_STATE)) {
					states.add(new State(element.getAttribute("name")));
				}
			}
		}
	}

}
