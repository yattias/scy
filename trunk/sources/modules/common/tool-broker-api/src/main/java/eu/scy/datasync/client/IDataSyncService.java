package eu.scy.datasync.client;

import eu.scy.communications.message.impl.SyncMessage;

/**
 * Interface for the data sync service
 * 
 * @author thomasd
 *
 */
public interface IDataSyncService {

    void sendMessage(SyncMessage syncMessage);

}
