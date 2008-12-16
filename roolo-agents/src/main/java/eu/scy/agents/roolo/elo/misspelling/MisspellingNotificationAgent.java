package eu.scy.agents.roolo.elo.misspelling;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.util.Locale;

import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;
import eu.scy.agents.api.elo.IELOFilterAgent;
import eu.scy.agents.impl.AbstractAgent;

public class MisspellingNotificationAgent<T extends IELO<K>, K extends IMetadataKey> extends AbstractAgent implements IELOFilterAgent<T, K> {
    
    @Override
    public void processElo(T elo) {
        try {
            TupleSpace ts = getTupleSpace();
            ts.write(new Tuple("misspellings", System.currentTimeMillis(), elo.getContent(Locale.GERMAN).getXml()));
        } catch (TupleSpaceException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
