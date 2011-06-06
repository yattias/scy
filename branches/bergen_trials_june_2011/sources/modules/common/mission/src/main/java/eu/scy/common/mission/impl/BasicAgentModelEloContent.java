package eu.scy.common.mission.impl;

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import eu.scy.common.mission.AgentModelEloContent;

public class BasicAgentModelEloContent implements AgentModelEloContent {

	private Map<String, Map<String, URI>> agentModelUris;

	public BasicAgentModelEloContent() {
		agentModelUris = new ConcurrentHashMap<String, Map<String, URI>>();
	}

	@Override
	public URI getModelEloUri(String key, String language) {
		synchronized (agentModelUris) {
			Map<String, URI> map = agentModelUris.get(key);
			return map.get(language);
		}
	}

	public Set<String> getKeys() {
		synchronized (agentModelUris) {
			return Collections.unmodifiableSet(agentModelUris.keySet());
		}
	}

	public Set<String> getLanguages(String key) {
		synchronized (agentModelUris) {
			Map<String, URI> map = agentModelUris.get(key);
			return Collections.unmodifiableSet(map.keySet());
		}
	}

	@Override
	public void setModelEloUri(String key, String language, URI modelEloUri) {
		synchronized (agentModelUris) {
			Map<String, URI> languageMap = agentModelUris.get(key);
			if (languageMap == null) {
				languageMap = new HashMap<String, URI>();
				agentModelUris.put(key, languageMap);
			}
			languageMap.put(language, modelEloUri);
		}
	}

	@Override
	public URI getModelEloUri(String key, Locale language) {
		return getModelEloUri(key, language.getLanguage());
	}

}
