package eu.scy.agents.agenda.evaluation;

import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import eu.scy.agents.agenda.evaluation.evaluators.ActionTypeEvaluator;
import eu.scy.agents.agenda.evaluation.evaluators.IEvaluator;

public class ActivityFinishedEvaluationAgent extends AbstractActivityEvaluationAgent {

	public static final String AGENT_NAME = "activityfinishedevaluator";
	
	public static final String REQUEST_TYPE = "last_finished";
	
	public static final String TYPE_FINISHED = "finished";
	
	// (<ID>:String, <AgentName>:String, "last_finished":String, <Mission>:String, <UserName>:String, 
	// <Timestamp>:Long, <EloURIs>:String)
	private static final Tuple TEMPLATE_FOR_LAST_FINISHED_REQUEST = 
		new Tuple(String.class, AGENT_NAME, REQUEST_TYPE, String.class, String.class,  Long.class, String.class);


	public ActivityFinishedEvaluationAgent(Map<String, Object> map) {
		super(ActivityFinishedEvaluationAgent.class.getName(), map);
	}

	@Override
	protected void initiateSignatures() {
		initiateFinishedSignatures();
	}
	
	private void initiateFinishedSignatures() {
		// TODO insert correct values here
		
		IEvaluator notificationEvaluator = createNotificationEvaluator();
		registerEvaluator(notificationEvaluator);
	}
	
	private IEvaluator createNotificationEvaluator() {
		String toolName = "scy-desktop";
		List<String> actionTypes = new ArrayList<String>();
		actionTypes.add("elo_finished");
		return new ActionTypeEvaluator(toolName, actionTypes);
	}

	@Override
	protected void handleMatchingUserAction(long timestamp, String mission, String userName, String tool, String eloUri) {
		// user modified an ELO, so write a tuple to the space
		try {
			Tuple modificationTuple = new Tuple(TYPE_FINISHED, mission, userName, eloUri, timestamp);
			logger.debug(String.format(
					"Writing finish tuple to TupleSpace [ User: %s | Mission: %s | Tool: %s | EloURI: %s]", 
					userName, mission, tool, eloUri));
			this.commandSpace.write(modificationTuple);
		} catch (TupleSpaceException e) {
			logger.error("Could not write activity finished tuple!");
		}
	}

	@Override
	protected Tuple getRequestTemplate() {
		return TEMPLATE_FOR_LAST_FINISHED_REQUEST;
	}
	
}
