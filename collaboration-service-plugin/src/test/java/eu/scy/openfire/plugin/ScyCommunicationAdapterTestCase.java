package eu.scy.openfire.plugin;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.log4j.Logger;

import eu.scy.core.model.impl.ScyBaseObject;



public class ScyCommunicationAdapterTestCase extends TestCase implements IScyCommunicationAdapter  {
    
    private static final Logger logger = Logger.getLogger(ScyCommunicationAdapterTestCase.class.getName());
    private SQLSpaceAdapter sqlSpaceAdapter;

    
    
    public ScyCommunicationAdapterTestCase(String testName) {
        super(testName);
    }   
    
    public static Test suite() {
        return new TestSuite(ScyCommunicationAdapterTestCase.class);
    }
    
    private SQLSpaceAdapter getTupleAdapter() {
        if (sqlSpaceAdapter == null) {
            sqlSpaceAdapter = SQLSpaceAdapter.createAdapter("thomasd", SQLSpaceAdapter.COLLABORATION_SERVICE_SPACE, this);         
        }
        return sqlSpaceAdapter;
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

    @Override
    public void sendCallBack(String something) {
        // TODO Auto-generated method stub
        
    }    
}
