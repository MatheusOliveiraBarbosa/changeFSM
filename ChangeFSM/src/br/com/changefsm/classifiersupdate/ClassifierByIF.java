package br.com.changefsm.classifiersupdate;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import br.com.changefsm.models.State;
import br.com.changefsm.models.Transition;
import br.com.changefsm.models.UpdateSM;
import br.com.changefsm.models.UpdateSMType;
import ch.uzh.ifi.seal.changedistiller.model.classifiers.java.JavaEntityType;
import ch.uzh.ifi.seal.changedistiller.model.entities.Delete;
import ch.uzh.ifi.seal.changedistiller.model.entities.Insert;
import ch.uzh.ifi.seal.changedistiller.model.entities.SourceCodeChange;
import ch.uzh.ifi.seal.changedistiller.model.entities.Update;
import ch.uzh.ifi.seal.changedistiller.treedifferencing.Node;

public class ClassifierByIF extends ClassifierUpdate implements InterfaceClassifierByIF {

	// private static final Logger log = LogManager.getLogger(ClassifierByIF.class);

	/**
	 * Classify the updates in the SM by means of changes in IFs
	 * 
	 * @param classChanged
	 * @param stateMachine
	 * @param change
	 * @param updateSM
	 */
	@Override
	public void classifyByIF(UpdateSM updateSM, List<State> statesForClassification) {
		if ( (updateSM.getCodeChange() instanceof Update) && isGuard(updateSM)) {
			updateSM.setUpdateSMType(UpdateSMType.UPDATE_GUARD);
		} else if ((updateSM.getCodeChange() instanceof Delete) && isGuard(updateSM)) {
			updateSM.setUpdateSMType(UpdateSMType.REMOVE_GUARD);
		} else if (updateSM.getCodeChange() instanceof Insert) {
			Enumeration<?> enumerationByNode = updateSM.getCodeChange().getRootEntity().getBodyRigth()
					.preorderEnumeration();
			Node nodeChanged = findNodeChanged(enumerationByNode, updateSM);
			if (nodeChanged != null) {
				List<Node> nodes = findChildrens(nodeChanged.children());
				if (isAPossibleGuard(nodes, statesForClassification)) {
					updateSM.setUpdateSMType(UpdateSMType.ADD_GUARD);
				}
			}
		}
	}

	private boolean isAPossibleGuard(List<Node> nodes, List<State> statesForClassification) {
		for (Node node : nodes) {
			for (State state : statesForClassification) {
				if (node.getValue().toLowerCase().contains(state.getName().toLowerCase())) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void classifyByELSE(UpdateSM updateSM, List<State> statesForClassification) {
		Enumeration<?> enumerationByNode = updateSM.getCodeChange().getRootEntity().getBodyRigth()
				.preorderEnumeration();
		Node nodeChanged = findNodeChanged(enumerationByNode, updateSM);
		if (nodeChanged != null) {
			List<Node> nodes = findChildrens(nodeChanged.children());
			if (updateSM.getCodeChange().toString().startsWith(getDELETE())) {
				updateSM.setUpdateSMType(UpdateSMType.REMOVE_GUARD);
			} else if (updateSM.getCodeChange().toString().startsWith(getINSERT())) {
				if (isAPossibleGuard(nodes, statesForClassification)) {
					updateSM.setUpdateSMType(UpdateSMType.ADD_GUARD);
				}
			}
		}
	}

	private boolean isGuard(UpdateSM updateSM) {
		for (Transition transition : updateSM.getStateMachine().getTransitions()) {
			String guards = transition.getGuard().trim();
			if (verifyPossibilityGuard(guards, updateSM.getCodeChange())) {
				return true;
			}
		}
		return false;
	}

	// ([a-zA-z0-9]+[^\w]{1,2}([a-zA-z0-9]*))
	// \([a-zA-z0-9\W]+\)
	// [&|\|]{1,2}
	// [&|\||>|<|=|\(|\)]{1,2}
	private boolean verifyPossibilityGuard(String guards, SourceCodeChange sourceCodeChange) {
		String regex = "\"[&|\\\\|>|<|=|(|)]{1,2}\"";
		String[] guardList = guards.toLowerCase().split(regex);
		for (String guard : guardList) {
			if (!guard.equals("") && !guard.equals(" ")) {
				if (sourceCodeChange.getChangedEntity().toString().toLowerCase().contains(guard)) {
					return true;
				}
			}
		}
		return false;
	}

	private Node findNodeChanged(Enumeration<?> enumerationByNode, UpdateSM updateSM) {
		Enumeration<?> nodes = updateSM.getCodeChange().getRootEntity().getBodyRigth().preorderEnumeration();
		while (nodes.hasMoreElements()) {
			Node node = (Node) nodes.nextElement();
			if ((node.getEntity().getType() == JavaEntityType.IF_STATEMENT
					|| node.getEntity().getType() == JavaEntityType.ELSE_STATEMENT)
					&& (node.getEntity().equals(updateSM.getCodeChange().getChangedEntity()))) {
				return node;
			}
		}
		return null;
	}

	private List<Node> findChildrens(Enumeration<?> enumerationByNode) {
		List<Node> childrenNodes = new ArrayList<Node>();
		while (enumerationByNode.hasMoreElements()) {
			Node node = (Node) enumerationByNode.nextElement();
			if (!node.getEntity().getType().equals(JavaEntityType.ELSE_STATEMENT)
					&& !node.getEntity().getType().equals(JavaEntityType.IF_STATEMENT)) {
				childrenNodes.add(node);
				if (node.children().hasMoreElements()) {
					childrenNodes.addAll(findChildrens(node.children()));
				}
			}
		}
		return childrenNodes;
	}
}
