package eu.scy.agents.agenda.guidance;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import eu.scy.agents.agenda.guidance.model.MissionModel;
import eu.scy.agents.agenda.guidance.model.UserModel;
import eu.scy.agents.session.Session;

public class UserModelDictionary {

	private final Map<String, UserModel> userModelMap = new HashMap<String, UserModel>();
	private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
	private final Session session;

	public UserModelDictionary(Session session) {
		this.session = session;
	}
	
	public void addUserModel(UserModel userModel) {
		this.rwLock.writeLock().lock();
		try {
			this.userModelMap.put(userModel.getUserName(), userModel);
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
		if(user == null) {
			throw new IllegalArgumentException("user was null");
		}
		// ReentrantReadWriteLock usage taken from JavaDoc example
		this.rwLock.readLock().lock();
		if(!this.userModelMap.containsKey(user)) {
			this.rwLock.readLock().unlock();
			this.rwLock.writeLock().lock();
			try {
				UserModel userModel = this.userModelMap.get(user);
				if(userModel == null) {
					userModel = new UserModel(this.session, user);
					this.userModelMap.put(user, userModel);
				}
				return userModel;
			} finally {
				this.rwLock.writeLock().unlock();
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

	public MissionModel getMissionModel(String user, String missionRuntimeUri) {
		this.rwLock.readLock().lock();
		try {
			UserModel userModel = this.userModelMap.get(user);
			if(userModel != null) {
				return userModel.getMission(missionRuntimeUri);
			} else {
				return null;
			}
		} finally {
			this.rwLock.readLock().unlock();
		}
	}
	
}
