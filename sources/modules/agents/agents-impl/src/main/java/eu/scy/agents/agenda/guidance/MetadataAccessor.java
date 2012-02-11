package eu.scy.agents.agenda.guidance;

import eu.scy.agents.agenda.exception.NoMetadataTypeManagerAvailableException;
import roolo.elo.api.IMetadata;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.IMetadataValueContainer;

public class MetadataAccessor {

	private IMetadataTypeManager manager;
	
	public MetadataAccessor() {
	}
	
	public void setMetadataTypeManager(IMetadataTypeManager manager) {
		this.manager = manager;
	}
	
	/**
	 * Read metadata value. If metadata key is not present, null will be returned
	 *
	 * @param metadata the metadata
	 * @param metadataKey the metadata key
	 * @return the MetadataValueContainer for the specified key or null if no such key exists
	 * @throws NoMetadataTypeManagerAvailableException the no metadata type manager available exception
	 */
	public IMetadataValueContainer readMetadataValue(IMetadata metadata, String metadataKey) throws NoMetadataTypeManagerAvailableException {
		if(this.manager == null) {
			throw new NoMetadataTypeManagerAvailableException("Could not get Metadata because no MetadataTypeManager available");
		}
		IMetadataKey iMetadataKey = this.manager.getMetadataKey(metadataKey);
		if(this.manager.isMetadataKeyRegistered(iMetadataKey)) {
			return metadata.getMetadataValueContainer(iMetadataKey);
		} else {
			return null;
		}
	}
	
}
