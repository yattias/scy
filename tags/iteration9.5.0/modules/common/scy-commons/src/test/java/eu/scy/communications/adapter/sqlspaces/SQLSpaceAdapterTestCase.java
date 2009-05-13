package eu.scy.communications.adapter.sqlspaces;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;

import org.apache.log4j.Logger;

import eu.scy.communications.message.IScyMessage;
import eu.scy.communications.message.impl.ScyMessage;


public class SQLSpaceAdapterTestCase {
    
    private static final Logger logger = Logger.getLogger(SQLSpaceAdapterTestCase.class.getName());
    private SQLSpaceAdapter sqlSpaceAdapter;
    private static final long TWO_SECONDS = 2*1000;
    private static final long FOUR_SECONDS = 4*1000;
    private static final long TEN_SECONDS = 10*1000;
    
    
    public SQLSpaceAdapterTestCase() {
    }


    public static Test suite() { 
        return new JUnit4TestAdapter(SQLSpaceAdapterTestCase.class); 
    }
    
    
    private SQLSpaceAdapter getTupleAdapter() {
        if (sqlSpaceAdapter == null) {
            sqlSpaceAdapter = new SQLSpaceAdapter();
            sqlSpaceAdapter.initialize("thomasd", SQLSpaceAdapter.COLLABORATION_SERVICE_SPACE);         
        }
        return sqlSpaceAdapter;
    }
    
    private ScyMessage getScyMessage() {
        ScyMessage sm = new ScyMessage();
        sm.setId("1337");
        sm.setName("a nice name for this test object");
        sm.setDescription("dezkript");
        return sm;
    }
    
    @org.junit.Test
    public void testCreateTupleAdapter() {
        assertNotNull(getTupleAdapter());
    }

    

    @org.junit.Test
    public void testWriteReadDelete() {
        /*String id = null;
        // write
        id = getTupleAdapter().write(getScyMessage());
        assertNotNull(id);
        // read
        IScyMessage sm = getTupleAdapter().readById(id);
        assertNotNull(sm);
        // delete
        id = getTupleAdapter().delete(id);
        assertNotNull(id);
        sm = getTupleAdapter().readById(id);
        assertNull(sm);
        */
    }


    @org.junit.Test
    public void testWriteWithExpiration() {
        /*String id = null;
        IScyMessage sm = getScyMessage();
        sm.setExpiraton(TWO_SECONDS);
        // write with expiration
        id = getTupleAdapter().write(sm);
        assertNotNull(id);
        // confirm tuple
        sm = getTupleAdapter().readById(id);
        assertNotNull(sm);
        try {
            Thread.sleep(TEN_SECONDS);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // confirm tuple has expired
        sm = getTupleAdapter().readById(id);
        assertNull(sm);
        */
    }
  
}
