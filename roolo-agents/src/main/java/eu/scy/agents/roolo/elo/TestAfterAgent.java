package eu.scy.agents.roolo.elo;

import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;
import eu.scy.agents.api.elo.IELOFilterAgent;
import eu.scy.agents.impl.AbstractAgent;

public class TestAfterAgent<T extends IELO<K>, K extends IMetadataKey> extends AbstractAgent implements IELOFilterAgent<T, K> {
    
    @Override
    public void processElo(T elo) {
        System.out.println(elo.getUri());
    }
    
}
