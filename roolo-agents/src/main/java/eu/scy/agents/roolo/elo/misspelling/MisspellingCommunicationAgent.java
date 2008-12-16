package eu.scy.agents.roolo.elo.misspelling;

import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import roolo.elo.api.IMetadataKey;
import eu.scy.agents.impl.AbstractCommunicationAgent;

public class MisspellingCommunicationAgent<K extends IMetadataKey> extends AbstractCommunicationAgent<K> {
    
    @Override
    protected void doRun() {
        Tuple t;
        try {
            t = getTupleSpace().waitToTake(new Tuple("misspellings", Long.class, Integer.class));
        } catch (TupleSpaceException e) {
            throw new RuntimeException(e);
        }
        
        Integer numberOfErrors = (Integer) t.getField(2).getValue();
        // INotification notification = new Notification();
        // notification.addProperty("errors", "" + numberOfErrors);
        // notification.addProperty("target", "misspellings");
        // getToolBrokerAPI().getNotificationService().notifyCallbacks(notification);
        System.out.println("***************** your document has " + numberOfErrors + " ********** spelling errors");
    }
}
