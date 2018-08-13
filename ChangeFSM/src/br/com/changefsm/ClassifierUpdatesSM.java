package br.com.changefsm;

import java.util.List;

import br.com.changefsm.models.ClassChanged;
import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;

public class ClassifierUpdatesSM {

	private final String METHOD_CALL = "METHOD_INVOCATION";
	private final String ASSIGNMENT = "ASSIGNMENT";
	private final String IF_INSTANCE = "IF_STATEMENT";
	private final String ELSE_INSTANCE = "ELSE_STATEMENT";
//	private final String ENUM_VALUE = "ENUM_VALUE";
//	private final String SCAPE_RETURN = "SCAPE_RETURN";

	public ClassifierUpdatesSM() {
	}

	public void searchAndClassifySMUpdates(List<ClassChanged> candidateCodeClasses) {

		for (ClassChanged classChanged : candidateCodeClasses) {
			analyseChanges(classChanged.getChanges());
		}

	}

	private void analyseChanges(List<SourceCodeChange> changes) {
		for (SourceCodeChange scc : changes) {
			System.out.println("----------------------------NEW CHANGE------------------------------------");
			// System.out.println(scc.getLabel());
			// System.out.println(scc.getParentEntity());
			// System.out.println(scc.getChangedEntity());
			// System.out.println(scc.getChangedEntity().getLabel());
			System.out.println(scc.getRootEntity());
			switch (scc.getChangedEntity().getLabel()) {

			case METHOD_CALL:
				System.out.println("mudança em chamada de método");
				break;

			case ASSIGNMENT:
				System.out.println("mudança em assinatura de objeto");
				break;

			case IF_INSTANCE:
				System.out.println("mudança em instÂncia de IF");
				break;

			case ELSE_INSTANCE:
				System.out.println("mudança em instância de ELSE");
				break;
				
//			case ENUM_VALUE:
//				System.out.println("mudança de valor no enum");
//				break;
//				
//			case SCAPE_RETURN:
//				System.out.println("mudança de retorno");
//				break;
				
			default:
				break;

			}
			System.out.println("----------------------------END CHANGE------------------------------------\n");
		}
	}

}
