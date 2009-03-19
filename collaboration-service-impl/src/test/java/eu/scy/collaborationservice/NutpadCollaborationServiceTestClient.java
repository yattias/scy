package eu.scy.collaborationservice;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;

import org.apache.log4j.Logger;

import eu.scy.collaborationservice.dialog.ScyMessageCreateDialog;
import eu.scy.collaborationservice.event.ICollaborationServiceEvent;
import eu.scy.collaborationservice.event.ICollaborationServiceListener;
import eu.scy.communications.message.IScyMessage;
import eu.scy.communications.message.impl.ScyMessage;

public class NutpadCollaborationServiceTestClient extends JFrame implements ICollaborationServiceListener {
    
    private static final long serialVersionUID = -7511012297227857853L;
    private final static Logger logger = Logger.getLogger(NutpadCollaborationServiceTestClient.class.getName());
    private static final String HARD_CODED_TOOL_NAME = "NUTPAD";
    private static final String SESSIONID = "NUTPAD_SESSION";
    
    private JTextArea editArea;
    private Action openCSAction = new OpenFromCollaborationServiceAction();
    private Action saveToCollaborationServiceAction = new SaveToCollaborationServiceAction();
    private Action exitAction = new ExitAction();
    
    private ICollaborationService collaborationService;
    private ArrayList<IScyMessage> scyMessages;
    private String userName;
    
    public static void main(String[] args) {
        new NutpadCollaborationServiceTestClient();
    }
    
    public NutpadCollaborationServiceTestClient() {
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout(0,0));
        
        JToolBar toolBar = new JToolBar();
        toolBar.add(openCSAction);
        toolBar.add(saveToCollaborationServiceAction);
        toolBar.addSeparator();
        toolBar.add(exitAction);
        
        
        editArea = new JTextArea(15, 80);
        editArea.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        editArea.setFont(new Font("monospaced", Font.PLAIN, 14));
        JScrollPane scrollingText = new JScrollPane(editArea);
        
     
        contentPanel.add(toolBar, BorderLayout.NORTH);
        contentPanel.add(scrollingText, BorderLayout.CENTER);
        
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = menuBar.add(new JMenu("File"));

        
        setContentPane(contentPanel);
        
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("$$ NutPad");
        setPreferredSize(new Dimension(550, 650));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        
        this.initialize();
        
    }
    
    public void initialize() {
        
        //generate UUID each instance of the tool will be unique username
        UUID uuid = UUID.randomUUID();

        userName = uuid.toString();
        
        try {
            collaborationService = CollaborationServiceFactory.getCollaborationService(CollaborationServiceFactory.LOCAL_STYLE);
        } catch (CollaborationServiceException e) {
            e.printStackTrace();
        }
        collaborationService.addCollaborationListener(this);
        
    }
    
    
    class OpenFromCollaborationServiceAction extends AbstractAction {
        
        private static final long serialVersionUID = -5599432544551421021L;
        
        public OpenFromCollaborationServiceAction() {
            super("sychronize w/cs");
            putValue(MNEMONIC_KEY, new Integer('2'));
        }
        
        public void actionPerformed(ActionEvent e) {
            scyMessages = collaborationService.synchronizeClientState("NUTPAD", SESSIONID);
            
            
            Date date = new java.util.Date(System.currentTimeMillis());

            java.sql.Timestamp ts = new java.sql.Timestamp(date.getTime());
            
            //update textarea
            editArea.append("sychronizing....." + ts + "\n");
            StringBuffer sb = new StringBuffer();
            for (IScyMessage scyMessage : scyMessages) {
               
                sb.append("------ Description: ").append(scyMessage.toString() +"\n");
                editArea.append(sb.toString());
            }
            
        }
    }
    
    class SaveToCollaborationServiceAction extends AbstractAction {
        
        private static final long serialVersionUID = 2570708232031173971L;
        
        SaveToCollaborationServiceAction() {
            super("Create and Send Message");
            putValue(MNEMONIC_KEY, new Integer('4'));
        }
        
        public void actionPerformed(ActionEvent e) {
            //create pop up 

            ScyMessageCreateDialog d = new ScyMessageCreateDialog(NutpadCollaborationServiceTestClient.this,userName, HARD_CODED_TOOL_NAME, "create", SESSIONID );
            
            String[] messageStrings = d.showDialog();

            
            //or create message
            IScyMessage mess =ScyMessage.createScyMessage(messageStrings[0], messageStrings[1], messageStrings[2], messageStrings[3], messageStrings[4],messageStrings[5], messageStrings[6], messageStrings[7], messageStrings[8], 0, messageStrings[10]);
            try {
                collaborationService.create(mess);
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
        if( e.getScyMessage().getUserName() != null ) {
            
            Date date = new java.util.Date(System.currentTimeMillis());

            java.sql.Timestamp ts = new java.sql.Timestamp(date.getTime());

            
            editArea.append("\n-------- new message --------- " + ts + "\n" + e.getScyMessage().toString());
            
        }
        
    }
}
