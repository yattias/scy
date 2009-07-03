package eu.scy.datasync.client;

import eu.scy.communications.datasync.event.IDataSyncListener;
import eu.scy.communications.message.ISyncMessage;

/**
 * Interface for the data sync service
 * 
 * @author thomasd
 *
 */
public interface IDataSyncService {

   /**
    * sends a sync message to the server
    * 
    * @param syncMessage
    */
   public void sendMessage(ISyncMessage syncMessage);

   /**
    * creates a new session
    * 
    * @param toolName
    * @param userName
    */
   public void createSession(String toolName, String userName);

   /**
    * adds a data sync listener
    * 
    * @param iDataSyncListener
    */
   public void addDataSyncListener(IDataSyncListener iDataSyncListener);

}
