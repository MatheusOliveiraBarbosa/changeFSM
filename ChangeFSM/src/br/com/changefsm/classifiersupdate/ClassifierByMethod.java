package br.com.changefsm.classifiersupdate;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import br.com.changefsm.models.State;
import br.com.changefsm.models.StateAction;
import br.com.changefsm.models.Transition;
import br.com.changefsm.models.TypeStateAction;
import br.com.changefsm.models.UpdateSM;
import br.com.changefsm.models.UpdateSMType;
import ch.uzh.ifi.seal.changedistiller.treedifferencing.Node;

public class ClassifierByMethod extends ClassifierUpdate implements InterfaceClassifierByMethod {

	private List<UpdateSM> updates;

	@Override
	public List<UpdateSM> classifyByMethod(UpdateSM updateSM, List<State> statesForClassification) {
		updates = new ArrayList<UpdateSM>();
		searchForPossibleUpdates(updateSM, statesForClassification);
		return updates;
	}

	private void searchForPossibleUpdates(UpdateSM updateSM, List<State> statesForClassification) {
		if (updateSM.getCodeChange().getBodyStructure() != null) {
			Enumeration<?> enume = updateSM.getCodeChange().getBodyStructure().preorderEnumeration();
			while (enume.hasMoreElements()) {
				Node node = (Node) enume.nextElement();
				if (updateSM.getCodeChange().toString().startsWith(getDELETE())
						&& updateSM.getCodeChange().getLabel().equals("REMOVED_FUNCTIONALITY")) {
					identifyAndClassifyByRemoving(node, updateSM);
				} else if (updateSM.getCodeChange().toString().startsWith(getINSERT())
						&& updateSM.getCodeChange().getLabel().equals("ADDITIONAL_FUNCTIONALITY")) {
					identifyAndClassifyByInserting(node, updateSM, statesForClassification);
				}
			}
			verifyAddEvent();
		}
	}

	private void verifyAddEvent() {
		for (UpdateSM updateSM : updates) {
			if(updateSM.getUpdateSMType() == UpdateSMType.ADD_TRANSITION) {
				UpdateSM update = new UpdateSM(updateSM.getClassJava(), updateSM.getCodeChange(),
						updateSM.getStateMachine(), UpdateSMType.ADD_EVENT);
				updates.add(update);
				break;
			}
		}
		
	}

	/**
	 * 
	 * @param node
	 * @param updateSM
	 * @param statesForClassification
	 */
	private void identifyAndClassifyByInserting(Node node, UpdateSM updateSM, List<State> statesForClassification) {
		if (node.getLabel().toString().equals("ASSIGNMENT")) {
			classifyInsertAssignemt(node, updateSM, statesForClassification);
		} else if (node.getLabel().toString().equals("METHOD_INVOCATION")) {
			classifyInsertMethodCall(node, updateSM, statesForClassification);
		} else if (node.getLabel().toString().equals("RETURN_STATEMENT")) {
			classifyInsertReturnScape(node, updateSM, statesForClassification);
		}

	}

	private void identifyAndClassifyByRemoving(Node node, UpdateSM updateSM) {
		if (node.getLabel().toString().equals("ASSIGNMENT")) {
			classifyRemoveAssignemt(node, updateSM);
		} else if (node.getLabel().toString().equals("METHOD_INVOCATION")) {
			classifyRemoveMethodCall(node, updateSM);
		} else if (node.getLabel().toString().equals("RETURN_STATEMENT")) {
			classifyRemoveReturnScape(node, updateSM);
		} else if (node.getLabel().toString().equals("METHOD")) {
			classifyRemoveMethod(node, updateSM);
		}
	}

	private void classifyRemoveMethod(Node node, UpdateSM updateSM) {
		String nameMethod = node.getValue();
		int indexLastPoint = nameMethod.indexOf(".");
		nameMethod = nameMethod.substring(indexLastPoint);
		for(Transition transition : updateSM.getStateMachine().getTransitions()) {
			if(nameMethod.toLowerCase().contains(transition.getEvent().toLowerCase())) {
				UpdateSM update = new UpdateSM(updateSM.getClassJava(), updateSM.getCodeChange(),
						updateSM.getStateMachine(), UpdateSMType.REMOVE_EVENT);
				updates.add(update);
				break;
			}
		}
	}

	/**
	 * 
	 * @param node
	 * @param updateSM
	 * @param statesForClassification
	 */
	private void classifyInsertReturnScape(Node node, UpdateSM updateSM, List<State> statesForClassification) {
		int numberOfStates = 0;
		for (State state : statesForClassification) {
			if (node.getValue().toLowerCase().contains(state.getName().toLowerCase())) {
				numberOfStates++;
			}
		}
		if (numberOfStates > 1) {
			UpdateSM update = new UpdateSM(updateSM.getClassJava(), updateSM.getCodeChange(),
					updateSM.getStateMachine(), UpdateSMType.ADD_COMPOSITE_STATE);
			updates.add(update);
		}
	}

	/**
	 * 
	 * @param node
	 * @param updateSM
	 * @param statesForClassification
	 */
	private void classifyInsertMethodCall(Node node, UpdateSM updateSM, List<State> statesForClassification) {
		for (State state : statesForClassification) {
			if (node.getValue().toLowerCase().contains(state.getName().toLowerCase())) {
				UpdateSM update = new UpdateSM(updateSM.getClassJava(), updateSM.getCodeChange(),
						updateSM.getStateMachine(), UpdateSMType.ADD_TRANSITION);
				updates.add(update);
				break;
			}
		}
	}

	/**
	 * 
	 * @param node
	 * @param updateSM
	 * @param statesForClassification
	 */
	private void classifyInsertAssignemt(Node node, UpdateSM updateSM, List<State> statesForClassification) {
		if (isPossibleTransition(node, statesForClassification)) {
			UpdateSM update = new UpdateSM(updateSM.getClassJava(), updateSM.getCodeChange(),
					updateSM.getStateMachine(), UpdateSMType.ADD_TRANSITION);
			updates.add(update);
		}
	}

	/**
	 * 
	 * @param node
	 * @param statesForClassification
	 * @return
	 */
	private boolean isPossibleTransition(Node node, List<State> statesForClassification) {
		String assign = node.getValue();
		int indexEq = assign.indexOf("=");
		assign = assign.substring(indexEq);
		if (assign.contains("(")) {
			int indexP1 = assign.indexOf("(");
			int indexP2 = assign.indexOf(")");
			String param = assign.substring(indexP1 + 1, indexP2);
			if (param != "") {
				return hasStateInParam(param, statesForClassification);
			} else {
				assign = assign.substring(0, indexP1);
				for (State state : statesForClassification) {
					if (assign.toLowerCase().contains(state.getName().toLowerCase())) {
						return true;
					}
				}
			}
		} else {
			for (State state : statesForClassification) {
				if (assign.contains(state.getName())) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean hasStateInParam(String assign, List<State> statesForClassification) {
		assign = assign.trim();
		String[] params = assign.split(",");
		for (int i = 0; i < params.length; i++) {
			for (State state : statesForClassification) {
				if (params[i].toLowerCase().contains(state.getName().toLowerCase())) {
					return true;
				}
			}
		}
		return false;
	}

	private void classifyRemoveAssignemt(Node node, UpdateSM updateSM) {
		if (isPossibleTransition(node, updateSM.getStateMachine().getStates())) {
			UpdateSM update = new UpdateSM(updateSM.getClassJava(), updateSM.getCodeChange(),
					updateSM.getStateMachine(), UpdateSMType.REMOVE_TRANSITION);
			updates.add(update);
		}
	}

	private void classifyRemoveReturnScape(Node node, UpdateSM updateSM) {
		int numberOfStates = 0;
		for (State state : updateSM.getStateMachine().getStates()) {
			if (node.getValue().toLowerCase().contains(state.getName().toLowerCase())) {
				numberOfStates++;
			}
		}
		if (numberOfStates > 1) {
			UpdateSM update = new UpdateSM(updateSM.getClassJava(), updateSM.getCodeChange(),
					updateSM.getStateMachine(), UpdateSMType.REMOVE_COMPOSITE_STATE);
			updates.add(update);
		}

	}

	private void classifyRemoveMethodCall(Node node, UpdateSM updateSM) {
		String nameMethod = extractNameMethod(node);
		classifyRemotionActionState(updateSM, nameMethod);
		classifyRemotionTransition(updateSM, nameMethod, node);
	}

	private void classifyRemotionTransition(UpdateSM updateSM, String nameMethod, Node node) {
		for (Transition transition : updateSM.getStateMachine().getTransitions()) {
			if (transition.getAction().toLowerCase().contains(nameMethod)) {
				UpdateSM update = new UpdateSM(updateSM.getClassJava(), updateSM.getCodeChange(),
						updateSM.getStateMachine(), UpdateSMType.REMOVE_ACTION);
				updates.add(update);
				if (hasStateInParam(updateSM.getStateMachine().getStates(), node)) {
					UpdateSM updateT = new UpdateSM(updateSM.getClassJava(), updateSM.getCodeChange(),
							updateSM.getStateMachine(), UpdateSMType.REMOVE_TRANSITION);
					updates.add(updateT);
				}
			} else if (transition.getEvent().toLowerCase().contains(nameMethod)) {
				UpdateSM update = new UpdateSM(updateSM.getClassJava(), updateSM.getCodeChange(),
						updateSM.getStateMachine(), UpdateSMType.REMOVE_EVENT);
				updates.add(update);
			}
		}
	}

	private boolean hasStateInParam(List<State> states, Node node) {
		String param = extractParams(node);
		for (State state : states) {
			if (param.toLowerCase().contains(state.getName().toLowerCase())) {
				return true;
			}
		}
		return false;
	}

	private String extractParams(Node node) {
		int indexParam = node.getValue().indexOf("(");
		int indexLastParam = node.getValue().indexOf(")");
		String params = node.getValue().substring(indexParam, indexLastParam);
		return params;
	}

	private void classifyRemotionActionState(UpdateSM updateSM, String nameMethod) {
		for (State state : updateSM.getStateMachine().getStates()) {
			for (StateAction action : state.getActions()) {
				if (action.getName().toLowerCase().contains(nameMethod.toLowerCase())) {
					if (action.getTypeStateAction() == TypeStateAction.DO) {
						UpdateSM update = new UpdateSM(updateSM.getClassJava(), updateSM.getCodeChange(),
								updateSM.getStateMachine(), UpdateSMType.REMOVE_DOACTION_STATE);
						updates.add(update);
					} else if (action.getTypeStateAction() == TypeStateAction.ENTRY) {
						UpdateSM update = new UpdateSM(updateSM.getClassJava(), updateSM.getCodeChange(),
								updateSM.getStateMachine(), UpdateSMType.REMOVE_ENTRYACTION_STATE);
						updates.add(update);

					} else if (action.getTypeStateAction() == TypeStateAction.EXIT) {
						UpdateSM update = new UpdateSM(updateSM.getClassJava(), updateSM.getCodeChange(),
								updateSM.getStateMachine(), UpdateSMType.REMOVE_EXITACTION_STATE);
						updates.add(update);
					}
				}
			}
		}
	}

	private String extractNameMethod(Node node) {
		String nameMethod = node.getValue();
		int indexParam = nameMethod.indexOf("(");
		int indexPoint = nameMethod.indexOf(".");
		nameMethod = nameMethod.substring(0, indexParam);
		if (indexPoint > 0) {
			nameMethod = nameMethod.substring(indexPoint, indexParam);
		}
		return nameMethod;
	}

}
