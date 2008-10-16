/**
 * 
 */
package eu.scy.toolbrokerapi;

import roolo.api.IELO;
import roolo.api.IExtensionManager;
import roolo.api.IMetadataKey;
import roolo.api.IMetadataTypeManager;
import roolo.api.IRepository;

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
    
}
