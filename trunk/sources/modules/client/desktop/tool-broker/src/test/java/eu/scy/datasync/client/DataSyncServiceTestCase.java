package eu.scy.datasync.client;

import static org.junit.Assert.*;

import org.apache.log4j.Logger;
import org.hibernate.validator.AssertTrue;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import roolo.elo.api.IMetadataKey;
import eu.scy.communications.datasync.event.IDataSyncEvent;
import eu.scy.communications.datasync.event.IDataSyncListener;
import eu.scy.communications.datasync.properties.CommunicationProperties;
import eu.scy.communications.message.ISyncMessage;
import eu.scy.communications.message.impl.SyncMessage;
import eu.scy.communications.message.impl.SyncMessageHelper;
import eu.scy.toolbroker.ToolBrokerImpl;

/**
 * Testing the client side of data sync.
 * 
 * @author thomasd
 *
 */

public class DataSyncServiceTestCase {
    
    private final static Logger logger = Logger.getLogger(DataSyncServiceTestCase.class.getName());
    private static final String HARD_CODED_TOOL_NAME = "eu.scy.client.tools.dataSyncServiceTestCase";
    private static final String HARD_CODED_USER_NAME = "obama";
    private static final String HARD_CODED_PASSWORD = "obama";
    
    private IDataSyncService dataSyncService;
    private ToolBrokerImpl<IMetadataKey> tbi;
    private String currentSession;
    private String sessions;
    
    private CommunicationProperties props;
    
    @Before
    public void init() {
        logger.debug("================ Setting up stuff before the tests can begin");

        props = new CommunicationProperties();
        tbi = new ToolBrokerImpl<IMetadataKey>();
        dataSyncService = tbi.getDataSyncService();
        dataSyncService.init(tbi.getConnection(HARD_CODED_USER_NAME, HARD_CODED_PASSWORD));
        dataSyncService.addDataSyncListener( new IDataSyncListener() {

            @Override
            public void handleDataSyncEvent(IDataSyncEvent e) {
                ISyncMessage syncMessage = e.getSyncMessage();
               
                if (syncMessage.getEvent().equals(props.clientEventCreateSession)) {
                    logger.debug("-------- CREATE SESSION ---------");
                    logger.debug(syncMessage.toString());
                    currentSession = syncMessage.getToolSessionId();                    
                } else if (syncMessage.getEvent().equals(props.clientEventGetSessions)) {
                    sessions = syncMessage.getContent();
                    logger.debug("-------- GET SESSIONS ---------");
                    logger.debug(sessions);
                } else {
                    logger.debug("-------- SOME OTHER MESSAGE ---------");
                    logger.debug(syncMessage.toString());
                }
            }
        });  
    }

    
    @After
    public void wipeAss() {
        logger.debug("================ Cleaning up after tests");
        currentSession = null;
        sessions = null;
    }
    
    
    //@Test
    public void testInit() {
        assertNotNull(props);
        assertNotNull(tbi);
        assertNotNull(dataSyncService);
    }
    
    
    //@Test
    public void testCreateSessionAndGetSessions() {        
        dataSyncService.createSession(HARD_CODED_TOOL_NAME, HARD_CODED_USER_NAME);
        try {
            Thread.sleep(2000);
            assertNotNull(currentSession);
            assertTrue(currentSession.length() > 19);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        ISyncMessage syncMessage = SyncMessageHelper.createSyncMessageWithDefaultExp(null, HARD_CODED_TOOL_NAME, HARD_CODED_USER_NAME, HARD_CODED_USER_NAME, null, props.clientEventGetSessions, null);
        assertNotNull(syncMessage);
        dataSyncService.sendMessage((SyncMessage) syncMessage);
        try {
            Thread.sleep(2000);
            assertNotNull(sessions);
            assertTrue(sessions.length() > 19);
        } catch (InterruptedException e2) {
            e2.printStackTrace();
        }
    }

}
