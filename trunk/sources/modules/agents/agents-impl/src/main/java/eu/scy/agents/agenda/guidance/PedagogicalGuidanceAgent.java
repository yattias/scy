package eu.scy.agents.agenda.guidance;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Callback;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import roolo.elo.BasicELO;
import roolo.elo.api.IMetadata;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataValueContainer;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import eu.scy.agents.agenda.evaluation.ActivityFinishedEvaluationAgent;
import eu.scy.agents.agenda.evaluation.ActivityModifiedEvaluationAgent;
import eu.scy.agents.agenda.guidance.model.Activity;
import eu.scy.agents.agenda.guidance.model.Activity.State;
import eu.scy.agents.agenda.guidance.model.ActivityModelUser;
import eu.scy.agents.agenda.guidance.model.MissionModel;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.impl.AbstractThreadedAgent;
import eu.scy.agents.impl.AgentProtocol;
import eu.scy.agents.session.Session;

public class PedagogicalGuidanceAgent extends AbstractThreadedAgent {

	private static final Tuple TEMPLATE_FOR_MODIFIED_ACTIVITY = new Tuple(ActivityModifiedEvaluationAgent.TYPE_MODIFIED, String.class, String.class, String.class, Long.class);
	
	private static final Tuple TEMPLATE_FOR_FINISHED_ACTIVITY = new Tuple(ActivityFinishedEvaluationAgent.TYPE_FINISHED, String.class, String.class, String.class, Long.class);
	
	private static final Logger logger = Logger.getLogger(PedagogicalGuidanceAgent.class.getName());
	
	private final List<Integer> registeredCallbacks = new ArrayList<Integer>();
	
	private final Map<String, ActivityModelUser> userModelMap = new HashMap<String, ActivityModelUser>();

	private TupleSpace actionSpace;
	
	private TupleSpace commandSpace;
	
	private RooloAccessor rooloAccessor;
	
	private Session session;
	
	
	public PedagogicalGuidanceAgent(Map<String, Object> map) {
        super(PedagogicalGuidanceAgent.class.getName(), (String) map.get(AgentProtocol.PARAM_AGENT_ID), (String) map.get(AgentProtocol.TS_HOST), (Integer) map.get(AgentProtocol.TS_PORT));

        try {
			this.commandSpace = new TupleSpace(new User(getSimpleName()), host, port, false, false, AgentProtocol.COMMAND_SPACE_NAME);
			this.actionSpace = new TupleSpace(new User(getSimpleName()), host, port, false, false, AgentProtocol.ACTION_SPACE_NAME);

			this.session = new Session(new TupleSpace(new User(getSimpleName()), host, port, false, false, AgentProtocol.SESSION_SPACE_NAME));
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
		public void call(Command cmd, int seqnum, Tuple afterTuple,
				Tuple beforeTuple) {
			
			processTuple(afterTuple);
		}
		
	}
	
	private void processTuple(Tuple tuple) {
		// ("modified"/"finished":String, mission:String, userName:String, eloUri:String, timestamp:long)
		String userName = tuple.getField(2).getValue().toString();
		String missionName = tuple.getField(1).getValue().toString();
		
		try {
			// TODO currently not thread-safe
			ActivityModelUser user = null;
			user = this.userModelMap.get(userName);
			
			if(user == null) {
				// generate new user
				user = new ActivityModelUser(userName);
				this.userModelMap.put(userName, user);
			}
			
			MissionModel mission = user.getMission(missionName);
			if(mission == null) {
				// generate new mission model
				mission = generateNewMissionModel(missionName, userName);
				user.addMission(mission);
			}
			
			String type = tuple.getField(0).getValue().toString();
			String eloUri = tuple.getField(3).getValue().toString();
			long timestamp = Long.valueOf(tuple.getField(4).getValue().toString());
			
			// change activity
			Activity activity = mission.getActivityByUserElo(eloUri);
			if(timestamp > activity.getLatestModificationTime()) {
				State state = Activity.State.valueOf(type);
				activity.setState(state);
				activity.setLatestModificationTime(timestamp);
				
				List<Activity> dependingActivities = mission.getDependingActivities(activity);
				for(Activity act : dependingActivities) {
					// TODO perform changes on the depending activities
					// Maybe checks must be performed recursivly ... needs to be discussed
				}
			}

		} catch (IllegalArgumentException e) {
			logger.warn("Ignoring activity change tuple with invalid type " + tuple.toString());
			
		} catch (TupleSpaceException e) {
			logger.debug("Connection to TupleSpace lost");
		}
	}
	
	private MissionModel generateNewMissionModel(String missionName, String userName) throws TupleSpaceException {

		String depSpec = readXmlDependencySpec();
		
		MissionModel model = createDependencyModel(missionName, userName, depSpec);
		
		List<String> eloUris = this.rooloAccessor.getEloUris(missionName, userName);
		if(eloUris != null) {
			
			// add concrete ELO URI for each activity. identification is done by template URI
			for(String eloUri : eloUris) {
				String templateEloUri = getTemplateEloUri(eloUri);

				Activity act = model.getActivityByTemplateUri(templateEloUri);
				act.setEloUri(eloUri);
			}
		}
		return model;
	}
	
	private String readXmlDependencySpec() {
		// TODO read dependency specification in xml format
		return "";
	}
	
	private MissionModel createDependencyModel(String missionName, String userName, String xmlSpec) throws TupleSpaceException {
		// Get the mission specification and create dependency model
		String missionRuntimeURI = session.getMissionRuntimeURI(userName);
		String missionSpecificationURI = session.getMissionSpecification(missionRuntimeURI);
		BasicELO missionSpec = this.rooloAccessor.getBasicElo(missionSpecificationURI);

		MissionModel missionModel = new MissionModel(missionName, userName);

		// TODO: parse xml dependency specification
		
		return missionModel;
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

