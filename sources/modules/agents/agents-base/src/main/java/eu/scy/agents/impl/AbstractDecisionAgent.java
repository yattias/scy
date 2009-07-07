package eu.scy.agents.impl;

import eu.scy.agents.api.IDecisionAgent;

public abstract class AbstractDecisionAgent extends AbstractThreadedAgent
		implements IDecisionAgent {

	protected AbstractDecisionAgent(String name) {
		super(name);
	}

}
