package eu.scy.agents.impl;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.TupleSpaceException;
import eu.scy.agents.api.IAgent;
import eu.scy.agents.api.IPersistentStorage;

public class AbstractAgent implements IAgent {
    
    @Override
    public IPersistentStorage getPersistentStorage() {
        return new PersistentStorage();
    }
    
    @Override
    public TupleSpace getTupleSpace() {
        try {
            return new TupleSpace();
        } catch (TupleSpaceException e) {
            throw new RuntimeException(e);
        }
    }
    
}
