package eu.scy.agents.agenda.guidance.model;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import eu.scy.agents.session.Session;

public class UserModel {

	private static final String MISSION_PREFIX = "roolo://";
	private final String userName;
	private final Map<String, MissionModel> missionMap;
	private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
	private final Session session;
	
	public UserModel(Session session, String name) {
		this.session = session;
		this.userName = name;
		this.missionMap = new HashMap<String, MissionModel>();
	}

	public String getUserName() {
		return this.userName;
	}

	public MissionModel getMission(String missionRuntimeUri) {
		this.rwLock.readLock().lock();
		try {
			return this.missionMap.get(missionRuntimeUri);
		} finally {
			this.rwLock.readLock().unlock();
		}
	}

	public void addMission(MissionModel mission) {
		this.rwLock.writeLock().lock();
		try {
			this.missionMap.put(mission.getMissionRuntimeUri(), mission);
		} finally {
			this.rwLock.writeLock().unlock();
		}
	}

	public MissionModel getOrCreateMissionModel(String missionRuntimeUri) {
		if(!missionRuntimeUri.startsWith(MISSION_PREFIX)) {
			throw new IllegalArgumentException("Ignoring tuple, because MissionRuntimeUri was invalid: " + missionRuntimeUri);
		}
		// ReentrantReadWriteLock usage taken from JavaDoc example
		this.rwLock.readLock().lock();
		if(!this.missionMap.containsKey(missionRuntimeUri)) {
			this.rwLock.readLock().unlock();
			this.rwLock.writeLock().lock();
			try {
				MissionModel missionModel = this.missionMap.get(missionRuntimeUri);
				if(missionModel == null) {
					missionModel = new MissionModel(this.session, missionRuntimeUri, this.userName);
					this.missionMap.put(missionRuntimeUri, missionModel);
				}
				return missionModel;
			} finally {
				this.rwLock.writeLock().unlock();
			}
		} else {
			try {
				return this.missionMap.get(missionRuntimeUri);
			} finally {
				this.rwLock.readLock().unlock();
			}
		}
	}
	
}
