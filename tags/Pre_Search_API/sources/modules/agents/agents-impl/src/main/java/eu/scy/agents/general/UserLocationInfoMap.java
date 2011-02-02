package eu.scy.agents.general;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Saves information for several users. The information can be retrieved by
 * username. This class is thread safe as each operation just has read access
 * and is backed by a thread safe collection.
 * 
 */
public class UserLocationInfoMap {

	private ConcurrentHashMap<String, UserLocationInfo> userLocationInfoMap;

	public UserLocationInfoMap() {
		userLocationInfoMap = new ConcurrentHashMap<String, UserLocationInfo>();
	}

	public UserLocationInfo getUserInfo(String user) {
		return userLocationInfoMap.get(user);
	}

	public void add(String user, UserLocationInfo userInfo) {
		userLocationInfoMap.put(user, userInfo);
	}

	public void remove(String user) {
		userLocationInfoMap.remove(user);
	}

	/**
	 * Gets all users in a LAS. This method is thread safe as it returns the
	 * state at the moment of the call.
	 * 
	 * @param las
	 *            The las to get all users for.
	 * @return A not thread safe list of users that were in the requested las at
	 *         the moment of the call.
	 */
	public List<String> getUserInLas(String las) {
		List<String> userInLas = new ArrayList<String>();
		for (Entry<String, UserLocationInfo> entry : userLocationInfoMap
				.entrySet()) {
			UserLocationInfo userLocationInfo = entry.getValue();
			if (userLocationInfo.getLas().equals(las)) {
				userInLas.add(entry.getKey());
			}
		}
		return userInLas;
	}

	public Set<String> getUsers() {
		return userLocationInfoMap.keySet();
	}

}
