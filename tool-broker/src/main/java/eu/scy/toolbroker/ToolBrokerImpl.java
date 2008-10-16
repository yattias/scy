/**
 * 
 */
package eu.scy.toolbroker;

import roolo.api.IELO;
import roolo.api.IMetadataKey;
import roolo.api.IRepository;
import eu.scy.toolbrokerapi.ToolBrokerAPI;

/**
 * This class implements the ToolBrokerAPI interface and provides all the
 * services. The main idea of this implementation is that all the services are
 * injected by Spring into this class and then can be accessed by all clients.
 * 
 * @author Giemza
 */
public class ToolBrokerImpl<K extends IMetadataKey> implements ToolBrokerAPI<K> {
    
    private IRepository<IELO<K>, K> repository;
    
    /**
     * Sets the repository instance into the ToolBroker. Is mainly used for
     * Spring bean injection.
     * 
     * @param repository
     *            the repository instance to set
     */
    public void setRepository(IRepository<IELO<K>, K> repository) {
        this.repository = repository;
    }
    
    /*
     * (non-Javadoc)
     * @see eu.scy.toolbrokerapi.ToolBrokerAPI#getRepository()
     */
    public IRepository<IELO<K>, K> getRepository() {
        return repository;
    }
}
