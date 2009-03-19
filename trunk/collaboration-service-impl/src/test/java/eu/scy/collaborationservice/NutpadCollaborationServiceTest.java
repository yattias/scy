package eu.scy.collaborationservice;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.apache.log4j.Logger;

import eu.scy.collaborationservice.event.ICollaborationServiceEvent;
import eu.scy.collaborationservice.event.ICollaborationServiceListener;
import eu.scy.communications.message.IScyMessage;
import eu.scy.communications.message.impl.ScyMessage;

public class NutpadCollaborationServiceTest extends JFrame implements ICollaborationServiceListener {
    
    private static final long serialVersionUID = -7511012297227857853L;
    private final static Logger logger = Logger.getLogger(NutpadCollaborationServiceTest.class.getName());
    private static final String HARD_CODED_TOOL_NAME = "NUTPAD";
    private static final String SESSIONID = "NUTPAD_SESSION";
    
    private JTextArea editArea;
    private Action openCSAction = new OpenFromCollaborationServiceAction();
    private Action saveToCollaborationServiceAction = new SaveToCollaborationServiceAction();
    private Action exitAction = new ExitAction();
    
    private String documentSqlSpaceId = null;
    private boolean connected = false;
    private ICollaborationService collaborationService;
    private ArrayList<IScyMessage> scyMessages;
    private String toolId = Math.random() + "";
    
    public static void main(String[] args) {
        new NutpadCollaborationServiceTest();
    }
    
    public NutpadCollaborationServiceTest() {
        
        
        editArea = new JTextArea(15, 80);
        editArea.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        editArea.setFont(new Font("monospaced", Font.PLAIN, 14));
        JScrollPane scrollingText = new JScrollPane(editArea);
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        
        contentPanel.add(scrollingText, BorderLayout.CENTER);
        
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = menuBar.add(new JMenu("File"));
        fileMenu.setMnemonic('F');
        fileMenu.add(openCSAction);
        fileMenu.add(saveToCollaborationServiceAction);
        fileMenu.addSeparator();
        fileMenu.add(exitAction);
        
        setContentPane(contentPanel);
        setJMenuBar(menuBar);
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("NutPad");
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        
        this.initialize();
        
    }
    
    public void initialize() {
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
            
            //update textarea
            editArea.append("sychronizing.....\n");
            StringBuffer sb = new StringBuffer();
            for (IScyMessage scyMessage : scyMessages) {
               
                sb.append("Description: ").append(scyMessage.getDescription() +"\n");
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
            //pop up 
            //create message
            IScyMessage mess =ScyMessage.createScyMessage(toolId, HARD_CODED_TOOL_NAME, null, null, "",Math.random()+"", null, null, "create", 0, SESSIONID);
            try {
                collaborationService.create(mess);
            } catch (CollaborationServiceException e1) {
                // TODO Auto-generated catch block
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
        if( e.getScyMessage().getUserName() != null && !e.getScyMessage().getUserName().equals(toolId) ) {
            editArea.append("\n-------- new message ---------\n"+ e.getScyMessage().toString());
            
        }
        
    }
}
