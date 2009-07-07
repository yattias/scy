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
 * These tests need openfire running with scyhub external component
 * 
 * @author thomasd
 *
 */

public class DataSyncServiceTestCase {
    
    private final static Logger logger = Logger.getLogger(DataSyncServiceTestCase.class.getName());
    private static final String HARD_CODED_TOOL_NAME = "eu.scy.client.tools.dataSyncServiceTestCase";
    private static final String HARD_CODED_USER_NAME = "obama";
    private static final String HARD_CODED_PASSWORD = "obama";
    private static final String HARD_CODED_CONTENT = "the lazy brown tortoise crawled under the quick black chicken, who had pox";
    
    private IDataSyncService dataSyncService;
    private ToolBrokerImpl<IMetadataKey> tbi;
    private String currentSession;
    private String sessions;
    private String synchronizedData;
    
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
                    //write to global field so the test can see if anything happened
                    currentSession = syncMessage.getToolSessionId();                    
                    logger.debug(syncMessage.toString());
                } else if (syncMessage.getEvent().equals(props.clientEventGetSessions)) {
                    logger.debug("-------- GET SESSIONS ---------");
                    //write to global field so the test can see if anything happened
                    sessions = syncMessage.getContent();
                    logger.debug(sessions);
                } else if (syncMessage.getEvent().equals(props.clientEventSynchronize)) {
                    logger.debug("-------- DATA SYNCHRONIZATION ---------");
                    //write to global field so the test can see if anything happened
                    synchronizedData = syncMessage.getContent();
                    logger.debug(synchronizedData);
                } else {
                    logger.debug("-------- SOME OTHER MESSAGE ---------");
                    logger.debug("Incoming message: \n" + syncMessage.toString());
                }
            }
        });  
    }

    
    @After
    public void wipeAss() {
        logger.debug("================ Cleaning up after tests");
        currentSession = null;
        sessions = null;
//        dataSyncService.disconnect();
//        dataSyncService = null;
//        tbi = null;
//        props = null;
    }
    
    
    //@Test'
    public void testInit() {
        assertNotNull(props);
        assertNotNull(tbi);
        assertNotNull(dataSyncService);
    }
    
    
    @Test
    public void testCreateSessionAndGetSessions() {
        dataSyncService.createSession(HARD_CODED_TOOL_NAME, HARD_CODED_USER_NAME);
        try {
            //wait for the session id to return
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
            //wait for the message containing session ids to return
            Thread.sleep(2000);
            assertNotNull(sessions);
            assertTrue(sessions.length() > 19);
        } catch (InterruptedException e2) {
            e2.printStackTrace();
        }
    }

    
    @Test
    public void testDataSyncronization() {
        dataSyncService.createSession(HARD_CODED_TOOL_NAME, HARD_CODED_USER_NAME);
        try {
            //wait for the session id to return
            Thread.sleep(2000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        ISyncMessage syncMessage = SyncMessageHelper.createSyncMessageWithDefaultExp(currentSession, HARD_CODED_TOOL_NAME, HARD_CODED_USER_NAME, HARD_CODED_USER_NAME, HARD_CODED_CONTENT + " == 1", props.clientEventCreateData, null);
        dataSyncService.sendMessage((SyncMessage) syncMessage);
        syncMessage = SyncMessageHelper.createSyncMessageWithDefaultExp(currentSession, HARD_CODED_TOOL_NAME, HARD_CODED_USER_NAME, HARD_CODED_USER_NAME, HARD_CODED_CONTENT + " == 2", props.clientEventCreateData, null);
        dataSyncService.sendMessage((SyncMessage) syncMessage);
        syncMessage = SyncMessageHelper.createSyncMessageWithDefaultExp(currentSession, HARD_CODED_TOOL_NAME, HARD_CODED_USER_NAME, HARD_CODED_USER_NAME, HARD_CODED_CONTENT + " == 3", props.clientEventCreateData, null);
        dataSyncService.sendMessage((SyncMessage) syncMessage);
        try {
            //wait for messages to reach the persistence layer
            Thread.sleep(2000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        
        //synchronize the data back to this client
        syncMessage = SyncMessageHelper.createSyncMessageWithDefaultExp(currentSession, HARD_CODED_TOOL_NAME, HARD_CODED_USER_NAME, HARD_CODED_USER_NAME, null, props.clientEventSynchronize, null);
        dataSyncService.sendMessage((SyncMessage) syncMessage);
        try {
            //wait for the sync to begin
            Thread.sleep(2000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
//        assertNotNull(synchronizedData);
//        assertTrue(synchronizedData.contains(HARD_CODED_CONTENT));
//        assertTrue(synchronizedData.contains(" == 1"));
//        assertTrue(synchronizedData.contains(" == 3"));
//        assertTrue(synchronizedData.contains(" == 3"));
    }
}
