package eu.scy.agents.roolo.impl;

import eu.scy.agents.roolo.api.IRooloAgent;
import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.TupleSpaceException;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;

public abstract class AbstractAgent<T extends IELO<K>, K extends IMetadataKey> implements IRooloAgent<T, K> {
    
    @Override
    public TupleSpace getTupleSpace() {
        try {
            return new TupleSpace();
        } catch (TupleSpaceException e) {
            e.printStackTrace();
        }
        return null;
    }
    
}
