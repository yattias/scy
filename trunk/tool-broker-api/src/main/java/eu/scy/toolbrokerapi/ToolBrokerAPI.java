/**
 * 
 */
package eu.scy.toolbrokerapi;

import roolo.api.IExtensionManager;
import roolo.api.IRepository;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataTypeManager;
import eu.scy.actionlogging.api.IActionLogger;
import eu.scy.notification.api.INotificationService;

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
    public IRepository<IELO<K>, K> getRepository();
    
    /**
     * @return the metaDataTypeManager
     */
    public IMetadataTypeManager<K> getMetaDataTypeManager();
    
    /**
     * @return the extensionManager
     */
    public IExtensionManager getExtensionManager();

    public UserSessionManager getUserSession(String username, String password);
    
    /**
     * @return the action logger
     */
    public IActionLogger getActionLogger();
    
    /**
     * @return the notification service
     */
    public INotificationService getNotificationService();
}
