package eu.scy.agents.agenda.guidance.model;

import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.activity.InvalidActivityException;

import org.apache.log4j.Logger;

import roolo.elo.api.IELO;
import eu.scy.agents.agenda.exception.MissionMapModelException;
import eu.scy.agents.agenda.exception.NoRepositoryAvailableException;
import eu.scy.agents.agenda.guidance.EloUriCache;
import eu.scy.agents.agenda.guidance.PedagogicalGuidanceAgent;
import eu.scy.agents.agenda.guidance.event.ActivityModificationEvent;
import eu.scy.agents.agenda.guidance.event.ActivityModificationListener;
import eu.scy.agents.agenda.guidance.event.HistoryRequestEvent;
import eu.scy.agents.agenda.guidance.event.HistoryRequestListener;
import eu.scy.agents.agenda.guidance.model.Activity.ActivityState;
import eu.scy.agents.agenda.serialization.UserAction;
import eu.scy.agents.agenda.serialization.UserAction.Type;
import eu.scy.agents.session.Session;
import eu.scy.common.mission.Las;
import eu.scy.common.mission.MissionAnchor;
import eu.scy.common.mission.MissionModelElo;
import eu.scy.common.mission.MissionRuntimeElo;
import eu.scy.common.scyelo.RooloServices;
import eu.scy.common.scyelo.ScyElo;

public class MissionModel {

	enum ModelState {
		Enabled, 
		Initializing, 
		Initialized;
	}
	
	private static final Logger logger = Logger.getLogger(MissionModel.class.getName());
	
	private RooloServices rooloServices = PedagogicalGuidanceAgent.getRooloServices();
	
	// ELO URI (Version #0) -> Activity
	private final Map<String, Activity> eloUriV0ToActivityMap = new HashMap<String, Activity>();
	private final List<Dependency> dependencies = new ArrayList<Dependency>();
	private final List<ActivityModificationListener> modificationListeners = new ArrayList<ActivityModificationListener>();
	private final EloUriCache cache = new EloUriCache();
	private final Session session;
	
	private final String missionRuntimeUri;
	private final String user;
	private volatile ModelState state;
	private HistoryRequestListener historyListener;
	private MissionModelBuilder mmb;
	
	
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
	
	public boolean isEnabled() {
		synchronized (this) {
			return (this.state == ModelState.Enabled);
		}
	}
	
	/**
	 * Gets the activity by elo uri. Returns null if no first version ELO can be found for
	 * the given ELO URI.
	 *
	 * @param eloUri the elo uri
	 * @return the activity by elo uri
	 * @throws URISyntaxException the uRI syntax exception
	 * @throws NoRepositoryAvailableException the no repository available exception
	 */
	private Activity getActivityByEloUri(String eloUri) throws NoRepositoryAvailableException, URISyntaxException {
		String firstVersionEloUri = this.cache.getFirstVersion(eloUri);
		if(this.eloUriV0ToActivityMap.containsKey(firstVersionEloUri)) {
			return this.eloUriV0ToActivityMap.get(firstVersionEloUri);
		} else {
			IELO firstVersionElo = PedagogicalGuidanceAgent.getRooloAccessor().getEloFirstVersion(eloUri);
			firstVersionEloUri = firstVersionElo.getUri().toString();
			
			// TODO does not work atm
//			while(this.eloUriV0ToActivityMap.containsKey(firstVersionEloUri)) {
//				ScyElo scyElo = ScyElo.loadElo(URI.create(eloUri), PedagogicalGuidanceAgent.getRooloServices());
//				firstVersionEloUri = scyElo.getIsForkedOfEloUri().toString();
//				
//			}
//			while (! contains fveu) 
//				firstVersionEloUri = loadfirstversionfromforkbase(firsv);
//				if template break
//			
			if(this.eloUriV0ToActivityMap.containsKey(firstVersionEloUri)) {
				this.cache.addEntry(firstVersionEloUri, eloUri);
				return this.eloUriV0ToActivityMap.get(firstVersionEloUri);
			}
			throw new RuntimeException("No first version ELO found for uri: " + eloUri);
		}
	}
	
	private List<Activity> getDependingActivities(Activity activity) {
		ArrayList<Activity> list = new ArrayList<Activity>(); 
		for(Dependency dep : this.dependencies) {
			if(dep.getDependent().equals(activity)) {
				list.add(dep.getDependsOn());
			}
		}
		return list;
	}
	
	public void processTuple(Tuple tuple) {
		synchronized(this) {
			switch(this.state) {
			case Initializing:
				this.mmb.addTuple(tuple);
				break;
			case Initialized:
				// tidy up
				if(this.mmb != null) {
					this.mmb = null;
				}
				try {
					applyTupleToModel(tuple);
				} catch (URISyntaxException e1) {
					logger.warn("Could not process tuple, because no first version elo was found for ELO URI", e1);
				} catch (NoRepositoryAvailableException e1) {
					logger.warn("Could not process tuple, because repository was not available");
				}
				break;
			case Enabled:
				// TODO error handling...
				break;
			}
		}
	}
	
	private void applyTupleToModel(Tuple tuple) throws URISyntaxException, NoRepositoryAvailableException {
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
	
	public void build() {
		synchronized(this) {
			this.state = ModelState.Initializing;
			this.mmb = new MissionModelBuilder();
			new Thread(this.mmb).start();
		}
	}
	
	private void buildMissionModel() throws MissionMapModelException, URISyntaxException, NoRepositoryAvailableException, InvalidActivityException {
		logger.debug(String.format("Creating dependency model [ user: %s | missionRuntimeUri %s ]", 
				getUser(), 
				getMissionRuntimeUri()));
		
		MissionRuntimeElo missionRuntimeElo = MissionRuntimeElo.loadElo(URI.create(this.missionRuntimeUri), rooloServices);
		URI missionMapModelEloUri = missionRuntimeElo.getTypedContent().getMissionMapModelEloUri();
		
//		// MissionAnchorID -> MissionAnchorModel
		Map<String, MissionAnchor> missionIdToMissionAnchorModelMap = new HashMap<String, MissionAnchor>();
		
		// MissionAnchorID -> Activity
		Map<String, Activity> missionIdToActivityMap = new HashMap<String, Activity>();
		
		if (missionMapModelEloUri != null) {
			MissionModelElo missionModelElo = MissionModelElo.loadElo(missionMapModelEloUri, rooloServices);
			List<Las> lasses = missionModelElo.getTypedContent().getLasses();
			for (Las las : lasses) {
				MissionAnchor missionAnchor = las.getMissionAnchor();
				String anchorId = missionAnchor.getId();
				String eloUri = missionAnchor.getEloUri().toString();
				missionIdToMissionAnchorModelMap.put(anchorId, missionAnchor);
				
				Activity activity = new Activity(anchorId);
				activity.setFirstVersionEloUri(eloUri);
				this.eloUriV0ToActivityMap.put(eloUri, activity);
				missionIdToActivityMap.put(anchorId, activity);

				List<MissionAnchor> intermediateAnchors = las.getIntermediateAnchors();
				for (MissionAnchor intermediateAnchor : intermediateAnchors) {
					String anchorId2 = intermediateAnchor.getId();
					String eloUri2 = intermediateAnchor.getEloUri().toString();
					missionIdToMissionAnchorModelMap.put(anchorId2, intermediateAnchor);
					
					Activity activity2 = new Activity(anchorId2);
					activity2.setFirstVersionEloUri(eloUri2);
					this.eloUriV0ToActivityMap.put(eloUri2, activity2);
					missionIdToActivityMap.put(anchorId2, activity2);
				}
			}
		}
		
		// now add the dependencies for each activity
		for(String anchorId : missionIdToMissionAnchorModelMap.keySet()) {
			Activity dependent = missionIdToActivityMap.get(anchorId);
			for(String dependingOnId : missionIdToMissionAnchorModelMap.get(anchorId).getDependingOnMissionAnchorIds()) {
				Activity dependingOn = missionIdToActivityMap.get(dependingOnId);
				if(dependingOn == null) {
					logger.warn("Could not find Activity for: " + dependingOnId);
					continue;
				}
				Dependency dep = new Dependency(dependent, dependingOn);
				this.dependencies.add(dep);
			}
		}
		logger.debug(String.format(
				"MissionModel for user %s successfully created. Activities: %s, Dependencies: %s", 
				this.user,
				missionIdToActivityMap.size(),
				dependencies.size()));
	}
	
	private void requestHistory(long timestamp) {
		if(this.historyListener == null) {
			return;
		}
		
		HistoryRequestEvent event = new HistoryRequestEvent(this, this.eloUriV0ToActivityMap.values(), timestamp);
		try {
			List<UserAction> history = this.historyListener.requestHistory(event);
			for(UserAction userAction : history) {
				try {
					Activity activity = getActivityByEloUri(userAction.getEloUri());
					activity.setLatestModificationTime(userAction.getTimestamp());
					if(userAction.getActiontype() == Type.MODIFICATION) {
						activity.setState(ActivityState.MODIFIED);
					} else {
						activity.setState(ActivityState.FINISHED);
					}
				} catch (URISyntaxException e1) {
					logger.warn("Could not process tuple, because no first version elo was found for ELO URI: " + userAction.getEloUri());
				} catch (NoRepositoryAvailableException e1) {
					logger.warn("Could not process tuple, because repository was not available");
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
		
		public MissionModelBuilder() {
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
				// 1. option: use missionRuntime content to get missionMapModelElo
				buildMissionModel();
				
				// request history of this mission model
//				long date = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" ).parse("2012-01-01 00:00:00").getTime();
//				requestHistory(date);
				
				// after reconstructing the model, all tuples will be applied to it. if a newer tuple arrives
				// in the meantime , tuples in this queue won't be applied to the model due to older timestamps
				synchronized (this) {
					state = ModelState.Initialized;
				}
				while(!tupleQueue.isEmpty()) {
					Tuple tuple = tupleQueue.remove();
					processTuple(tuple);
				}
			} catch (NoSuchElementException e) {
				logger.warn("Tried to remove an element from an empty queue");
			} catch (URISyntaxException e) {
				logger.error("Could not create model because eloUri is no valid URL", e);
			} catch (NoRepositoryAvailableException e) {
				logger.error("Could not create mission model, because no repository available.", e);
			} catch (InvalidActivityException e) {
				logger.error("Could not create mission model, because activities could not be created properly.", e);
			} catch (MissionMapModelException e) {
				logger.error("Could not create mission model, because reading of mission map model failed. " + e.getMessage(), e);
//			} catch (ParseException e) {
//				logger.error("Could not create mission model, because reading of mission map model failed. " + e.getMessage(), e);
			} catch (Exception e) {
				logger.error("Could not create mission model! Reason: " + e.getMessage(), e);
			}
		}
	}
	
}
