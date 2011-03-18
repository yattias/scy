package eu.scy.agents.groupformation.cache;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class MissionGroupCache {

	private Map<String, Map<String, GroupCache>> missionLasCache;

	public MissionGroupCache() {
		missionLasCache = new ConcurrentHashMap<String, Map<String, GroupCache>>();
	}

	public synchronized Collection<Set<String>> getGroups(String mission,
			String las) {
		Map<String, GroupCache> lasCache = missionLasCache.get(mission);
		if (lasCache == null) {
			return Collections.emptyList();
		}
		GroupCache groupCache = lasCache.get(las);
		return groupCache.getGroups();
	}

	public synchronized void addGroup(String mission, String las,
			Set<String> group) {
		Map<String, GroupCache> lasCache = missionLasCache.get(mission);
		if (lasCache == null) {
			lasCache = createCacheMap();
			missionLasCache.put(mission, lasCache);
		}
		GroupCache groupCache = lasCache.get(las);
		if (groupCache == null) {
			groupCache = new GroupCache();
			lasCache.put(las, groupCache);
		}
		groupCache.addGroup(group);
	}

	public synchronized void addGroups(String mission, String las,
			Collection<Set<String>> formedGroup) {
		for (Set<String> group : formedGroup) {
			addGroup(mission, las, group);
		}
	}

	public synchronized Set<String> getGroup(String mission, String las,
			String user) {
		Map<String, GroupCache> lasCache = missionLasCache.get(mission);
		if (lasCache == null) {
			return Collections.emptySet();
		}
		GroupCache groupCache = lasCache.get(las);
		if (groupCache == null) {
			return Collections.emptySet();
		}
		return groupCache.getGroup(user);
	}

	public synchronized void clear(String mission, String las) {
		Map<String, GroupCache> lasCache = missionLasCache.get(mission);
		if (lasCache == null) {
			return;
		}
		GroupCache groupCache = lasCache.get(las);
		if (groupCache == null) {
			return;
		}
		groupCache.clear();
	}

	public synchronized void removeFromCache(String mission, String las,
			String userToRemove, int minGroupSize) {
		Map<String, GroupCache> lasCache = missionLasCache.get(mission);
		if (lasCache == null) {
			return;
		}
		GroupCache groupCache = lasCache.get(las);
		if (groupCache == null) {
			return;
		}
		groupCache.removeFromCache(userToRemove, minGroupSize);
	}

	public synchronized boolean contains(String mission, String las, String user) {
		Map<String, GroupCache> lasCache = missionLasCache.get(mission);
		if (lasCache == null) {
			return false;
		}
		GroupCache groupCache = lasCache.get(las);
		if (groupCache == null) {
			return false;
		}
		return groupCache.contains(user);
	}

	public synchronized GroupCache get(String mission, String las) {
		Map<String, GroupCache> lasCache = missionLasCache.get(mission);
		if (lasCache == null) {
			lasCache = createCacheMap();
			missionLasCache.put(mission, lasCache);
		}
		GroupCache groupCache = lasCache.get(las);
		if (groupCache == null) {
			groupCache = new GroupCache();
			lasCache.put(las, groupCache);
		}
		return groupCache;
	}

	private Map<String, GroupCache> createCacheMap() {
		return new ConcurrentHashMap<String, GroupCache>();
	}
}
