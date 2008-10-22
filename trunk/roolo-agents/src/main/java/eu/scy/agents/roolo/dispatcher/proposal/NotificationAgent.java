package eu.scy.agents.roolo.dispatcher.proposal;

import roolo.api.IELO;
import roolo.api.IMetadata;
import roolo.api.IMetadataKey;

public class NotificationAgent<T extends IELO<K>, K extends IMetadataKey> extends AbstractAgent<T, K> {
    
    @Override
    public void processElo(T elo) {
        long started = System.currentTimeMillis();
        while (((System.currentTimeMillis() - started) / 1000) < 15) {
            // wait
        }
        System.out.println("Processing " + elo.getUri() + " by notification finished after " + ((System.currentTimeMillis() - started) / 1000) + " sek");
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void processMetadata(IMetadata metadata) {
    // do nothing
    }
    
}
