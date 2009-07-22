package eu.scy.agents.roolo.elo.conceptawareness;

import java.util.Map;

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

	protected ConceptMapSavedAgent(Map<String, Object> map) {
		super("ConceptMapSavedAgent",(String) map.get("id"));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void processElo(T elo) {
		if (elo == null) {
			return;
		}
		IMetadata<K> metadata = elo.getMetadata();
		if (metadata != null) {
			IMetadataValueContainer type = metadata
					.getMetadataValueContainer((K) metadataTypeManager
							.getMetadataKey("type"));
			if (!"scy/scymapping".equals(type.getValue())) {
				return;
			}
		}

		System.err.println("concept map elo saved");

		try {
			TupleSpace ts = getTupleSpace();
			ts.write(new Tuple("scymapper", System.currentTimeMillis(), elo
					.getUri().toString()));
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		}

	}

}
