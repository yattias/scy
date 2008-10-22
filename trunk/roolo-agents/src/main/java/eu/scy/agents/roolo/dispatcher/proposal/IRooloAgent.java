package eu.scy.agents.roolo.dispatcher.proposal;

import info.collide.sqlspaces.client.TupleSpace;
import roolo.api.IELO;
import roolo.api.IMetadata;
import roolo.api.IMetadataKey;

public abstract interface IRooloAgent<T extends IELO<K>, K extends IMetadataKey> {
    
    public void processElo(T elo);
    
    public void processMetadata(IMetadata<K> metadata);
    
    public TupleSpace getTupleSpace();
    
}
