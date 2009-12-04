package eu.scy.datasync.adapter.communication;

import static org.junit.Assert.assertNotNull;
import info.collide.sqlspaces.commons.TupleSpaceException;
import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;

import org.apache.log4j.Logger;

import eu.scy.common.configuration.Configuration;
import eu.scy.communications.datasync.properties.CommunicationProperties;
import eu.scy.communications.message.ISyncMessage;
import eu.scy.communications.message.impl.SyncMessageHelper;
import eu.scy.datasync.adapter.IScyCommunicationAdapter;
import eu.scy.datasync.adapter.IScyCommunicationListener;
import eu.scy.datasync.adapter.sqlspaces.SQLSpaceAdapter;



public class ScyCommunicationAdapterTestCase implements IScyCommunicationAdapter  {
    
    private static final Logger logger = Logger.getLogger(ScyCommunicationAdapterTestCase.class.getName());
    private SQLSpaceAdapter sqlSpaceAdapter;

    private static final String TEST_CONTENT = "This is the content, but there isn't much.";
    private static final String TEST_EVENT = "important event";
    private static final String TEST_TOOL_ID = "eu.scy.test." + ScyCommunicationAdapterTestCase.class.getName();
    private static final String TEST_TOOL_SESSION_ID = "1234567890";
    private static final String TEST_FROM = "passerby@scy.intermedia.uio.no";
    private static final String TEST_TO = "passerby@scy.intermedia.uio.no";
    
    public ScyCommunicationAdapterTestCase() {
    }   

    
    public static Test suite() { 
        return new JUnit4TestAdapter(ScyCommunicationAdapterTestCase.class); 
    }
    
    
    private SQLSpaceAdapter getTupleAdapter() {
        if (sqlSpaceAdapter == null) {
            sqlSpaceAdapter = new SQLSpaceAdapter();
            try {
                sqlSpaceAdapter.initialize("thomasd", Configuration.getInstance().getSqlSpacesServerSpaceDatasync());
            } catch (TupleSpaceException e) {
                logger.error("Tuple space fluke " + e);
                e.printStackTrace();
                sqlSpaceAdapter = null;
            }     
        }
        return sqlSpaceAdapter;
    }
    
    
    private ISyncMessage getTestSyncMessage() {
        return SyncMessageHelper.createSyncMessageWithDefaultExp(TEST_TOOL_SESSION_ID, TEST_TOOL_ID, TEST_FROM,TEST_TO, TEST_CONTENT, TEST_EVENT, null);
    }
    
    
    
    @org.junit.Test
    public void testCreateTupleAdapter() {
        assertNotNull(getTupleAdapter());
        ISyncMessage syncMessage = getTestSyncMessage();
        assertNotNull(syncMessage);
    }
    
    
    @Override
    public void actionUponDelete(ISyncMessage syncMessage) {
        logger.info("Callback sez: Stuff deleted from sqlspaces");
    }


    @Override
    public void actionUponWrite(ISyncMessage syncMessage) {
        logger.info("Callback sez: Stuff written to sqlspaces");
    }

    @Override
    public void actionUponUpdate(ISyncMessage syncMessage) {
        logger.info("Callback sez: Stuff updateed to sqlspaces");
    }

    @Override
    public String create(ISyncMessage syncMessage) {
        // TODO Auto-generated method stub        
        return null;
    }

    @Override
    public String delete(String id) {
        // TODO Auto-generated method stub        
        return null;
    }

    @Override
    public ISyncMessage read(String id) {
        // TODO Auto-generated method stub        
        return null;
    }

    @Override
    public String update(ISyncMessage sm) {
        // TODO Auto-generated method stub        
        return null;
    }

    @Override
    public void sendCallBack(ISyncMessage syncMessage) {
        // TODO Auto-generated method stub        
    }


    @Override
    public void addScyCommunicationListener(IScyCommunicationListener listener) {
        // TODO Auto-generated method stub        
    }    
}
