package eu.scy.toolbroker;

import javax.security.auth.login.LoginException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import roolo.api.IExtensionManager;
import roolo.api.IRepository;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataTypeManager;
import eu.scy.actionlogging.api.IActionLogger;
import eu.scy.awareness.IAwarenessService;
import eu.scy.collaborationservice.ICollaborationService;
import eu.scy.notification.api.INotificationService;
import eu.scy.sessionmanager.SessionManager;
import eu.scy.toolbrokerapi.ToolBrokerAPI;

import eu.scy.datasync.api.IDataSyncModule;
import eu.scy.datasync.client.DataSyncService;
import eu.scy.datasync.client.IDataSyncService;

/**
 * This class implements the ToolBrokerAPI interface and provides all the
 * services. The main idea of this implementation is that all the services are
 * injected by Spring into this class and then can be accessed by all clients.
 * 
 * @author Giemza
 */
public class ToolBrokerImpl<K extends IMetadataKey> implements ToolBrokerAPI<K> {
    
    private static final String beanConfigurationFile = "beans.xml";
    
    private ApplicationContext context;
    
    private IRepository<IELO<K>, K> repository;
    
    private IMetadataTypeManager<K> metaDataTypeManager;
    
    private IExtensionManager extensionManager;
    
    private IActionLogger actionLogger;
    
    private INotificationService notificationService;
    
    private SessionManager sessionManager;

    private IAwarenessService awarenessService;

    private ICollaborationService collaborationService;

    private IDataSyncService dataSyncService;
    
    
    @SuppressWarnings("unchecked")
    public ToolBrokerImpl() {
        context = new ClassPathXmlApplicationContext(beanConfigurationFile);
        
        repository = (IRepository<IELO<K>, K>) context.getBean("repository");
        metaDataTypeManager = (IMetadataTypeManager<K>) context.getBean("metadataTypeManager");
        extensionManager = (IExtensionManager) context.getBean("extensionManager");
        
        actionLogger = (IActionLogger) context.getBean("actionlogger");
        notificationService = (INotificationService) context.getBean("notificationService");
        
        sessionManager = (SessionManager) context.getBean("sessionManager");
        awarenessService = (IAwarenessService) context.getBean("awarenessService");
        collaborationService = (ICollaborationService) context.getBean("collaborationService");
        dataSyncService = (IDataSyncService) context.getBean("dataSyncService");
    }
    
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
    
    /**
     * @param metaDataTypeManager
     *            the metaDataTypeManager to set
     */
    public void setMetaDataTypeManager(IMetadataTypeManager<K> metaDataTypeManager) {
        this.metaDataTypeManager = metaDataTypeManager;
    }
    
    /**
     * @param extensionManager
     *            the extensionManager to set
     */
    public void setExtensionManager(IExtensionManager extensionManager) {
        this.extensionManager = extensionManager;
    }
    
    /**
     * @param actionLogger
     *            the actionLogger to be set
     */
    public void setActionLogger(IActionLogger actionLogger) {
        this.actionLogger = actionLogger;
    }
    
    /**
     * @param notificationService
     *            the notificationService to be set
     */
    public void setNotificationService(INotificationService notificationService) {
        this.notificationService = notificationService;
    }
    
    public void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }
    
    /*
     * (non-Javadoc)
     * @see eu.scy.toolbrokerapi.ToolBrokerAPI#getRepository()
     */
    public IRepository<IELO<K>, K> getRepository() {
        return repository;
    }
    
    /*
     * (non-Javadoc)
     * @see eu.scy.toolbrokerapi.ToolBrokerAPI#getMetaDataTypeManager()
     */
    public IMetadataTypeManager<K> getMetaDataTypeManager() {
        return metaDataTypeManager;
    }
    
    /*
     * (non-Javadoc)
     * @see eu.scy.toolbrokerapi.ToolBrokerAPI#getExtensionManager()
     */
    public IExtensionManager getExtensionManager() {
        return extensionManager;
    }
    
    public IActionLogger getActionLogger() {
        return actionLogger;
    }
    
    public INotificationService getNotificationService() {
        return notificationService;
    }
    
    @Override
    public SessionManager getUserSession(String username, String password) {
        try {
            sessionManager.login(username, password);
            return sessionManager;
        } catch (LoginException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public IAwarenessService getAwarenessService() {
        return awarenessService;
    }
    
    @Override
    public ICollaborationService getCollaborationService() {
        return collaborationService;
    }
    
    @Override
    public IDataSyncService getDataSyncService() {
        return dataSyncService;
    }
    
}
