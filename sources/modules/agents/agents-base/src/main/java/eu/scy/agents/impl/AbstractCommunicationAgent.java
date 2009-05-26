package eu.scy.agents.impl;

import roolo.elo.api.IMetadataKey;
import eu.scy.agents.api.ICommunicationAgent;

public abstract class AbstractCommunicationAgent<K extends IMetadataKey>
		extends AbstractThreadedAgent implements ICommunicationAgent<K> {

	public AbstractCommunicationAgent(String threadName) {
		super(threadName);
	}

}
