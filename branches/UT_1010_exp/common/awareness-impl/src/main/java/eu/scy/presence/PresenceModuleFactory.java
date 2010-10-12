package eu.scy.presence;

import eu.scy.presence.impl.PresenceModuleMockImpl;
import eu.scy.presence.impl.PresenceModuleXMPPImpl;


/**
 * Class that implememts the factory design pattern. Gives the ability
 * to create many different presence module that implement the standard interfaces and
 * have an efficient way to access them
 * 
 * @author anthonyp
 *
 */
public class PresenceModuleFactory {

    public static final String XMPP_STYLE = "XMPP_STYLE";
    public static final String MOCK_STYLE = "MOCK_STYLE";
    
    private static IPresenceModule presenceModule;
    
    /**
     * Factory method
     * 
     * @param style
     * @return
     * @throws PresenceModuleException
     */
    public static IPresenceModule getPresenceModule(String style) throws PresenceModuleException {
        
    	PresenceModuleException presenceException = new PresenceModuleException("unknown style of presence module");
        
        if(style == null)
            throw presenceException;
        
        if( style.equals(MOCK_STYLE)){
            presenceModule = new PresenceModuleMockImpl();
            //maybe intialization?
            return presenceModule;
        } else if(style.equals(XMPP_STYLE)){
        	presenceModule = new PresenceModuleXMPPImpl();
            //maybe intialization?
            return presenceModule;
        } else {
            //we dont know this style
            throw presenceException;
        }
    }
    
}
