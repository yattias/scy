package eu.scy.agents.roolo.elo.conceptawareness;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadata;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataValueContainer;
import eu.scy.agents.impl.elo.AbstractELOAgent;

public class ConceptMapSavedAgent<T extends IELO<K>, K extends IMetadataKey>
		extends AbstractELOAgent<T, K> {

	@SuppressWarnings("unchecked")
	@Override
	public void processElo(T elo) {
		if(elo == null) {
			return;
		}
		IMetadata<K> metadata = elo.getMetadata();
		if (metadata != null) {
			IMetadataValueContainer type = metadata
					.getMetadataValueContainer((K) metadataTypeManager
							.getMetadataKey("type"));
			if (!"scy/mapping".equals(type.getValue())) {
				return;
			}
		}
		try {
			TupleSpace ts = getTupleSpace();
			ts.write(new Tuple("scymapper", System.currentTimeMillis(), elo
					.getUri().toString()));
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		}

	}

}
