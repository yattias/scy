package eu.scy.agents.agenda.guidance;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Callback;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.User;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

import roolo.elo.BasicELO;
import roolo.elo.api.IMetadata;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataValueContainer;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import eu.scy.agents.agenda.evaluation.ActivityFinishedEvaluationAgent;
import eu.scy.agents.agenda.evaluation.ActivityModifiedEvaluationAgent;
import eu.scy.agents.agenda.exception.AgentDoesNotRespondException;
import eu.scy.agents.agenda.guidance.model.Activity;
import eu.scy.agents.agenda.guidance.model.Activity.State;
import eu.scy.agents.agenda.guidance.model.MissionModel;
import eu.scy.agents.agenda.guidance.model.UserModel;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.impl.AbstractThreadedAgent;
import eu.scy.agents.impl.AgentProtocol;
import eu.scy.agents.session.Session;

public class PedagogicalGuidanceAgent extends AbstractThreadedAgent {

	private static final Tuple TEMPLATE_FOR_MODIFIED_ACTIVITY = new Tuple(ActivityModifiedEvaluationAgent.TYPE_MODIFIED, String.class, String.class, String.class, Long.class);
	private static final Tuple TEMPLATE_FOR_FINISHED_ACTIVITY = new Tuple(ActivityFinishedEvaluationAgent.TYPE_FINISHED, String.class, String.class, String.class, Long.class);
	
	private static final Logger logger = Logger.getLogger(PedagogicalGuidanceAgent.class.getName());
	private final List<Integer> registeredCallbacks = new ArrayList<Integer>();
	private final UserModelDictionary userModelDict = new UserModelDictionary();
	
	private KnownMissionConsumer knownMissionConsumer; 
	private UnknownMissionConsumer unknownMissionConsumer; 

	private TupleSpace actionSpace;
	private TupleSpace commandSpace;
	private Session session;
	
	
	public PedagogicalGuidanceAgent(Map<String, Object> map) {
        super(PedagogicalGuidanceAgent.class.getName(), (String) map.get(AgentProtocol.PARAM_AGENT_ID), (String) map.get(AgentProtocol.TS_HOST), (Integer) map.get(AgentProtocol.TS_PORT));

        try {
			this.commandSpace = new TupleSpace(new User(getSimpleName()), host, port, false, false, AgentProtocol.COMMAND_SPACE_NAME);
			this.actionSpace = new TupleSpace(new User(getSimpleName()), host, port, false, false, AgentProtocol.ACTION_SPACE_NAME);

			this.session = new Session(new TupleSpace(new User(getSimpleName()), host, port, false, false, AgentProtocol.SESSION_SPACE_NAME));
			this.knownMissionConsumer = new KnownMissionConsumer(this.userModelDict);
			this.unknownMissionConsumer = new UnknownMissionConsumer(this.userModelDict, this.commandSpace);
		} catch (TupleSpaceException e) {
			logger.error(getName() + " could not be started, because connection to the TupleSpace could not be established.");
		}
	}

	@Override
	protected void doRun() throws TupleSpaceException, AgentLifecycleException, InterruptedException {
		Thread knownMissionConsumerThread = new Thread(this.knownMissionConsumer);
		knownMissionConsumerThread.setDaemon(false);
		knownMissionConsumerThread.start();
		Thread unknownMissionConsumerThread = new Thread(this.unknownMissionConsumer);
		unknownMissionConsumerThread.setDaemon(false);
		unknownMissionConsumerThread.start();
		registerCallbacks();
		
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
        this.knownMissionConsumer.stop();
        this.unknownMissionConsumer.stop();
	}

	@Override
	public boolean isStopped() {
        return (this.status != Status.Running);
	}

	private void registerCallbacks() throws TupleSpaceException {
		Callback amcb = new ActivityChangedCallback();
		int id = this.commandSpace.eventRegister(Command.WRITE, TEMPLATE_FOR_MODIFIED_ACTIVITY, amcb, true);
		this.registeredCallbacks.add(id);
		logger.debug("Registered activity modified callback");

		Callback afcb = new ActivityChangedCallback();
		id = this.commandSpace.eventRegister(Command.WRITE, TEMPLATE_FOR_FINISHED_ACTIVITY, afcb, true);
		this.registeredCallbacks.add(id);
		logger.debug("Registered activity finished callback");
	}
	
	class ActivityChangedCallback implements Callback {

		@Override
		public void call(Command cmd, int seqnum, Tuple afterTuple, Tuple beforeTuple) {
			processTuple(afterTuple);
		}
	}
	
	private void processTuple(Tuple tuple) {
		// Signature: ("modified"/"finished":String, mission:String, userName:String, eloUri:String, timestamp:long)
		String userName = tuple.getField(2).getValue().toString();
		String missionName = tuple.getField(1).getValue().toString();
		try {
			
			UserModel userModel = this.userModelDict.getOrCreateUserModel(userName);
			MissionModel missionModel = userModel.getOrCreateMissionModel(missionName);

			if(missionModel.isInitialized()) {
				this.knownMissionConsumer.addTuple(tuple);
			} else {
				this.unknownMissionConsumer.addTuple(tuple);
			}

		} catch (IllegalArgumentException e) {
			logger.warn("Ignoring activity change tuple with invalid type " + tuple.toString());
		} catch (InterruptedException e) {
			logger.warn("Ignoring activity change tuple, because tuple could not be added to queue.");
		}
	}

	
	class KnownMissionConsumer implements Runnable {
		
		protected final BlockingQueue<Tuple> tupleQueue = new LinkedBlockingQueue<Tuple>();
		protected final UserModelDictionary userModelDict;
		protected volatile boolean running = true;
		
		public KnownMissionConsumer(UserModelDictionary userModelDict) {
			this.userModelDict = userModelDict;
		}
		
		public void addTuple(Tuple t) throws InterruptedException {
			this.tupleQueue.put(t);
		}
		
		public void stop() {
			this.running = false;
		}
		
		@Override
		public void run() {
			this.running = true;
			while(this.running) {
				try {
					Tuple tuple = tupleQueue.take();
					String userName = tuple.getField(2).getValue().toString();
					String missionName = tuple.getField(1).getValue().toString();
					MissionModel missionModel = userModelDict.getMissionModel(userName, missionName);
					applyTupleToMissionModel(missionModel, tuple);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		protected void applyTupleToMissionModel(MissionModel missionModel, Tuple tuple) {
			String type = tuple.getField(0).getValue().toString();
			String eloUri = tuple.getField(3).getValue().toString();
			long timestamp = Long.valueOf(tuple.getField(4).getValue().toString());
			
			// change activity
			Activity activity = missionModel.getActivityByUserElo(eloUri);
			if(timestamp > activity.getLatestModificationTime()) {
				State state = Activity.State.valueOf(type);
				activity.setState(state);
				activity.setLatestModificationTime(timestamp);
				
				List<Activity> dependingActivities = missionModel.getDependingActivities(activity);
				for(Activity act : dependingActivities) {
					// TODO perform changes on the depending activities
					// Maybe checks must be performed recursivly ... needs to be discussed
				}
			}
		}
	}

	class UnknownMissionConsumer extends KnownMissionConsumer {
		
		private RooloAccessor rooloAccessor;

		public UnknownMissionConsumer(UserModelDictionary userModelDict, TupleSpace commandSpace) {
			super(userModelDict);
			this.rooloAccessor = new RooloAccessor(commandSpace, logger);
		}

		@Override
		public void run() {
			this.running = true;
			while(this.running) {
				try {
					Tuple tuple = tupleQueue.take();
					String userName = tuple.getField(2).getValue().toString();
					String missionName = tuple.getField(1).getValue().toString();
					
					MissionModel missionModel = this.userModelDict.getMissionModel(userName, missionName);
					reconstructMissionModel(missionModel);
					missionModel.setInitialized(true);
					
					applyTupleToMissionModel(missionModel, tuple);
				} catch (AgentDoesNotRespondException e) {
					logger.warn(e.getMessage());
				} catch (InterruptedException e) {
					logger.warn("Waiting to take tuple has been interrupted");
				} catch (TupleSpaceException e) {
					logger.error("Connection to TupleSpace lost");
					this.running = false;
				}
			}
		}
		
		private void reconstructMissionModel(MissionModel mission) throws TupleSpaceException, AgentDoesNotRespondException {
			// MissionModel is empty, so we'll create the dependencies and activities for this mission
			createDependencyModel(mission);

			// dependency model has just template URIs, so we'll add the concrete ELO URIs
			List<String> eloUris = this.rooloAccessor.getEloUris(mission.getName(), mission.getUser());
			if(eloUris != null) {
				for(String eloUri : eloUris) {
					String templateEloUri = getTemplateEloUri(eloUri);
					Activity act = mission.getActivityByTemplateUri(templateEloUri);
					act.setEloUri(eloUri);
				}
			} else {
				throw new AgentDoesNotRespondException("Could not reconstruct mission model, because RooloAccessorAgent did not respond.");
			}
		}
		
		private void createDependencyModel(MissionModel missionModel) throws TupleSpaceException {
			// Get the mission specification and create dependency model (which contains template URIs)
			String missionRuntimeURI = session.getMissionRuntimeURI(missionModel.getUser());
			String missionSpecificationURI = session.getMissionSpecification(missionRuntimeURI);
			BasicELO missionSpec = this.rooloAccessor.getBasicElo(missionSpecificationURI);

			// TODO: parse xml dependency specification and add Activity and Dependency object 
			// to the missionModel
			
		}
		
		private String getTemplateEloUri(String eloUri) throws TupleSpaceException {
			BasicELO elo = this.rooloAccessor.getBasicElo(eloUri);
			
			IMetadata metadata = elo.getMetadata();
			Set<IMetadataKey> metadataKeys = metadata.getAllMetadataKeys();

			// Search for the IS_FORK_OF metadata key
			IMetadataValueContainer valueContainer = null;
			for (Iterator<IMetadataKey> iterator = metadataKeys.iterator(); iterator.hasNext();) {
				IMetadataKey key = iterator.next();
				if(key.getId().equals(CoreRooloMetadataKeyIds.IS_FORK_OF.getId())) {
					valueContainer = metadata.getMetadataValueContainer(key);
					break;
				}
			}
			if(valueContainer != null) {
				return getTemplateEloUri(valueContainer.getValue().toString());
			} 
			return eloUri;
		}
	}
}

