package eu.scy.agents.roolo.elo.conceptawareness;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.util.Map;

import roolo.elo.api.IELO;
import roolo.elo.api.IMetadata;
import roolo.elo.api.IMetadataValueContainer;
import eu.scy.agents.impl.elo.AbstractELOAgent;

/**
 * Notifies the {@link SearchForSimilarConceptsAgent} that a concept map has
 * been saved.
 * 
 * @author Florian Schulz
 * 
 */
public class ConceptMapSavedAgent extends AbstractELOAgent {

	/**
	 * Create a new ConceptMapSavedAgent filtering agent. The argument
	 * <code>map</code> is used to initialize special parameters.
	 * 
	 * @param map
	 *            Parameters needed to initialize the agent.
	 */
	public ConceptMapSavedAgent(Map<String, Object> map) {
		super("ConceptMapSavedAgent", (String) map.get("id"));
	}

	@Override
	public void processElo(IELO elo) {
		if (elo == null) {
			return;
		}
		IMetadata metadata = elo.getMetadata();
		if (metadata != null) {
			IMetadataValueContainer type = metadata
					.getMetadataValueContainer(metadataTypeManager
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
