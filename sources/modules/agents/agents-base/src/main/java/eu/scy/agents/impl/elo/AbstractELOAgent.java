package eu.scy.agents.impl.elo;

import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataTypeManager;
import eu.scy.agents.api.elo.IELOFilterAgent;
import eu.scy.agents.impl.AbstractAgent;

public abstract class AbstractELOAgent<T extends IELO<K>, K extends IMetadataKey>
		extends AbstractAgent implements IELOFilterAgent<T, K> {

	protected IMetadataTypeManager<IMetadataKey> metadataTypeManager;

	protected AbstractELOAgent(String name) {
		super(name);
	}

	protected IMetadataTypeManager<IMetadataKey> getMetadataTypeManager() {
		return metadataTypeManager;
	}

	public void setMetadataTypeManager(
			IMetadataTypeManager<IMetadataKey> typeManager) {
		metadataTypeManager = typeManager;
	}

}
