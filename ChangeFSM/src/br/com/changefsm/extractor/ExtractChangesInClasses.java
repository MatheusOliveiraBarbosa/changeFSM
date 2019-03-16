package br.com.changefsm.extractor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.changefsm.models.ClassChanged;
import ch.uzh.ifi.seal.changedistiller.ChangeDistiller;
import ch.uzh.ifi.seal.changedistiller.ChangeDistiller.Language;
import ch.uzh.ifi.seal.changedistiller.distilling.FileDistiller;
import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;

public class ExtractChangesInClasses {

	private List<ClassChanged> classesChanged;
	private Logger log = LogManager.getLogger(ExtractChangesInClasses.class);

	public ExtractChangesInClasses() {
	}

	public List<ClassChanged> extractChanges(List<File> oldJavaClasses, List<File> newJavaClasses) {
		classesChanged = new ArrayList<ClassChanged>();
		FileDistiller fileDistiller = ChangeDistiller.createFileDistiller(Language.JAVA);
		;
		/*
		 * Quando uma nova classe for adicionada mant�m-se TRUE para que seja criado um
		 * novo arquivo e feito a extra��o de tudo que foi adicionado
		 */
		boolean flagToNewClass;
		for (File fileNew : newJavaClasses) {
			flagToNewClass = true;

			for (File fileOld : oldJavaClasses) {

				// Verifica se s�o os arquivos com o mesmo nome
				if (fileNew.getName().toLowerCase().equals(fileOld.getName().toLowerCase())) {
					flagToNewClass = false;
					// Iniciando a ChangeDistiller para extrair a mudan�as das classes
					fileDistiller.extractClassifiedSourceCodeChanges(fileOld, fileNew);

					generateChangedClasses(fileDistiller, fileNew);

					oldJavaClasses.remove(fileOld); // Remover classe j� retirada as mudan�as
					break; // Quebra o la�o para ir � pr�xima classe da lista de classes da vers�o nova
				}
			}

			if (flagToNewClass) {
				// Create a empty class to extract the elements into the new class on the source
				// code
				File auxClass = GenerateClassToClassify.createEmptyClass(fileNew.getName());
				fileDistiller.extractClassifiedSourceCodeChanges(auxClass, fileNew);
				generateChangedClasses(fileDistiller, fileNew);
				if(auxClass.delete()) {
					log.info("extractChanges() - auxClass <"+ auxClass.getName() +"> file was be removed!");
				}
			}
		}

		if (!oldJavaClasses.isEmpty()) {
			// Create a empty class to extract the elements when this class be removed on
			// the source code
			for (File fileOld : oldJavaClasses) {
				File auxClass = GenerateClassToClassify.createEmptyClass(fileOld.getName());
				fileDistiller.extractClassifiedSourceCodeChanges(fileOld, auxClass);
				generateChangedClasses(fileDistiller, fileOld);
				if(auxClass.delete()) {
					log.info("extractChanges(): auxClass File was be removed!");
				}
			}
		}

		log.info("The classes with changes are: " + this.classesChanged);
		return classesChanged;
	}

	private void generateChangedClasses(FileDistiller fileDistiller, File fileNew) {
		ClassChanged cc;
		if (!fileDistiller.getSourceCodeChanges().isEmpty()) {
			cc = new ClassChanged();
			for (SourceCodeChange change : fileDistiller.getSourceCodeChanges()) {
				addChangesIgnoringCommentsAndJavadocs(cc, change);
			}

			/*
			 * Verifica se a classe teve mudan�as ignorando as de coment�rios e javadocs
			 */
			if (!cc.getChanges().isEmpty()) {
				cc.setClassFile(fileNew);
				classesChanged.add(cc);
			}
		}
	}

	/**
	 * Ignora mudan�as que ocorreram em javadocs e coment�rios para que ent�o seja
	 * adicionados na lista de ClassChanged (Classes com mudan�as)
	 * 
	 * @param cc
	 * @param change
	 */
	private void addChangesIgnoringCommentsAndJavadocs(ClassChanged cc, SourceCodeChange change) {

		if ((!change.getChangeType().toString().equals("DOC_UPDATE"))
				&& (!change.getChangeType().toString().equals("COMMENT_UPDATE"))
				&& (!change.getChangeType().toString().equals("DOC_DELETE"))
				&& (!change.getChangeType().toString().equals("COMMENT_DELETE"))
				&& (!change.getChangeType().toString().equals("DOC_INSERT"))
				&& (!change.getChangeType().toString().equals("COMMENT_INSERT"))
				&& (!change.getChangeType().toString().equals("COMMENT_MOVE"))) {
			cc.getChanges().add(change);
		}
	}

	public List<ClassChanged> getClassesChanged() {
		return classesChanged;
	}

	public void setClassesChangeds(List<ClassChanged> classesChangeds) {
		this.classesChanged = classesChangeds;
	}
}
