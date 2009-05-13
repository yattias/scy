package eu.scy.agents.api;

import roolo.elo.api.IMetadataKey;

public interface ICommunicationAgent<K extends IMetadataKey> extends IAgent,
		Runnable {

	// public ToolBrokerAPI<K> getToolBrokerAPI();

	public void run();

}
