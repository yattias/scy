package eu.scy.client.tools.drawing;

import roolo.elo.api.IMetadataKey;



public interface ELOLoadedChangedListener<K extends IMetadataKey>
{
	void eloLoadedChanged(ELOLoadedChangedEvent<K> eloLoadedChangedEvent);
}
