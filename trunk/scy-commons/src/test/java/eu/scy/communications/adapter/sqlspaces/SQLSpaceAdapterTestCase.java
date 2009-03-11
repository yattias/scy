package eu.scy.communications.adapter.sqlspaces;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.log4j.Logger;
import org.junit.Ignore;

import eu.scy.communications.adapter.IScyCommunicationAdapter;
import eu.scy.communications.adapter.IScyCommunicationListener;
import eu.scy.communications.message.IScyMessage;
import eu.scy.communications.message.impl.ScyMessage;



public class SQLSpaceAdapterTestCase extends TestCase implements IScyCommunicationAdapter  {
    
    private static final Logger logger = Logger.getLogger(SQLSpaceAdapter.class.getName());
    private SQLSpaceAdapter sqlSpaceAdapter;
    private static final long TWO_SECONDS = 2*1000;
    private static final long FOUR_SECONDS = 4*1000;
    private static final long TEN_SECONDS = 10*1000;
    
    
    public SQLSpaceAdapterTestCase(String testName) {
        super(testName);
    }
    
    
    public static Test suite() {
        return new TestSuite(SQLSpaceAdapterTestCase.class);
    }
    
    
    private SQLSpaceAdapter getTupleAdapter() {
        if (sqlSpaceAdapter == null) {
            sqlSpaceAdapter = SQLSpaceAdapter.createAdapter("thomasd", SQLSpaceAdapter.COLLABORATION_SERVICE_SPACE, this);         
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
    
    
    public void testCreateTupleAdapter() {
        assertNotNull(getTupleAdapter());
    }

    
    public void testWriteReadDelete() {
        String id = null;
        // write
        id = getTupleAdapter().write(getScyMessage());
        assertNotNull(id);
        // read
        IScyMessage sm = getTupleAdapter().readById(id);
        assertNotNull(sm);
        // delete
        id = getTupleAdapter().delete(id);
        assertNotNull(id);
    }

    @Ignore
    public void testWriteWithExpiration() {
        String id = null;
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
    }
    

    @Override
    public void actionUponDelete(IScyMessage scyMessage) {
        logger.info("Callback sez: Stuff deleted from sqlspaces");
    }


    @Override
    public void actionUponWrite(IScyMessage scyMessage) {
        logger.info("Callback sez: Stuff written to sqlspaces");
    }

    @Override
    public void addScyCommunicationListener(IScyCommunicationListener listener) {
        // TODO Auto-generated method stub        
    }


    @Override
    public String create(IScyMessage sm) {
        // TODO Auto-generated method stub        
        return null;
    }

    @Override
    public String delete(String id) {
        // TODO Auto-generated method stub        
        return null;
    }

    @Override
    public IScyMessage read(String id) {
        // TODO Auto-generated method stub        
        return null;
    }

    @Override
    public String update(IScyMessage sm, String id) {
        // TODO Auto-generated method stub        
        return null;
    }


    @Override
    public void sendCallBack(IScyMessage scyMessage) {
        // TODO Auto-generated method stub
        
    }    
}
