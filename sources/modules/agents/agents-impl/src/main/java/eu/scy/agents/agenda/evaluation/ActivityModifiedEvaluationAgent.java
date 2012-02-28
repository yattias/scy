package eu.scy.agents.agenda.evaluation;

import info.collide.sqlspaces.commons.Callback;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import eu.scy.agents.agenda.evaluation.evaluators.ActionTypeEvaluator;
import eu.scy.agents.agenda.evaluation.evaluators.IEvaluator;
import eu.scy.agents.agenda.evaluation.evaluators.RichtTextEvaluator;

/**
 * This agent interprets users action logs and informs other agents about a semantic change done by a 
 * user in one of his ELOs.
 * 
 * Agents can also ask to get the latest state of some mission activities.
 * 
 * @author Chris
 */
public class ActivityModifiedEvaluationAgent extends AbstractActivityEvaluationAgent {

	public static final String AGENT_NAME = "activitymodifiedevaluator";
	
	public static final String REQUEST_TYPE = "last_modified";
	
	public static final String TYPE_MODIFIED = "modified";
	
	// (<ID>:String, <AgentName>:String, "last_modified":String, <Mission>:String, <UserName>:String, 
	// <Timestamp>:Long, <EloURIs>:String)
	private static final Tuple TEMPLATE_FOR_LAST_MODIFIED_REQUEST = 
		new Tuple(String.class, AGENT_NAME, REQUEST_TYPE, String.class, String.class,  Long.class, String.class);

	
	public ActivityModifiedEvaluationAgent(Map<String, Object> map) {
		super(ActivityModifiedEvaluationAgent.class.getName(), map);
	}

	@Override
	protected void registerCallbacks() throws TupleSpaceException {
		Callback cb = new UserActionCallback();
		for(String tool: this.toolEvaluatorMap.keySet()) {
			Tuple actionSignature = new Tuple("action", String.class, Long.class, String.class, String.class, tool, Field.createWildCardField());
			int id = this.actionSpace.eventRegister(Command.WRITE, actionSignature, cb, true);
			this.registeredCallbacks.add(id);
			logger.info("Registered user action callback for tool: " + tool);
		}
	}
	
	@Override
	protected void initiateSignatures() {
		IEvaluator conceptMapEvaluator = createConceptMapEvaluator();
		IEvaluator copexEvaluator = createCopexEvaluator();
		IEvaluator fitexEvaluator = createFitexEvaluator();
		IEvaluator resultCardEvaluator = createResultCardEvaluator();
		IEvaluator richTextEvaluator = createRichTextEvaluator();
		IEvaluator simulatorEvaluator = createSimulatorEvaluator();
		registerEvaluator(conceptMapEvaluator);
		registerEvaluator(copexEvaluator);
		registerEvaluator(fitexEvaluator);
		registerEvaluator(resultCardEvaluator);
		registerEvaluator(richTextEvaluator);
		registerEvaluator(simulatorEvaluator);
	}
	
	private static IEvaluator createConceptMapEvaluator() {
		String toolName = "conceptmap";
		List<String> actionTypes = new ArrayList<String>();
		actionTypes.add("node_added");
		actionTypes.add("node_renamed");
		actionTypes.add("node_removed");
		actionTypes.add("link_added");
		actionTypes.add("link_renamed");
		actionTypes.add("link_removed");
		actionTypes.add("link_flipped");
		return new ActionTypeEvaluator(toolName, actionTypes);
	}

	private static IEvaluator createRichTextEvaluator() {
		String toolName = "richtext";
		List<String> actionTypes = new ArrayList<String>();
		actionTypes.add("text_inserted");
		actionTypes.add("text_deleted");
		return new RichtTextEvaluator(toolName, actionTypes);
	}
	
	private static IEvaluator createSimulatorEvaluator() {
		String toolName = "simulator";
		List<String> actionTypes = new ArrayList<String>();
		actionTypes.add("value_changed");
		actionTypes.add("row_added");
		return new ActionTypeEvaluator(toolName, actionTypes);
	}
	
	private static IEvaluator createCopexEvaluator() {
		String toolName = "copex";
		List<String> actionTypes = new ArrayList<String>();
		actionTypes.add("material_added");
		actionTypes.add("material_updated");
		actionTypes.add("material_deleted");
		actionTypes.add("step_added");
		actionTypes.add("step_updated");
		actionTypes.add("part_procedure_cut");
		actionTypes.add("part_procedure_pasted");
		actionTypes.add("part_procedure_deleted");
		return new ActionTypeEvaluator(toolName, actionTypes);
	}
	
	private static IEvaluator createResultCardEvaluator() {
		String toolName = "resultcard";
		List<String> actionTypes = new ArrayList<String>();
		actionTypes.add("result_binder_value_changed");
		return new ActionTypeEvaluator(toolName, actionTypes);
	}
	
	private static IEvaluator createFitexEvaluator() {
		String toolName = "fitex";
		List<String> scyMapperActionTypes = new ArrayList<String>();
		scyMapperActionTypes.add("data_edited");
		scyMapperActionTypes.add("data_ignored");
		scyMapperActionTypes.add("datasheet_cut");
		scyMapperActionTypes.add("datasheet_pasted");
		scyMapperActionTypes.add("datasheet_deleted");
		scyMapperActionTypes.add("operation_added");
		scyMapperActionTypes.add("datasheet_columns_inserted");
		scyMapperActionTypes.add("action_redone");
		scyMapperActionTypes.add("action_undone");
		return new ActionTypeEvaluator(toolName, scyMapperActionTypes);
	}
	
//	private static IEvaluator createInterviewEvaluator() {
//		String toolName = "interview";
//		List<String> actionTypes = new ArrayList<String>();
//		actionTypes.add("question_changed");
//		actionTypes.add("topic_added");
//		actionTypes.add("indicators_shown");
//		actionTypes.add("text_deleted");
//		return new ActionTypeEvaluator(toolName, actionTypes);
//	}
	
//	private static IEvaluator createScyDesktopEvaluator() {
//	String toolName = "scy-desktop";
//	List<String> actionTypes = new ArrayList<String>();
//	actionTypes.add("las_changed");
//	return new ActionTypeEvaluator(toolName, actionTypes);
//}

//private static IEvaluator createScyDynamicsEvaluator() {
//	String toolName = "scy-dynamics";
//	List<String> actionTypes = new ArrayList<String>();
//	actionTypes.add("graph_viewed");
//	actionTypes.add("table_viewed");
//	return new ActionTypeEvaluator(toolName, actionTypes);
//}

	@Override
	protected void handleMatchingUserAction(long timestamp, String mission, String userName, String tool, String eloUri) {
		// user modified an ELO, so write a tuple to the space
		try {
			logger.debug(String.format(
					"Writing modification tuple to TupleSpace [ User: %s | Mission: %s | Tool: %s | EloURI: %s]", 
					userName, mission, tool, eloUri));
			Tuple modificationTuple = new Tuple(TYPE_MODIFIED, mission, userName, eloUri, timestamp);
			this.commandSpace.write(modificationTuple);
		} catch (TupleSpaceException e) {
			logger.error("Could not write activity modified tuple!");
		}
	}

	@Override
	protected Tuple getRequestTemplate() {
		return TEMPLATE_FOR_LAST_MODIFIED_REQUEST;
	}
	
}
