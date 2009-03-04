package eu.scy.openfire.plugin;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.log4j.Logger;

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
    
    private ScyBaseObject getScyBaseObject() {
        ScyBaseObject sbo = new ScyBaseObject();
        sbo.setId("1337");
        sbo.setName("a nice name for this test object");
        sbo.setDescription("dezkript");
        return sbo;
    }
    
    
    public void testCreateTupleAdapter() {
        assertNotNull(getTupleAdapter());
    }

    
    public void testWriteReadDelete() {
        String id = null;
        // write
        id = getTupleAdapter().write("someTestCase", getScyBaseObject());
        assertNotNull(id);
        // read
        ScyBaseObject sbo = getTupleAdapter().readById(id);
        assertNotNull(sbo);
        // delete
        id = getTupleAdapter().delete(id);
        assertNotNull(id);
    }

    
    public void testWriteWithExpiration() {
        String id = null;
        // write with expiration
        id = getTupleAdapter().write("someTestCase", getScyBaseObject(), TWO_SECONDS);
        assertNotNull(id);
        // confirm tuple
        ScyBaseObject sbo = getTupleAdapter().readById(id);
        assertNotNull(sbo);
        try {
            Thread.sleep(FOUR_SECONDS);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // confirm tuple has expired
        sbo = getTupleAdapter().readById(id);
        assertNull(sbo);
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
    public String create(ScyBaseObject sbo) {
        // TODO Auto-generated method stub        
        return null;
    }

    @Override
    public String createWithExpiration(ScyBaseObject sbo, long expiration) {
        // TODO Auto-generated method stub        
        return null;
    }

    @Override
    public String delete(String id) {
        // TODO Auto-generated method stub        
        return null;
    }

    @Override
    public ScyBaseObject read(String id) {
        // TODO Auto-generated method stub        
        return null;
    }

    @Override
    public String update(ScyBaseObject sbo, String id) {
        // TODO Auto-generated method stub        
        return null;
    }

    @Override
    public String updateWithExpiration(ScyBaseObject sbo, String id, long expiration) {
        // TODO Auto-generated method stub        
        return null;
    }    
}
