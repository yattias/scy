package eu.scy.agents.agenda.guidance;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import eu.scy.agents.agenda.guidance.model.MissionModel;
import eu.scy.agents.agenda.guidance.model.UserModel;

public class UserModelDictionary {

	private final Map<String, UserModel> userModelMap = new HashMap<String, UserModel>();
	private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();

	public UserModelDictionary() {
	}
	
	public void addUserModel(UserModel userModel) {
		this.rwLock.writeLock().lock();
		try {
			this.userModelMap.put(userModel.getName(), userModel);
		} finally {
			this.rwLock.writeLock().unlock();
		}
	}

	public void addMissionModel(String user, MissionModel missionModel) {
		this.rwLock.writeLock().lock();
		try {
			UserModel userModel = this.userModelMap.get(user);
			userModel.addMission(missionModel);
		} finally {
			this.rwLock.writeLock().unlock();
		}
	}
	
	public UserModel getOrCreateUserModel(String user) {
		// ReentrantReadWriteLock usage taken from JavaDoc example
		this.rwLock.readLock().lock();
		if(!this.userModelMap.containsKey(user)) {
			this.rwLock.readLock().unlock();
			this.rwLock.writeLock().lock();
			try {
				UserModel userModel = this.userModelMap.get(user);
				if(userModel == null) {
					userModel = new UserModel(user);
					this.userModelMap.put(user, userModel);
				}
				return userModel;
			} finally {
				this.rwLock.writeLock().lock();
			}
		} else {
			try {
				return this.userModelMap.get(user);
			} finally {
				this.rwLock.readLock().unlock();
			}
		}
	}
	
	public UserModel getUserModel(String user) {
		this.rwLock.readLock().lock();
		try {
			return this.userModelMap.get(user);
		} finally {
			this.rwLock.readLock().unlock();
		}
	}

	public MissionModel getMissionModel(String user, String mission) {
		this.rwLock.readLock().lock();
		try {
			UserModel userModel = this.userModelMap.get(user);
			return userModel.getMission(mission);
		} catch (Exception e) {
			return null;
		} finally {
			this.rwLock.readLock().unlock();
		}
	}
	
}
