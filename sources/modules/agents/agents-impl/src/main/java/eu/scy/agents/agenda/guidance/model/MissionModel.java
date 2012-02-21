package eu.scy.agents.agenda.guidance.model;

import info.collide.sqlspaces.commons.Tuple;

import java.net.URI;
import java.net.URISyntaxException;
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
import eu.scy.agents.agenda.guidance.event.SendMessageEvent;
import eu.scy.agents.agenda.guidance.event.SendMessageListener;
import eu.scy.agents.agenda.guidance.event.StatusChangedEvent;
import eu.scy.agents.agenda.guidance.event.StatusChangedListener;
import eu.scy.agents.agenda.guidance.model.Activity.ActivityState;
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
	
	// MissionID -> Activity
	private final Map<String, Activity> missionIdToActivityMap = new HashMap<String, Activity>();
	
	// ELO URI -> MissionID
	private final Map<String, String> eloUriV0ToMissionIdyMap = new HashMap<String, String>();

	private final String userName;
	private volatile ModelState state;
	private String missionRuntimeUri;
	private String missionTitle;
	private MissionModelBuilder mmb;
	
	private final RooloServices rooloServices;
	private StatusChangedListener changeListener;
	private SendMessageListener messageListener;
//	private HistoryRequestListener historyListener;
	
	
	public MissionModel(RooloServices rooloServices, String missionRuntimeUri, String userName) {
		this.rooloServices = rooloServices;
		this.missionRuntimeUri = missionRuntimeUri;
		this.missionTitle = "Unknown title";
		this.userName = userName;
		this.state = ModelState.Enabled;
	}

	public String getMissionRuntimeUri() {
		return this.missionRuntimeUri;
	}

	public String getUserName() {
		return this.userName;
	}
	
	public boolean isEnabled() {
		synchronized (this) {
			return (this.state == ModelState.Enabled);
		}
	}
	
	/**
	 * Searchs the activity by elo uri. Returns null if no first version ELO can be found for
	 * the given ELO URI.
	 *
	 * @param eloUri the elo uri
	 * @return the activity by elo uri
	 * @throws URISyntaxException the uRI syntax exception
	 * @throws NoRepositoryAvailableException the no repository available exception
	 */
	private Activity searchActivityByEloUri(String eloUri) throws NoRepositoryAvailableException, URISyntaxException {
		String tempEloUri = eloUri;
		while(true) {
			// tempEloUri contains current version of ELO (Version #x)
			
			if(this.eloUriV0ToMissionIdyMap.containsKey(tempEloUri)) {
				// We already have the ELO URI cached
				String missionId = this.eloUriV0ToMissionIdyMap.get(tempEloUri);
				Activity activity = this.missionIdToActivityMap.get(missionId);
				if(activity.getEloTitle() == null) {
					// some ELOs do not increment their version number. So we have the ELO URI
					// in our cache, but the activity has no ELO title set.
					ScyElo scyElo = ScyElo.loadElo(URI.create(tempEloUri), this.rooloServices);
					activity.setEloTitle(scyElo.getTitle());
				}
				return activity;
			} else {
				// ELO URI not in cache, so get first version of ELO URI (Version #0)
				logger.debug("Getting first version of ELO with URI: " + tempEloUri);
				ScyElo scyElo = ScyElo.loadElo(URI.create(tempEloUri), this.rooloServices);

				URI uriFirstVersion = scyElo.getUriFirstVersion();
				if(uriFirstVersion == null && scyElo.getTemplate()) {
					// template... stop here
					logger.warn(String.format("Could not find activity for ELO with URI: %s (Template reached)", eloUri));
					return null;
				} else if(uriFirstVersion == null && !scyElo.getTemplate()) {
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
					activity.setEloTitle(scyElo.getTitle());
					return activity;
				} else {
					// first version is unknown - maybe this ELO is a fork
					URI forkedOfEloUri = scyElo.getIsForkedOfEloUri();
					if(forkedOfEloUri != null) {
						// try again with forkedOf ELO URI
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
			}
		}
	}
	
	private void applyTupleToModel(Tuple tuple) throws URISyntaxException, NoRepositoryAvailableException {
		String type = tuple.getField(0).getValue().toString().toUpperCase();
		String eloUri = tuple.getField(3).getValue().toString();
		long timestamp = Long.valueOf(tuple.getField(4).getValue().toString());

		Activity activity = searchActivityByEloUri(eloUri);
		if(activity == null) {
			// already logged
			return;
		}
		if(timestamp <= activity.getLastModificationTime()) {
			// action too old
			return;
		}
		
		try {
			ActivityState activityOldState = activity.getState();
			ActivityState activityNewState = ActivityState.valueOf(type);
			activity.setLastModificationTime(timestamp);
			if(activityNewState == activityOldState) {
				return;
			}
			
			activity.setState(activityNewState);
			logger.debug(String.format(
					"Activity state has been changed: %s -> %s ( User: %s | Mission: %s | ELO: %s )",
					activityOldState.toString(),
					activityNewState.toString(),
					getUserName(), 
					getMissionRuntimeUri(),
					eloUri));
			
			if(activityNewState == ActivityState.MODIFIED) {
				
				sendStatusNotification(activity, activityNewState);
				if(activityOldState == ActivityState.FINISHED) {
					// finished -> modified
					sendMessage(
							String.format("You have modified ELO '%s' which has already been marked as finished", activity.getEloTitle()), 
							timestamp);
					
					// check the state of all successors
					// finished activities will be marked as NEEDTOCHECK
					for(Activity dependency : activity.getSuccessors()) {
						ActivityState depOldState = dependency.getState();
						if(depOldState == ActivityState.FINISHED) {
							ActivityState depNewState = ActivityState.NEEDTOCHECK;
							
							dependency.setState(depNewState);
							sendStatusNotification(dependency, depNewState);
							sendMessage(
									String.format("Your modification of ELO '%s' means that you should have a look at ELO '%s'", 
											activity.getEloTitle(),
											dependency.getEloTitle()), 
									timestamp);
						}
					}
					
				} else if(activityOldState == ActivityState.NEEDTOCHECK) {
					// needtocheck -> modified
					
					sendMessage(
							String.format("You have modified ELO '%s' which was marked as need to check", activity.getEloTitle()), 
							timestamp);
					
					// check the state of all successors
					// finished activities will be marked as NEEDTOCHECK
					for(Activity dependency : activity.getSuccessors()) {
						ActivityState depOldState = dependency.getState();
						if(depOldState == ActivityState.FINISHED) {
							ActivityState depNewState = ActivityState.NEEDTOCHECK;
							
							dependency.setState(depNewState);
							sendStatusNotification(dependency, depNewState);
							sendMessage(
									String.format("Your modification of ELO '%s' means that you should have a look at ELO '%s'", 
											activity.getEloTitle(),
											dependency.getEloTitle()), 
									timestamp);
						}
					}
					
				} else {
					// enabled -> modified
					sendMessage(
							String.format("You have modified ELO '%s'", activity.getEloTitle()), 
							timestamp);
				}
				
			} else if(activityNewState == ActivityState.FINISHED) {
				// * -> finished
				// display new state of this activity.
				sendStatusNotification(activity, activityNewState);
				sendMessage(
						String.format("You have marked ELO '%s' as finished", activity.getEloTitle()), 
						timestamp);

				// Add following ELOs (successors) to curtain, that become available by finishing an activity.
				// A following ELO will be displayed if all predecessors are finished
				for(Activity successor : activity.getSuccessors()) {
					if(successor.getState() != ActivityState.ENABLED) {
						// when successor is modified, needtocheck or finished, it is already in the curtain
						continue;
					}
					boolean allPredecessorsFinished = true;
					for(Activity predecessor : successor.getPredecessors()) {
						if(predecessor.getState() != ActivityState.FINISHED) {
							allPredecessorsFinished = false;
							break;
						}
					}
					if(allPredecessorsFinished) {
						// status has not changed, but maybe it was not visible
						if(successor.getEloTitle() == null) {
							searchActivityByEloUri(successor.getCurrentEloUri());
						}
						sendStatusNotification(successor, successor.getState());
						sendMessage(
								String.format(
										"Your completion of ELO '%s' has enabled ELO '%s'", 
										activity.getEloTitle(), 
										successor.getEloTitle()), 
								timestamp);
					}
				}
			}
			
		} catch (IllegalArgumentException e) {
			logger.warn(String.format("Received tuple for user '%s' and mission '%s' has the unknown type: '%s'.", 
					getUserName(), 
					getMissionRuntimeUri(),
					type));
		}
		logger.debug(toString());
	}

	public void build() {
		synchronized(this) {
			this.state = ModelState.Initializing;
			this.mmb = new MissionModelBuilder(this);
			new Thread(this.mmb).start();
		}
	}
	
	private void buildMissionModel() throws MissionMapModelException, URISyntaxException, NoRepositoryAvailableException, InvalidActivityException {
		logger.debug(String.format("Creating dependency model [ user: %s | missionRuntimeUri %s ]", 
				getUserName(), 
				getMissionRuntimeUri()));
		
		MissionRuntimeElo missionRuntimeElo = MissionRuntimeElo.loadElo(URI.create(this.missionRuntimeUri), rooloServices);
		URI missionMapModelEloUri = missionRuntimeElo.getTypedContent().getMissionMapModelEloUri();
		this.missionTitle = missionRuntimeElo.getTitle();
		
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
			Activity activity = missionIdToActivityMap.get(anchorId);
			for(String dependingOnId : missionIdToMissionAnchorModelMap.get(anchorId).getDependingOnMissionAnchorIds()) {
				Activity dependingOn = missionIdToActivityMap.get(dependingOnId);
				if(dependingOn == null) {
					logger.warn("Could not find Activity for: " + dependingOnId);
					continue;
				}
				activity.getPredecessors().add(dependingOn);
				dependingOn.getSuccessors().add(activity);
				dependencyCount++;
			}
		}
		logger.debug(String.format(
				"MissionModel for user %s successfully created. Activities: %s, Dependencies: %s", 
				this.userName,
				missionIdToActivityMap.size(),
				dependencyCount));
		sendMessage("You have opened mission '" + this.missionTitle + "'", System.currentTimeMillis());
		logger.debug(toString());
	}
	
	private void sendMessage(String message, long timestamp) {
		SendMessageEvent event = new SendMessageEvent(this, message, timestamp);
		this.messageListener.sendMessage(event);
	}
	
	private void sendStatusNotification(Activity activity, ActivityState afterState) {
		StatusChangedEvent event = new StatusChangedEvent(this, activity, afterState);
		this.changeListener.statusChanged(event);
		
	}
	
	public void setActivityModificationListener(StatusChangedListener listener) {
		if(listener != null) {
			this.changeListener = listener;
		}
	}
	
	public void setSendMessageListener(SendMessageListener listener) {
		if(listener != null) {
			this.messageListener = listener;
		}
	}

//	public void setHistoryRequestListener(HistoryRequestListener listener) {
//		this.historyListener = listener;
//	}
	
//	private void requestHistory(long timestamp) throws TupleSpaceException, NoRepositoryAvailableException {
//		if(this.historyListener == null) {
//			return;
//		}
//		
//		// FIXME problematic, because elo uris change... so activity evaluation agents won't find all corresponding tuples
//		HistoryRequestEvent event = new HistoryRequestEvent(this, this.missionIdToActivityMap.values(), timestamp);
//		List<UserAction> history = this.historyListener.requestHistory(event);
//		for(UserAction userAction : history) {
//			try {
//				Activity activity = getActivityByEloUri(userAction.getEloUri());
//				if(activity == null) {
//					logger.warn("Could not find activity for ELO with URI: " + userAction.getEloUri());
//					return;
//				}
//				activity.setLatestModificationTime(userAction.getTimestamp());
//				if(userAction.getActiontype() == Type.MODIFICATION) {
//					activity.setState(ActivityState.MODIFIED);
//				} else {
//					activity.setState(ActivityState.FINISHED);
//				}
//			} catch (URISyntaxException e1) {
//				logger.warn("Could not process tuple, because no first version elo was found for ELO URI: " + userAction.getEloUri());
//			}
//		}
//	}
	
	class MissionModelBuilder implements Runnable {
		
		private final BlockingQueue<Tuple> tupleQueue = new LinkedBlockingQueue<Tuple>();
		private final MissionModel missionModel;
		
		public MissionModelBuilder(MissionModel missionModel) {
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
				buildMissionModel();
				
				// request history of this mission model
//				long date = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" ).parse("2012-01-01 00:00:00").getTime();
//				requestHistory(date);
				
				// after reconstructing the model, all tuples will be applied to it. if a newer tuple arrives
				// in the meantime , tuples in this queue won't be applied to the model due to older timestamps
				synchronized (missionModel) {
					state = ModelState.Initialized;
					while(!tupleQueue.isEmpty()) {
						Tuple tuple = tupleQueue.remove();
						processTuple(tuple);
					}
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
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Title: ");
		sb.append(this.missionTitle);
		sb.append("\n");
		sb.append("MissionRuntime URI: ");
		sb.append(this.missionRuntimeUri);
		sb.append("\n");
		sb.append("UserName: ");
		sb.append(this.userName);
		sb.append("\n");
		sb.append("Activities: ");
		sb.append(this.missionIdToActivityMap.size());
		sb.append("\n");
		for(Activity activity : this.missionIdToActivityMap.values()) {
			sb.append(activity.toString());
			sb.append("\n");
		}
		return sb.toString();
	}
	
}
