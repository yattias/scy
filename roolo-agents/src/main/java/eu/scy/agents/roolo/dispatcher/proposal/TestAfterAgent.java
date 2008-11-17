package eu.scy.agents.roolo.dispatcher.proposal;

import roolo.elo.api.IELO;
import roolo.elo.api.IMetadata;
import roolo.elo.api.IMetadataKey;

public class TestAfterAgent<T extends IELO<K>, K extends IMetadataKey> extends AbstractAgent<T, K> {
    
    @Override
    public void processElo(T elo) {
        System.out.println(elo.getUri());
    }
    
    @Override
    public void processMetadata(IMetadata<K> metadata) {
        // do nothing
    }
    
}
