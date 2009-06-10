package eu.scy.datasync.adapter.communication;

import static org.junit.Assert.assertNotNull;
import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;

import org.apache.log4j.Logger;
import org.junit.Ignore;

import eu.scy.communications.message.IScyMessage;
import eu.scy.communications.message.impl.ScyMessage;
import eu.scy.datasync.adapter.IScyCommunicationAdapter;
import eu.scy.datasync.adapter.IScyCommunicationListener;
import eu.scy.datasync.adapter.sqlspaces.SQLSpaceAdapter;



public class ScyCommunicationAdapterTestCase implements IScyCommunicationAdapter  {
    
    private static final Logger logger = Logger.getLogger(ScyCommunicationAdapterTestCase.class.getName());
    private SQLSpaceAdapter sqlSpaceAdapter;

    
    
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
    
    private IScyMessage getScyMessage() {
        ScyMessage sm = new ScyMessage();
        sm.setId("1338");
        sm.setName("leeter that leet - test object");
        sm.setDescription("dezkript");
        return sm;
    }
    
    @org.junit.Test
    public void dummyForNow() {
    }
    
    //@Ignore
    @org.junit.Test
    public void testCreateTupleAdapter() {
        assertNotNull(getTupleAdapter());
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

    @Override
    public void actionUponUpdate(IScyMessage scyMessage) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void addScyCommunicationListener(IScyCommunicationListener listener) {
        // TODO Auto-generated method stub
        
    }    
}
