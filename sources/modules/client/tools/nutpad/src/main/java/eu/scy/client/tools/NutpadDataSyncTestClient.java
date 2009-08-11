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

import org.apache.log4j.Logger;

import roolo.elo.api.IMetadataKey;
import eu.scy.actionlogging.api.IActionLogger;
import eu.scy.communications.datasync.event.IDataSyncEvent;
import eu.scy.communications.datasync.event.IDataSyncListener;
import eu.scy.communications.datasync.properties.CommunicationProperties;
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
    private Action openCSAction = new CreateSession();
    private Action clearEditAreaAction = new ClearEditAreaAction();
    private Action saveToDataSyncAction = new SaveToDataSyncAction();
    private Action sendXmppMessageAction = new SendXmppMessageAction();
    private Action getAllSessionsAction = new GetAllSessionsActions();
    private Action exitAction = new ExitAction();
    
    private IDataSyncService dataSyncService;
    private ArrayList<ISyncMessage> syncMessages;
    private String currentSession;
    
    private IActionLogger actionLogger;

    
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
        dataSyncService.init(tbi.getConnection(HARD_CODED_USER_NAME, HARD_CODED_PASSWORD));
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
            }
        });
        
        
        //action logging test
        actionLogger = tbi.getActionLogger();
    }
    
    
    class CreateSession extends AbstractAction {
        
        private static final long serialVersionUID = -5599432544551421021L;
        
        public CreateSession() {
            super("CREATE SESSION");
            logger.debug("sychronizing with data sync module");
            putValue(MNEMONIC_KEY, new Integer('2'));
        }
        
        public void actionPerformed(ActionEvent e) {
//            dataSyncService.createSession(HARD_CODED_TOOL_NAME, HARD_CODED_USER_NAME);
            
            eu.scy.actionlogging.logger.Action action = new eu.scy.actionlogging.logger.Action("create_nutpad_session", HARD_CODED_USER_NAME);
            action.addContext("tool", HARD_CODED_TOOL_NAME);
            action.addContext("status", "no session");
            action.addAttribute("sessionname", "Nutpad Session");
            
            actionLogger.log(HARD_CODED_USER_NAME, HARD_CODED_TOOL_NAME, action);
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
//            dataSyncService.sendMessage(syncMessage);
            
            eu.scy.actionlogging.logger.Action action = new eu.scy.actionlogging.logger.Action("send_message", HARD_CODED_USER_NAME);
            action.addContext("tool", HARD_CODED_TOOL_NAME);
            action.addContext("status", "in session");
            action.addAttribute("sessionname", "Nutpad Session");
            action.addAttribute("message", "english", "<text color=\"red\">" + messageStrings[4] + "</text>");
            
            actionLogger.log(HARD_CODED_USER_NAME, HARD_CODED_TOOL_NAME, action);
        }        
    }
    
}
