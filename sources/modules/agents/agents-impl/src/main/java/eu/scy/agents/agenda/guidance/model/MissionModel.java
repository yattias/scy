package eu.scy.agents.agenda.guidance.model;

import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.activity.InvalidActivityException;

import org.apache.log4j.Logger;

import roolo.elo.api.IELO;
import roolo.elo.api.IMetadata;
import roolo.elo.api.IMetadataValueContainer;
import roolo.elo.api.exceptions.MetaDataException;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import eu.scy.agents.agenda.exception.MissionMapModelException;
import eu.scy.agents.agenda.exception.NoMetadataTypeManagerAvailableException;
import eu.scy.agents.agenda.exception.NoRepositoryAvailableException;
import eu.scy.agents.agenda.guidance.MetadataAccessor;
import eu.scy.agents.agenda.guidance.PedagogicalGuidanceAgent;
import eu.scy.agents.agenda.guidance.RooloAccessor;
import eu.scy.agents.agenda.guidance.event.ActivityModificationEvent;
import eu.scy.agents.agenda.guidance.event.ActivityModificationListener;
import eu.scy.agents.agenda.guidance.event.HistoryRequestEvent;
import eu.scy.agents.agenda.guidance.event.HistoryRequestListener;
import eu.scy.agents.agenda.guidance.model.Activity.ActivityState;
import eu.scy.agents.agenda.serialization.BasicMissionAnchorModel;
import eu.scy.agents.agenda.serialization.EloParser;
import eu.scy.agents.agenda.serialization.UserAction;
import eu.scy.agents.agenda.serialization.UserAction.Type;
import eu.scy.agents.session.Session;

public class MissionModel {

	enum ModelState {
		Enabled, 
		Initializing, 
		Initialized;
	}
	
	private static final Logger logger = Logger.getLogger(MissionModel.class.getName());
	
	private final Map<String, Activity> userElo2Activity = new HashMap<String, Activity>();
	private final Set<Dependency> dependencies = new HashSet<Dependency>();
	private final List<ActivityModificationListener> modificationListeners = new ArrayList<ActivityModificationListener>();
	private final Session session;
	
	private HistoryRequestListener historyListener;
	private volatile ModelState state;
	private final String missionRuntimeUri;
	private final String user;
	private MissionModelBuilder umc;
	
	
	public MissionModel(Session session, String missionRuntimeUri, String userName) {
		this.session = session;
		this.missionRuntimeUri = missionRuntimeUri;
		this.user = userName;
		this.state = ModelState.Enabled;
	}

	public String getMissionRuntimeUri() {
		return this.missionRuntimeUri;
	}

	public String getUser() {
		return this.user;
	}
	
	public void setInitialized() {
		synchronized (this) {
			this.state = ModelState.Initialized;
		}
	}
	
	public boolean isEnabled() {
		synchronized (this) {
			return (this.state == ModelState.Enabled);
		}
	}
	
	public Activity getActivityByEloUri(String eloUri) {
		return this.userElo2Activity.get(eloUri);
	}
	
	public List<Activity> getDependingActivities(Activity activity) {
		ArrayList<Activity> list = new ArrayList<Activity>(); 
		for(Dependency dep : this.dependencies) {
			if(dep.getDependent().equals(activity)) {
				list.add(dep.getDependsOn());
			}
		}
		return list;
	}
	
	private void addActivity(Activity act) throws InvalidActivityException {
		if(act.getEloUri() == null) {
			throw new InvalidActivityException();
		}
		this.userElo2Activity.put(act.getEloUri(), act);
	}
	
	private void addDependency(Activity dependent, Activity dependsOn) {
		addDependency(new Dependency(dependent, dependsOn));
	}
	
	private void addDependency(Dependency dep) {
		this.dependencies.add(dep);
	}
	
	public void processTuple(Tuple tuple) {
		synchronized(this) {
			switch(this.state) {
			case Enabled:
				this.state = ModelState.Initializing;
				this.umc = new MissionModelBuilder(this);
				new Thread(this.umc).start();
				break;
			case Initializing:
				this.umc.addTuple(tuple);
				break;
			case Initialized:
				// tidy up
				if(this.umc != null) {
					this.umc = null;
				}
				applyTupleToModel(tuple);
				break;
			}
		}
	}
	
	private void applyTupleToModel(Tuple tuple) {
		String type = tuple.getField(0).getValue().toString().toUpperCase();
		String eloUri = tuple.getField(3).getValue().toString();
		long timestamp = Long.valueOf(tuple.getField(4).getValue().toString());
		// change activity
		Activity activity = getActivityByEloUri(eloUri);
		if(timestamp > activity.getLatestModificationTime()) {
			try {
				Activity.ActivityState actState = Activity.ActivityState.valueOf(type);
				activity.setLatestModificationTime(timestamp);
				logger.debug(String.format("Set state %s for activity %s (user: %s | mission: %s).", 
						type,
						eloUri, 
						getUser(), 
						getMissionRuntimeUri()));
				
				if(actState != activity.getState()) {
					
					if(actState == ActivityState.MODIFIED && activity.getState() == ActivityState.FINISHED) {
						List<Activity> dependingActivities = getDependingActivities(activity);
						for(Activity act : dependingActivities) {
							ActivityModificationEvent event = new ActivityModificationEvent(this, act);
							for(ActivityModificationListener listener : this.modificationListeners) {
								listener.activityModified(event);
							}
						}
					}
					activity.setState(actState);
				}
			} catch (IllegalArgumentException e) {
				logger.warn(String.format("Received tuple for user '%s' and mission '%s' has the unknown type: '%s'.", 
						getUser(), 
						getMissionRuntimeUri(),
						type));
			}
		}
	}
	
	private void reconstruct() throws InvalidActivityException, MissionMapModelException, URISyntaxException, NoRepositoryAvailableException, NoMetadataTypeManagerAvailableException {

		// Which one to use?
//		String missionRuntimeURI = this.missionRuntimeUri;
		String missionRuntimeURI = this.session.getMissionRuntimeURI(getUser());

		// 1. option: use missionRuntime content to get missionMapModelElo
		reconstructMissionModelUsingMissionRuntime(missionRuntimeURI);
		
		// 2. option: use missionSpecificiation content 
		reconstructMissionModelUsingMissionSpecification(missionRuntimeURI);
		
		// request history of this mission model
		requestHistory();
	}
	
	private void reconstructMissionModelUsingMissionRuntime(String missionRuntimeURI) throws MissionMapModelException, URISyntaxException, NoRepositoryAvailableException, InvalidActivityException {
		logger.debug(String.format("Creating dependency model [ user: %s | missionRuntimeUri %s ]", 
				getUser(), 
				getMissionRuntimeUri()));
		
		// Get mission map model URI from mission runtime
		IELO missionMapModel = getMissionMapModel(missionRuntimeURI);
		
		// MissionAnchorID -> MissionAnchorModel
		Map<String, BasicMissionAnchorModel> anchorMap = EloParser.parseMissionMapModel(missionMapModel.getContent());
		
		// MissionAnchorID -> Activity
		Map<String, Activity> activityMap = new HashMap<String, Activity>();
		
		// create the activities of this mission
		for(BasicMissionAnchorModel anchorModel : anchorMap.values()) {
			String eloUri = anchorModel.getUri();
			String anchorId = anchorModel.getId();
			
			Activity activity = new Activity(anchorId);
			activity.setEloUri(eloUri);
			addActivity(activity);
			activityMap.put(anchorId, activity);
		}
		
		// now add the dependencies for each activity
		for(String anchorId : anchorMap.keySet()) {
			Activity dependent = activityMap.get(anchorId);
			for(String dependingOnId : anchorMap.get(anchorId).getDependingOnMissionIds()) {
				Activity dependsOn = activityMap.get(dependingOnId);
				addDependency(dependent, dependsOn);
			}
		}
	}

	private void reconstructMissionModelUsingMissionSpecification(String missionRuntimeURI) throws MissionMapModelException, URISyntaxException, NoRepositoryAvailableException, NoMetadataTypeManagerAvailableException, InvalidActivityException {
		logger.debug(String.format("Creating dependency model [ user: %s | missionRuntimeUri %s ]", 
				getUser(), 
				missionRuntimeURI));
		
		// 1. option: Get missionID and specification from missionRuntime metadata
		String missionId = readMetadataValue(missionRuntimeURI, CoreRooloMetadataKeyIds.MISSION_ID);
		String missionSpecificationUri = readMetadataValue(missionRuntimeURI, CoreRooloMetadataKeyIds.MISSION_RUNNING);
		
		// 2. option: Get missionId and mission specification from session space
//		String missionId = this.session.getMissionId(missionRuntimeURI);
//		String missionSpecificationURI = this.session.getMissionSpecification(missionRuntimeURI);


		// Get mission map model URI from mission specification
		IELO missionMapModel = getMissionMapModel(missionSpecificationUri);
		
		Map<String, BasicMissionAnchorModel> anchorMap = EloParser.parseMissionMapModel(missionMapModel.getContent());
		
		// template URI -> Activity
		Map<String, Activity> templateActivityMap = new HashMap<String, Activity>();
		
		// create the activities of this mission
		for(BasicMissionAnchorModel anchorModel : anchorMap.values()) {
			String templateUri = anchorModel.getUri();
			String anchorId = anchorModel.getId();
			
			Activity activity = new Activity(anchorId);
			activity.setTemplateUri(templateUri);
			templateActivityMap.put(templateUri, activity);
		}


		// Now the activities are created, but has just template URIs set. 
		// So we get all ELO URIs that belong to users current mission and retrieve their template URI.
		// With that template URI, we get back the activity, set the ELO URI and add it to the model.
		// You think this is weird? You are right!
		RooloAccessor rooloAccessor = PedagogicalGuidanceAgent.getRooloAccessor();
		List<String> eloUris = rooloAccessor.getEloUrisForMissionRuntime(missionId, getUser());
		for(String eloUri : eloUris) {
			String templateUri = getTemplateEloUri(eloUri);
			Activity activity = templateActivityMap.get(templateUri);
			if(activity != null) {
				activity.setEloUri(eloUri);
				addActivity(activity);
			} else {
				logger.warn("Tried to add activity, but template URI is unknown. ELO URI: " + eloUri);
			}
		}
		
		// Activities are completed and added to the mission model. 
		// Next step is to add the dependencies for each activity
		for(String anchorId : anchorMap.keySet()) {
			Activity dependent = templateActivityMap.get(anchorId);
			for(String dependingOnId : anchorMap.get(anchorId).getDependingOnMissionIds()) {
				Activity dependsOn = templateActivityMap.get(dependingOnId);
				addDependency(dependent, dependsOn);
			}
		}
	}

	private IELO getMissionMapModel(String missionSpecOrRuntimeUri) throws MissionMapModelException, URISyntaxException, NoRepositoryAvailableException {
		RooloAccessor rooloAccessor = PedagogicalGuidanceAgent.getRooloAccessor();
		IELO missionSpecification = rooloAccessor.getElo(missionSpecOrRuntimeUri);
		String missionMapModelUri = EloParser.getMissionMapModelUriFromMissionRuntimeContent(missionSpecification.getContent());
		if(missionMapModelUri == null) {
			throw new MissionMapModelException("Could not get mission map model ELO URI from ELO content");
		}
		return rooloAccessor.getElo(missionMapModelUri);
	}
	
	private String readMetadataValue(String missionRuntimeURI, CoreRooloMetadataKeyIds metadataKey) throws URISyntaxException, NoRepositoryAvailableException, NoMetadataTypeManagerAvailableException {
		RooloAccessor rooloAccessor = PedagogicalGuidanceAgent.getRooloAccessor();
		MetadataAccessor metadataAccessor = PedagogicalGuidanceAgent.getMetadataAccessor();
		IMetadata missionRuntimeMetadata = rooloAccessor.getMetadata(missionRuntimeURI);
		IMetadataValueContainer valueContainer = metadataAccessor.readMetadataValue(missionRuntimeMetadata, metadataKey.getId());
		if(valueContainer == null) {
			throw new MetaDataException(String.format(
					"MetadataValueContainer does not contain key '%s'", metadataKey.getId()));
		}
		return valueContainer.getValue().toString();
	}
	
	private String getTemplateEloUri(String eloUri) throws URISyntaxException, NoRepositoryAvailableException, NoMetadataTypeManagerAvailableException {
		RooloAccessor rooloAccessor = PedagogicalGuidanceAgent.getRooloAccessor();
		MetadataAccessor metadataAccessor = PedagogicalGuidanceAgent.getMetadataAccessor();

		IMetadata metadata =  rooloAccessor.getMetadata(eloUri);
		IMetadataValueContainer forkOfContainer = metadataAccessor.readMetadataValue(metadata, CoreRooloMetadataKeyIds.IS_FORK_OF.getId());
		if(forkOfContainer != null) {
			return getTemplateEloUri(forkOfContainer.getValue().toString());
		}
		// No fork, so this is the template
		return eloUri;
	}
	
	private void requestHistory() {
		if(this.historyListener == null) {
			return;
		}
		HistoryRequestEvent event = new HistoryRequestEvent(this, this.userElo2Activity.values(), System.currentTimeMillis());
		try {
			List<UserAction> history = this.historyListener.requestHistory(event);
			for(UserAction userAction : history) {
				Activity activity = getActivityByEloUri(userAction.getEloUri());
				activity.setLatestModificationTime(userAction.getTimestamp());
				if(userAction.getActiontype() == Type.MODIFICATION) {
					activity.setState(ActivityState.MODIFIED);
				} else {
					activity.setState(ActivityState.FINISHED);
				}
			}
		} catch (TupleSpaceException e) {
			// TODO handle error
			e.printStackTrace();
		}
	}
	
	public void addActivityModificationListener(ActivityModificationListener listener) {
		if(listener != null) {
			this.modificationListeners.add(listener);
		}
	}
	
	public void setHistoryRequestListener(HistoryRequestListener listener) {
		this.historyListener = listener;
	}
	
	class MissionModelBuilder implements Runnable {
		
		private final BlockingQueue<Tuple> tupleQueue = new LinkedBlockingQueue<Tuple>();
		private final MissionModel missionModel;
		
		public MissionModelBuilder(MissionModel missionModel) {
			if(missionModel == null) {
				throw new IllegalArgumentException("missionModel can not be null");
			}
			this.missionModel = missionModel;
		}

		public void addTuple(Tuple t) {
			try {
				this.tupleQueue.put(t);
			} catch (InterruptedException e) {
				logger.warn("Error while adding Tuple to creation queue.");
			}
		}
		
		@Override
		public void run() {
			try {
				missionModel.reconstruct();
				
				// after reconstructing the model, all tuples will be applied to it. if a newer tuple arrives
				// in the meantime , tuples in this queue won't be applied to the model due to older timestamps
				missionModel.setInitialized();
				while(!tupleQueue.isEmpty()) {
					Tuple tuple = tupleQueue.remove();
					missionModel.processTuple(tuple);
				}
			} catch (NoSuchElementException e) {
				logger.warn("Tried to remove an element from an empty queue");
			} catch (URISyntaxException e) {
				logger.error("Could not create model because eloUri is no valid URL", e);
			} catch (NoRepositoryAvailableException e) {
				logger.error("Could not create mission model, because no repository available.", e);
			} catch (NoMetadataTypeManagerAvailableException e) {
				logger.error("Could not create mission model, because no MetadataTypeManager available.", e);
			} catch (InvalidActivityException e) {
				logger.error("Could not create mission model, because activities could not be created properly.", e);
			} catch (MissionMapModelException e) {
				logger.error("Could not create mission model, because reading of mission map model failed. " + e.getMessage(), e);
			} catch (Exception e) {
				logger.error("Could not create mission model! Reason: " + e.getMessage(), e);
			}
		}
	}
}
