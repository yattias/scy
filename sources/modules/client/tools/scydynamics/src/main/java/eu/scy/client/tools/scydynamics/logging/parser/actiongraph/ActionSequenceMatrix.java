package eu.scy.client.tools.scydynamics.logging.parser.actiongraph;

import eu.scy.actionlogging.api.IAction;
import eu.scy.client.tools.scydynamics.logging.ModellingLogger;
import eu.scy.client.tools.scydynamics.logging.ModellingLoggerFES;
import eu.scy.client.tools.scydynamics.logging.parser.UserModel;

public class ActionSequenceMatrix {

	public enum ActionType {
		FEEDBACK_REQUEST, MODEL_RAN, MODEL_ERROR, HYPOTHESIZE
	}

	private ActionType lastActionType;
	private double[][] matrix;
	private String userName;
	
	public ActionSequenceMatrix(UserModel userModel) {
		this.lastActionType = null;
		matrix = new double[ActionType.values().length][ActionType.values().length];
		parseUserModel(userModel);
	}
	
	public ActionSequenceMatrix() {
		this(null);
	}
	
	public void add(double[][] newMatrix) {
		for (int row=0; row<matrix.length; row++) {
			for (int column=0; column<matrix.length; column++) {
				matrix[row][column] = matrix[row][column]+newMatrix[row][column];
			}
		}	
	}
	
	public void add(ActionSequenceMatrix newMatrix) {
		add(newMatrix.getActionMatrix());
	}
	
	public double[][] getActionMatrix() {
		return matrix;
	}
	
	public static double getSum(double[][] matrix) {
		double sum = 0;
		for (int row=0; row<matrix.length; row++) {
			for (int column=0; column<matrix.length; column++) {
				sum = sum +matrix[row][column];
			}
		}		
		return sum;
	}
	
	private void parseUserModel(UserModel userModel) {
		if (userModel == null) {
			this.userName = "joint matrix";
			return;
		}
		this.userName = userModel.getUserName();
		ActionType aType = null;
		for (IAction action: userModel.getActions()) {
			aType = getActionCategory(action);
			if (aType != null) {
				addNext(aType);
			}
		}
	}
	
	public ActionType getActionCategory(IAction action) {
		String actionTypeName = action.getType();
		if (actionTypeName.equals(ModellingLogger.SPECIFICATION_CHANGED)) {
			return ActionType.HYPOTHESIZE;
		} else if (actionTypeName.equals(ModellingLogger.AUXILIARY_ADDED)) {
			return ActionType.HYPOTHESIZE;
		} else if (actionTypeName.equals(ModellingLogger.AUXILIARY_DELETED)) {
			return ActionType.HYPOTHESIZE;
		} else if (actionTypeName.equals(ModellingLogger.CONSTANT_ADDED)) {
			return ActionType.HYPOTHESIZE;
		} else if (actionTypeName.equals(ModellingLogger.CONSTANT_DELETED)) {
			return ActionType.HYPOTHESIZE;
		} else if (actionTypeName.equals(ModellingLogger.ELEMENT_RENAMED)) {
			return ActionType.HYPOTHESIZE;
		} else if (actionTypeName.equals(ModellingLogger.FLOW_ADDED)) {
			return ActionType.HYPOTHESIZE;
		} else if (actionTypeName.equals(ModellingLogger.FLOW_DELETED)) {
			return ActionType.HYPOTHESIZE;
		} else if (actionTypeName.equals(ModellingLogger.MODEL_RAN)) {
			return ActionType.MODEL_RAN;
		} else if (actionTypeName.equals(ModellingLogger.MODEL_RAN_ERROR)) {
			return ActionType.MODEL_ERROR;
		} else if (actionTypeName.equals(ModellingLogger.RELATION_ADDED)) {
			return ActionType.HYPOTHESIZE;
		} else if (actionTypeName.equals(ModellingLogger.RELATION_DELETED)) {
			return ActionType.HYPOTHESIZE;
		} else if (actionTypeName.equals(ModellingLogger.STOCK_ADDED)) {
			return ActionType.HYPOTHESIZE;
		} else if (actionTypeName.equals(ModellingLogger.STOCK_DELETED)) {
			return ActionType.HYPOTHESIZE;
		} else if (actionTypeName.equals(ModellingLoggerFES.FEEDBACK_REQUESTED)) {
			return ActionType.FEEDBACK_REQUEST;
		} else {
			return null;
		}

	}

	public String getUserName() {
		return userName;
	}
	
	private void add(ActionType from, ActionType to) {
		matrix[from.ordinal()][to.ordinal()] = matrix[from.ordinal()][to.ordinal()]+1;
		lastActionType = to;
	}
	
	private void addNext(ActionType to) {		
		if (lastActionType == null) {
			lastActionType = to;
		} else {
			if (lastActionType.equals(ActionType.FEEDBACK_REQUEST) && to.equals(ActionType.FEEDBACK_REQUEST)) {
				// skip feedback-loops
				return;
			}
			add(lastActionType, to);
		}
	}
	
}
