package eu.scy.agents.topics;

import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.net.URI;
import java.rmi.dgc.VMID;

import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataValueContainer;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import eu.scy.agents.impl.elo.AbstractELOAgent;

public class TextForTopicSaved extends AbstractELOAgent {

	private static final String NAME = "eu.scy.agents.topics.TextForTopicSaved";

	public TextForTopicSaved() {
		super(NAME, new VMID().toString());
	}

	@Override
	public void processElo(IELO elo) {
		if (elo != null) {
			if (isValidType(elo)) {
				URI eloUri = elo.getUri();
				try {
					getTupleSpace().write(
							new Tuple("topicDetector", eloUri.toString()));
				} catch (TupleSpaceException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private boolean isValidType(IELO elo) {
		IMetadataValueContainer type = elo
				.getMetadata()
				.getMetadataValueContainer(
						metadataTypeManager
								.getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT
										.getId()));
		if ("scy/text".equals(type.getValue())) {
			return true;
		}
		return false;
	}

}
