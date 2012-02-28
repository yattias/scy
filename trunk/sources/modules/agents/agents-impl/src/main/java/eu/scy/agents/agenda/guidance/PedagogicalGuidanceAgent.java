package eu.scy.agents.agenda.guidance;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Callback;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleID;
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
import eu.scy.agents.agenda.guidance.event.ClearCurtainEvent;
import eu.scy.agents.agenda.guidance.event.ClearCurtainListener;
import eu.scy.agents.agenda.guidance.event.DialogNotificationEvent;
import eu.scy.agents.agenda.guidance.event.DialogNotificationListener;
import eu.scy.agents.agenda.guidance.event.LoadActivitiesEvent;
import eu.scy.agents.agenda.guidance.event.LoadActivitiesListener;
import eu.scy.agents.agenda.guidance.event.LoadMessagesEvent;
import eu.scy.agents.agenda.guidance.event.LoadMessagesListener;
import eu.scy.agents.agenda.guidance.event.SaveActivityEvent;
import eu.scy.agents.agenda.guidance.event.SaveActivityListener;
import eu.scy.agents.agenda.guidance.event.SendMessageEvent;
import eu.scy.agents.agenda.guidance.event.SendMessageListener;
import eu.scy.agents.agenda.guidance.event.StatusChangedEvent;
import eu.scy.agents.agenda.guidance.event.StatusChangedListener;
import eu.scy.agents.agenda.guidance.model.Activity;
import eu.scy.agents.agenda.guidance.model.Message;
import eu.scy.agents.agenda.guidance.model.MissionModel;
import eu.scy.agents.agenda.guidance.model.UserModel;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.impl.AbstractThreadedAgent;
import eu.scy.agents.impl.AgentProtocol;

public class PedagogicalGuidanceAgent extends AbstractThreadedAgent 
	implements IRepositoryAgent, StatusChangedListener, SendMessageListener, SaveActivityListener, 
	LoadActivitiesListener, LoadMessagesListener, ClearCurtainListener, DialogNotificationListener {

	private static final Logger logger = Logger.getLogger(PedagogicalGuidanceAgent.class.getName());

	public static final String GUIDANCE_SPACE_NAME = "guidance";
	
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
	private TupleSpace guidanceSpace;
	
    
	public PedagogicalGuidanceAgent(Map<String, Object> map) {
        super(PedagogicalGuidanceAgent.class.getName(), (String) map.get(AgentProtocol.PARAM_AGENT_ID), (String) map.get(AgentProtocol.TS_HOST), (Integer) map.get(AgentProtocol.TS_PORT));
        
        try {
        	this.actionSpace = new TupleSpace(new User(getSimpleName()), host, port, false, false, AgentProtocol.ACTION_SPACE_NAME);
			this.commandSpace = new TupleSpace(new User(getSimpleName()), host, port, false, false, AgentProtocol.COMMAND_SPACE_NAME);
			this.guidanceSpace = new TupleSpace(new User(getSimpleName()), host, port, false, false, GUIDANCE_SPACE_NAME);
			
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
	
    @Override
    public void setRepository(IRepository rep) {
        rooloServices.setRepository(rep);
    }

    @Override
    public void setMetadataTypeManager(IMetadataTypeManager manager) {
    	rooloServices.setMetadataTypeManager(manager);
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
		missionModel.setCurtainReloadNeeded(true);
		
		if(missionModel.isEnabled()) {
			// first login: build new model and register listeners
			missionModel.setActivityModificationListener(this);
			missionModel.setSendMessageListener(this);
			missionModel.setSaveActivityListener(this);
			missionModel.setLoadActivitiesListener(this);
			missionModel.setLoadMessagesListener(this);
			missionModel.setClearCurtainListener(this);
			missionModel.setDialogNotificationListener(this);
			missionModel.build();
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
		
		UserModel userModel = this.userModelDict.getUserModel(userName);
		if(userModel != null) {
			MissionModel missionModel = userModel.getMissionModel(missionRuntimeUri);
			if(missionModel != null && missionModel.isCurtainReloadNeeded()) {
				missionModel.reloadCurtain();
			}
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
			UserModel userModel = this.userModelDict.getOrCreateUserModel(userName);
			MissionModel missionModel = userModel.getOrCreateMissionModel(missionRuntimeUri);
			if(missionModel.isEnabled()) {
				// user logged in before the agent has been started (i.e. agent restart)
				// create models and register listeners
				missionModel.setActivityModificationListener(this);
				missionModel.setSendMessageListener(this);
				missionModel.setSaveActivityListener(this);
				missionModel.setLoadActivitiesListener(this);
				missionModel.setLoadMessagesListener(this);
				missionModel.setClearCurtainListener(this);
				missionModel.setDialogNotificationListener(this);
				missionModel.build();
			}
			
			logger.debug(String.format(
					"Received activity modified tuple [ type: %s | user: %s | missionRuntimeUri: %s | ELO URI: %s ]", 
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
	public void statusChanged(StatusChangedEvent event) throws TupleSpaceException {
		MissionModel missionModel = (MissionModel) event.getSource();
		Activity activity = event.getActivity();

        Tuple statusNotificationTuple = createStatusNotificationTuple(
        		missionModel.getUserName(), 
        		activity.getEloTitle(), 
        		event.getNewState().getText(), 
        		activity.getLastModificationTime(), 
        		activity.getFirstVersionEloUri());
        logger.debug("Writing status changed tuple: " + statusNotificationTuple);
        commandSpace.write(statusNotificationTuple);
	}

    private Tuple createStatusNotificationTuple(String user, String eloTitle, String status, long time, String eloUri) {
        Tuple notificationTuple = new Tuple();
        notificationTuple.add(AgentProtocol.NOTIFICATION);
        notificationTuple.add(new VMID().toString());
        notificationTuple.add(user);
        notificationTuple.add("scylab");
        notificationTuple.add("process guidance agent");
        notificationTuple.add("mission");
        notificationTuple.add("session");
        notificationTuple.add("type=agenda_notify");
        notificationTuple.add("text=" + eloTitle);
        notificationTuple.add("timestamp=" + time);
        notificationTuple.add("state=" + status);
        notificationTuple.add("elouri=" + eloUri);
        return notificationTuple;
    }

	@Override
	public void sendCurtainMessage(SendMessageEvent event) throws TupleSpaceException {
		Message message = event.getMessage();

        Tuple curtainMessageTuple = createCurtainMessageTuple(
        		message.getUserName(), 
        		message.getText(), 
        		message.getTimestamp());
        logger.debug("Writing message tuple: " + curtainMessageTuple);
        commandSpace.write(curtainMessageTuple);
        
        Tuple guidanceSpacetuple = message.toTuple();
        logger.debug("Persisting message in guidance space: " + curtainMessageTuple);
        this.guidanceSpace.write(guidanceSpacetuple);
	}
	
    private Tuple createCurtainMessageTuple(String userName, String message, long time) {
        Tuple notificationTuple = new Tuple();
        notificationTuple.add(AgentProtocol.NOTIFICATION);
        notificationTuple.add(new VMID().toString());
        notificationTuple.add(userName);
        notificationTuple.add("no specific elo");
        notificationTuple.add(PedagogicalGuidanceAgent.class.getName());
        notificationTuple.add("mission");
        notificationTuple.add("session");
        notificationTuple.add("type=agenda_notify");
        notificationTuple.add("text=" + message);
        notificationTuple.add("timestamp=" + time);
        return notificationTuple;
    }

	@Override
	public List<Tuple> loadMessages(LoadMessagesEvent event) throws TupleSpaceException {
		MissionModel missionModel = (MissionModel)event.getSource();
		List<Tuple> messages = new ArrayList<Tuple>();
		Tuple signature = new Tuple(Message.FIELD_MESSAGE, missionModel.getUserName(), missionModel.getMissionRuntimeUri(), Field.createWildCardField());
		for(Tuple t : this.guidanceSpace.readAll(signature, 30)) {
			messages.add(t);
		}
		return messages;
	}

	@Override
	public void sendDialogNotification(DialogNotificationEvent event) throws TupleSpaceException {
		MissionModel missionModel = (MissionModel)event.getSource();

        Tuple dialogTuple = createDialogNotificationTuple(
        		missionModel.getUserName(), 
        		missionModel.getMissionRuntimeUri(), 
        		event.getSession(),
        		new VMID().toString(),
        		event.getText());
        logger.debug("Writing dialog tuple: " + dialogTuple);
        commandSpace.write(dialogTuple);
	}

    private Tuple createDialogNotificationTuple(String userName, String mission, String session, String notificationId, String message) {
        Tuple notificationTuple = new Tuple();
        notificationTuple.add(AgentProtocol.NOTIFICATION);
        notificationTuple.add(notificationId);
        notificationTuple.add(userName);
        notificationTuple.add("no specific elo");
        notificationTuple.add(PedagogicalGuidanceAgent.class.getName());
        notificationTuple.add(mission);
        notificationTuple.add(session);
        notificationTuple.add("text=" + message.toString());
        notificationTuple.add("title=Mission Guidance");
        notificationTuple.add("type=message_dialog_show");
        notificationTuple.add("modal=true");
        notificationTuple.add("dialogType=OK_DIALOG");
        return notificationTuple;
    }
	
	@Override
	public void clearCurtain(ClearCurtainEvent event) throws TupleSpaceException {
		// FIXME curtain clear does not work
		MissionModel missionModel = (MissionModel)event.getSource();
		
        Tuple clearCurtainNotificationTuple = createClearCurtainTuple(
        		missionModel.getUserName(), 
        		missionModel.getMissionRuntimeUri(),
        		System.currentTimeMillis());
        logger.debug("Writing clear curtain tuple: " + clearCurtainNotificationTuple);
        commandSpace.write(clearCurtainNotificationTuple);
	}
	
    private Tuple createClearCurtainTuple(String userName, String mission, long time) {
        Tuple notificationTuple = new Tuple();
        notificationTuple.add(AgentProtocol.NOTIFICATION);
        notificationTuple.add(new VMID().toString());
        notificationTuple.add(userName);
        notificationTuple.add("no specific elo");
        notificationTuple.add(PedagogicalGuidanceAgent.class.getName());
        notificationTuple.add(mission);
        notificationTuple.add("n/a");
        notificationTuple.add("text=asd");
        notificationTuple.add("type=agenda_notify");
        notificationTuple.add("timestamp=" + time);
        notificationTuple.add("remove=all");
        return notificationTuple;
    }

	@Override
	public TupleID saveActivity(SaveActivityEvent event) throws TupleSpaceException {
		MissionModel missionModel = (MissionModel)event.getSource();
		TupleID tupleID = event.getTupleID();
		Tuple tuple = event.getActivity().toTuple(missionModel.getUserName(), missionModel.getMissionRuntimeUri());
		if(tupleID == null) {
			// save tuple
			tupleID = this.guidanceSpace.write(tuple);
		} else {
			// update tuple
			this.guidanceSpace.update(tupleID, tuple);
		}
		return tupleID;
	}

	@Override
	public List<Tuple> loadActivities(LoadActivitiesEvent event) throws TupleSpaceException {
		MissionModel missionModel = (MissionModel)event.getSource();
		List<Tuple> activities = new ArrayList<Tuple>();
		Tuple signature = new Tuple(Activity.FIELD_ACTIVITY, missionModel.getUserName(), missionModel.getMissionRuntimeUri(), Field.createWildCardField());
		for(Tuple t : this.guidanceSpace.readAll(signature, 30)) {
			activities.add(t);
		}
		return activities;
	}

}
