package eu.scy.client.tools;

import static org.junit.Assert.*;
import roolo.elo.api.IMetadataKey;
import eu.scy.communications.datasync.properties.CommunicationProperties;
import eu.scy.communications.datasync.session.IDataSyncSession;
import eu.scy.communications.message.ISyncMessage;
import eu.scy.communications.message.impl.SyncMessage;
import eu.scy.communications.message.impl.SyncMessageHelper;
import eu.scy.datasync.api.DataSyncException;
import eu.scy.datasync.api.IDataSyncModule;
import eu.scy.datasync.client.IDataSyncService;
import eu.scy.datasync.impl.factory.DataSyncModuleFactory;
import eu.scy.toolbroker.ToolBrokerImpl;



public class NutpadTestCase {

    private static final String TEST_TOOL_SESSION_ID = "1234567890";
    private static final String TEST_TOOL_ID = "eu.scy.test." + NutpadTestCase.class.getName();
    private static final String TEST_FROM = "passerby@wiki.intermedia.uio.no";
    private static final String TEST_TO = "obama@wiki.intermedia.uio.no";
    private static final String TEST_CONTENT = "This is the content, but there isn't much.";
    private static final String TEST_EVENT = "test event";
    private static final String TEST_PERSISTENCE_ID = "1239999999";

    private static final String HARD_CODED_USER_NAME = "obama";
    private static final String HARD_CODED_PASSWORD = "obama";
    
    private IDataSyncSession dataSyncSession;
    private ToolBrokerImpl<IMetadataKey> tbi;
    private IDataSyncService dataSyncService;
    private static CommunicationProperties props = new CommunicationProperties();
    
    public NutpadTestCase() {        
    }
    
    private void init() {
        ToolBrokerImpl<IMetadataKey> tbi = new ToolBrokerImpl<IMetadataKey>();
        dataSyncService = tbi.getDataSyncService();
        dataSyncService.init(tbi.getConnection(HARD_CODED_USER_NAME, HARD_CODED_PASSWORD));
        dataSyncService.createSession("obama", "obama");
    }
    
    @org.junit.Test
    public void testInit() {
        init();
        assertTrue(props != null);
        assertTrue(dataSyncService != null);
    }
}
