package eu.scy.client.tools;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;

import net.sf.sail.webapp.dao.authentication.impl.HibernateAclEntryDao;

import org.apache.log4j.Logger;

import roolo.elo.api.IMetadataKey;
import eu.scy.communications.datasync.event.IDataSyncEvent;
import eu.scy.communications.datasync.event.IDataSyncListener;
import eu.scy.communications.datasync.properties.CommunicationProperties;
import eu.scy.communications.datasync.session.IDataSyncSession;
import eu.scy.communications.message.ISyncMessage;
import eu.scy.communications.message.impl.SyncMessage;
import eu.scy.communications.message.impl.SyncMessageHelper;
import eu.scy.datasync.client.IDataSyncService;
import eu.scy.toolbroker.ToolBrokerImpl;




/**
 * Simple tool client used for testing tool broker and data sync.
 * 
 */
public class NutpadDataSyncTestClient extends JFrame{
    
    private static final long serialVersionUID = -7511012297227857853L;
    private final static Logger logger = Logger.getLogger(NutpadDataSyncTestClient.class.getName());
    private JTextArea editArea;
    private Action openCSAction = new OpenFromDataSyncAction();
    private Action clearEditAreaAction = new ClearEditAreaAction();
    private Action saveToDataSyncAction = new SaveToDataSyncAction();
    private Action sendXmppMessageAction = new SendXmppMessageAction();
    private Action getAllSessionsAction = new GetAllSessionsActions();
    private Action exitAction = new ExitAction();
    
    private IDataSyncSession dataSyncSession;
    private IDataSyncService dataSyncService;
    private ArrayList<ISyncMessage> syncMessages;
    private String currentSession;

    
    //init props
    private CommunicationProperties props  = new CommunicationProperties();
    
    private static final String HARD_CODED_TOOL_NAME = "eu.scy.client.tools.nutpad";
    private static final String HARD_CODED_USER_NAME = "obama";
    private static final String HARD_CODED_PASSWORD = "obama";
    
    public static void main(String[] args) {
        new NutpadDataSyncTestClient();
    }
    
    
    public NutpadDataSyncTestClient() {
        
        
        
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout(0, 0));
        
        // build the toolbar
        JToolBar toolBar = new JToolBar();
        toolBar.add(openCSAction);
        toolBar.add(saveToDataSyncAction);
        toolBar.add(clearEditAreaAction);
        toolBar.add(sendXmppMessageAction);
        toolBar.add(getAllSessionsAction);
        toolBar.addSeparator();
        toolBar.add(exitAction);
        
        // define props for the edit area
        editArea = new JTextArea(15, 80);
        editArea.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        editArea.setFont(new Font("monospaced", Font.PLAIN, 14));
        editArea.setEditable(false);
        JScrollPane scrollingText = new JScrollPane(editArea);
        
        contentPanel.add(toolBar, BorderLayout.NORTH);
        contentPanel.add(scrollingText, BorderLayout.CENTER);        
        setContentPane(contentPanel);        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("NutPad makes you happy");
        setPreferredSize(new Dimension(800, 650));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        
        this.initialize();        
    }
    
    
    public void initialize() {    
        // init the collaboration service
        // add listner in order to get callbacks on stuff that's happening
        ToolBrokerImpl<IMetadataKey> tbi = new ToolBrokerImpl<IMetadataKey>();
        dataSyncService = tbi.getDataSyncService();
        //create new session
        dataSyncService.init(tbi.getConnection(HARD_CODED_USER_NAME, HARD_CODED_PASSWORD));
        dataSyncService.createSession(HARD_CODED_TOOL_NAME, HARD_CODED_USER_NAME);
//        ISyncMessage queryAllSessions = new SyncMessage();
//        queryAllSessions.setFrom(HARD_CODED_USER_NAME);
//        queryAllSessions.setToolId(HARD_CODED_TOOL_NAME);
//        dataSyncService.getSessions(queryAllSessions);
        dataSyncService.addDataSyncListener( new IDataSyncListener() {

            @Override
            public void handleDataSyncEvent(IDataSyncEvent e) {
                ISyncMessage syncMessage = e.getSyncMessage();
                    Date date = new java.util.Date(System.currentTimeMillis());
                    java.sql.Timestamp ts = new java.sql.Timestamp(date.getTime());
                   
                    if( syncMessage.getEvent().equals(props.clientEventCreateSession) ) {
                        editArea.append("\n-------- CREATE SESSION --------- " + ts + "\n" + syncMessage.toString());
                        currentSession = syncMessage.getToolSessionId();
                        
                    } else if( syncMessage.getEvent().equals(props.clientEventGetSessions)) {
                        String content = syncMessage.getContent();
                        editArea.append("\n-------- GET SESSIONS --------- " + ts + "\n" + content);
                    } else {
                        editArea.append("\n-------- new message --------- " + ts + "\n" + syncMessage.toString());
                    }
                    editArea.setCaretPosition(editArea.getText().length());
                
            }});
       
    }
    
    
    
    
    class OpenFromDataSyncAction extends AbstractAction {
        
        private static final long serialVersionUID = -5599432544551421021L;
        
        public OpenFromDataSyncAction() {
            super("Synchronize with data sync module");
            logger.debug("sychronizing with data sync module");
            putValue(MNEMONIC_KEY, new Integer('2'));
        }
        
        public void actionPerformed(ActionEvent e) {
            
            // get nutpad-specific messages which also belong to this session
            //syncMessages = dataSyncModule.synchronizeClientState(HARD_CODED_USER_NAME, HARD_CODED_TOOL_NAME, dataSyncSession.getId(), true);
            
            Date date = new java.util.Date(System.currentTimeMillis());            
            java.sql.Timestamp ts = new java.sql.Timestamp(date.getTime());
            
            // update textarea
            editArea.append("sychronizing..... " + ts + "\n");
            StringBuffer sb = new StringBuffer();
            for (ISyncMessage syncMessage : syncMessages) {                
                sb.append("------ syncMessage ------\n").append(syncMessage.toString() + "\n");
                editArea.append(sb.toString());
                editArea.setCaretPosition(editArea.getText().length());
            }            
        }
    }
    
    
    class ClearEditAreaAction extends AbstractAction {
        
        private static final long serialVersionUID = -5599432544551421021L;
        
        public ClearEditAreaAction() {
            super("Clear edit area");
            putValue(MNEMONIC_KEY, new Integer('3'));
        }
        
        public void actionPerformed(ActionEvent e) {
            // clear textarea
            editArea.setText("");
        }
    }
    
    
    class SaveToDataSyncAction extends AbstractAction {
        
        private static final long serialVersionUID = 2570708232031173971L;
        
        SaveToDataSyncAction() {
            super("Create and Send Message");
            putValue(MNEMONIC_KEY, new Integer('4'));
        }
        
        public void actionPerformed(ActionEvent e) {
            // create pop up            
            SyncMessageCreateDialog d = new SyncMessageCreateDialog(NutpadDataSyncTestClient.this, HARD_CODED_USER_NAME, HARD_CODED_TOOL_NAME, props.clientEventSynchronize,currentSession);            
            String[] messageStrings = d.showDialog();
            ISyncMessage syncMessage = SyncMessageHelper.createSyncMessage(messageStrings[0], messageStrings[1], messageStrings[2], messageStrings[3], messageStrings[4], messageStrings[5], messageStrings[6], Long.parseLong(messageStrings[7].trim()));
            
            dataSyncService.sendMessage((SyncMessage) syncMessage);
//            try {
//                // pass syncMessage to DataSyncModule for storing
//                //dataSyncModule.create(syncMessage);
//                logger.debug("sync ok. impressive.");
//            } catch (DataSyncException e1) {
//                logger.error("failed to synchronize " + e1);
//                e1.printStackTrace();
//            }
        }
    }
    
    class GetAllSessionsActions extends AbstractAction {
        
        private static final long serialVersionUID = 2570708232031173971L;
        
        GetAllSessionsActions() {
            super("Get all sessions");
            putValue(MNEMONIC_KEY, new Integer('4'));
        }
        
        public void actionPerformed(ActionEvent e) {
            // create pop up            
            SyncMessageCreateDialog d = new SyncMessageCreateDialog(NutpadDataSyncTestClient.this, HARD_CODED_USER_NAME, HARD_CODED_TOOL_NAME, props.clientEventGetSessions,null);            
            String[] messageStrings = d.showDialog();
            ISyncMessage syncMessage = SyncMessageHelper.createSyncMessage(messageStrings[0], messageStrings[1], messageStrings[2], messageStrings[3], messageStrings[4], messageStrings[5], messageStrings[6], Long.parseLong(messageStrings[7].trim()));
            
            dataSyncService.sendMessage((SyncMessage) syncMessage);
//            try {
//                // pass syncMessage to DataSyncModule for storing
//                //dataSyncModule.create(syncMessage);
//                logger.debug("sync ok. impressive.");
//            } catch (DataSyncException e1) {
//                logger.error("failed to synchronize " + e1);
//                e1.printStackTrace();
//            }
        }
    }
    
    
    class ExitAction extends AbstractAction {
        
        private static final long serialVersionUID = -7603073618047398002L;
        
        public ExitAction() {
            super("Exit");
            putValue(MNEMONIC_KEY, new Integer('5'));
        }
        
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }
    
    
    class SendXmppMessageAction extends AbstractAction {
        
        private static final long serialVersionUID = 2570708232031173971L;
        
        SendXmppMessageAction() {
            super("Send Message XMPP");
            putValue(MNEMONIC_KEY, new Integer('6'));
        }
        
        public void actionPerformed(ActionEvent e) {
            // create pop up
            SyncMessageCreateDialog d = new SyncMessageCreateDialog(NutpadDataSyncTestClient.this, HARD_CODED_USER_NAME, HARD_CODED_TOOL_NAME, props.clientEventCreateData,currentSession);            
            String[] messageStrings = d.showDialog();
            SyncMessage syncMessage = (SyncMessage) SyncMessageHelper.createSyncMessage(currentSession, messageStrings[1], messageStrings[2], messageStrings[3], messageStrings[4], messageStrings[5], messageStrings[6],  Long.parseLong(messageStrings[7].trim()));
            dataSyncService.sendMessage(syncMessage);
        }        
        
        
        
//        public void actionPerformed(ActionEvent e) {
//            // create pop up            
//            SyncMessageCreateDialog d = new SyncMessageCreateDialog(NutpadDataSyncTestClient.this, HARD_CODED_USER_NAME, HARD_CODED_TOOL_NAME, "create", dataSyncSession.getId());            
//            String[] messageStrings = d.showDialog();
//                        
//            ConnectionConfiguration config = new ConnectionConfiguration("imediamac09.uio.no", new Integer("5222").intValue(), "imediamac09.uio.no");
//            config.setCompressionEnabled(true);
//            config.setSASLAuthenticationEnabled(true);
//            config.setReconnectionAllowed(true);
//            
//            final XMPPConnection xmppConnection = new XMPPConnection(config);
//            
//            xmppConnection.DEBUG_ENABLED = true;
//            try {
//                
//                xmppConnection.connect();
//                xmppConnection.addConnectionListener(new ConnectionListener() {
//                    
//                    @Override
//                    public void connectionClosed() {
//                        System.out.println("datasync server closed;");
//                        try {
//                            xmppConnection.connect();
//                        } catch (XMPPException e) {
//                            e.printStackTrace();
//                        }
//                        System.out.println("datasync server trying to reconnect;");
//                    }
//                    
//                    @Override
//                    public void connectionClosedOnError(Exception arg0) {
//                        System.out.println("datasync server error closed;");
//                    }
//                    
//                    @Override
//                    public void reconnectingIn(int arg0) {
//                        System.out.println("datasync server reconnecting;");
//                    }
//                    @Override
//                    public void reconnectionFailed(Exception arg0) {
//                        System.out.println("datasync server reconnecting failed");
//                    }
//                    @Override
//                    public void reconnectionSuccessful() {
//                        System.out.println("datasync server reconnectings success");
//                    }
//                });
//                                
//                xmppConnection.login("obama", "obama");
//                
//                SyncMessage syncMessage = (SyncMessage) SyncMessage.createSyncMessage("9908d583-9778-4915-9142-4be7d5c89516", messageStrings[1], messageStrings[2], messageStrings[3], messageStrings[4], messageStrings[5], Long.parseLong(messageStrings[6].trim()));
//                
//                Message smackMessage = syncMessage.convertToXMPPMessage();                
//                smackMessage.addExtension((PacketExtension) new DataSyncPacketExtension(syncMessage));
//                smackMessage.setFrom("obama@imediamac09.uio.no");
//                smackMessage.setTo("scyhub.imediamac09.uio.no");
//                xmppConnection.sendPacket(smackMessage);
//
//            } catch (XMPPException xe) {
//                logger.error("Error during connect");
//                xe.printStackTrace();
//            }
//        }
    }
    
}
