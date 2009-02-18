package eu.scy.collaborationservice.tuplespaceconnector;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.log4j.Logger;


public class TuplespaceConectorTestCase extends TestCase {
    
    private static final Logger logger = Logger.getLogger(TuplespaceConectorTestCase.class.getName());
    

    public TuplespaceConectorTestCase(String testName) {
        super(testName);
    }

    
    public static Test suite() {
        return new TestSuite(TuplespaceConectorTestCase.class);
    }

    
    public void testUserPing() {
        logger.debug("testing ping write");
        TupleSpaceConnectorSwitchboard tscs = new TupleSpaceConnectorSwitchboard();
        String id = tscs.userPing("asdf");
        assertNotNull(id);
    }
}
