package eu.scy.agents.roolo.elo.elobrowsernotification;

import eu.scy.agents.api.IAgentFactory;
import eu.scy.agents.api.IParameter;
import eu.scy.agents.api.IThreadedAgent;

public class NotifyEloBrowserAgentFactory implements IAgentFactory {

	@Override
	public IThreadedAgent create(IParameter params) {
		return new NotifyEloBrowserAgent();
	}

	@Override
	public String getAgentName() {
		return NotifyEloBrowserAgent.NOTIFY_ELO_BROWSER_AGENT_NAME;
	}

}
