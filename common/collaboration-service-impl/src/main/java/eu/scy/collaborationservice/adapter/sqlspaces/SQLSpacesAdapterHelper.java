package eu.scy.collaborationservice.adapter.sqlspaces;

import org.apache.log4j.Logger;

/**
 * SQLSpaces helper, acts like a factory
 * 
 * @author anthonyp
 *
 */
public class SQLSpacesAdapterHelper {
    
    public static final Logger logger = Logger.getLogger(SQLSpacesAdapterHelper.class.getName());
    private static SQLSpaceAdapter sqlSpacesAdapterHelper;
    
    /**
     * Gets an instance of the adapter
     * 
     * @return
     */
    public static SQLSpaceAdapter getInstance() {
        if (sqlSpacesAdapterHelper == null) {
            logger.debug("create the scyAdapter");
            sqlSpacesAdapterHelper = new SQLSpaceAdapter();
        }
        return sqlSpacesAdapterHelper;
    }
}
