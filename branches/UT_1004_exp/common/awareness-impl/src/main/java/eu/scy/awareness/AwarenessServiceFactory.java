package eu.scy.awareness;

import eu.scy.awareness.impl.AwarenessServiceMockImpl;
import eu.scy.awareness.impl.AwarenessServiceXMPPImpl;


/**
 * Class that implememts the factory design pattern. Gives the ability
 * to create many different awareness services that implement the standard interfaces and
 * have an efficient way to access them
 * 
 * @author anthonyp
 *
 */
public class AwarenessServiceFactory {

    public static final String XMPP_STYLE = "XMPP_STYLE";
    public static final String MOCK_STYLE = "MOCK_STYLE";
    
    private static IAwarenessService awarenessService;
    
    /**
     * Factory method
     * 
     * @param style
     * @return
     * @throws AwarenessServiceException
     */
    public static IAwarenessService getAwarenessService(String style) throws AwarenessServiceException {
        
        AwarenessServiceException awarenessException = new AwarenessServiceException("unknown style of awareness service");
        
        if(style == null)
            throw awarenessException;
        
        if( style.equals(MOCK_STYLE)){
            awarenessService = new AwarenessServiceMockImpl();
            //maybe intialization?
            return awarenessService;
        } else if(style.equals(XMPP_STYLE)){
            awarenessService = new AwarenessServiceXMPPImpl();
            //maybe intialization?
            return awarenessService;
        } else {
            //we dont know this style
            throw awarenessException;
        }
    }
    
}
