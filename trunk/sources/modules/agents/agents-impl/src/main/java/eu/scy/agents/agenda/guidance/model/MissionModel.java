package eu.scy.agents.agenda.guidance.model;

import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleID;
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
import java.util.concurrent.locks.ReentrantLock;

import javax.activity.InvalidActivityException;

import org.apache.log4j.Logger;

import eu.scy.agents.agenda.exception.InvalidActivityTupleException;
import eu.scy.agents.agenda.guidance.event.ClearCurtainEvent;
import eu.scy.agents.agenda.guidance.event.ClearCurtainListener;
import eu.scy.agents.agenda.guidance.event.DialogNotificationEvent;
import eu.scy.agents.agenda.guidance.event.DialogNotificationListener;
import eu.scy.agents.agenda.guidance.event.LoadActivitiesEvent;
import eu.scy.agents.agenda.guidance.event.LoadActivitiesListener;
import eu.scy.agents.agenda.guidance.event.SaveActivityEvent;
import eu.scy.agents.agenda.guidance.event.SaveActivityListener;
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
	
	// ELO URI -> AnchorID
	private final Map<String, String> eloUriV0ToAnchorIdMap = new HashMap<String, String>();

	// AnchorID -> Activity
	private final Map<String, Activity> anchorIdToActivityMap = new HashMap<String, Activity>();
	
	// AnchorID -> TupleID
	private final Map<String, TupleID> anchorIdToTupleIdMap = new HashMap<String, TupleID>();

	private final List<Message> messageHistory = new ArrayList<Message>();
	private final RooloServices rooloServices;
	private final String userName;
	private volatile ModelState state;
	private String missionRuntimeUri;
	private String missionTitle;
	private MissionModelBuilder mmb;
	private boolean curtainReloadNeeded = false;
	
	private StatusChangedListener statusChangedListener;
	private SendMessageListener sendMessageListener;
	private SaveActivityListener saveActivityListener;
	private LoadActivitiesListener loadActivitiesListener;
	private ClearCurtainListener clearCurtainListener;
	private DialogNotificationListener dialogNotificationListener;
	private ReentrantLock reloadCurtainLock = new ReentrantLock(true);
	
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
	 */
	private Activity searchActivityByEloUri(String eloUri) throws URISyntaxException {
		String tempEloUri = eloUri;
		while(true) {
			// tempEloUri contains current version of ELO (Version #x)
			
			if(this.eloUriV0ToAnchorIdMap.containsKey(tempEloUri)) {
				// We already have the ELO URI cached
				String anchorId = this.eloUriV0ToAnchorIdMap.get(tempEloUri);
				Activity activity = this.anchorIdToActivityMap.get(anchorId);
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
				if(uriFirstVersion == null) {
					if(scyElo.getTemplate()) {
						// template... stop here
						logger.warn(String.format("Could not find activity for ELO with URI: %s (Template reached)", eloUri));
					} else {
						// no template and this is the fist version... stop here
						logger.warn(String.format("Could not find activity for ELO with URI: %s (ELO has no first version)", eloUri));
					}
					return null;
				} 
				
				// now working with the first version ELO URI
				tempEloUri = uriFirstVersion.toString();
				scyElo = ScyElo.loadElo(URI.create(tempEloUri), this.rooloServices);
				if(this.eloUriV0ToAnchorIdMap.containsKey(tempEloUri)) {
					// We already know version#0 ELO URI, so save version#x URI and return activity
					String anchorId = this.eloUriV0ToAnchorIdMap.get(tempEloUri);
					this.eloUriV0ToAnchorIdMap.put(eloUri, anchorId);

					Activity activity = this.anchorIdToActivityMap.get(anchorId);
					activity.setCurrentEloUri(eloUri);
					activity.setEloTitle(scyElo.getTitle());
					return activity;
				} else {
					// first version is unknown - maybe this ELO is a fork
					URI forkedOfEloUri = scyElo.getIsForkedOfEloUri();
					if(forkedOfEloUri == null) {
						// No first version and no fork... what the hell is going on?
						logger.error(String.format(
								"Could not find activity for ELO with URI: %s (Ended with elo with no first version and no forkOf: %s)",
								eloUri,
								tempEloUri));
						return null;
					}
					
					// try again with forkedOf ELO URI
					logger.debug(String.format("Lookup forked version of ELO ( %s -> %s)", tempEloUri, forkedOfEloUri.toString()));
					tempEloUri = forkedOfEloUri.toString();
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
				try {
					applyTupleToModel(tuple);
					
				} catch (URISyntaxException e1) {
					logger.warn("Could not process tuple, because no first version elo was found for ELO URI", e1);
				}
				break;
			}
		}
	}
	
	private void applyTupleToModel(Tuple tuple) throws URISyntaxException {
		String type = tuple.getField(0).getValue().toString().toUpperCase();
		String eloUri = tuple.getField(3).getValue().toString();
		long timestamp = Long.valueOf(tuple.getField(4).getValue().toString());

		Activity activity = searchActivityByEloUri(eloUri);
		if(activity == null) {
			// already logged
			return;
		}
		if(timestamp <= activity.getLastModificationTime()) {
			// action too old, save
			return;
		}
		
		ActivityState activityOldState = activity.getState();
		ActivityState activityNewState = null;
		try {
			activityNewState = ActivityState.valueOf(type);
		} catch (IllegalArgumentException e) {
			logger.warn(String.format("Received tuple for user '%s' and mission '%s' has the unknown type: '%s'.", 
					getUserName(), 
					getMissionRuntimeUri(),
					type));
			return;
		}
		
		activity.setLastModificationTime(timestamp);
		changeActivity(activity, activityNewState, true);
		
		if(activityOldState != activityNewState) {
			logger.debug(String.format(
					"Activity state has been changed: %s -> %s ( User: %s | Mission: %s | ELO Title: %s | ELO: %s )",
					activityOldState.toString(),
					activityNewState.toString(),
					getUserName(), 
					getMissionRuntimeUri(),
					activity.getEloTitle(),
					eloUri));
		}
		
		if(activityNewState == ActivityState.MODIFIED && activityOldState != ActivityState.MODIFIED) {
			
			if(activityOldState == ActivityState.NEEDTOCHECK || activityOldState == ActivityState.FINISHED) {
				String message = null;
				if(activityOldState == ActivityState.FINISHED) {
					// finished -> modified
					message = String.format("You have modified ELO '%s' which has already been marked as finished", activity.getEloTitle());
				} else {
					// needtocheck -> modified
					message = String.format("You have modified ELO '%s' which was marked as need to check", activity.getEloTitle());
				}
				sendMessage(message, timestamp);
				
				// check the state of all successors
				// finished activities will be marked as NEEDTOCHECK
				List<Activity> needToCheckSuccessors = changeFinishedActivitiesInNeedToCheck(activity, timestamp);
				
				// print dialog notification to inform user about changes
				if(needToCheckSuccessors.size() > 0) {
					String text = "";
					if(needToCheckSuccessors.size() == 1) {
						text = String.format("Your modification of ELO '%s' implies that you should have a look at ELO '%s'", 
								activity.getEloTitle(),
								needToCheckSuccessors.get(0).getEloTitle());
					} else {
						StringBuilder sb = new StringBuilder();
						sb.append("Your modification of ELO '");
						sb.append(activity.getEloTitle());
						sb.append("' implies that you should have a look at the following ELOs:\n");
						for(Activity successor : needToCheckSuccessors) {
							sb.append("\n");
							sb.append(successor.getEloTitle());
						}
						text = sb.toString();
					}
					sendDialogNotification(text);
				}
			} else if(activityOldState == ActivityState.ENABLED) {
				// enabled -> modified
				sendMessage(
						String.format("You have modified ELO '%s'", activity.getEloTitle()), 
						timestamp);
			}
			
		} else if(activityNewState == ActivityState.FINISHED && activityOldState != ActivityState.FINISHED) {
			// * -> finished
			// display new state of this activity.
			sendMessage(
					String.format("You have marked ELO '%s' as finished", activity.getEloTitle()), 
					timestamp);
			displaySuccessorsIfDependenciesFinished(activity, timestamp);
		}
		
		logger.debug(toString());
	}
	
	private List<Activity> changeFinishedActivitiesInNeedToCheck(Activity activity, long timestamp) {
		List<Activity> needToCheckSuccessors = new ArrayList<Activity>();
		for(Activity successor : activity.getSuccessors()) {
			ActivityState succOldState = successor.getState();
			if(succOldState == ActivityState.FINISHED) {
				
				changeActivity(successor, ActivityState.NEEDTOCHECK, true);
				needToCheckSuccessors.add(successor);
				sendMessage(
						String.format("Your modification of ELO '%s' means that you should have a look at ELO '%s'", 
								activity.getEloTitle(),
								successor.getEloTitle()), 
								timestamp);
			}
		}
		return needToCheckSuccessors;
	}
	
	private void displaySuccessorsIfDependenciesFinished(Activity activity, long timestamp) {
		// Add following ELOs (successors) to curtain, that become available by finishing an activity.
		// A following ELO will be displayed if all predecessors are finished
		for(Activity successor : activity.getSuccessors()) {
			if(successor.isDisplayedInCurtain()) {
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
				changeActivity(successor, successor.getState(), true);
				sendMessage(
						String.format(
								"Your completion of ELO '%s' has enabled ELO '%s'", 
								activity.getEloTitle(), 
								successor.getEloTitle()), 
								timestamp);
			}
		}
	}
	
	private void changeActivity(Activity activity, ActivityState newState, boolean displayInCurtain) {
		if(activity.getEloTitle() == null) {
			ScyElo scyElo = ScyElo.loadElo(URI.create(activity.getCurrentEloUri()), this.rooloServices);
			activity.setEloTitle(scyElo.getTitle());
		}
		if(activity.getState() == newState && activity.isDisplayedInCurtain() == displayInCurtain) {
			// nothing has changed
			return;
		}
		// this means that the activity has been changed
		activity.setState(newState);
		activity.setDisplayedInCurtain(displayInCurtain);
		sendStatusNotification(activity, newState);
		saveActivityInGuidanceSpace(activity);
	}
	
	private void saveActivityInGuidanceSpace(Activity activity) {
		if(this.saveActivityListener == null) {
			return;
		}
		TupleID tupleID = this.anchorIdToTupleIdMap.get(activity.getAnchorId());
		SaveActivityEvent event = new SaveActivityEvent(this, activity, tupleID);
		TupleID returnTupleID;
		try {
			returnTupleID = this.saveActivityListener.saveActivity(event);
			if(tupleID == null) {
				this.anchorIdToTupleIdMap.put(activity.getAnchorId(), returnTupleID);
			}
		} catch (TupleSpaceException e) {
			logger.warn("Could not save activity, because connection to TupleSpace is lost.");
		}
	}

	public void build() {
		synchronized(this) {
			this.state = ModelState.Initializing;
			this.mmb = new MissionModelBuilder(this);
			new Thread(this.mmb).start();
		}
	}

	/**
	 * Builds the mission model. This method gets missionRuntime from repository to look up the mission map model.
	 * Mission map model is then parsed to build the activities of this model. Afterwards the dependencies will
	 * be added to the activities. At last the states of all activities will be set.
	 * After calling this method, the mission model will represent a fresh model with no user actions done.
	 *
	 * @throws URISyntaxException the uRI syntax exception
	 * @throws InvalidActivityException the invalid activity exception
	 */
	private void buildMissionModel() throws URISyntaxException, InvalidActivityException {
		logger.debug(String.format("Creating dependency model [ user: %s | missionRuntimeUri %s ]", 
				getUserName(), 
				getMissionRuntimeUri()));
		
		MissionRuntimeElo missionRuntimeElo = MissionRuntimeElo.loadElo(URI.create(this.missionRuntimeUri), rooloServices);
		URI missionMapModelEloUri = missionRuntimeElo.getTypedContent().getMissionMapModelEloUri();
		this.missionTitle = missionRuntimeElo.getTitle();
		
//		// AnchorID -> MissionAnchorModel
		Map<String, MissionAnchor> anchorIdToMissionAnchorModelMap = new HashMap<String, MissionAnchor>();
		
		if (missionMapModelEloUri != null) {
			MissionModelElo missionModelElo = MissionModelElo.loadElo(missionMapModelEloUri, rooloServices);
			List<Las> lasses = missionModelElo.getTypedContent().getLasses();
			for (Las las : lasses) {
				MissionAnchor mainAnchor = las.getMissionAnchor();
				String mainAnchorId = mainAnchor.getId().trim();
				String mainAnchorEloUri = mainAnchor.getEloUri().toString();
				anchorIdToMissionAnchorModelMap.put(mainAnchorId, mainAnchor);
				
				Activity mainActivity = new Activity(mainAnchorId, mainAnchorEloUri);
				this.eloUriV0ToAnchorIdMap.put(mainAnchorEloUri, mainAnchorId);
				this.anchorIdToActivityMap.put(mainAnchorId, mainActivity);

				List<MissionAnchor> intermediateAnchors = las.getIntermediateAnchors();
				for (MissionAnchor intermediateAnchor : intermediateAnchors) {
					String intermediateAnchorId = intermediateAnchor.getId().trim();
					String intermediateAnchorEloUri = intermediateAnchor.getEloUri().toString();
					anchorIdToMissionAnchorModelMap.put(intermediateAnchorId, intermediateAnchor);
					
					Activity intermediateActivity = new Activity(intermediateAnchorId, intermediateAnchorEloUri);
					this.eloUriV0ToAnchorIdMap.put(intermediateAnchorEloUri, intermediateAnchorId);
					this.anchorIdToActivityMap.put(intermediateAnchorId, intermediateActivity);
				}
			}
		}
		
		// now add the dependencies for each activity
		int dependencyCount = 0; 
		for(String anchorId : anchorIdToMissionAnchorModelMap.keySet()) {
			Activity activity = anchorIdToActivityMap.get(anchorId);
			for(String dependingOnId : anchorIdToMissionAnchorModelMap.get(anchorId).getDependingOnMissionAnchorIds()) {
				Activity dependingOn = anchorIdToActivityMap.get(dependingOnId);
				if(dependingOn == null) {
					logger.warn("Could not find Activity for: " + dependingOnId);
					continue;
				}
				activity.getPredecessors().add(dependingOn);
				dependingOn.getSuccessors().add(activity);
				dependencyCount++;
			}
		}
		// All activities with dependencies will be initiated as finished.
		for(Activity activity : this.anchorIdToActivityMap.values()) {
			if(activity.getPredecessors().size() > 0 || activity.getSuccessors().size() > 0) {
				activity.setState(ActivityState.FINISHED);
			}
		}
		logger.debug(String.format(
				"MissionModel for user %s successfully created. Activities: %s, Dependencies: %s", 
				this.userName,
				anchorIdToActivityMap.size(),
				dependencyCount));
		logger.debug(toString());
	}
	
	private void loadActivityStatesFromGuidanceSpace() {
		if(this.loadActivitiesListener == null) {
			return;
		}
		try {
			List<Tuple> activityTuples = this.loadActivitiesListener.loadActivities(new LoadActivitiesEvent(this));
			logger.debug(String.format(
					"Loaded %s activities persisted in GuidanceSpace [ User: %s | MissionRuntimeUri: %s]",
					activityTuples.size(),
					getUserName(),
					getMissionRuntimeUri()));
			
			for(Tuple tuple : activityTuples) {
				String anchorId = tuple.getField(3).getValue().toString().trim();
				Activity activity = this.anchorIdToActivityMap.get(anchorId);
				if(activity == null) {
					// ignore tuples with unknown anchor id.
					continue;
				}
				try {
					activity.loadFromTuple(tuple);
					this.anchorIdToTupleIdMap.put(anchorId, tuple.getTupleID());
				} catch (InvalidActivityTupleException e) {
					logger.warn(String.format("Error loading activity '%s': %s", anchorId, e.getMessage()));
				}
			}
			if(activityTuples.size() > 0) {
				logger.debug(toString());
			}
		} catch (TupleSpaceException e) {
			logger.warn("Could not load activity states, because connection to TupleSpace is lost.");
		}
	}
	
	private void sendMessage(String text, long timestamp) {
		Message message = new Message(timestamp, text);
		boolean success = sendMessage(message);
		if(success) {
			this.messageHistory.add(message);
		}
	}
	
	private boolean sendMessage(Message message) {
		if(this.sendMessageListener != null) {
			try {
				SendMessageEvent event = new SendMessageEvent(this, message.getMessage(), message.getTimestamp());
				this.sendMessageListener.sendCurtainMessage(event);
				return true;
			} catch (TupleSpaceException e) {
				logger.warn("Could not send message, because connection to TupleSpace is lost.");
			}
		}
		return false;
	}
	
	private void sendStatusNotification(Activity activity, ActivityState afterState) {
		if(this.statusChangedListener != null) {
			try {
				StatusChangedEvent event = new StatusChangedEvent(this, activity, afterState);
				this.statusChangedListener.statusChanged(event);
			} catch (TupleSpaceException e) {
				logger.warn("Could not send status notification, because connection to TupleSpace is lost.");
			}
		}
	}
	
	private void clearCurtain() {
		if(this.clearCurtainListener != null) {
			try {
				this.clearCurtainListener.clearCurtain(new ClearCurtainEvent(this));
			} catch (TupleSpaceException e) {
				logger.warn("Could not send clear curtain message, because connection to TupleSpace is lost.");
			}
		}
	}
	
	private void sendDialogNotification(String text) {
		if(this.dialogNotificationListener != null) {
			try {
				DialogNotificationEvent event = new DialogNotificationEvent(this, text, "n/a");
				this.dialogNotificationListener.sendDialogNotification(event);
			} catch (TupleSpaceException e) {
				logger.warn("Could not send dialog notification, because connection to TupleSpace is lost.");
			}
		}
	}
	
	public void setActivityModificationListener(StatusChangedListener listener) {
		if(listener != null) {
			this.statusChangedListener = listener;
		}
	}
	
	public void setSendMessageListener(SendMessageListener listener) {
		if(listener != null) {
			this.sendMessageListener = listener;
		}
	}

	public void setSaveActivityListener(SaveActivityListener listener) {
		if(listener != null) {
			this.saveActivityListener = listener;
		}
	}
	
	public void setLoadActivitiesListener(LoadActivitiesListener listener) {
		if(listener != null) {
			this.loadActivitiesListener = listener;
		}
	}
	
	public void setClearCurtainListener(ClearCurtainListener listener) {
		if(listener != null) {
			this.clearCurtainListener = listener;
		}
	}
	
	public void setDialogNotificationListener(DialogNotificationListener listener) {
		if(listener != null) {
			this.dialogNotificationListener = listener;
		}
	}
	
	/**
	 * Checks if curtain should be reinitiated.
	 *
	 * @return true, if is reinitiate curtain
	 */
	public boolean isCurtainReloadNeeded() {
		this.reloadCurtainLock.lock();
		try {
			return this.curtainReloadNeeded;
		} finally {
			this.reloadCurtainLock.unlock();
		}
	}

	public void setCurtainReloadNeeded(boolean curtainReloadNeeded) {
		this.reloadCurtainLock.lock();
		try {
			this.curtainReloadNeeded = curtainReloadNeeded;
		} finally {
			this.reloadCurtainLock.unlock();
		}
	}
	
	/**
	 * Reloads the curtain. This method will force a resent of all messages and will also
	 * send messages to display the state of all activities (ELOs).
	 */
	public void reloadCurtain() {
		this.reloadCurtainLock.lock();
		try {
			logger.debug(String.format("Reinitiating curtain for mission model [ user: %s | missionRuntimeUri %s ]", 
					getUserName(), 
					getMissionRuntimeUri()));
			
			// resend messages
			for(Message message : this.messageHistory) {
				sendMessage(message);
			}
			
			// resend activity states
			synchronized (this) {
				for(Activity activity : this.anchorIdToActivityMap.values()) {
					if(!activity.isDisplayedInCurtain()) {
						continue;
					}
					if(activity.getState() != ActivityState.ENABLED) {
						sendStatusNotification(activity, activity.getState());
					} else {
						
						// Add ELOs to curtain whose predecessors are all finished
						boolean allPredecessorsFinished = true;
						for(Activity predecessor : activity.getPredecessors()) {
							if(predecessor.getState() != ActivityState.FINISHED) {
								allPredecessorsFinished = false;
								break;
							}
						}
						if(allPredecessorsFinished) {
							// status has not changed, but maybe it was not visible
							if(activity.getEloTitle() == null) {
								ScyElo scyElo = ScyElo.loadElo(URI.create(activity.getCurrentEloUri()), this.rooloServices);
								activity.setEloTitle(scyElo.getTitle());
							}
							sendStatusNotification(activity, activity.getState());
						}
					}
				}
			}
		} finally {
			this.curtainReloadNeeded = false;
			this.reloadCurtainLock.unlock();
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
		sb.append(this.anchorIdToActivityMap.size());
		sb.append("\n");
		for(Activity activity : this.anchorIdToActivityMap.values()) {
			sb.append(activity.toString());
			sb.append("\n");
		}
		return sb.toString();
	}
	
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
				loadActivityStatesFromGuidanceSpace();
				reloadCurtain();
				
				synchronized (missionModel) {
					// this will empty the queue... new tuples have to wait in the meantime
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
			} catch (InvalidActivityException e) {
				logger.error("Could not create mission model, because activities could not be created properly.", e);
			} catch (Exception e) {
				logger.error("Could not create mission model! Reason: " + e.getMessage(), e);
			}
		}
	}
	
}
