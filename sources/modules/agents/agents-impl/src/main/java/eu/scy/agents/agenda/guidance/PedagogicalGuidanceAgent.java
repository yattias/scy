package eu.scy.agents.agenda.guidance;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Callback;
import info.collide.sqlspaces.commons.Field;
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
import eu.scy.agents.agenda.guidance.event.HistoryRequestEvent;
import eu.scy.agents.agenda.guidance.event.HistoryRequestListener;
import eu.scy.agents.agenda.guidance.event.SendMessageEvent;
import eu.scy.agents.agenda.guidance.event.SendMessageListener;
import eu.scy.agents.agenda.guidance.event.StatusChangedEvent;
import eu.scy.agents.agenda.guidance.event.StatusChangedListener;
import eu.scy.agents.agenda.guidance.model.Activity;
import eu.scy.agents.agenda.guidance.model.MissionModel;
import eu.scy.agents.agenda.guidance.model.UserModel;
import eu.scy.agents.agenda.serialization.UserAction;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.impl.AbstractThreadedAgent;
import eu.scy.agents.impl.AgentProtocol;

public class PedagogicalGuidanceAgent extends AbstractThreadedAgent implements IRepositoryAgent, StatusChangedListener, HistoryRequestListener, SendMessageListener {

	private static final Logger logger = Logger.getLogger(PedagogicalGuidanceAgent.class.getName());

//	private static final String REQUEST_HISTORY_RESPONSE = "response";
//	private static final long REQUEST_HISTORY_TIMEOUT = 5000;
	
	private static final String ACTION_TYPE_LOGGED_IN = "logged_in";
	private static final String ACTION_TYPE_LAS_CHANGED = "las_changed";
	private static final String TOOL_LAS_CHANGED = "scy-desktop";
	private static final Tuple TEMPLATE_FOR_MODIFIED_ACTIVITY = new Tuple(ActivityModifiedEvaluationAgent.TYPE_MODIFIED, String.class, String.class, String.class, Long.class);
	private static final Tuple TEMPLATE_FOR_FINISHED_ACTIVITY = new Tuple(ActivityFinishedEvaluationAgent.TYPE_FINISHED, String.class, String.class, String.class, Long.class);
	private static final Tuple TEMPLATE_FOR_USER_LOGIN = new Tuple("action", String.class, Long.class, ACTION_TYPE_LOGGED_IN, String.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class);
	private static final Tuple TEMPLATE_FOR_LAS_CHANGED = new Tuple("action", String.class, Long.class, ACTION_TYPE_LAS_CHANGED, String.class, TOOL_LAS_CHANGED, Field.createWildCardField());
	

	private final List<Integer> registeredCallbacks = new ArrayList<Integer>();
	private AgentRooloServiceImpl rooloServices;
	private UserModelDictionary userModelDict;

	private TupleSpace actionSpace;
	private TupleSpace commandSpace;
	
    
	public PedagogicalGuidanceAgent(Map<String, Object> map) {
        super(PedagogicalGuidanceAgent.class.getName(), (String) map.get(AgentProtocol.PARAM_AGENT_ID), (String) map.get(AgentProtocol.TS_HOST), (Integer) map.get(AgentProtocol.TS_PORT));
        
        try {
        	this.actionSpace = new TupleSpace(new User(getSimpleName()), host, port, false, false, AgentProtocol.ACTION_SPACE_NAME);
			this.commandSpace = new TupleSpace(new User(getSimpleName()), host, port, false, false, AgentProtocol.COMMAND_SPACE_NAME);
			
			this.rooloServices = new AgentRooloServiceImpl();
			this.userModelDict = new UserModelDictionary(rooloServices);
			
		} catch (TupleSpaceException e) {
			logger.error(getName() + " could not be started, because connection to the TupleSpace could not be established.");
		}
	}

	@Override
	protected void doRun() throws TupleSpaceException, AgentLifecycleException, InterruptedException {
		registerActivityCallbacks();
		registerLoginCallback();
		registerLasChangedCallback();
		
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
	}

	@Override
	public boolean isStopped() {
        return (this.status != Status.Running);
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
	
	private void registerLasChangedCallback() throws TupleSpaceException {
		Callback lcc = new LasChangedCallback();
		int id = this.actionSpace.eventRegister(Command.WRITE, TEMPLATE_FOR_LAS_CHANGED, lcc, true);
		this.registeredCallbacks.add(id);
		logger.debug("Registered callback for las changed logs");
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
			// first login: register listener when no MissionModel exists
			missionModel.setActivityModificationListener(this);
			missionModel.setSendMessageListener(this);
//			missionModel.setHistoryRequestListener(this);
			missionModel.build();
		} else {
			// model has already been built... fill the curtain with messages
			missionModel.setReinitiateCurtain(true);
		}
	}
	
	class LasChangedCallback implements Callback {

		@Override
		public void call(Command cmd, int seqnum, Tuple afterTuple,
				Tuple beforeTuple) {
			processLasChangedTuple(afterTuple);
		}
	}
	
	private void processLasChangedTuple(Tuple tuple) {
		String userName = tuple.getField(4).getValue().toString();
		String missionRuntimeUri = tuple.getField(6).getValue().toString();
		
		// check if model is already present, if not create it
		UserModel userModel = this.userModelDict.getUserModel(userName);
		MissionModel missionModel = userModel.getMissionModel(missionRuntimeUri);
		if(missionModel.isReinitiateCurtain()) {
			missionModel.reinitiateCurtain();
		}
	}
	
	class ActivityChangedCallback implements Callback {
		
		@Override
		public void call(Command cmd, int seqnum, Tuple afterTuple, Tuple beforeTuple) {
			processActionTuple(afterTuple);
		}
	}
	
	private void processActionTuple(Tuple tuple) {
		// ("modified"/"finished":String, mission:String, userName:String, eloUri:String, timestamp:long)
		String type = tuple.getField(0).getValue().toString();
		String missionRuntimeUri = tuple.getField(1).getValue().toString();
		String userName = tuple.getField(2).getValue().toString();
		String eloUri = tuple.getField(3).getValue().toString();
			
		try {
			UserModel userModel = this.userModelDict.getUserModel(userName);
			if(userModel == null) {
				logger.warn("No user model found for user: " + userName);
				return;
			}
			MissionModel missionModel = userModel.getMissionModel(missionRuntimeUri);
			if(missionModel == null) {
				logger.warn(String.format("No mission model found for user: %s and mission: %s", userName, missionRuntimeUri));
				return;
			}
			
			logger.debug(String.format(
					"Received activity change tuple [ type: %s | user: %s | missionRuntimeUri: %s | ELO URI: %s ]", 
					type, 
					userName, 
					missionRuntimeUri,
					eloUri));
			missionModel.processTuple(tuple);

		} catch (IllegalArgumentException e) {
			logger.warn("Ignoring activity change tuple with invalid type " + tuple.toString());
		}
	}

    @Override
    public void setRepository(IRepository rep) {
        rooloServices.setRepository(rep);
    }

    @Override
    public void setMetadataTypeManager(IMetadataTypeManager manager) {
    	rooloServices.setMetadataTypeManager(manager);
    }

	@Override
	public void statusChanged(StatusChangedEvent event) {
		MissionModel missionModel = (MissionModel) event.getSource();
		Activity activity = event.getActivity();

        try {
            Tuple statusNotificationTuple = createStatusNotificationTuple(
            		missionModel.getUserName(), 
            		activity.getEloTitle(), 
            		event.getNewState().getText(), 
            		activity.getLastModificationTime(), 
            		activity.getFirstVersionEloUri());
            logger.debug("Writing status changed tuple: " + statusNotificationTuple);
            commandSpace.write(statusNotificationTuple);
        } catch (TupleSpaceException e) {
            logger.info("Error in TupleSpace while load an object in roolo");
        }
	}

    private Tuple createStatusNotificationTuple(String user, String eloTitle, String status, long time, String eloUri) {
        Tuple notificationTuple = new Tuple();
        notificationTuple.add(AgentProtocol.NOTIFICATION); // 1
        notificationTuple.add(new VMID().toString()); // 2
        notificationTuple.add(user); // 3
        notificationTuple.add("scylab"); // 4
        notificationTuple.add("process guidance agent"); // 5
        notificationTuple.add("mission"); // 6
        notificationTuple.add("session"); // 7
        notificationTuple.add("type=agenda_notify"); // 8
        notificationTuple.add("text=" + eloTitle); // 9
        notificationTuple.add("timestamp=" + time); // 10
        notificationTuple.add("state=" + status); // 11
        notificationTuple.add("elouri=" + eloUri); // 12
        return notificationTuple;
    }

	@Override
	public void sendMessage(SendMessageEvent event) {
		MissionModel missionModel = (MissionModel)event.getSource();

        try {
            Tuple messageNotificationTuple = createMessageNotificationTuple(
            		missionModel.getUserName(), 
            		event.getMessage(), 
            		event.getTimestamp());
            logger.debug("Writing message tuple: " + messageNotificationTuple);
            commandSpace.write(messageNotificationTuple);
        } catch (TupleSpaceException e) {
            logger.error("Could not write message to TupleSpace!", e);
        }
	}
	
    private Tuple createMessageNotificationTuple(String userName, String message, long time) {
    	// ("notification":String, <id>:String, "userName":String, "scylab":String, "process guidance agent":String, 
    	// "mission":String, "session":String, "type=agenda_notify":String, "text=<message>":String, "timestamp=<Timestamp>":String
        Tuple notificationTuple = new Tuple();
        notificationTuple.add(AgentProtocol.NOTIFICATION);
        notificationTuple.add(new VMID().toString());
        notificationTuple.add(userName);
        notificationTuple.add("scylab");
        notificationTuple.add("process guidance agent");
        notificationTuple.add("mission");
        notificationTuple.add("session");
        notificationTuple.add("type=agenda_notify");
        notificationTuple.add("text=" + message);
        notificationTuple.add("timestamp=" + time);
        return notificationTuple;
    }

	@Override
	public List<UserAction> requestHistory(HistoryRequestEvent event) throws TupleSpaceException {
		return new ArrayList<UserAction>();
		// (<ID>:String, <AgentName>:String, "last_modified":String, <Mission>:String, <UserName>:String, 
		// <Timestamp>:Long, <EloURIs>:String)

//		Map<String, UserAction> modificationHistory = getHistory(event, ActivityModifiedEvaluationAgent.AGENT_NAME, ActivityModifiedEvaluationAgent.REQUEST_TYPE);
//		Map<String, UserAction> completionHistory = getHistory(event, ActivityFinishedEvaluationAgent.AGENT_NAME, ActivityFinishedEvaluationAgent.REQUEST_TYPE);
//		if(modificationHistory != null && completionHistory != null) {
//			return mergeHistory(modificationHistory, completionHistory);
//		} else {
//			return null;
//		}
	}
	
//	private List<UserAction> mergeHistory(Map<String, UserAction> modificationHistory, Map<String, UserAction> completionHistory) {
//		// chooses the useraction with the latest timestamp for each ELO
//		List<UserAction> result = new ArrayList<UserAction>();
//		for(UserAction modAction : modificationHistory.values()) {
//			UserAction compAction = completionHistory.get(modAction.getEloUri());
//			if(modAction.getTimestamp() > compAction.getTimestamp()) {
//				result.add(modAction);
//			} else {
//				compAction.setActiontype(Type.COMPLETION);
//				result.add(compAction);
//			}
//		}
//		return result;
//	}
	
//	private Map<String, UserAction> getHistory(HistoryRequestEvent event, String agentName, String requestType) throws TupleSpaceException {
//		MissionModel missionModel = (MissionModel) event.getSource();
//		
//		// FIXME problematic, because elo uris change... so activity evaluation agents won't find all corresponding tuples
//		String[] eloUris = new String[event.getActivities().size()];
//		int i = 0;
//		for (Activity activity : event.getActivities()) {
//			eloUris[i] = activity.getFirstVersionEloUri();
//			i++;
//		}
//		String escapedEloUris = EscapeUtils.escape(eloUris);
//		
//		VMID requestId = new VMID();
//		Tuple requestTuple = new Tuple(
//				requestId, 
//				agentName,
//				requestType,
//				missionModel.getMissionRuntimeUri(), 
//				missionModel.getUser(),
//				event.getTimestamp(),
//				escapedEloUris);
//		
//		Tuple responseSignature = new Tuple(REQUEST_HISTORY_RESPONSE, requestId, String.class);
//		this.commandSpace.write(requestTuple);
//		Tuple responseTuple = this.commandSpace.waitToTake(responseSignature, REQUEST_HISTORY_TIMEOUT);
//		if(responseTuple == null) {
//			logger.warn(String.format("Could not retrieve history from agent '%s', agent does not respond.", agentName));
//			return null;
//		}
//		String responseAsString = responseTuple.getField(2).getValue().toString();
//		return UserAction.deserializeUserActions(responseAsString);
//	}

}

