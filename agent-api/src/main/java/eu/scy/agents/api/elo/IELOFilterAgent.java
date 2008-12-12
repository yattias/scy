package eu.scy.agents.api.elo;

import eu.scy.agents.api.IAgent;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;

public abstract interface IELOFilterAgent<T extends IELO<K>, K extends IMetadataKey> extends IAgent {
    
    public void processElo(T elo);
    
}
