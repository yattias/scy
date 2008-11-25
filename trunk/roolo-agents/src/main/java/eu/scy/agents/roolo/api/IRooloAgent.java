package eu.scy.agents.roolo.api;

import info.collide.sqlspaces.client.TupleSpace;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;

public abstract interface IRooloAgent<T extends IELO<K>, K extends IMetadataKey> {
    
    public void processElo(T elo);
    
    public TupleSpace getTupleSpace();
    
}
