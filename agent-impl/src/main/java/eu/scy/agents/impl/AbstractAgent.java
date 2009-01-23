package eu.scy.agents.impl;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.TupleSpaceException;
import eu.scy.agents.api.IAgent;
import eu.scy.agents.api.IPersistentStorage;

/**
 * Implementation of the IAgent interface.
 * 
 * @author fschulz
 */
public class AbstractAgent implements IAgent {
    
    private TupleSpace tupleSpace;
    
    public AbstractAgent() {
        try {
            tupleSpace = new TupleSpace();
        } catch (TupleSpaceException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public IPersistentStorage getPersistentStorage() {
        return new PersistentStorage();
    }
    
    @Override
    public TupleSpace getTupleSpace() {
        return tupleSpace;
    }
    
}
