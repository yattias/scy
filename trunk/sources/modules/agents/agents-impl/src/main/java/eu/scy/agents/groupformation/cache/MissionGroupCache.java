package eu.scy.agents.groupformation.cache;

import eu.scy.agents.Mission;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MissionGroupCache {

    private Map<Mission, Map<String, GroupCache>> missionLasCache;

    public MissionGroupCache() {
        missionLasCache = new ConcurrentHashMap<Mission, Map<String, GroupCache>>();
    }

    public synchronized Collection<Group> getGroups(Mission mission, String las) {
        Map<String, GroupCache> lasCache = missionLasCache.get(mission);
        if ( lasCache == null ) {
            return Collections.emptyList();
        }
        GroupCache groupCache = lasCache.get(las);
        if ( groupCache == null ) {
            return Collections.emptyList();
        }
        return groupCache.getGroups();
    }

    public synchronized void addGroup(Mission mission, String las, Group group) {
        Map<String, GroupCache> lasCache = missionLasCache.get(mission);
        if ( lasCache == null ) {
            lasCache = createCacheMap();
            missionLasCache.put(mission, lasCache);
        }
        GroupCache groupCache = lasCache.get(las);
        if ( groupCache == null ) {
            groupCache = new GroupCache();
            lasCache.put(las, groupCache);
        }
        groupCache.addGroup(group);
    }

    public synchronized void addGroups(Mission mission, String las,
                                       Collection<Group> formedGroup) {
        for ( Group group : formedGroup ) {
            addGroup(mission, las, group);
        }
    }

    public synchronized Group getGroup(Mission mission, String las,
                                       String user) {
        Map<String, GroupCache> lasCache = missionLasCache.get(mission);
        if ( lasCache == null ) {
            return Group.emptyGroup();
        }
        GroupCache groupCache = lasCache.get(las);
        if ( groupCache == null ) {
            return Group.emptyGroup();
        }
        return groupCache.getGroup(user);
    }

    public synchronized void clear(Mission mission, String las) {
        Map<String, GroupCache> lasCache = missionLasCache.get(mission);
        if ( lasCache == null ) {
            return;
        }
        GroupCache groupCache = lasCache.get(las);
        if ( groupCache == null ) {
            return;
        }
        groupCache.clear();
    }

    public synchronized void removeFromCache(Mission mission, String las,
                                             String userToRemove, int minGroupSize) {
        Map<String, GroupCache> lasCache = missionLasCache.get(mission);
        if ( lasCache == null ) {
            return;
        }
        GroupCache groupCache = lasCache.get(las);
        if ( groupCache == null ) {
            return;
        }
        groupCache.removeFromCache(userToRemove, minGroupSize);
    }

    public synchronized boolean contains(Mission mission, String las,
                                         String user) {
        Map<String, GroupCache> lasCache = missionLasCache.get(mission);
        if ( lasCache == null ) {
            return false;
        }
        GroupCache groupCache = lasCache.get(las);
        if ( groupCache == null ) {
            return false;
        }
        return groupCache.contains(user);
    }

    public synchronized GroupCache get(Mission mission, String las) {
        Map<String, GroupCache> lasCache = missionLasCache.get(mission);
        if ( lasCache == null ) {
            lasCache = createCacheMap();
            missionLasCache.put(mission, lasCache);
        }
        GroupCache groupCache = lasCache.get(las);
        if ( groupCache == null ) {
            groupCache = new GroupCache();
            lasCache.put(las, groupCache);
        }
        return groupCache;
    }

    private Map<String, GroupCache> createCacheMap() {
        return new ConcurrentHashMap<String, GroupCache>();
    }

    public void removeUser(String user) {
        for ( Mission mission : missionLasCache.keySet() ) {
            Map<String, GroupCache> lasCache = missionLasCache.get(mission);
            for ( String las : lasCache.keySet() ) {
                GroupCache groupCache = lasCache.get(las);
                groupCache.removeFromCache(user, 2);
            }
        }
    }
}
