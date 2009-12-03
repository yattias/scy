package eu.scy.toolbrokerapi;

import org.jivesoftware.smack.XMPPConnection;

import roolo.api.IExtensionManager;
import roolo.api.IRepository;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataTypeManager;
import eu.scy.actionlogging.api.IActionLogger;
import eu.scy.awareness.IAwarenessService;
import eu.scy.client.common.datasync.IDataSyncService;
import eu.scy.notification.api.INotifiable;
import eu.scy.server.pedagogicalplan.PedagogicalPlanService;
import eu.scy.sessionmanager.SessionManager;

/**
 * This is the interface of the Tool-Broker-API. The Tool-Broker-API can return
 * references to all SCY broker services like the repository, the user
 * management etc. These services can then be used by the tools.
 * 
 * @author Giemza
 */
public interface ToolBrokerAPI<K extends IMetadataKey> {
    
    /**
     * This method returns the repository instance to be used by clients of the
     * ToolsBrokerAPI.
     * 
     * @return repository the repository instace
     */
    public IRepository getRepository();
    
    /**
     * @return the metaDataTypeManager
     */
    public IMetadataTypeManager getMetaDataTypeManager();
    
    /**
     * @return the extensionManager
     */
    public IExtensionManager getExtensionManager();

    /**
     * The session manager for handling user authentication and authorisation
     * @param username
     * @param password
     * @return
     */
    public SessionManager getUserSession(String username, String password);
    
    /**
     * @return the action logger
     */
    public IActionLogger getActionLogger();
    
    /**
     * Registers the {@link INotifiable} for notifications 
     */
    public void registerForNotifications(INotifiable notifiable);
    
    /**
     * @return the awareness service
     */
    public IAwarenessService getAwarenessService();
    
    /**
     * @return the data sync service
     */
    public IDataSyncService getDataSyncService();
    
    /**
     * returns an xmpp connection
     * 
     * @return
     */
    public XMPPConnection getConnection(String username, String password);

    /**
     *
     * Returns the pedagogical plan service
     *
     * @return  the pedagogical plan service
     */
    public PedagogicalPlanService getPedagogicalPlanService();
}
