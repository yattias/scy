package eu.scy.agents.agenda.evaluation;

import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import eu.scy.agents.agenda.evaluation.evaluators.ActionTypeEvaluator;

/**
 * This agent interprets users action logs and informs other agents about a semantic change done by a 
 * user in one of his ELOs.
 * 
 * Agents can also ask to get the latest state of some mission activities.
 * 
 * @author Chris
 */
public class ActivityModifiedEvaluationAgent extends AbstractActivityEvaluationAgent {

	private static final String AGENT_NAME = "activitymodifiedevaluator";
	
	private static final String REQUEST_TYPE = "last_modified";
	
	public static final String TYPE_MODIFIED = "modified";
	
	// (<ID>:String, <AgentName>:String, "last_modified":String, <Mission>:String, <UserName>:String, 
	// <Timestamp>:Long, <EloURIs>:String)
	private static final Tuple TEMPLATE_FOR_LAST_MODIFIED_REQUEST = 
		new Tuple(String.class, AGENT_NAME, REQUEST_TYPE, String.class, String.class,  Long.class, String.class);

	
	public ActivityModifiedEvaluationAgent(Map<String, Object> map) {
		super(ActivityModifiedEvaluationAgent.class.getName(), map);
	}

	@Override
	protected void initiateSignatures() {
		initiateSCYMapperSignature();
	}
	
	private void initiateSCYMapperSignature() {
		String toolName = "SCYMapper";
		List<String> scyMapperActionTypes = new ArrayList<String>();
		scyMapperActionTypes.add("node_added");
		scyMapperActionTypes.add("node_renamed");
		scyMapperActionTypes.add("node_removed");
		scyMapperActionTypes.add("link_added");
		scyMapperActionTypes.add("link_renamed");
		scyMapperActionTypes.add("link_removed");
		scyMapperActionTypes.add("link_flipped");
		this.signatures.put(toolName, new ActionTypeEvaluator(toolName, scyMapperActionTypes));
	}

	@Override
	protected void handleMatchingUserAction(long timestamp, String mission, String userName, String eloUri) {
		// user modified an ELO, so write a tuple to the space
		try {
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
