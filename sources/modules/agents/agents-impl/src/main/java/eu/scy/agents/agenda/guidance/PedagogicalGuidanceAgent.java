package eu.scy.agents.agenda.guidance;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Callback;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.User;

import java.rmi.dgc.VMID;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import roolo.api.IRepository;
import roolo.elo.api.IMetadataTypeManager;
import eu.scy.agents.AgentRooloServiceImpl;
import eu.scy.agents.IRepositoryAgent;
import eu.scy.agents.agenda.evaluation.ActivityFinishedEvaluationAgent;
import eu.scy.agents.agenda.evaluation.ActivityModifiedEvaluationAgent;
import eu.scy.agents.agenda.guidance.event.ActivityModificationEvent;
import eu.scy.agents.agenda.guidance.event.ActivityModificationListener;
import eu.scy.agents.agenda.guidance.event.HistoryRequestEvent;
import eu.scy.agents.agenda.guidance.event.HistoryRequestListener;
import eu.scy.agents.agenda.guidance.model.Activity;
import eu.scy.agents.agenda.guidance.model.MissionModel;
import eu.scy.agents.agenda.guidance.model.UserModel;
import eu.scy.agents.agenda.serialization.UserAction;
import eu.scy.agents.agenda.serialization.UserAction.Type;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.impl.AbstractThreadedAgent;
import eu.scy.agents.impl.AgentProtocol;
import eu.scy.agents.session.Session;
import eu.scy.common.scyelo.RooloServices;

public class PedagogicalGuidanceAgent extends AbstractThreadedAgent implements IRepositoryAgent, ActivityModificationListener, HistoryRequestListener {

	private static final Logger logger = Logger.getLogger(PedagogicalGuidanceAgent.class.getName());

	private static final String REQUEST_HISTORY_RESPONSE = "response";
	private static final long REQUEST_HISTORY_TIMEOUT = 5000;
																										
	private static final Tuple TEMPLATE_FOR_MODIFIED_ACTIVITY = new Tuple(ActivityModifiedEvaluationAgent.TYPE_MODIFIED, String.class, String.class, String.class, Long.class);
	private static final Tuple TEMPLATE_FOR_FINISHED_ACTIVITY = new Tuple(ActivityFinishedEvaluationAgent.TYPE_FINISHED, String.class, String.class, String.class, Long.class);
	private static final Tuple TEMPLATE_FOR_USER_LOGIN = new Tuple("action", String.class, Long.class, "logged_in", String.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class);
	
	private static final RooloAccessor rooloAccessor = new RooloAccessor();
	private static final MetadataAccessor metadataAccessor = new MetadataAccessor();
	private static AgentRooloServiceImpl rooloServices;

	private final List<Integer> registeredCallbacks = new ArrayList<Integer>();
	private UserModelDictionary userModelDict;
	private MissionTupleConsumer missionTupleConsumer; 

	private TupleSpace actionSpace;
	private TupleSpace commandSpace;
	
    
	public PedagogicalGuidanceAgent(Map<String, Object> map) {
        super(PedagogicalGuidanceAgent.class.getName(), (String) map.get(AgentProtocol.PARAM_AGENT_ID), (String) map.get(AgentProtocol.TS_HOST), (Integer) map.get(AgentProtocol.TS_PORT));
        
        this.rooloServices = new AgentRooloServiceImpl();
        
        try {
        	this.actionSpace = new TupleSpace(new User(getSimpleName()), host, port, false, false, AgentProtocol.ACTION_SPACE_NAME);
			this.commandSpace = new TupleSpace(new User(getSimpleName()), host, port, false, false, AgentProtocol.COMMAND_SPACE_NAME);
			TupleSpace sessionSpace = new TupleSpace(new User(getSimpleName()), host, port, false, false, AgentProtocol.SESSION_SPACE_NAME);
			
			Session session = new Session(sessionSpace);
			this.userModelDict = new UserModelDictionary(session);
			
			this.missionTupleConsumer = new MissionTupleConsumer(this.userModelDict);
		} catch (TupleSpaceException e) {
			logger.error(getName() + " could not be started, because connection to the TupleSpace could not be established.");
		}
	}

	@Override
	protected void doRun() throws TupleSpaceException, AgentLifecycleException, InterruptedException {
		startMissionTupleConsumer();
		registerActivityCallbacks();
		registerLoginCallback();
		
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
		Callback acc = new ActivityChangedCallback();
		int id = this.commandSpace.eventRegister(Command.WRITE, TEMPLATE_FOR_MODIFIED_ACTIVITY, acc, true);
		this.registeredCallbacks.add(id);
		logger.debug("Registered callback for modified activities");

		id = this.commandSpace.eventRegister(Command.WRITE, TEMPLATE_FOR_FINISHED_ACTIVITY, acc, true);
		this.registeredCallbacks.add(id);
		logger.debug("Registered callback for finished activities");
	}
	
	private void registerLoginCallback() throws TupleSpaceException {
		Callback ulc = new UserLoginCallback();
		int id = this.actionSpace.eventRegister(Command.WRITE, TEMPLATE_FOR_USER_LOGIN, ulc, true);
		this.registeredCallbacks.add(id);
		logger.debug("Registered callback for user logins");
	}
	
	class UserLoginCallback implements Callback {

		@Override
		public void call(Command cmd, int seqnum, Tuple afterTuple, Tuple beforeTuple) {
			processLoginTuple(afterTuple);
		}
	}
	
	private void processLoginTuple(Tuple tuple) {
		String userName = tuple.getField(4).getValue().toString();
		String missionRuntimeUri = tuple.getField(6).getValue().toString();
		
		// check if model is already present, if not create it
		UserModel userModel = this.userModelDict.getOrCreateUserModel(userName);
		MissionModel missionModel = userModel.getOrCreateMissionModel(missionRuntimeUri);
		if(missionModel.isEnabled()) {
			// only register listener when MissionModel was just created
			missionModel.addActivityModificationListener(this);
			missionModel.setHistoryRequestListener(this);
			missionModel.build();
		}
	}
	
	class ActivityChangedCallback implements Callback {
		
		@Override
		public void call(Command cmd, int seqnum, Tuple afterTuple, Tuple beforeTuple) {
			processActionTuple(afterTuple);
		}
	}
	
	private void processActionTuple(Tuple tuple) {
		// Signature: ("modified"/"finished":String, mission:String, userName:String, eloUri:String, timestamp:long)
		String type = tuple.getField(0).getValue().toString();
		String missionRuntimeUri = tuple.getField(1).getValue().toString();
		String userName = tuple.getField(2).getValue().toString();
			
		try {
			UserModel userModel = this.userModelDict.getUserModel(userName);
			if(userModel == null) {
				logger.warn("No user model found for user: " + userName);
				return;
			}
			MissionModel missionModel = userModel.getMission(missionRuntimeUri);
			if(missionModel == null) {
				logger.warn(String.format("No mission model found for user: %s and mission: %s", userName, missionRuntimeUri));
				return;
			}
			
			logger.debug(String.format("Received activity change tuple [ type: %s | user: %s | missionRuntimeUri: %s ]", type, userName, missionRuntimeUri));
			missionModel.processTuple(tuple);

		} catch (IllegalArgumentException e) {
			logger.warn("Ignoring activity change tuple with invalid type " + tuple.toString());
		}
	}

    @Override
    public void setRepository(IRepository rep) {
        rooloAccessor.setRepository(rep);
        rooloServices.setRepository(rep);
    }

    @Override
    public void setMetadataTypeManager(IMetadataTypeManager manager) {
    	metadataAccessor.setMetadataTypeManager(manager);
    	rooloServices.setMetadataTypeManager(manager);
    }

    public static RooloAccessor getRooloAccessor() {
    	return rooloAccessor;
    }

    public static MetadataAccessor getMetadataAccessor() {
    	return metadataAccessor;
    }

    public static RooloServices getRooloServices() {
    	return rooloServices;
    }

	@Override
	public void activityModified(ActivityModificationEvent event) {
		MissionModel missionModel = (MissionModel) event.getSource();
		Activity activity = event.getActivity();

		Tuple modificationTuple = new Tuple();
		modificationTuple.add("notification");
		modificationTuple.add("valid_activities");
		modificationTuple.add(missionModel.getUser());
		modificationTuple.add(missionModel.getMissionRuntimeUri());
		modificationTuple.add(activity.getLatestModificationTime());
		modificationTuple.add(activity.getEloUri());
		
		logger.debug(modificationTuple);
		// TODO write tuple to tuple space
	}

	@Override
	public List<UserAction> requestHistory(HistoryRequestEvent event) throws TupleSpaceException {
		// (<ID>:String, <AgentName>:String, "last_modified":String, <Mission>:String, <UserName>:String, 
		// <Timestamp>:Long, <EloURIs>:String)

		Map<String, UserAction> modificationHistory = getHistory(event, ActivityModifiedEvaluationAgent.AGENT_NAME, ActivityModifiedEvaluationAgent.REQUEST_TYPE);
		Map<String, UserAction> completionHistory = getHistory(event, ActivityFinishedEvaluationAgent.AGENT_NAME, ActivityFinishedEvaluationAgent.REQUEST_TYPE);
		return mergeHistory(modificationHistory, completionHistory);
	}
	
	private List<UserAction> mergeHistory(Map<String, UserAction> modificationHistory, Map<String, UserAction> completionHistory) {
		List<UserAction> result = new ArrayList<UserAction>();
		for(UserAction modAction : modificationHistory.values()) {
			UserAction compAction = completionHistory.get(modAction.getEloUri());
			if(modAction.getTimestamp() > compAction.getTimestamp()) {
				result.add(modAction);
			} else {
				compAction.setActiontype(Type.COMPLETION);
				result.add(compAction);
			}
		}
		return result;
	}
	
	private Map<String, UserAction> getHistory(HistoryRequestEvent event, String agentName, String requestType) throws TupleSpaceException {
		MissionModel missionModel = (MissionModel) event.getSource();
		
		StringBuilder sb = new StringBuilder();
		boolean start = true;
		for(Activity act : event.getActivities()) {
			if(start) {
				start = false;
			} else {
				sb.append(",");
			}
			sb.append(act.getEloUri());
		}
		
		VMID requestId = new VMID();
		Tuple requestTuple = new Tuple(
				requestId, 
				agentName,
				requestType,
				missionModel.getMissionRuntimeUri(), 
				missionModel.getUser(),
				event.getTimestamp(),
				sb.toString());
		
		Tuple responseSignature = new Tuple(REQUEST_HISTORY_RESPONSE, requestId, String.class);
		this.commandSpace.write(requestTuple);
		Tuple responseTuple = this.commandSpace.waitToTake(responseSignature, REQUEST_HISTORY_TIMEOUT);
		if(responseTuple == null) {
			// TODO handle agent down
		}
		String responseAsString = responseTuple.getField(2).getValue().toString();
		return UserAction.deserializeUserActions(responseAsString);
	}

}

