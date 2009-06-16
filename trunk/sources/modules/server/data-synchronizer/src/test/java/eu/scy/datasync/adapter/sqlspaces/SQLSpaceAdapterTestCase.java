package eu.scy.datasync.adapter.sqlspaces;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;

import org.apache.log4j.Logger;
import org.junit.Ignore;

import eu.scy.datasync.adapter.sqlspaces.SQLSpaceAdapter;
import eu.scy.datasync.api.ISyncMessage;
import eu.scy.datasync.impl.SyncMessage;


public class SQLSpaceAdapterTestCase {
    
    private static final Logger logger = Logger.getLogger(SQLSpaceAdapterTestCase.class.getName());
    private SQLSpaceAdapter sqlSpaceAdapter;
    private static final long ONE_SECOND = 1*1000;
    private static final long EIGHT_SECONDS = 6*1000;
    
    private static final String TEST_CONTENT = "This is the content, but there isn't much.";
    private static final String TEST_EVENT = "important event";
    private static final String TEST_TOOL_ID = "eu.scy.test." + SQLSpaceAdapterTestCase.class.getName();
    private static final String TEST_TOOL_SESSION_ID = "1234567890";
    private static final String TEST_FROM = "passerby@wiki.intermedia.uio.no";
    
    
    public SQLSpaceAdapterTestCase() {
    }


    public static Test suite() { 
        return new JUnit4TestAdapter(SQLSpaceAdapterTestCase.class); 
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
    public void doNothingForNow() {
    }
    
    
    @org.junit.Test
    public void testCreateTupleAdapter() {
        assertNotNull(getTupleAdapter());
    }

    
    @org.junit.Test
    public void testWriteReadDelete() {
        String id = null;
        // write
        id = getTupleAdapter().write(getTestSyncMessage());
        assertNotNull(id);
        // read
        ISyncMessage sm = getTupleAdapter().readById(id);
        assertNotNull(sm);
        // delete
        id = getTupleAdapter().delete(id);
        assertNotNull(id);
        sm = getTupleAdapter().readById(id);
        assertNull(sm);        
    }


    @org.junit.Test
    public void testWriteWithExpiration() {
        String id = null;
        ISyncMessage sm = getTestSyncMessage();
        sm.setExpiration(ONE_SECOND);
        // write with expiration
        id = getTupleAdapter().write(sm);
        assertNotNull(id);
        // confirm tuple
        sm = getTupleAdapter().readById(id);
        assertNotNull(sm);
        try {
            Thread.sleep(EIGHT_SECONDS);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // confirm tuple has expired
        sm = getTupleAdapter().readById(id);
        assertNull(sm);
    }
  
}
