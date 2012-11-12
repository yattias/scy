package eu.scy.client.tools.scydynamics.logging.parser;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Vector;
import java.util.concurrent.Callable;

import org.apache.commons.math.MathException;
import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math.stat.regression.SimpleRegression;

import colab.um.draw.JdRelation;
import colab.um.xml.model.JxmModel;

import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.actionlogging.api.IAction;
import eu.scy.client.tools.scydynamics.domain.Concept;
import eu.scy.client.tools.scydynamics.domain.Concept.ConceptType;
import eu.scy.client.tools.scydynamics.domain.Domain;
import eu.scy.client.tools.scydynamics.domain.Feedback;
import eu.scy.client.tools.scydynamics.logging.ModellingLogger;
import eu.scy.client.tools.scydynamics.logging.ModellingLoggerFES;
import eu.scy.client.tools.scydynamics.model.Model;

public class UserModel implements Runnable {

	public static final String STATISTICS_TEMPLATE = "name, actions, duration[minutes], phase_changes, feedback_requested, model_ran, model_ran_error, model_score, correctNodeNames, incorrectNodeNames, correctRelations, incorrectRelations, correctDirections, incorrectDirections, links_added, links_deleted, stocks_added, stocks_deleted, aux_added, aux_deleted, constant_added, constant_deleted, modelscore_r2_b4_eating, modelscore_r2_after_eating, domainTerms, domainRelatedTerms, domainUnrelatedTerms, domainRelations, domainRelatedRelations, domainUnrelatedRelations, correctNodeTypes, incorrectNodeTypes, unknownNodeTypes, correctLinkTypes, incorrectLinkTypes, unknownLinkTypes, eating, modelScoreGradientChangesBeforeEating, modelScoreGradientChangesAfterEating";
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
	private int stocksAdded = 0;
	private int stocksDeleted = 0;
	private int auxAdded = 0;
	private int auxDeleted = 0;
	private int constantAdded = 0;
	private int constantDeleted = 0;
	private Feedback bestFeedback;
	private long duration;
	private SimpleRegression modelScoreRegressionBeforeEating;
	private SimpleRegression modelScoreRegressionAfterEating;
	private LinkedList<Integer> modelScoresBeforeEating = new LinkedList<Integer>();
	private LinkedList<Integer> modelScoresAfterEating = new LinkedList<Integer>();
	private int step;
	private boolean beforeEating = true;
	private HashSet<String> termSpace;
	private HashSet<String> relationSpace;
	private int domainTerms = -99;
	private int domainRelatedTerms = -99;
	private int domainUnrelatedTerms = -99;
	private int domainRelations = -99;
	private int domainRelatedRelations = -99;
	private int domainUnrelatedRelations = -99;

	public UserModel(String userName, Domain domain) {
		this.userName = userName;
		this.domain = domain;
		actions = new TreeMap<Long, IAction>();
		termSpace = new HashSet<String>();
		relationSpace = new HashSet<String>();
		backupActions = new TreeMap<Long, IAction>();
		nextPhaseActions = new LinkedList<IAction>();
		feedbackActions = new LinkedList<IAction>();
		modelRanActions = new LinkedList<IAction>();
		modelRanErrorActions = new LinkedList<IAction>();
		duration = -99;
		statisticsValid = false;
	}

	public Domain getDomain() {
		return domain;
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

	public void filterForMissions(HashSet<String> selectedMissions) {
		TreeMap<Long, IAction> actionsClone = (TreeMap<Long, IAction>) actions.clone();
		for (long timestamp : actionsClone.keySet()) {
			if (!selectedMissions.contains(actionsClone.get(timestamp).getContext(ContextConstants.mission))) {
				actions.remove(timestamp);
			}
		}
		statisticsValid = false;
	}

	public void addAction(IAction newAction) {
		if (userName.equals(newAction.getUser())) {
			this.actions.put(newAction.getTimeInMillis(), newAction);
			if (newAction.getType().equals(ModellingLogger.START_APPLICATION)) {
				System.out.println("start application: user "+newAction.getUser()+" / time: "+newAction.getTimeInMillis());
			}
			statisticsValid = false;
		} else {
			System.out.println("tried to add action to wrong user (expected: " + userName + ", received " + newAction.getUser() + "; ignoring.");
		}
	}

	public String getUserName() {
		return this.userName;
	}

	private void _calculateDuration() {
		// this version substracts all idle times when not running scydynamics
		long tempDuration = -99;
		try {
			tempDuration = actions.get(actions.lastKey()).getTimeInMillis() - actions.get(actions.firstKey()).getTimeInMillis();
			// we have to substract the idle time between running scydynamics
			long previousTimestamp = actions.firstEntry().getKey();
			for (IAction action: actions.values()) {
				if (action.getType().equals(ModellingLogger.START_APPLICATION)) {
					tempDuration = tempDuration - (action.getTimeInMillis() - previousTimestamp);
				}
				previousTimestamp = action.getTimeInMillis();
			}
		} catch (Exception e) {
			System.out.println("exception caught in UserModel.getDuration: " + e.getMessage());
		}
		duration = tempDuration / 60000;
	}

	private void calculateDuration() {
		// this version only substracts idle time when on two different days
		long tempDuration = -99;
		try {
			tempDuration = actions.get(actions.lastKey()).getTimeInMillis() - actions.get(actions.firstKey()).getTimeInMillis();
			// we have to substract the idle time between running scydynamics
			long previousTimestamp = actions.firstEntry().getKey();
			for (IAction action: actions.values()) {
				if (action.getType().equals(ModellingLogger.START_APPLICATION)) {
					Calendar previousDate = Calendar.getInstance();
					previousDate.setTimeInMillis(previousTimestamp);
					Calendar currentDate = Calendar.getInstance();
					currentDate.setTimeInMillis(action.getTimeInMillis());
					if (previousDate.get(Calendar.DAY_OF_YEAR) != currentDate.get(Calendar.DAY_OF_YEAR)) {
						// the two timestamps are on different days
						System.out.println("substracting idle time between days ("+previousDate.get(Calendar.DAY_OF_YEAR)+" != "+currentDate.get(Calendar.DAY_OF_YEAR)+")");
						tempDuration = tempDuration - (action.getTimeInMillis() - previousTimestamp);
					}
				}
				previousTimestamp = action.getTimeInMillis();
			}
		} catch (Exception e) {
			System.out.println("exception caught in UserModel.getDuration: " + e.getMessage());
		}
		duration = tempDuration / 60000;
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
		int correctNodeTypes = -99;
		int incorrectNodeTypes = -99;
		int unknownNodeTypes = -99;
		int correctLinkTypes = -99;
		int incorrectLinkTypes = -99;
		int unknownLinkTypes = -99;
		if (bestFeedback != null) {
			// best model & feedback has been found -> fill in real values;
			modelScore = bestFeedback.getCorrectnessRatio();
			correctNodeNames = bestFeedback.getCorrectNames();
			incorrectNodeNames = bestFeedback.getIncorrectNames();
			correctRelations = bestFeedback.getCorrectRelations();
			incorrectRelations = bestFeedback.getIncorrectRelations();
			correctDirections = bestFeedback.getCorrectDirections();
			incorrectDirections = bestFeedback.getIncorrectDirections();
			correctNodeTypes =  bestFeedback.getCorrectNodeType();
			incorrectNodeTypes = bestFeedback.getIncorrectNodeType();
			unknownNodeTypes = bestFeedback.getUnknownNodeType();
			correctLinkTypes = bestFeedback.getCorrectLinkTypes();
			incorrectLinkTypes = bestFeedback.getIncorrectLinkTypes();
			unknownLinkTypes = bestFeedback.getUnknownLinkTypes();
		}
		double r2_afterEating = modelScoreRegressionAfterEating.getRSquare();
		if ((r2_afterEating == Double.NaN) && (beforeEating == false)) {
			r2_afterEating = 0;
		}
		int eating = 1;
		if (beforeEating) {
			eating = 0;
		}
		int modelScoreGradientChangesBeforeEating = -99;
		int modelScoreGradientChangesAfterEating = -99;
		modelScoreGradientChangesBeforeEating = calculateGradientChanges(modelScoresBeforeEating);		
		if (!beforeEating) {
			modelScoreGradientChangesAfterEating = calculateGradientChanges(modelScoresAfterEating);
		}
		return userName + ", " + actions.size() + ", " + duration + ", " + nextPhaseActions.size() +", "+feedbackActions.size()+", "+modelRanActions.size()+", "+modelRanErrorActions.size()+", "+modelScore+", "+correctNodeNames+", "+incorrectNodeNames+", "+correctRelations+", "+incorrectRelations+", "+correctDirections+", "+incorrectDirections+", "+linksAdded+", "+linksDeleted+", "+stocksAdded+", "+stocksDeleted+", "+auxAdded+", "+auxDeleted+", "+constantAdded+", "+constantDeleted+", "+modelScoreRegressionBeforeEating.getRSquare()+", "+r2_afterEating+", "+domainTerms+", "+domainRelatedTerms+", "+domainUnrelatedTerms+", "+domainRelations+", "+domainRelatedRelations+", "+domainUnrelatedRelations+", "+correctNodeTypes+", "+ incorrectNodeTypes+", "+unknownNodeTypes+", "+correctLinkTypes+", "+incorrectLinkTypes+", "+unknownLinkTypes+", "+eating+", "+modelScoreGradientChangesBeforeEating+", "+modelScoreGradientChangesAfterEating;
	}

	private int calculateGradientChanges(LinkedList<Integer> integers) {
		final int down = -1;
		final int up = 1;
		final int stable = 0;
		int gradientChanges = 0;
		int currentGradient = stable;
		LinkedList<Integer> gradients = new LinkedList<Integer>();
		int lastValue, currentValue; 
		try {
			currentValue = integers.removeFirst();			
			for (int i: integers) {
				lastValue = currentValue;
				currentValue = i;
				if (currentValue > lastValue) {
					currentGradient = up;
				} else if (currentValue < lastValue) {
					currentGradient = down;
				} else {
					currentGradient = stable;
				}
				if (currentGradient != stable) {
					if (gradients.isEmpty()) {
						gradients.add(currentGradient);
					} else if (currentGradient != gradients.getLast()) {
						gradients.add(currentGradient);
					}
				}
			}
			if (gradients.size() >= 1) {
				gradientChanges = gradients.size()-1;
			}
		} catch (Exception ex) {
			//
		}
				
		return gradientChanges;
	}

	@Override
	public void run() {
		calculateStatistics();			
	}

	public void calculateStatistics() {
		System.out.println("calculating statistics for user '"+this.userName+"'.");
		modelScoreRegressionBeforeEating = new SimpleRegression();
		modelScoreRegressionAfterEating = new SimpleRegression();
		step = 1;
		nextPhaseActions = new LinkedList<IAction>();
		for (IAction action : actions.values()) {
			if (action.getType().equals(ModellingLogger.NEXT_PHASE)) {
				nextPhaseActions.add(action);
			} else if (action.getType().equals(ModellingLoggerFES.FEEDBACK_REQUESTED)) {
				addFeedback(action);
				addScoreStatisticsValue(action);
				populateNodeSpace(action);
				populateRelationSpace(action);
			} else if (action.getType().equals(ModellingLogger.MODEL_RAN)) {
				modelRanActions.add(action);
				addScoreStatisticsValue(action);
				populateNodeSpace(action);
				populateRelationSpace(action);
			} else if (action.getType().equals(ModellingLogger.MODEL_RAN_ERROR)) {
				modelRanErrorActions.add(action);
				addScoreStatisticsValue(action);
				populateNodeSpace(action);
				populateRelationSpace(action);
			} else if (action.getType().equals(ModellingLogger.RELATION_DELETED)
					|| action.getType().equals(ModellingLogger.FLOW_DELETED)) {
				linksDeleted++;
			} else if (action.getType().equals(ModellingLogger.RELATION_ADDED)
					|| action.getType().equals(ModellingLogger.FLOW_ADDED)) {
				linksAdded++;
			} else if (action.getType().equals(ModellingLogger.STOCK_ADDED)) {
				stocksAdded++;
			} else if (action.getType().equals(ModellingLogger.STOCK_DELETED)) {
				stocksDeleted++;
			} else if (action.getType().equals(ModellingLogger.AUXILIARY_ADDED)) {
				auxAdded++;
			} else if (action.getType().equals(ModellingLogger.AUXILIARY_DELETED)) {
				auxDeleted++;
			} else if (action.getType().equals(ModellingLogger.CONSTANT_ADDED)) {
				constantAdded++;
			} else if (action.getType().equals(ModellingLogger.CONSTANT_DELETED)) {
				constantDeleted++;
			}	else if (action.getType().equals(ModellingLogger.SPECIFICATION_CHANGED)) {
				populateNodeSpace(action);
				populateRelationSpace(action);
			}
		}

		Model bestModel = getBestModel();
		if (bestModel != null) {
			bestFeedback = new Feedback(bestModel, domain);
		}

		calculateDuration();

		calculateDomainTerms();
		calculateDomainRelations();

		statisticsValid = true;
	}

	private void calculateDomainTerms() {
		domainTerms = 0;
		domainRelatedTerms = 0;
		domainUnrelatedTerms = 0;
		Iterator<String> itr = termSpace.iterator();
		while (itr.hasNext()) {
			String term = itr.next();
			Concept concept = domain.getConceptByTerm(term);
			if (concept != null) {
				if (concept.getType().equals(ConceptType.DOMAIN_RELATED)) {
					domainRelatedTerms++;
				} else if (concept.getType().equals(ConceptType.DOMAIN_UNRELATED)) {
					domainUnrelatedTerms++;
				} else {
					// must be a stock, const or aux
					domainTerms++;
				}
			} else {
				// cannot recognize it at all
				domainUnrelatedTerms++;
			}
		}
	}

	private void addScoreStatisticsValue(IAction action) {
		String modelString = action.getAttribute("model");
		if ( modelString != null) {
			Model model = new Model(null);
			model.setXmModel(JxmModel.readStringXML(modelString));
			Feedback feedback = new Feedback(model, domain);
			if (beforeEating && containsEating(model)) {
				// find the first time that the concept "eating" appears in the model
				beforeEating = false;
				System.out.println("'eating' found at step "+step);
			}
			if (beforeEating) {
				modelScoreRegressionBeforeEating.addData(step++, feedback.getCorrectnessRatio());
				modelScoresBeforeEating.add(feedback.getCorrectnessRatio());
			} else {
				modelScoreRegressionAfterEating.addData(step++, feedback.getCorrectnessRatio());
				modelScoresAfterEating.add(feedback.getCorrectnessRatio());
			}
		}
	}
	
	private void calculateDomainRelations() {
		domainRelations= 0;
		domainRelatedRelations = 0;
		domainUnrelatedRelations = 0;
		Iterator<String> itr = relationSpace.iterator();
		while (itr.hasNext()) {
			
			try {
				String relationString = itr.next();
				String[] terms = relationString.split("-");
				String term1 = terms[0];
				String term2 = terms[1];
				ConceptType type1;
				ConceptType type2;
				type1 = domain.getConceptByTerm(term1).getType();
				type2 = domain.getConceptByTerm(term2).getType();
				
				if (type1.equals(ConceptType.DOMAIN_UNRELATED) || type2.equals(ConceptType.DOMAIN_UNRELATED)) {
					domainUnrelatedRelations++;
				} else if (type1.equals(ConceptType.DOMAIN_RELATED) || type2.equals(ConceptType.DOMAIN_RELATED)) {
					domainRelatedRelations++;
				} else {
					// both types are in the domain (stock, aux etc...)
					domainRelations++;
				}
			} catch (Exception e) {
				// something went wrong (e.g. can't get the type of one term)
				// relation is "domain unrelated"
				domainUnrelatedRelations++;
				break;
			}
		}
	}

	private void populateRelationSpace(IAction  action) {
		String modelString = action.getAttribute("model");
		if ( modelString != null) {
			Model model = new Model(null);
			model.setXmModel(JxmModel.readStringXML(modelString));
			for (JdRelation relation: model.getRelations()) {
				String term1;
				String term2;
				try {
					term1 = relation.getFigure1().getProperties().get("label").toString();
					term2 = relation.getFigure2().getProperties().get("label").toString();
					// delimiting the relation-node-names with an "-", as I can make sure that this
					// is not allowed in node-names
					relationSpace.add(term1+"-"+term2);
				} catch (NullPointerException e) {
					// NPE might happen if one of the figures is a flow-crtl
				}
			}
		}
	}
	
	private void populateNodeSpace(IAction  action) {
		String modelString = action.getAttribute("model");
		if ( modelString != null) {
			Model model = new Model(null);
			model.setXmModel(JxmModel.readStringXML(modelString));
			for (String nodeName: model.getNodes().keySet()) {
				if (nodeName.toLowerCase().startsWith("stock_")
						|| nodeName.toLowerCase().startsWith("aux_")
						|| nodeName.toLowerCase().startsWith("const_")) {
					// do nothing
				} else {
					termSpace.add(nodeName);
				}
			}
		}

	}

	private boolean containsEating(Model model) {
		for (String nodeName: model.getNodes().keySet()) {
			String concept = domain.getConceptNameByTerm(nodeName);
			if (concept != null && concept.equalsIgnoreCase("eating")) {
				return true;
			}
		}
		return false;
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
		try {
			SortedMap<Long, IAction> betweenActions = actions.subMap(firstAction.getTimeInMillis(), lastAction.getTimeInMillis());		
			//System.out.print("meaningful/feedback for "+this.userName+": checking "+betweenActions.size()+" between "+firstAction.getType()+" and "+lastAction.getType());
			for (IAction action : betweenActions.values()) {
				if (meaningfulAction(action)) {
					//System.out.println(" -> true");
					return true;
				} else {
					// no meaningful action found yet, continue for-loop
				}
			}
			// no meaningful action found at all, return false
			//System.out.println(" -> false");
			return false;
		} catch (Exception ex) {
			// in dubio pro reo
			return true;
		}
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

		int step = 1;
		for (IAction action: actions.values()) {
			String modelString = action.getAttribute("model");
			if ( modelString != null) {
				if (tempBestModel == null) {
					// setting the first best model;
					tempBestModel = new Model(null);
					tempBestModel.setXmModel(JxmModel.readStringXML(modelString));
					tempBestModelFeedback = new Feedback(tempBestModel, domain);
				} else {
					tempModel = new Model(null);
					tempModel.setXmModel(JxmModel.readStringXML(modelString));
					tempModelFeedback = new Feedback(tempModel, domain);
					//modelScoreStatistics.addValue(tempModelFeedback.getCorrectnessRatio());
					//modelScoreRegression.addData(step++, tempModelFeedback.getCorrectnessRatio());
					if (tempModelFeedback.getCorrectnessRatio() > tempBestModelFeedback.getCorrectnessRatio()) {
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
