package eu.scy.datasync.client;

import static org.junit.Assert.*;

import org.apache.log4j.Logger;
import org.hibernate.validator.AssertTrue;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import roolo.elo.api.IMetadataKey;
import eu.scy.communications.datasync.event.IDataSyncEvent;
import eu.scy.communications.datasync.event.IDataSyncListener;
import eu.scy.communications.datasync.properties.CommunicationProperties;
import eu.scy.communications.message.ISyncMessage;
import eu.scy.communications.message.impl.SyncMessage;
import eu.scy.communications.message.impl.SyncMessageHelper;
import eu.scy.toolbroker.ToolBrokerImpl;
import eu.scy.common.configuration.Configuration;

/**
 * Testing the client side of data sync. 
 * 
 * These tests need openfire running with scyhub external component
 * 
 * @author thomasd
 *
 */

public class DataSyncServiceTestCase {
    
//    private final static Logger logger = Logger.getLogger(DataSyncServiceTestCase.class.getName());
//    private static final String HARD_CODED_TOOL_NAME = "eu.scy.client.tools.dataSyncServiceTestCase";
//    private static final String HARD_CODED_USER_NAME = "obama";
//    private static final String HARD_CODED_PASSWORD = "obama";
//    private static final String HARD_CODED_CONTENT = "the lazy brown tortoise crawled under the quick black chicken, who had pox";
//    
//    private IDataSyncService dataSyncService;
//    private ToolBrokerImpl<IMetadataKey> tbi;
//    private String currentSession;
//    private String sessions;
//    private String synchronizedData = "";
//    
//    private Configuration props;
//    private String joinSessionId;
//    
//    @Ignore
//    public void init() {
//        logger.debug("================ Setting up stuff before the tests can begin");
//
//        props = Configuration.getInstance();
//        tbi = new ToolBrokerImpl<IMetadataKey>();
//        dataSyncService = tbi.getDataSyncService();
//        dataSyncService.init(tbi.getConnection(HARD_CODED_USER_NAME, HARD_CODED_PASSWORD));
//        dataSyncService.addDataSyncListener(createListener());  
//    }
//
//    @Ignore
//    protected IDataSyncListener createListener() {
//        return new IDataSyncListener() {
//
//            @Override
//            public void handleDataSyncEvent(IDataSyncEvent e) {
//                ISyncMessage syncMessage = e.getSyncMessage();
//               
//                if (syncMessage.getEvent().equals(props.getClientEventCreateSession())) {
//                    logger.debug("-------- CREATE SESSION ---------");
//                    //write to global field so the test can see if anything happened
//                    currentSession = syncMessage.getToolSessionId();                    
//                    logger.debug(syncMessage.toString());
//                } else if (syncMessage.getEvent().equals(props.getClientEventGetSessions())) {
//                    logger.debug("-------- GET SESSIONS ---------");
//                    //write to global field so the test can see if anything happened
//                    sessions = syncMessage.getContent();
//                    logger.debug(sessions);
//                } else if (syncMessage.getEvent().equals(props.getClientEventCreateData())) {
//                    logger.debug("-------- CREATE DATA ---------");
//                    //write to global field so the test can see if anything happened
//                    logger.debug(syncMessage.getContent());
//                } else if (syncMessage.getEvent().equals(props.getClientEventCreateData())) {
//                    logger.debug("-------- DATA SYNCHRONIZATION ---------");
//                    //write to global field so the test can see if anything happened
//                    synchronizedData = synchronizedData + syncMessage.getContent();
//                    logger.debug(synchronizedData);
//                } else if(syncMessage.getEvent().equals(props.getClientEventJoinSession())) {
//                    logger.debug("-------- JOIN SESSION ---------");
//                    //write to global field so the test can see if anything happened
//                    joinSessionId = syncMessage.getToolSessionId();
//                    logger.debug(synchronizedData);
//                } else {
//                    logger.debug("-------- SOME OTHER MESSAGE ---------");
//                    logger.debug("Incoming message: \n" + syncMessage.toString());
//                }
//            }
//        };
//    }
//    
//    //@Test
//    @Ignore
//    public void dummy() {}
//    
//    @Ignore
//    public void wipeAss() {
//        logger.debug("Cleaning up after tests");
//        currentSession = null;
//        sessions = null;
////        dataSyncService.disconnect();
////        dataSyncService = null;
////        tbi = null;
////        props = null;
//    }
//    
//    
//    //@Test'
//    @Ignore
//    public void testInit() {
//        assertNotNull(props);
//        assertNotNull(tbi);
//        assertNotNull(dataSyncService);
//    }
//    
//    
////    @Test
//    @Ignore
//    public void testCreateSessionAndGetSessions() {
//        dataSyncService.createSession(HARD_CODED_TOOL_NAME, HARD_CODED_USER_NAME);
//        try {
//            //wait for the session id to return
//            Thread.sleep(2000);
//            assertNotNull(currentSession);
//            assertTrue(currentSession.length() > 19);
//        } catch (InterruptedException e1) {
//            e1.printStackTrace();
//        }
//        ISyncMessage syncMessage = SyncMessageHelper.createSyncMessageWithDefaultExp(null, HARD_CODED_TOOL_NAME, HARD_CODED_USER_NAME, HARD_CODED_USER_NAME, null, props.getClientEventGetSessions(), null);
//        assertNotNull(syncMessage);
//        dataSyncService.sendMessage((SyncMessage) syncMessage);
//        try {
//            //wait for the message containing session ids to return
//            Thread.sleep(2000);
//            assertNotNull(sessions);
//            assertTrue(sessions.length() > 19);
//        } catch (InterruptedException e2) {
//            e2.printStackTrace();
//        }
//    }
//
//    
////    @Test
//    @Ignore
//    public void testDataSyncronization() {
//        dataSyncService.createSession(HARD_CODED_TOOL_NAME, HARD_CODED_USER_NAME);
//        try {
//            //wait for the session id to return
//            Thread.sleep(2000);
//        } catch (InterruptedException e1) {
//            e1.printStackTrace();
//        }
//        ISyncMessage syncMessage = SyncMessageHelper.createSyncMessageWithDefaultExp(currentSession, HARD_CODED_TOOL_NAME, HARD_CODED_USER_NAME, HARD_CODED_USER_NAME, HARD_CODED_CONTENT + " == 1", props.getClientEventCreateData(), null);
//        dataSyncService.sendMessage((SyncMessage) syncMessage);
//        syncMessage = SyncMessageHelper.createSyncMessageWithDefaultExp(currentSession, HARD_CODED_TOOL_NAME, HARD_CODED_USER_NAME, HARD_CODED_USER_NAME, HARD_CODED_CONTENT + " == 2", props.getClientEventCreateData(), null);
//        dataSyncService.sendMessage((SyncMessage) syncMessage);
//        syncMessage = SyncMessageHelper.createSyncMessageWithDefaultExp(currentSession, HARD_CODED_TOOL_NAME, HARD_CODED_USER_NAME, HARD_CODED_USER_NAME, HARD_CODED_CONTENT + " == 3", props.getClientEventCreateData(), null);
//        dataSyncService.sendMessage((SyncMessage) syncMessage);
//        try {
//            //wait for messages to reach the persistence layer
//            Thread.sleep(2000);
//        } catch (InterruptedException e1) {
//            e1.printStackTrace();
//        }
//        
//        //synchronize the data back to this client
//        syncMessage = SyncMessageHelper.createSyncMessageWithDefaultExp(currentSession, HARD_CODED_TOOL_NAME, HARD_CODED_USER_NAME, HARD_CODED_USER_NAME, null, props.getClientEventSynchronize(), null);
//        dataSyncService.sendMessage((SyncMessage) syncMessage);
//        try {
//            //wait for the sync to begin
//            Thread.sleep(5000);
//        } catch (InterruptedException e1) {
//            e1.printStackTrace();
//        }
//        
//        assertNotNull(synchronizedData);
//        assertTrue(synchronizedData.contains(HARD_CODED_CONTENT));
//        assertTrue(synchronizedData.contains(" == 1"));
//        assertTrue(synchronizedData.contains(" == 3"));
//        assertTrue(synchronizedData.contains(" == 3"));
//    }
//    
////    @Test
//    @Ignore
//    public void testJoinSession() {
//        dataSyncService.createSession(HARD_CODED_TOOL_NAME, HARD_CODED_USER_NAME);
//        try {
//            //wait for the session id to return
//            Thread.sleep(2000);
//        } catch (InterruptedException e1) {
//            e1.printStackTrace();
//        }
//        
//        //create a new user connection
//        String NEW_USER = "biden";
//        String NEW_PASSWORD = "biden";
//        ToolBrokerImpl<IMetadataKey> newTBI = new ToolBrokerImpl<IMetadataKey>();
//        IDataSyncService newDataSyncService = newTBI.getDataSyncService();
//        newDataSyncService.init(newTBI.getConnection(NEW_USER, NEW_PASSWORD));
//        newDataSyncService.addDataSyncListener(createListener());
//        
//        
//        
//        newDataSyncService.joinSession(currentSession,"funky tool", NEW_USER);
//        
//        try {
//            //wait for messages to reach the persistence layer
//            Thread.sleep(5000);
//        } catch (InterruptedException e1) {
//            e1.printStackTrace();
//        }
//        
//        
//        assertNotNull(joinSessionId);
//        assertEquals(currentSession, joinSessionId);
//    }
}
