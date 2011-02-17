package eu.scy.common.mission;

import java.net.URI;
import java.util.Locale;
import java.util.Set;

public interface AgentModelEloContent {

	public URI getModelEloUri(String key, String language);

	public URI getModelEloUri(String key, Locale language);

	public Set<String> getKeys();

	public Set<String> getLanguages(String key);

	public void setModelEloUri(String key, String language, URI modelEloUri);

}
