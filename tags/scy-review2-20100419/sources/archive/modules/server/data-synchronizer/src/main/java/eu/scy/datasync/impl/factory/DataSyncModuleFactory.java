package eu.scy.datasync.impl.factory;

import eu.scy.datasync.api.DataSyncException;
import eu.scy.datasync.api.IDataSyncModule;

/**
 * Class that implememts the factory design pattern. Gives the ability
 * to create many different DataSyncModules that implement the standard interfaces and
 * have an efficient way to access them
 * 
 * @author thomasd
 *
 */
public class DataSyncModuleFactory {

    public static final String XMPP_STYLE = "XMPP_STYLE";
    public static final String LOCAL_STYLE = "LOCAL_STYLE";
    
    private static IDataSyncModule dataSyncModule;
    
    /**
     * Factory method
     * 
     * @param style
     * @return
     * @throws DataSyncException
     */
    public static IDataSyncModule getDataSyncModule(String style) throws DataSyncException {
        
        DataSyncException dataSyncException = new DataSyncException("unknown style of data sync");
        
        if(style == null)
            throw dataSyncException;
        
        if( style.equals(LOCAL_STYLE)){
            dataSyncModule = new DataSyncModule();
            //maybe intialization?
            return dataSyncModule;
        } else {
            //we dont know this style
            throw dataSyncException;
        }
    }
    
}
