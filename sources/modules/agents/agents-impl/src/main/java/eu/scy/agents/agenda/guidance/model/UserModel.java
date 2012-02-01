package eu.scy.agents.agenda.guidance.model;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class UserModel {

	private final String name;
	private final Map<String, MissionModel> missionMap;
	private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
	
	public UserModel(String name) {
		super();
		this.name = name;
		this.missionMap = new HashMap<String, MissionModel>();
	}

	public String getName() {
		return this.name;
	}

	public MissionModel getMission(String mission) {
		this.rwLock.readLock().lock();
		try {
			return this.missionMap.get(mission);
		} finally {
			this.rwLock.readLock().unlock();
		}
	}

	public void addMission(MissionModel mission) {
		this.rwLock.writeLock().lock();
		try {
			this.missionMap.put(mission.getName(), mission);
		} finally {
			this.rwLock.writeLock().unlock();
		}
	}

	public MissionModel getOrCreateMissionModel(String mission) {
		// ReentrantReadWriteLock usage taken from JavaDoc example
		this.rwLock.readLock().lock();
		if(!this.missionMap.containsKey(mission)) {
			this.rwLock.readLock().unlock();
			this.rwLock.writeLock().lock();
			try {
				MissionModel missionModel = this.missionMap.get(mission);
				if(missionModel == null) {
					missionModel = new MissionModel(mission, this.name);
					this.missionMap.put(mission, missionModel);
				}
				return missionModel;
			} finally {
				this.rwLock.writeLock().unlock();
			}
		} else {
			try {
				return this.missionMap.get(mission);
			} finally {
				this.rwLock.readLock().unlock();
			}
		}
	}
	
}
