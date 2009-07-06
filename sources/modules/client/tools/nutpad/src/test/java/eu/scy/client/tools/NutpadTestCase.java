package eu.scy.client.tools;

import roolo.elo.api.IMetadataKey;
import eu.scy.communications.message.ISyncMessage;
import eu.scy.communications.message.impl.SyncMessage;
import eu.scy.communications.message.impl.SyncMessageHelper;
import eu.scy.datasync.CommunicationProperties;
import eu.scy.datasync.api.DataSyncException;
import eu.scy.datasync.api.IDataSyncModule;
import eu.scy.datasync.api.session.IDataSyncSession;
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

    private IDataSyncSession dataSyncSession;
    private ToolBrokerImpl<IMetadataKey> tbi;
    private IDataSyncService dataSyncService;
    CommunicationProperties props;
    
    public NutpadTestCase() {        
    }
    
    private void init() {
        IDataSyncModule dataSyncModule = null;
        try {
            dataSyncModule = DataSyncModuleFactory.getDataSyncModule(DataSyncModuleFactory.LOCAL_STYLE);
        } catch (DataSyncException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        dataSyncSession = dataSyncModule.createSession(TEST_TOOL_ID, TEST_FROM);
        tbi = new ToolBrokerImpl<IMetadataKey>();
        dataSyncService = tbi.getDataSyncService();
        props = new CommunicationProperties();
    }
    
    @org.junit.Test
    public void testSendMessage() {
        init();
        ISyncMessage syncMessage = SyncMessageHelper.createSyncMessage(TEST_TOOL_SESSION_ID, TEST_TOOL_ID, TEST_FROM,TEST_TO, TEST_CONTENT, TEST_EVENT, null, SyncMessage.DEFAULT_MESSAGE_EXPIRATION_TIME);
        dataSyncService.sendMessage((SyncMessage) syncMessage);
    }
}
