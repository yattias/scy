package eu.scy.agents.roolo.elo.misspelling;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import roolo.elo.api.IContent;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;
import eu.scy.agents.impl.elo.AbstractELOAgent;

public class MisspellingProcessELO<T extends IELO<K>, K extends IMetadataKey>
		extends AbstractELOAgent<T, K> {

	@Override
	public void processElo(T elo) {
		if (elo == null) {
			return;
		}
		try {
			IContent content = elo.getContent();
			if (content != null && content.getXmlString() != null) {
				TupleSpace ts = getTupleSpace();
				ts.write(new Tuple("misspellings", elo.getUri().toString(),
						System.currentTimeMillis(), content.getXmlString()));
			}
		} catch (TupleSpaceException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
