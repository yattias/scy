package eu.scy.agents.api;

import info.collide.sqlspaces.client.TupleSpace;

public interface IAgent {
    
    public TupleSpace getTupleSpace();
    
    public IPersistentStorage getPersistentStorage();
    
}
