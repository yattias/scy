package eu.scy.agents.roolo.dispatcher.proposal;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.TupleSpaceException;
import roolo.api.IELO;
import roolo.api.IMetadataKey;

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
