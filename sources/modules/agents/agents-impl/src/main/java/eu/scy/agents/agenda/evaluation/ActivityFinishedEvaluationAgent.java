package eu.scy.agents.agenda.evaluation;

import info.collide.sqlspaces.commons.Callback;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.util.Map;

import eu.scy.agents.agenda.evaluation.evaluators.IEvaluator;
import eu.scy.agents.agenda.evaluation.evaluators.ToolEvaluator;

public class ActivityFinishedEvaluationAgent extends AbstractActivityEvaluationAgent {

	private static final String ACTION_TYPE_ELO_FINISHED = "elo_finished";

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
	protected void registerCallbacks() throws TupleSpaceException {
		// ("action":String, <ID>:String, <Timestamp>:long, <Type>:String, <User>:String, <Tool>:String,
		//  <Mission>:String, <Session>:String, <ELOUri>:String, <Key=Value>:String*)
		Callback cb = new UserActionCallback();
		Tuple eloFinishedSignature = new Tuple("action", String.class, Long.class, ACTION_TYPE_ELO_FINISHED, String.class, String.class, String.class, String.class, String.class, Field.createWildCardField());
		int id = this.actionSpace.eventRegister(Command.WRITE, eloFinishedSignature, cb, true);
		this.registeredCallbacks.add(id);
		logger.info("Registered elo finished callback");
	}
	
	@Override
	protected void initiateSignatures() {
		IEvaluator conceptMapEvaluator = new ToolEvaluator("conceptmap");
		IEvaluator richTextEvaluator = new ToolEvaluator("richtext");
		IEvaluator simulatorEvaluator = new ToolEvaluator("simulator");
		IEvaluator copexEvaluator = new ToolEvaluator("copex");
		IEvaluator resultCardEvaluator = new ToolEvaluator("resultcard");
		IEvaluator fitexEvaluator = new ToolEvaluator("fitex");
		registerEvaluator(conceptMapEvaluator);
		registerEvaluator(richTextEvaluator);
		registerEvaluator(simulatorEvaluator);
		registerEvaluator(copexEvaluator);
		registerEvaluator(resultCardEvaluator);
		registerEvaluator(fitexEvaluator);
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
