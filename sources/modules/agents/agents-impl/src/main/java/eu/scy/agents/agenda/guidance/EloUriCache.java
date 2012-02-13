package eu.scy.agents.agenda.guidance;

import java.util.HashMap;
import java.util.Map;

public class EloUriCache {

	private final Map<String, String> firstVersionToCurrent = new HashMap<String, String>();
	private final Map<String, String> currentToFirstVersion = new HashMap<String, String>();
	
	public EloUriCache() {
	}
	
	public void addEntry(String firstVersionEloUri, String currentEloUri) {
		String lastCurrent = this.firstVersionToCurrent.get(firstVersionEloUri);
		this.firstVersionToCurrent.put(firstVersionEloUri, currentEloUri);
		this.currentToFirstVersion.put(currentEloUri, firstVersionEloUri);

		// remove old entry from cache
		if(this.currentToFirstVersion.containsKey(lastCurrent)) {
			this.currentToFirstVersion.remove(lastCurrent);
		}
	}
	
	public String getFirstVersion(String currentEloUri) {
		return this.currentToFirstVersion.get(currentEloUri);
	}

	public String getCurrent(String firstVersionEloUri) {
		return this.firstVersionToCurrent.get(firstVersionEloUri);
	}

}
