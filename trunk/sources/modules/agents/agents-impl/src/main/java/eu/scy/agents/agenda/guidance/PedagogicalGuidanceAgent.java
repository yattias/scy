package eu.scy.agents.agenda.guidance;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Callback;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import eu.scy.agents.agenda.evaluation.ActivityFinishedEvaluationAgent;
import eu.scy.agents.agenda.evaluation.ActivityModifiedEvaluationAgent;
import eu.scy.agents.agenda.guidance.model.MissionModel;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.impl.AbstractThreadedAgent;
import eu.scy.agents.impl.AgentProtocol;

public class PedagogicalGuidanceAgent extends AbstractThreadedAgent {

	private static final Tuple TEMPLATE_FOR_MODIFIED_ACTIVITY = new Tuple(ActivityModifiedEvaluationAgent.TYPE_MODIFIED, String.class, String.class, String.class, Long.class);
	
	private static final Tuple TEMPLATE_FOR_FINISHED_ACTIVITY = new Tuple(ActivityFinishedEvaluationAgent.TYPE_FINISHED, String.class, String.class, String.class, Long.class);
	
	private static final Logger logger = Logger.getLogger(PedagogicalGuidanceAgent.class.getName());
	
	private final List<Integer> registeredCallbacks = new ArrayList<Integer>();
	
	// userName -> (missionName -> Mission)
	private final Map<String, Map<String, MissionModel>> userMissionMap = new HashMap<String, Map<String, MissionModel>>();

	private TupleSpace actionSpace;
	
	private TupleSpace commandSpace;
	
	private RooloAccessor rooloAccessor;
	
	public PedagogicalGuidanceAgent(Map<String, Object> map) {
        super(PedagogicalGuidanceAgent.class.getName(), (String) map.get(AgentProtocol.PARAM_AGENT_ID), (String) map.get(AgentProtocol.TS_HOST), (Integer) map.get(AgentProtocol.TS_PORT));

        try {
			this.commandSpace = new TupleSpace(new User(getSimpleName()), host, port, false, false, AgentProtocol.COMMAND_SPACE_NAME);
			this.actionSpace = new TupleSpace(new User(getSimpleName()), host, port, false, false, AgentProtocol.ACTION_SPACE_NAME);

			this.rooloAccessor = new RooloAccessor(this.commandSpace, logger);
		} catch (TupleSpaceException e) {
			logger.error(getName() + " could not be started, because connection to the TupleSpace could not be established.");
		}
	}

	@Override
	protected void doRun() throws TupleSpaceException, AgentLifecycleException, InterruptedException {
		registerCallbacks();
        while (this.status == Status.Running) {
            try {
                sendAliveUpdate();
            } catch (TupleSpaceException e) {
                e.printStackTrace();
            }
            
            // Perform stuff
        }
	}

	@Override
	protected void doStop() throws AgentLifecycleException {
        try {
            this.actionSpace.disconnect();
        } catch (TupleSpaceException e) {
    		logger.error("Error while disconnecting from ActionSpace!");
        }
        try {
        	this.commandSpace.disconnect();
        } catch (TupleSpaceException e) {
        	logger.error("Error while disconnecting from CommandSpace!");
        }
	}

	@Override
	public boolean isStopped() {
        return (this.status != Status.Running);
	}

	private void registerCallbacks() throws TupleSpaceException {
		Callback amcb = new ActivityModifiedCallback();
		int id = this.commandSpace.eventRegister(Command.WRITE, TEMPLATE_FOR_MODIFIED_ACTIVITY, amcb, true);
		this.registeredCallbacks.add(id);
		logger.debug("Registered activity modified callback");

		Callback afcb = new ActivityFinishedCallback();
		id = this.commandSpace.eventRegister(Command.WRITE, TEMPLATE_FOR_FINISHED_ACTIVITY, afcb, true);
		this.registeredCallbacks.add(id);
		logger.debug("Registered activity finished callback");
	}
	
	class ActivityModifiedCallback implements Callback {

		@Override
		public void call(Command cmd, int seqnum, Tuple afterTuple,
				Tuple beforeTuple) {
			
			processTuple(afterTuple);
		}
		
	}
	
	class ActivityFinishedCallback implements Callback {

		@Override
		public void call(Command cmd, int seqnum, Tuple afterTuple,
				Tuple beforeTuple) {
			
			processTuple(afterTuple);
		}
		
	}
	
	private void processTuple(Tuple tuple) {
		// ("modified"/"finished":String, mission:String, userName:String, eloUri:String, timestamp:long)
		String userName = tuple.getField(2).getValue().toString();
		
		Map<String, MissionModel> missionModelMap = this.userMissionMap.get(userName);
		if(missionModelMap == null) {
			missionModelMap = new HashMap<String, MissionModel>();
		}

		// get the Mission model
		String missionName = tuple.getField(1).getValue().toString();
		MissionModel missionModel = missionModelMap.get(missionName);
		
		if(missionModel == null) {
			
		}
		
		String eloUri = tuple.getField(3).getValue().toString();
		long timestamp = Long.valueOf(tuple.getField(4).getValue().toString());
		
		
	}
	
	private MissionModel generateNewMissionModel(String missionName, String userName) throws TupleSpaceException {
		List<String> eloUris = this.rooloAccessor.getEloUris(missionName, userName);

		// TODO: generate a new model
		return null;
	}
	
}

