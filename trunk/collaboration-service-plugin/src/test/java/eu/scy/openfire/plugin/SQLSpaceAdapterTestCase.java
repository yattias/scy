package eu.scy.openfire.plugin;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.log4j.Logger;

import eu.scy.core.model.impl.ScyBaseObject;



public class SQLSpaceAdapterTestCase extends TestCase implements IScyCommunicationAdapter  {
    
    private static final Logger logger = Logger.getLogger(SQLSpaceAdapter.class.getName());
    private SQLSpaceAdapter sqlSpaceAdapter;
    
    
    public SQLSpaceAdapterTestCase(String testName) {
        super(testName);
    }
    
    
    public static Test suite() {
        return new TestSuite(SQLSpaceAdapterTestCase.class);
    }
    
    
    private SQLSpaceAdapter getSQLSpaceAdapter() {
        if (sqlSpaceAdapter == null) {
            sqlSpaceAdapter = SQLSpaceAdapter.createAdapter("thomasd", SQLSpaceAdapter.COLLABORATION_SERVICE_SPACE, this);         
        }
        return sqlSpaceAdapter;
    }
    
    
    public void testCreateSQLSpaceAdapter() {
        assertNotNull(getSQLSpaceAdapter());
    }



    @Override
    public void actionUponDelete(String username) {
        // TODO Auto-generated method stub
        
    }


    @Override
    public void actionUponWrite(String username) {
        // TODO Auto-generated method stub
        
    }


    @Override
    public String create(ScyBaseObject sbo) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public ScyBaseObject delete(String id) {
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


    

    
}
