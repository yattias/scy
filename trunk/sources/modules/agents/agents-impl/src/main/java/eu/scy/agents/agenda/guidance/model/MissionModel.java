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

import eu.scy.agents.agenda.exception.MissionMapModelException;
import eu.scy.agents.agenda.exception.NoRepositoryAvailableException;
import eu.scy.agents.agenda.guidance.event.ActivityModificationEvent;
import eu.scy.agents.agenda.guidance.event.ActivityModificationListener;
import eu.scy.agents.agenda.guidance.event.HistoryRequestEvent;
import eu.scy.agents.agenda.guidance.event.HistoryRequestListener;
import eu.scy.agents.agenda.guidance.model.Activity.ActivityState;
import eu.scy.agents.agenda.serialization.UserAction;
import eu.scy.agents.agenda.serialization.UserAction.Type;
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
	
	private final List<ActivityModificationListener> modificationListeners = new ArrayList<ActivityModificationListener>();

	// MissionID -> Activity
	private final Map<String, Activity> missionIdToActivityMap = new HashMap<String, Activity>();
	
	// ELO URI -> MissionID
	private final Map<String, String> eloUriV0ToMissionIdyMap = new HashMap<String, String>();

	private final String missionRuntimeUri;
	private final String user;
	private volatile ModelState state;
	private HistoryRequestListener historyListener;
	private MissionModelBuilder mmb;
	
	private final RooloServices rooloServices;
	
	public MissionModel(RooloServices rooloServices, String missionRuntimeUri, String userName) {
		this.rooloServices = rooloServices;
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
		String tempEloUri = eloUri;
		while(true) {
			// tempEloUri contains current version of ELO (Version #x)
			
			if(this.eloUriV0ToMissionIdyMap.containsKey(tempEloUri)) {
				// We already know the uri
				String missionId = this.eloUriV0ToMissionIdyMap.get(tempEloUri);
				return this.missionIdToActivityMap.get(missionId);
			} else {
				// We don't know, so get first version of ELO URI (Version #0)
				logger.debug("Getting first version of ELO with URI: " + tempEloUri);
				ScyElo scyElo = ScyElo.loadElo(URI.create(tempEloUri), this.rooloServices);

				if(scyElo.getTemplate()) {
					// template... stop here
					logger.warn(String.format("Could not find activity for ELO with URI: %s (Template reached found)", eloUri));
					return null;
				}
				URI uriFirstVersion = scyElo.getUriFirstVersion();
				if(uriFirstVersion == null) {
					// no template and this is the fist version... stop here
					logger.warn(String.format("Could not find activity for ELO with URI: %s (ELO has no first version)", eloUri));
					return null;
				}
				
				// now working with the first version of ELO URI
				tempEloUri = uriFirstVersion.toString();
				scyElo = ScyElo.loadElo(URI.create(tempEloUri), this.rooloServices);
				if(this.eloUriV0ToMissionIdyMap.containsKey(tempEloUri)) {
					// We know the version#0 uri, so we'll save the version#x uri and return the activity
					String missionId = this.eloUriV0ToMissionIdyMap.get(tempEloUri);
					this.eloUriV0ToMissionIdyMap.put(eloUri, missionId);
					Activity activity = this.missionIdToActivityMap.get(missionId);
					activity.setCurrentEloUri(eloUri);
					return activity;
				} else {
					// first version is unknown - maybe this ELO is a fork
					URI forkedOfEloUri = scyElo.getIsForkedOfEloUri();
					if(forkedOfEloUri != null) {
						// try again with forkedOf ELO
						logger.debug(String.format("Lookup forked version of ELO ( %s -> %s)", tempEloUri, forkedOfEloUri.toString()));
						tempEloUri = forkedOfEloUri.toString();
					} else {
						// No first version and no fork... what the hell is going on?
						logger.error(String.format(
								"Could not find activity for ELO with URI: %s (Ended with elo with no first version and no forkOf: %s)",
								eloUri,
								tempEloUri));
						return null;
					}
				}
			}
		}
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
		if(activity == null) {
			// already logged
			return;
		}
		if(timestamp > activity.getLatestModificationTime()) {
			try {
				Activity.ActivityState actState = Activity.ActivityState.valueOf(type);
				activity.setLatestModificationTime(timestamp);
				
				if(actState != activity.getState()) {
					logger.debug(String.format("Set state %s for activity %s (user: %s | mission: %s).", 
							type,
							eloUri, 
							getUser(), 
							getMissionRuntimeUri()));
					
					if(actState == ActivityState.MODIFIED && activity.getState() == ActivityState.FINISHED) {
						List<Activity> dependingActivities = activity.getDependencies();
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
		
		if (missionMapModelEloUri != null) {
			MissionModelElo missionModelElo = MissionModelElo.loadElo(missionMapModelEloUri, rooloServices);
			List<Las> lasses = missionModelElo.getTypedContent().getLasses();
			for (Las las : lasses) {
				MissionAnchor mainAnchor = las.getMissionAnchor();
				String mainAnchorId = mainAnchor.getId();
				String mainAnchorEloUri = mainAnchor.getEloUri().toString();
				missionIdToMissionAnchorModelMap.put(mainAnchorId, mainAnchor);
				
				Activity mainActivity = new Activity(mainAnchorId, mainAnchorEloUri);
				this.eloUriV0ToMissionIdyMap.put(mainAnchorEloUri, mainAnchorId);
				this.missionIdToActivityMap.put(mainAnchorId, mainActivity);

				List<MissionAnchor> intermediateAnchors = las.getIntermediateAnchors();
				for (MissionAnchor intermediateAnchor : intermediateAnchors) {
					String intermediateAnchorId = intermediateAnchor.getId();
					String intermediateAnchorEloUri = intermediateAnchor.getEloUri().toString();
					missionIdToMissionAnchorModelMap.put(intermediateAnchorId, intermediateAnchor);
					
					Activity intermediateActivity = new Activity(intermediateAnchorId, intermediateAnchorEloUri);
					this.eloUriV0ToMissionIdyMap.put(intermediateAnchorEloUri, intermediateAnchorId);
					this.missionIdToActivityMap.put(intermediateAnchorId, intermediateActivity);
				}
			}
		}
		
		// now add the dependencies for each activity
		int dependencyCount = 0; 
		for(String anchorId : missionIdToMissionAnchorModelMap.keySet()) {
			Activity dependent = missionIdToActivityMap.get(anchorId);
			for(String dependingOnId : missionIdToMissionAnchorModelMap.get(anchorId).getDependingOnMissionAnchorIds()) {
				Activity dependingOn = missionIdToActivityMap.get(dependingOnId);
				if(dependingOn == null) {
					logger.warn("Could not find Activity for: " + dependingOnId);
					continue;
				}
				dependent.getDependencies().add(dependingOn);
				dependencyCount++;
			}
		}
		logger.debug(String.format(
				"MissionModel for user %s successfully created. Activities: %s, Dependencies: %s", 
				this.user,
				missionIdToActivityMap.size(),
				dependencyCount));
	}
	
	private void requestHistory(long timestamp) throws TupleSpaceException, NoRepositoryAvailableException {
		if(this.historyListener == null) {
			return;
		}
		
		// FIXME problematic, because elo uris change... so activity evaluation agents won't find all corresponding tuples
		HistoryRequestEvent event = new HistoryRequestEvent(this, this.missionIdToActivityMap.values(), timestamp);
		List<UserAction> history = this.historyListener.requestHistory(event);
		for(UserAction userAction : history) {
			try {
				Activity activity = getActivityByEloUri(userAction.getEloUri());
				if(activity == null) {
					logger.warn("Could not find activity for ELO with URI: " + userAction.getEloUri());
					return;
				}
				activity.setLatestModificationTime(userAction.getTimestamp());
				if(userAction.getActiontype() == Type.MODIFICATION) {
					activity.setState(ActivityState.MODIFIED);
				} else {
					activity.setState(ActivityState.FINISHED);
				}
			} catch (URISyntaxException e1) {
				logger.warn("Could not process tuple, because no first version elo was found for ELO URI: " + userAction.getEloUri());
			}
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
//			} catch (NoRepositoryAvailableException e1) {
//				logger.warn("Could not process tuple, because repository was not available");
			} catch (Exception e) {
				logger.error("Could not create mission model! Reason: " + e.getMessage(), e);
			}
		}
	}
	
}
