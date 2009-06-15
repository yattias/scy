package eu.scy.datasync.adapter.communication;

import static org.junit.Assert.assertNotNull;
import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;

import org.apache.log4j.Logger;
import org.junit.Ignore;

import eu.scy.datasync.adapter.IScyCommunicationAdapter;
import eu.scy.datasync.adapter.IScyCommunicationListener;
import eu.scy.datasync.adapter.sqlspaces.SQLSpaceAdapter;
import eu.scy.datasync.adapter.sqlspaces.SQLSpaceAdapterTestCase;
import eu.scy.datasync.api.ISyncMessage;
import eu.scy.datasync.impl.SyncMessage;
import eu.scy.datasync.impl.SyncMessageTestCase;



public class ScyCommunicationAdapterTestCase implements IScyCommunicationAdapter  {
    
    private static final Logger logger = Logger.getLogger(ScyCommunicationAdapterTestCase.class.getName());
    private SQLSpaceAdapter sqlSpaceAdapter;

    private static final String TEST_CONTENT = "This is the content, but there isn't much.";
    private static final String TEST_EVENT = "important event";
    private static final String TEST_TOOL_ID = "eu.scy.test." + ScyCommunicationAdapterTestCase.class.getName();
    private static final String TEST_TOOL_SESSION_ID = "1234567890";
    private static final String TEST_FROM = "passerby@wiki.intermedia.uio.no";

    
    public ScyCommunicationAdapterTestCase() {
    }   

    
    public static Test suite() { 
        return new JUnit4TestAdapter(ScyCommunicationAdapterTestCase.class); 
    }
    
    
    
    private SQLSpaceAdapter getTupleAdapter() {
        if (sqlSpaceAdapter == null) {
            sqlSpaceAdapter = new SQLSpaceAdapter();
            sqlSpaceAdapter.initialize("thomasd", SQLSpaceAdapter.DATA_SYNCHRONIZATION_SPACE);     
        }
        return sqlSpaceAdapter;
    }
    
    private ISyncMessage getTestSyncMessage() {
        return SyncMessage.createSyncMessage(TEST_TOOL_SESSION_ID, TEST_TOOL_ID, TEST_FROM, TEST_CONTENT, TEST_EVENT, null, SyncMessage.DEFAULT_MESSAGE_EXPIRATION_TIME);
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
    public void actionUponUpdate(ISyncMessage syncMessage) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void addScyCommunicationListener(IScyCommunicationListener listener) {
        // TODO Auto-generated method stub
        
    }    
}
