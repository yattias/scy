package eu.scy.collaborationservice;

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

import eu.scy.collaborationservice.dialog.ScyMessageCreateDialog;
import eu.scy.collaborationservice.event.ICollaborationServiceEvent;
import eu.scy.collaborationservice.event.ICollaborationServiceListener;
import eu.scy.collaborationservice.session.CollaborationSession;
import eu.scy.collaborationservice.session.ICollaborationSession;
import eu.scy.communications.message.IScyMessage;
import eu.scy.communications.message.impl.ScyMessage;

public class NutpadCollaborationServiceTestClient extends JFrame implements ICollaborationServiceListener {
    
    private static final long serialVersionUID = -7511012297227857853L;
    private final static Logger logger = Logger.getLogger(NutpadCollaborationServiceTestClient.class.getName());
    private static final String HARD_CODED_TOOL_NAME = "NUTPAD";
    private static final String HARD_CODED_USER_NAME = "thomasd";
    
    private JTextArea editArea;
    private Action openCSAction = new OpenFromCollaborationServiceAction();
    private Action clearEditAreaAction = new ClearEditAreaAction();
    private Action saveToCollaborationServiceAction = new SaveToCollaborationServiceAction();
    private Action exitAction = new ExitAction();
    
    private ICollaborationService collaborationService;
    private ICollaborationSession collaborationSession;
    private ArrayList<IScyMessage> scyMessages;
    
    
    public static void main(String[] args) {
        new NutpadCollaborationServiceTestClient();
    }
    

    public NutpadCollaborationServiceTestClient() {
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout(0, 0));

        // build the toolbar
        JToolBar toolBar = new JToolBar();
        toolBar.add(openCSAction);
        toolBar.add(saveToCollaborationServiceAction);
        toolBar.add(clearEditAreaAction);
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
        setPreferredSize(new Dimension(550, 650));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        
        this.initialize();
        
    }
    
    public void initialize() {    
        try {
            // init the collaboration service
            collaborationService = CollaborationServiceFactory.getCollaborationService(CollaborationServiceFactory.LOCAL_STYLE);
            // add listner in order to get callbacks on stuff that's happening
            collaborationService.addCollaborationListener(this);
            //create new session
            collaborationSession = collaborationService.createSession(HARD_CODED_TOOL_NAME, HARD_CODED_USER_NAME);
        } catch (CollaborationServiceException e) {
            logger.error("Failed to init collaboration service: " + e);
            e.printStackTrace();
        }        
    }
    
    class OpenFromCollaborationServiceAction extends AbstractAction {
        
        private static final long serialVersionUID = -5599432544551421021L;
        
        public OpenFromCollaborationServiceAction() {
            super("Synchronize with collab.service");
            logger.debug("sychronizing with collaboration service");
            putValue(MNEMONIC_KEY, new Integer('2'));
        }
        
        public void actionPerformed(ActionEvent e) {

            // get nutpad-specific messages which also belong to this session
            scyMessages = collaborationService.synchronizeClientState(HARD_CODED_USER_NAME, HARD_CODED_TOOL_NAME, collaborationSession.getId(), true);
            logger.debug("got " + scyMessages.size() + " messages for tool " + HARD_CODED_TOOL_NAME + " and session " + collaborationSession.getId());
            
            Date date = new java.util.Date(System.currentTimeMillis());            
            java.sql.Timestamp ts = new java.sql.Timestamp(date.getTime());
            
            // update textarea
            editArea.append("sychronizing..... " + ts + "\n");
            StringBuffer sb = new StringBuffer();
            for (IScyMessage scyMessage : scyMessages) {                
                sb.append("------ ScyMessage ------\n").append(scyMessage.toString() + "\n");
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
    
    class SaveToCollaborationServiceAction extends AbstractAction {
        
        private static final long serialVersionUID = 2570708232031173971L;
        
        SaveToCollaborationServiceAction() {
            super("Create and Send Message");
            putValue(MNEMONIC_KEY, new Integer('4'));
        }
        
        public void actionPerformed(ActionEvent e) {
            // create pop up            
            ScyMessageCreateDialog d = new ScyMessageCreateDialog(NutpadCollaborationServiceTestClient.this, HARD_CODED_USER_NAME, HARD_CODED_TOOL_NAME, "create", collaborationSession.getId());            
            String[] messageStrings = d.showDialog();
            
            // or create message
            IScyMessage scyMessage = ScyMessage.createScyMessage(messageStrings[0], messageStrings[1], messageStrings[2], messageStrings[3], messageStrings[4], messageStrings[5], messageStrings[6], messageStrings[7], messageStrings[8], CollaborationSession.DEFAULT_SESSION_EXPIRATION_TIME, messageStrings[10]);
            try {
                // pass scyMessage to collaboration service for storing
                collaborationService.create(scyMessage);
            } catch (CollaborationServiceException e1) {
                e1.printStackTrace();
            }
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
    
    @Override
    public void handleCollaborationServiceEvent(ICollaborationServiceEvent e) {
        IScyMessage scyMessage = e.getScyMessage();
        if (e.getScyMessage().getUserName() != null) {
            
            Date date = new java.util.Date(System.currentTimeMillis());
            java.sql.Timestamp ts = new java.sql.Timestamp(date.getTime());
            editArea.append("\n-------- new message --------- " + ts + "\n" + e.getScyMessage().toString());
            editArea.setCaretPosition(editArea.getText().length());
        }
        
    }
}
