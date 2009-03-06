package eu.scy.communications.adapter.sqlspaces;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.log4j.Logger;

import eu.scy.communications.adapter.IScyCommunicationAdapter;
import eu.scy.communications.adapter.IScyCommunicationListener;
import eu.scy.communications.message.ScyMessage;
import eu.scy.core.model.impl.ScyBaseObject;



public class SQLSpaceAdapterTestCase extends TestCase implements IScyCommunicationAdapter  {
    
    private static final Logger logger = Logger.getLogger(SQLSpaceAdapter.class.getName());
    private SQLSpaceAdapter sqlSpaceAdapter;
    private static final long TWO_SECONDS = 2*1000;
    private static final long FOUR_SECONDS = 4*1000;
    
    
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
        id = getTupleAdapter().write("someTestCase", getScyMessage());
        assertNotNull(id);
        // read
        ScyMessage sm = getTupleAdapter().readById(id);
        assertNotNull(sm);
        // delete
        id = getTupleAdapter().delete(id);
        assertNotNull(id);
    }

    
    public void testWriteWithExpiration() {
        String id = null;
        // write with expiration
        id = getTupleAdapter().write("someTestCase", getScyMessage(), TWO_SECONDS);
        assertNotNull(id);
        // confirm tuple
        ScyMessage sm = getTupleAdapter().readById(id);
        assertNotNull(sm);
        try {
            Thread.sleep(FOUR_SECONDS);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // confirm tuple has expired
        sm = getTupleAdapter().readById(id);
        //FIXME
        //assertNull(sm);
    }
    

    @Override
    public void actionUponDelete(String username) {
        logger.info("Callback sez: Stuff deleted from sqlspaces");
    }


    @Override
    public void actionUponWrite(String username) {
        logger.info("Callback sez: Stuff written to sqlspaces");
    }

    @Override
    public void addScyCommunicationListener(IScyCommunicationListener listener) {
        // TODO Auto-generated method stub        
    }


    @Override
    public String create(ScyMessage sm) {
        // TODO Auto-generated method stub        
        return null;
    }

    @Override
    public String createWithExpiration(ScyMessage sm, long expiration) {
        // TODO Auto-generated method stub        
        return null;
    }

    @Override
    public String delete(String id) {
        // TODO Auto-generated method stub        
        return null;
    }

    @Override
    public ScyMessage read(String id) {
        // TODO Auto-generated method stub        
        return null;
    }

    @Override
    public String update(ScyMessage sm, String id) {
        // TODO Auto-generated method stub        
        return null;
    }

    @Override
    public String updateWithExpiration(ScyMessage sm, String id, long expiration) {
        // TODO Auto-generated method stub        
        return null;
    }


    @Override
    public void sendCallBack(String something) {
        // TODO Auto-generated method stub
        
    }    
}
