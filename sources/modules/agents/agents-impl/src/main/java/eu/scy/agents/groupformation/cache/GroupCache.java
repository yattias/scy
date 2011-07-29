package eu.scy.agents.groupformation.cache;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

public class GroupCache {

	private Map<String, Group> groupCache;

	public GroupCache() {
		groupCache = new LinkedHashMap<String, Group>();
	}

	public Group getGroup(String user) {
		return groupCache.get(user);
	}

	public void addGroups(Collection<Group> formedGroup) {
		for (Group group : formedGroup) {
			addGroup(group);
		}
	}

	public void addGroup(Group group) {
		for (String user : group) {
			groupCache.put(user, group);
		}
	}

	public void clear() {
		groupCache.clear();
	}

	public Collection<Group> getGroups() {
		return new LinkedHashSet<Group>(groupCache.values());
	}

	public void removeFromCache(String userToRemove, int minGroupSize) {
		Group group = groupCache.get(userToRemove);
		if (group == null) {
			return;
		}
		if (group.isEmpty()) {
			return;
		}
		// Attention with references: Every group instance is the same. That's
		// why this works.
		group.remove(userToRemove);
		if (group.size() < minGroupSize) {
			for (String user : group) {
				groupCache.remove(user);
			}
		}
		groupCache.remove(userToRemove);
	}

	public boolean contains(String user) {
		return groupCache.containsKey(user);
	}

	@Override
	public String toString() {
		return groupCache.toString();
	}
}
