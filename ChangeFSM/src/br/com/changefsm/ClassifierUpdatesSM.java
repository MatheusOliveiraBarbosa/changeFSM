package br.com.changefsm;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.changefsm.models.ClassChanged;
import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;

public class ClassifierUpdatesSM {

	private final String METHOD_CALL = "METHOD_INVOCATION";
	private final String ASSIGNMENT = "ASSIGNMENT";
	private final String IF_INSTANCE = "IF_STATEMENT";
	private final String ELSE_INSTANCE = "ELSE_STATEMENT";
//	private final String ENUM_VALUE = "ENUM_VALUE";
//	private final String SCAPE_RETURN = "SCAPE_RETURN";
	
	private final Logger log = LogManager.getLogger(ClassifierUpdatesSM.class);

	public ClassifierUpdatesSM() {
	}

	public void searchAndClassifySMUpdates(List<ClassChanged> candidateCodeClasses) {

		for (ClassChanged classChanged : candidateCodeClasses) {
			analyseChanges(classChanged.getChanges());
		}

	}

	private void analyseChanges(List<SourceCodeChange> changes) {
		for (SourceCodeChange change : changes) {
			switch (change.getChangedEntity().getLabel()) {

			case METHOD_CALL:
				log.info("This change is in MethodCall");
				break;

			case ASSIGNMENT:
				log.info("This change is in Assignment");
				break;

			case IF_INSTANCE:
				log.info("This change is in IF");
				break;

			case ELSE_INSTANCE:
				log.info("This change is in Else");
				break;
				
//			case ENUM_VALUE:
//				log.info("This change is in Enum");
//				break;
//				
//			case SCAPE_RETURN:
//				log.info("This change is in Scape Return");
//				break;
				
			default:
				break;

			}
		}
	}

}
