package br.com.changefsm;

import java.util.List;

import br.com.changefsm.models.ClassChanged;
import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;

public class ClassifierUpdatesSM {

	private final String METHOD_CALL = "METHOD_INVOCATION";
	private final String ASSIGNMENT = "ASSIGNMENT";
	private final String IF_INSTANCE = "IF_STATEMENT";
	private final String ELSE_INSTANCE = "ELSE_STATEMENT";
	public ClassifierUpdatesSM() {
	}
	
	public void searchAndClassifySMUpdates(List<ClassChanged> candidateCodeClasses){
		
		for(ClassChanged classChanged : candidateCodeClasses) {
			analyseChanges(classChanged.getChanges());
		}
		
	}
	
	private void analyseChanges(List<SourceCodeChange> changes) {
		for(SourceCodeChange scc : changes) {
			System.out.println("----------------------------NEW CHANGE------------------------------------");
			System.out.println(scc.getLabel());
			System.out.println(scc.getParentEntity());
			System.out.println(scc.getChangedEntity());
//			System.out.println(scc.getChangedEntity().getLabel());
			System.out.println("----------------------------END CHANGE------------------------------------");
			if(scc.getChangedEntity().getLabel().equals(METHOD_CALL)) {
				System.out.println("mudança em chamada de método");
			}else if(scc.getChangedEntity().getLabel().equals(ASSIGNMENT)) {
				System.out.println("mudança em assinatura de objeto");				
			}else if(scc.getChangedEntity().getLabel().equals(IF_INSTANCE)) {
				System.out.println("mudança em instÂncia de IF");
			}else if(scc.getChangedEntity().getLabel().equals(ELSE_INSTANCE)) {
				System.out.println("mudança em instância de ELSE");
			}
			
			
		}
	}

}
