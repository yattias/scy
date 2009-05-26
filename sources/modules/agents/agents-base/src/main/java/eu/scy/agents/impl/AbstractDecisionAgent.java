package eu.scy.agents.impl;

import roolo.elo.api.IMetadataKey;
import eu.scy.agents.api.IDecisionAgent;

public abstract class AbstractDecisionAgent<K extends IMetadataKey> extends
		AbstractThreadedAgent implements IDecisionAgent<K> {

	protected AbstractDecisionAgent(String name) {
		super(name);
	}

	// @Override
	// public ToolBrokerAPI<K> getToolBrokerAPI() {
	// return new ToolBrokerImpl<K>();
	// }

}
