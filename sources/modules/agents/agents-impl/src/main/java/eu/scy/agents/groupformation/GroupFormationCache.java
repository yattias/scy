package eu.scy.agents.groupformation;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class GroupFormationCache {

	private Map<String, Set<String>> groupCache;

	public GroupFormationCache() {
		groupCache = new LinkedHashMap<String, Set<String>>();
	}

	public Set<String> getGroup(String user) {
		return groupCache.get(user);
	}

	public void addGroups(Collection<Set<String>> formedGroup) {
		for (Set<String> group : formedGroup) {
			addGroup(group);
		}
	}

	public void addGroup(Set<String> group) {
		for (String user : group) {
			groupCache.put(user, group);
		}
	}

	public void clear() {
		groupCache.clear();
	}

	public Collection<Set<String>> getGroups() {
		return new LinkedHashSet<Set<String>>(groupCache.values());
	}

	public void removeFromCache(String userToRemove, int minGroupSize) {
		Set<String> group = groupCache.get(userToRemove);
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

}
