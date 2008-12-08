/**
 * 
 */
package eu.scy.toolbroker.test;

import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import roolo.api.IExtensionManager;
import roolo.api.IRepository;
import roolo.api.search.ISearchResult;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataTypeManager;
import eu.scy.actionlogging.api.IAction;
import eu.scy.actionlogging.logger.Action;
import eu.scy.notification.api.INotification;
import eu.scy.notification.api.INotificationCallback;
import eu.scy.toolbroker.ToolBrokerImpl;

/**
 * This test will only run, if you have a server running configured in the
 * client.propeties file!
 * 
 * @author Giemza
 */
public class ToolBrokerTest {
    
    private static ToolBrokerImpl<IMetadataKey> toolBroker;
    
    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        toolBroker = new ToolBrokerImpl<IMetadataKey>();
    }
    
    /**
     * Test method for {@link eu.scy.toolbroker.ToolBrokerImpl#getRepository()}.
     */
    @Test
    public void testGetRepository() {
        IRepository<IELO<IMetadataKey>, IMetadataKey> repository = toolBroker.getRepository();
        Assert.assertNotNull("Repository should not be null!", repository);
    }
    
    /**
     * Test method for {@link eu.scy.toolbroker.ToolBrokerImpl#getMetaDataTypeManager()}.
     */
    @Test
    public void testGetMetaDataTypeManager() {
        IMetadataTypeManager<IMetadataKey> metaDataTypeManager = toolBroker.getMetaDataTypeManager();
        Assert.assertNotNull("MetaDataTypeManager should not be null!", metaDataTypeManager);
    }
    
    /**
     * Test method for {@link eu.scy.toolbroker.ToolBrokerImpl#getExtensionManager()}.
     */
    @Test
    public void testGetExtensionManager() {
        IExtensionManager extensionManager = toolBroker.getExtensionManager();
        Assert.assertNotNull("ExtensionManager should not be null!", extensionManager);
    }
    
    /**
     * Test method for {@link roolo.api.IRepository#retrieveELO(java.net.URI)}
     */
    @Test
    public void testSearchELOsOnRepository() throws Exception {
        List<ISearchResult> searchResult = toolBroker.getRepository().search(null);
        for (ISearchResult result : searchResult) {
            System.out.println(result.getUri());
        }
    }
    
    @Test
    public void testActionLogging() throws Exception {
        IAction action = new Action();
        action.addProperty("name", "adam");
        action.addProperty("tool", "map");
        toolBroker.getActionLogger().log(action);
    }
    
    @Test
    public void testNotificationRegistration() throws Exception {
        toolBroker.getNotificationService().registerCallback(new INotificationCallback() {
            
            @Override
            public void onNotification(INotification notification) {
                System.out.println(notification);
            }
            
        });
    }
    
}
