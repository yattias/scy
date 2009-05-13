package eu.scy.collaborationservice;

import eu.scy.collaborationservice.impl.CollaborationServiceLocalImpl;
import eu.scy.collaborationservice.impl.CollaborationServiceXMPPImpl;

/**
 * Class that implememts the factory design pattern. Gives the ability
 * to create many different collaboration services that implement the standard interfaces and
 * have an efficient way to access them
 * 
 * @author anthonyp
 *
 */
public class CollaborationServiceFactory {

    public static final String XMPP_STYLE = "XMPP_STYLE";
    public static final String LOCAL_STYLE = "LOCAL_STYLE";
    
    private static ICollaborationService collaborationService;
    
    /**
     * Factory method
     * 
     * @param style
     * @return
     * @throws CollaborationServiceException
     */
    public static ICollaborationService getCollaborationService(String style) throws CollaborationServiceException {
        
        CollaborationServiceException collaborationServiceException = new CollaborationServiceException("unknown style of collaboration service");
        
        if(style == null)
            throw collaborationServiceException;
        
        if( style.equals(LOCAL_STYLE)){
            collaborationService = new CollaborationServiceLocalImpl();
            //maybe intialization?
            return collaborationService;
        } else if(style.equals(XMPP_STYLE)){
            collaborationService = new CollaborationServiceXMPPImpl();
            //maybe intialization?
            return collaborationService;
        } else {
            //we dont know this style
            throw collaborationServiceException;
        }
    }
    
}
