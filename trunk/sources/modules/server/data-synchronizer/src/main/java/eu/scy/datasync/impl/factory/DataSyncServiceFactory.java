package eu.scy.datasync.impl.factory;

import eu.scy.datasync.api.DataSyncException;
import eu.scy.datasync.api.IDataSyncModule;

/**
 * Class that implememts the factory design pattern. Gives the ability
 * to create many different collaboration services that implement the standard interfaces and
 * have an efficient way to access them
 * 
 * @author anthonyp
 *
 */
public class DataSyncServiceFactory {

    public static final String XMPP_STYLE = "XMPP_STYLE";
    public static final String LOCAL_STYLE = "LOCAL_STYLE";
    
    private static IDataSyncModule collaborationService;
    
    /**
     * Factory method
     * 
     * @param style
     * @return
     * @throws DataSyncException
     */
    public static IDataSyncModule getCollaborationService(String style) throws DataSyncException {
        
        DataSyncException collaborationServiceException = new DataSyncException("unknown style of collaboration service");
        
        if(style == null)
            throw collaborationServiceException;
        
        if( style.equals(LOCAL_STYLE)){
            collaborationService = new DataSyncLocalImpl();
            //maybe intialization?
            return collaborationService;
        } else if(style.equals(XMPP_STYLE)){
            collaborationService = new DataSyncXMPPImpl();
            //maybe intialization?
            return collaborationService;
        } else {
            //we dont know this style
            throw collaborationServiceException;
        }
    }
    
}
