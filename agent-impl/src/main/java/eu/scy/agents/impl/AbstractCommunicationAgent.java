package eu.scy.agents.impl;

import roolo.elo.api.IMetadataKey;
import eu.scy.agents.api.ICommunicationAgent;

public abstract class AbstractCommunicationAgent<K extends IMetadataKey>
		extends AbstractAgent implements ICommunicationAgent<K> {

	protected boolean done;

	public AbstractCommunicationAgent() {
		done = false;
		Thread t = new Thread(this);
		t.start();
	}

	@Override
	public void run() {
		while (!done) {
			doRun();
		}
	}

	protected abstract void doRun();

	// @Override
	// public ToolBrokerAPI<K> getToolBrokerAPI() {
	// return new ToolBrokerImpl<K>();
	// }

}
