package eu.scy.agents.impl.elo;

import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataTypeManager;
import eu.scy.agents.api.elo.IELOFilterAgent;
import eu.scy.agents.impl.AbstractAgent;
import eu.scy.toolbroker.ToolBrokerImpl;
import eu.scy.toolbrokerapi.ToolBrokerAPI;

public abstract class AbstractELOAgent<T extends IELO<K>, K extends IMetadataKey>
		extends AbstractAgent implements IELOFilterAgent<T, K> {

	private ToolBrokerAPI<IMetadataKey> toolBroker;
	private IMetadataTypeManager<IMetadataKey> typeManger;

	protected IMetadataTypeManager<IMetadataKey> getMetadataTypeManager() {
		if (typeManger == null) {
			if (toolBroker == null) {
				toolBroker = new ToolBrokerImpl<IMetadataKey>();
			}
			typeManger = toolBroker.getMetaDataTypeManager();
		}
		return typeManger;
	}

	protected void setToolBroker(ToolBrokerAPI<IMetadataKey> toolBroker) {
		this.toolBroker = toolBroker;
	}

}
