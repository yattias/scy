package eu.scy.agents.impl;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.TupleSpaceException;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataTypeManager;
import eu.scy.agents.api.IAgent;
import eu.scy.agents.api.IPersistentStorage;
import eu.scy.toolbroker.ToolBrokerImpl;

/**
 * Implementation of the IAgent interface.
 * 
 * @author fschulz
 */
public class AbstractAgent implements IAgent {

	protected static IMetadataTypeManager<IMetadataKey> typeManager = new ToolBrokerImpl<IMetadataKey>()
			.getMetaDataTypeManager();

	@Override
	public IPersistentStorage getPersistentStorage() {
		return new PersistentStorage();
	}

	@Override
	public TupleSpace getTupleSpace() {
		try {
			return new TupleSpace();
		} catch (TupleSpaceException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public IMetadataTypeManager<IMetadataKey> getMetadataTypeManager() {
		return typeManager;
	}
}
