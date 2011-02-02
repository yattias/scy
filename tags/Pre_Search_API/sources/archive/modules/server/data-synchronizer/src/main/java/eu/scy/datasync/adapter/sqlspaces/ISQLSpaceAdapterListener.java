package eu.scy.datasync.adapter.sqlspaces;

/**
 * Interface for handling scycommunication events
 * 
 * @author anthonyp
 */
public interface ISQLSpaceAdapterListener  {
   
    /**
     * action handler for this event
     * 
     * @param scyCommunicationEvent
     */
    public void handleSQLSpacesEvent(SQLSpaceAdapterEvent sqlSpaceEvent);

}
