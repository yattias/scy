package eu.scy.agents.agenda.guidance;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Callback;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import roolo.api.IRepository;
import roolo.elo.api.IMetadataTypeManager;
import eu.scy.agents.IRepositoryAgent;
import eu.scy.agents.agenda.evaluation.ActivityFinishedEvaluationAgent;
import eu.scy.agents.agenda.evaluation.ActivityModifiedEvaluationAgent;
import eu.scy.agents.agenda.guidance.model.MissionModel;
import eu.scy.agents.agenda.guidance.model.UserModel;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.impl.AbstractThreadedAgent;
import eu.scy.agents.impl.AgentProtocol;
import eu.scy.agents.session.Session;

public class PedagogicalGuidanceAgent extends AbstractThreadedAgent implements IRepositoryAgent {

	private static final Logger logger = Logger.getLogger(PedagogicalGuidanceAgent.class.getName());
	
	private static final Tuple TEMPLATE_FOR_MODIFIED_ACTIVITY = new Tuple(ActivityModifiedEvaluationAgent.TYPE_MODIFIED, String.class, String.class, String.class, Long.class);
	private static final Tuple TEMPLATE_FOR_FINISHED_ACTIVITY = new Tuple(ActivityFinishedEvaluationAgent.TYPE_FINISHED, String.class, String.class, String.class, Long.class);
	
	private final List<Integer> registeredCallbacks = new ArrayList<Integer>();
	private UserModelDictionary userModelDict;
	private RooloAccessor rooloAccessor;
	private MissionTupleConsumer missionTupleConsumer; 

	private TupleSpace actionSpace;
	private TupleSpace commandSpace;


    
	public PedagogicalGuidanceAgent(Map<String, Object> map) {
        super(PedagogicalGuidanceAgent.class.getName(), (String) map.get(AgentProtocol.PARAM_AGENT_ID), (String) map.get(AgentProtocol.TS_HOST), (Integer) map.get(AgentProtocol.TS_PORT));

        try {
			this.commandSpace = new TupleSpace(new User(getSimpleName()), host, port, false, false, AgentProtocol.COMMAND_SPACE_NAME);
			this.actionSpace = new TupleSpace(new User(getSimpleName()), host, port, false, false, AgentProtocol.ACTION_SPACE_NAME);

			rooloAccessor = new RooloAccessor();
			Session session = new Session(new TupleSpace(new User(getSimpleName()), host, port, false, false, AgentProtocol.SESSION_SPACE_NAME));
			this.userModelDict = new UserModelDictionary(rooloAccessor, session);
			
			this.missionTupleConsumer = new MissionTupleConsumer(this.userModelDict);
		} catch (TupleSpaceException e) {
			logger.error(getName() + " could not be started, because connection to the TupleSpace could not be established.");
		}
	}

	@Override
	protected void doRun() throws TupleSpaceException, AgentLifecycleException, InterruptedException {
		startMissionTupleConsumer();
		registerActivityCallbacks();
		
		while (this.status == Status.Running) {
            try {
                sendAliveUpdate();
            } catch (TupleSpaceException e) {
                e.printStackTrace();
            }
            Thread.sleep(AgentProtocol.ALIVE_INTERVAL);
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
        this.missionTupleConsumer.stop();
	}

	@Override
	public boolean isStopped() {
        return (this.status != Status.Running);
	}
	
	private void startMissionTupleConsumer() {
		Thread missionTupleConsumerThread = new Thread(this.missionTupleConsumer);
		missionTupleConsumerThread.start();
	}

	private void registerActivityCallbacks() throws TupleSpaceException {
		Callback amcb = new ActivityChangedCallback();
		int id = this.commandSpace.eventRegister(Command.WRITE, TEMPLATE_FOR_MODIFIED_ACTIVITY, amcb, true);
		this.registeredCallbacks.add(id);
		logger.debug("Registered callback for modified activities");

		Callback afcb = new ActivityChangedCallback();
		id = this.commandSpace.eventRegister(Command.WRITE, TEMPLATE_FOR_FINISHED_ACTIVITY, afcb, true);
		this.registeredCallbacks.add(id);
		logger.debug("Registered callback for finished activities");
	}
	
	class ActivityChangedCallback implements Callback {
		
		@Override
		public void call(Command cmd, int seqnum, Tuple afterTuple, Tuple beforeTuple) {
			processTuple(afterTuple);
		}
	}
	
	private void processTuple(Tuple tuple) {
		// Signature: ("modified"/"finished":String, mission:String, userName:String, eloUri:String, timestamp:long)
		String type = tuple.getField(0).getValue().toString();
		String missionRuntimeUri = tuple.getField(1).getValue().toString();
		String userName = tuple.getField(2).getValue().toString();
			
		try {
			UserModel userModel = this.userModelDict.getOrCreateUserModel(userName);
			MissionModel missionModel = userModel.getOrCreateMissionModel(missionRuntimeUri);
			logger.debug(String.format("Received activity change tuple [ type: %s | user: %s | missionRuntimeUri: %s ]", type, userName, missionRuntimeUri));
			missionModel.processTuple(tuple);

		} catch (IllegalArgumentException e) {
			logger.warn("Ignoring activity change tuple with invalid type " + tuple.toString());
		}
	}

    @Override
    public void setRepository(IRepository rep) {
        this.rooloAccessor.setRepository(rep);
    }

    @Override
    public void setMetadataTypeManager(IMetadataTypeManager manager) {
//        this.metadataTypeManager = manager;
//        this.rooloAccessor.setTypeManager(manager);
    }

}

