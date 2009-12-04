package eu.scy.datasync.adapter.sqlspaces;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import info.collide.sqlspaces.commons.TupleSpaceException;
import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;

import org.apache.log4j.Logger;

import eu.scy.common.configuration.Configuration;
import eu.scy.communications.datasync.properties.CommunicationProperties;
import eu.scy.communications.message.ISyncMessage;
import eu.scy.communications.message.impl.SyncMessageHelper;


public class SQLSpaceAdapterTestCase {
    
    private static final Logger logger = Logger.getLogger(SQLSpaceAdapterTestCase.class.getName());
    private SQLSpaceAdapter sqlSpaceAdapter;
    private static final long ONE_SECOND = 1*1000;
    private static final long EIGHT_SECONDS = 6*1000;
    
    private static final String TEST_CONTENT = "This is the content, but there isn't much.";
    private static final String TEST_EVENT = "important event";
    private static final String TEST_TOOL_ID = "eu.scy.test." + SQLSpaceAdapterTestCase.class.getName();
    private static final String TEST_TOOL_SESSION_ID = "1234567890";
    private static final String TEST_FROM = "passerby@scy.intermedia.uio.no";
    private static final String TEST_TO = "obama@scy.intermedia.uio.no";
    
    public SQLSpaceAdapterTestCase() {
    }


    public static Test suite() { 
        return new JUnit4TestAdapter(SQLSpaceAdapterTestCase.class); 
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
        return SyncMessageHelper.createSyncMessage(TEST_TOOL_SESSION_ID, TEST_TOOL_ID, TEST_FROM, TEST_TO, TEST_CONTENT, TEST_EVENT, null, Configuration.getInstance().getDatasyncMessageDefaultExpiration());
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
