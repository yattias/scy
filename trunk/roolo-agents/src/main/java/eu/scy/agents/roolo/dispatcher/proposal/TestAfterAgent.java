package eu.scy.agents.roolo.dispatcher.proposal;

import roolo.api.IELO;
import roolo.api.IMetadata;
import roolo.api.IMetadataKey;

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
