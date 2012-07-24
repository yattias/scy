package eu.scy.client.tools.scydynamics.logging.parser;

import java.util.Collection;
import java.util.LinkedList;
import java.util.SortedMap;
import java.util.TreeMap;

import colab.um.xml.model.JxmModel;

import eu.scy.actionlogging.api.IAction;
import eu.scy.client.tools.scydynamics.domain.Domain;
import eu.scy.client.tools.scydynamics.domain.Feedback;
import eu.scy.client.tools.scydynamics.logging.ModellingLogger;
import eu.scy.client.tools.scydynamics.logging.ModellingLoggerFES;
import eu.scy.client.tools.scydynamics.model.Model;

public class UserModel {

	public static final String STATISTICS_TEMPLATE = "name, actions, duration[minutes], phase_changes, feedback_requested, model_ran, model_ran_error, model_score, correctNodeNames, incorrectNodeNames, correctRelations, incorrectRelations, correctDirections, incorrectDirections, links_added, links_deleted";
	private String userName;
	// actions are organized by timestamp in this treemap
	private TreeMap<Long, IAction> actions;
	private TreeMap<Long, IAction> backupActions;
	private LinkedList<IAction> nextPhaseActions;
	private LinkedList<IAction> feedbackActions;
	private boolean statisticsValid;
	private LinkedList<IAction> modelRanActions;
	private LinkedList<IAction> modelRanErrorActions;
	private Domain domain;
	private int linksDeleted = 0;
	private int linksAdded = 0;
	private Feedback bestFeedback;

	public UserModel(String userName, Domain domain) {
		this.userName = userName;
		this.domain = domain;
		actions = new TreeMap<Long, IAction>();
		backupActions = new TreeMap<Long, IAction>();
		nextPhaseActions = new LinkedList<IAction>();
		feedbackActions = new LinkedList<IAction>();
		modelRanActions = new LinkedList<IAction>();
		modelRanErrorActions = new LinkedList<IAction>();
		statisticsValid = false;
	}

	public IAction getLastAction() {
		return actions.lastEntry().getValue();
	}

	public IAction getFirstAction() {
		return actions.firstEntry().getValue();
	}

	public void releaseFilters() {
		actions = (TreeMap<Long, IAction>) backupActions.clone();
		statisticsValid = false;
	}

	public void filterForTime(long start, long end) {
		TreeMap<Long, IAction> actionsClone = (TreeMap<Long, IAction>) actions.clone();
		for (long timestamp : actionsClone.keySet()) {
			if (timestamp < start || timestamp > end) {
				actions.remove(timestamp);
			}
		}
		statisticsValid = false;
	}

	public void addAction(IAction newAction) {
		if (userName.equals(newAction.getUser())) {
			this.actions.put(newAction.getTimeInMillis(), newAction);
			statisticsValid = false;
		} else {
			System.out.println("tried to add action to wrong user (expected: " + userName + ", received " + newAction.getUser() + "; ignoring.");
		}
	}

	public String getUserName() {
		return this.userName;
	}

	private long getDurationMinutes() {
		long duration = -99;
		try {
			duration = actions.get(actions.lastKey()).getTimeInMillis() - actions.get(actions.firstKey()).getTimeInMillis();
			duration = duration / 60000;
		} catch (Exception e) {
			System.out.println("exception caught in UserModel.getDuration: " + e.getMessage());
		}
		return duration;
	}
	
	public LinkedList<IAction> getAction(String type) {
		LinkedList<IAction> typedActions = new LinkedList<IAction>();
		for (IAction action: actions.values()) {
			if (action.getType().equals(ModellingLoggerFES.FEEDBACK_REQUESTED)) {
				typedActions.add(action);
			}
		}
		return typedActions;
	}
	
	@Override
	public String toString() {
		if (!statisticsValid) {
			calculateStatistics();
		}
		
		// setting default values
		int modelScore = -99;
		int correctNodeNames = -99;
		int incorrectNodeNames = -99;
		int correctRelations = -99;
		int incorrectRelations = -99;
		int correctDirections = -99;
		int incorrectDirections = -99;
		if (bestFeedback != null) {
			// best model & feedback has been found -> fill in real values;
			modelScore = bestFeedback.getCorrectnessRatio();
			correctNodeNames = bestFeedback.getCorrectNames();
			incorrectNodeNames = bestFeedback.getIncorrectNames();
			correctRelations = bestFeedback.getCorrectRelations();
			incorrectRelations = bestFeedback.getIncorrectRelations();
			correctDirections = bestFeedback.getCorrectDirections();
			incorrectDirections = bestFeedback.getIncorrectDirections();
		}
		return userName + ", " + actions.size() + ", " + getDurationMinutes() + ", " + nextPhaseActions.size() +", "+feedbackActions.size()+", "+modelRanActions.size()+", "+modelRanErrorActions.size()+", "+modelScore+", "+correctNodeNames+", "+incorrectNodeNames+", "+correctRelations+", "+incorrectRelations+", "+correctDirections+", "+incorrectDirections+", "+linksAdded+", "+linksDeleted;

	}

	public void calculateStatistics() {
		System.out.println("calculating statistics for user '"+this.userName+"'.");
		nextPhaseActions = new LinkedList<IAction>();
		for (IAction action : actions.values()) {
			if (action.getType().equals(ModellingLogger.NEXT_PHASE)) {
				nextPhaseActions.add(action);
			} else if (action.getType().equals(ModellingLoggerFES.FEEDBACK_REQUESTED)) {
				addFeedback(action);
			} else if (action.getType().equals(ModellingLogger.MODEL_RAN)) {
				modelRanActions.add(action);
			} else if (action.getType().equals(ModellingLogger.MODEL_RAN_ERROR)) {
				modelRanErrorActions.add(action);
			} else if (action.getType().equals(ModellingLogger.RELATION_DELETED)
					|| action.getType().equals(ModellingLogger.FLOW_DELETED)) {
				linksDeleted++;
			} else if (action.getType().equals(ModellingLogger.RELATION_ADDED)
					|| action.getType().equals(ModellingLogger.FLOW_ADDED)) {
				linksAdded++;
			}
			
			Model bestModel = getBestModel();
			if (bestModel != null) {
				bestFeedback = new Feedback(bestModel, domain);
			}
				
		}
		
		statisticsValid = true;
	}

	private void addFeedback(IAction action) {
		if (feedbackActions.isEmpty()) {
			// this is the first feedback actions, simply add
			feedbackActions.add(action);
		} else {
			if (meaningfulActionsBetween(feedbackActions.getLast(), action)) {
				feedbackActions.add(action);
			} else {
				// do nothing
			}
		}
	}

	private boolean meaningfulActionsBetween(IAction firstAction, IAction lastAction) {
		SortedMap<Long, IAction> betweenActions = actions.subMap(firstAction.getTimeInMillis(), lastAction.getTimeInMillis());
		System.out.print("meaningful/feedback for "+this.userName+": checking "+betweenActions.size()+" between "+firstAction.getType()+" and "+lastAction.getType());
		for (IAction action : betweenActions.values()) {
			if (meaningfulAction(action)) {
				System.out.println(" -> true");
				return true;
			} else {
				// no meaningful action found yet, continue for-loop
			}
		}
		// no meaningful action found at all, return false
		System.out.println(" -> false");
		return false;
	}

	private boolean meaningfulAction(IAction action) {
		// meaningful actions are those actions that change the model
		// this is a little bit dangerous, because the first conditions are not type-safe
		if (action.getType().endsWith("_added") ||
				action.getType().endsWith("_deleted") ||
				action.getType().equals(ModellingLogger.SPECIFICATION_CHANGED) ||
				action.getType().equals(ModellingLogger.ELEMENT_RENAMED) ||
				action.getType().equals(ModellingLogger.MODEL_LOADED) ||
				action.getType().equals(ModellingLogger.MODEL_CLEARED)) {
			return true;
		} else {
			return false;
		}
	}

	private Model getBestModel() {
		Model tempBestModel = null;
		Feedback tempBestModelFeedback = null;
		Model tempModel = null;
		Feedback tempModelFeedback = null;
		for (IAction action: actions.values()) {
			//System.out.println("reading action type '"+action.getType()+"' at "+action.getTimeInMillis()+" for user "+action.getUser());
			String modelString = action.getAttribute("model");
			if ( modelString != null) {
				//System.out.println("... model found: "+modelString.substring(0, 20)+"...");
				if (tempBestModel == null) {
					// setting the first best model;
					tempBestModel = new Model(null);
					tempBestModel.setXmModel(JxmModel.readStringXML(modelString));
					tempBestModelFeedback = new Feedback(tempBestModel, domain);
				} else {
					tempModel = new Model(null);
					tempModel.setXmModel(JxmModel.readStringXML(modelString));
					tempModelFeedback = new Feedback(tempModel, domain);
					if (tempModelFeedback.getCorrectnessRatio() > tempBestModelFeedback.getCorrectnessRatio()) {
						//System.out.println("...ratio "+tempModelFeedback.getCorrectnessRatio()+" > "+tempBestModelFeedback.getCorrectnessRatio());
						tempBestModel = tempModel;
						tempBestModelFeedback = tempModelFeedback;
					}
				}
			}
		}
		return tempBestModel;
	}
	
	public Collection<IAction> getActions() {
		return this.actions.values();
	}

	public void setBackupPoint() {
		backupActions = (TreeMap<Long, IAction>) actions.clone();
	}
}
