package eu.scy.agents.helper;

import java.util.List;

import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.IMetadataValueContainer;

public class ELOFiller {

	private IELO elo;
	private IMetadataTypeManager metadataTypeManager;

	public ELOFiller(IELO elo, IMetadataTypeManager typeManager) {
		this.elo = elo;
		metadataTypeManager = typeManager;
	}

	public void fillListValue(String name, List<?> values) {
		IMetadataValueContainer container = elo.getMetadata()
				.getMetadataValueContainer(
						metadataTypeManager.getMetadataKey(name));
		for (Object value : values) {
			container.addValue(value);
		}
	}

	public void fillValue(String name, Object value) {
		IMetadataValueContainer container = elo.getMetadata()
				.getMetadataValueContainer(
						metadataTypeManager.getMetadataKey(name));
		container.setValue(value);
	}
}
