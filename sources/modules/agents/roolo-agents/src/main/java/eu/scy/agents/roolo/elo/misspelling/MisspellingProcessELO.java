package eu.scy.agents.roolo.elo.misspelling;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import roolo.elo.api.IContent;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadata;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataValueContainer;
import roolo.elo.metadata.keys.Contribute;
import eu.scy.agents.impl.elo.AbstractELOAgent;

public class MisspellingProcessELO<T extends IELO<K>, K extends IMetadataKey>
		extends AbstractELOAgent<T, K> {

	@SuppressWarnings("unchecked")
	@Override
	public void processElo(T elo) {
		System.err
				.println("*********************** Misspelling: Processing elo************************");
		if (elo == null) {
			return;
		}

		IMetadata<K> metadata = elo.getMetadata();
		if (metadata != null) {
			IMetadataValueContainer type = metadata
					.getMetadataValueContainer((K) metadataTypeManager
							.getMetadataKey("type"));
			if (!"scy/text".equals(type.getValue())) {
				return;
			}
		}

		IMetadataKey authorKey = metadataTypeManager.getMetadataKey("author");
		IMetadataValueContainer authorContainer = metadata
				.getMetadataValueContainer((K) authorKey);
		Contribute author = (Contribute) authorContainer.getValue();
		String user = author.getVCard();

		try {
			IContent content = elo.getContent();
			if (content != null && content.getXmlString() != null) {
				TupleSpace ts = getTupleSpace();
				ts.write(new Tuple("misspellings", elo.getUri().toString(),
						System.currentTimeMillis(), content.getXmlString(),
						user));
			}
		} catch (TupleSpaceException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
