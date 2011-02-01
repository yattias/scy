package eu.scy.client.tools;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;

import org.apache.log4j.Logger;

import roolo.elo.api.IMetadataKey;
import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.actionlogging.api.IActionLogger;
import eu.scy.actionlogging.logger.ActionLogger;
import eu.scy.client.common.datasync.IDataSyncService;
import eu.scy.client.common.datasync.ISyncListener;
import eu.scy.client.common.datasync.ISyncSession;
import eu.scy.common.configuration.Configuration;
import eu.scy.common.datasync.ISyncObject;
import eu.scy.common.datasync.SyncObject;
import eu.scy.communications.message.ISyncMessage;
import eu.scy.notification.api.INotifiable;
import eu.scy.notification.api.INotification;
import eu.scy.toolbroker.ToolBrokerImpl;
import eu.scy.client.common.datasync.DataSyncException;


/**
 * Simple tool client used for testing tool broker and data sync.
 * 
 */
public class NutpadDataSyncTestClient extends JFrame{
    
    private static final long serialVersionUID = -7511012297227857853L;
    private final static Logger logger = Logger.getLogger(NutpadDataSyncTestClient.class.getName());
    private JTextArea editArea;
    private ActionLogger actionLogger;
    private Action openCSAction = new CreateSession();
    private Action joinCSAction = new JoinSession();
//    private Action clearEditAreaAction = new ClearEditAreaAction();
//    private Action saveToDataSyncAction = new SaveToDataSyncAction();
//    private Action sendXmppMessageAction = new SendXmppMessageAction();
//    private Action getAllSessionsAction = new GetAllSessionsActions();
    private Action exitAction = new ExitAction();
    private Action createSyncObjectAction = new CreateSyncObject();
    private Action changeSyncObjectAction = new ChangeSyncObject();
    private Action removeSyncObjectAction = new RemoveSyncObject();
    private ISyncListener listener = new SyncListener(); 
    
    private IDataSyncService dataSyncService;
    private ArrayList<ISyncMessage> syncMessages;
    private String currentSession;
    
    
    private ISyncSession syncSession;
    
    private SyncObject syncObject;

    private Configuration props = Configuration.getInstance();
    
    private static final String HARD_CODED_TOOL_NAME = "eu.scy.client.tools.nutpad";
//    private static final String HARD_CODED_USER_NAME = "obama";
//    private static final String HARD_CODED_PASSWORD = "obama";
    private static String HARD_CODED_USER_NAME = "merkel";
    private static String HARD_CODED_PASSWORD = "merkel";
    private static final String HARD_CODED_MISSION_NAME = "nutpad mission";
    
    
    public static void main(String[] args) {
    	if(args.length == 2) {
    		HARD_CODED_USER_NAME = args[0];
    		HARD_CODED_PASSWORD = args[1];
    	}
        new NutpadDataSyncTestClient();
    }
    
    
    public NutpadDataSyncTestClient() {
        
    	// System.out.println(Configuration.getInstance());
    	
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout(0, 0));
        
        // build the toolbar
        JToolBar toolBar = new JToolBar();
        toolBar.add(openCSAction);
        toolBar.add(joinCSAction);
        toolBar.add(createSyncObjectAction);
        toolBar.add(changeSyncObjectAction);
        toolBar.add(removeSyncObjectAction);
        
        changeSyncObjectAction.setEnabled(false);
		removeSyncObjectAction.setEnabled(false);
//        toolBar.add(saveToDataSyncAction);
//        toolBar.add(clearEditAreaAction);
//        toolBar.add(sendXmppMessageAction);
//        toolBar.add(getAllSessionsAction);
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
        setTitle("NutPad makes " + HARD_CODED_USER_NAME + " happy!");
        setPreferredSize(new Dimension(800, 650));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        
        this.initialize();        
    }
    
    
    public void initialize() {    
        // init the collaboration service
        // add listner in order to get callbacks on stuff that's happening
        ToolBrokerImpl tbi = new ToolBrokerImpl(HARD_CODED_USER_NAME, HARD_CODED_PASSWORD);
        dataSyncService = tbi.getDataSyncService();
        
        tbi.registerForNotifications(new INotifiable() {
		
			@Override
			public void processNotification(INotification notification) {
				JOptionPane.showMessageDialog(NutpadDataSyncTestClient.this, notification.getFirstProperty("message"), "Notification from " + notification.getSender(), JOptionPane.INFORMATION_MESSAGE);
			}
		});
        
//        dataSyncService.addDataSyncListener( new IDataSyncListener() {
//
//            @Override
//            public void handleDataSyncEvent(IDataSyncEvent e) {
//            	// System.out.println("handlesyncevent");
//                ISyncMessage syncMessage = e.getSyncMessage();
//                Date date = new java.util.Date(System.currentTimeMillis());
//                java.sql.Timestamp ts = new java.sql.Timestamp(date.getTime());
//               
//                if( syncMessage.getEvent().equals(props.getClientEventCreateSession()) ) {
//                    editArea.append("\n-------- CREATE SESSION --------- " + ts + "\n" + syncMessage.toString());
//                    currentSession = syncMessage.getToolSessionId();                    
//                } else if( syncMessage.getEvent().equals(props.getClientEventGetSessions())) {
//                    String content = syncMessage.getContent();
//                    editArea.append("\n-------- GET SESSIONS --------- " + ts + "\n" + content);
//                } else {
//                    editArea.append("\n-------- new message --------- " + ts + "\n" + syncMessage.toString());
//                }
//                editArea.setCaretPosition(editArea.getText().length());             
//            }
//        });
        
        
        //action logging test
        actionLogger = (ActionLogger) tbi.getActionLogger();
        // TODO: this should be done inside the TBI
        //((ActionLogger)actionLogger).init(tbi.getConnection(HARD_CODED_USER_NAME, HARD_CODED_PASSWORD));
    }
    
    
    class SyncListener implements ISyncListener {
    	@Override
		public void syncObjectRemoved(ISyncObject syncObject) {
			editArea.append("SyncObject removed remotely: ");
			editArea.append("id = " + syncObject.getID());
			editArea.append(", user = " + syncObject.getCreator());
			editArea.append(", creation timestamp = " + syncObject.getCreationTime());
			editArea.append(", toolname = " + syncObject.getToolname());
			if(syncObject.getProperties() != null) {
				for (String key : syncObject.getProperties().keySet()) {
					editArea.append(", " + key + " = " + syncObject.getProperty(key));
				}
			}
			editArea.append("\n");
		}
	
		@Override
		public void syncObjectChanged(ISyncObject syncObject) {
			editArea.append("SyncObject changed remotely: ");
			editArea.append("id = " + syncObject.getID());
			editArea.append(", user = " + syncObject.getCreator());
			editArea.append(", creation timestamp = " + syncObject.getCreationTime());
			editArea.append(", toolname = " + syncObject.getToolname());
			if(syncObject.getProperties() != null) {
				for (String key : syncObject.getProperties().keySet()) {
					editArea.append(", " + key + " = " + syncObject.getProperty(key));
				}
			}
			editArea.append("\n");
		}
	
		@Override
		public void syncObjectAdded(ISyncObject syncObject) {
			editArea.append("SyncObject created remotely: ");
			editArea.append("id = " + syncObject.getID());
			editArea.append(", user = " + syncObject.getCreator());
			editArea.append(", creation timestamp = " + syncObject.getCreationTime());
			editArea.append(", toolname = " + syncObject.getToolname());
			if(syncObject.getProperties() != null) {
				for (String key : syncObject.getProperties().keySet()) {
					editArea.append(", " + key + " = " + syncObject.getProperty(key));
				}
			}
			editArea.append("\n");
		}
    }
    
    class CreateSession extends AbstractAction {
        
        private static final long serialVersionUID = -5599432544551421021L;
        
        public CreateSession() {
            super("CREATE SESSION");
            logger.debug("sychronizing with data sync module");
            putValue(MNEMONIC_KEY, new Integer('2'));
        }
        
        public void actionPerformed(ActionEvent e) {
        	// System.out.println("create session");
            try {
				syncSession = dataSyncService.createSession(listener);
				if(syncSession == null) {
					JOptionPane.showMessageDialog(NutpadDataSyncTestClient.this.rootPane.getParent(), "Could not create session!", "Error", JOptionPane.ERROR_MESSAGE);
				} else {
					editArea.append("Session \"" + syncSession.getId() + "\" created!\n");
					eu.scy.actionlogging.Action action = new eu.scy.actionlogging.Action();
					action.setType("create_nutpad_session");
					action.addContext(ContextConstants.tool, HARD_CODED_TOOL_NAME);
					action.addContext(ContextConstants.mission, "CO2-House");
					action.addContext(ContextConstants.session, syncSession.getId());
					action.addAttribute("sessionname", syncSession.getId());
					
					actionLogger.log(action);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
        }
    }
    
    class  JoinSession extends AbstractAction {
    	
		public JoinSession() {
			super("Join Session");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				syncSession = dataSyncService.joinSession("datasync@syncsessions.scy.collide.info", listener);				
				editArea.append("Session \"" + syncSession.getId() + "\" joined!\n");
				
				eu.scy.actionlogging.Action action = new eu.scy.actionlogging.Action();
				action.setUser(HARD_CODED_USER_NAME);
				action.setType("join_nutpad_session");
				action.addContext(ContextConstants.tool, HARD_CODED_TOOL_NAME);
				action.addAttribute("sessionname", syncSession.getId());
				
				actionLogger.log(action);
			}
			catch(DataSyncException dse) {
				// System.out.println("DataSyncException: "+dse);
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
            
            eu.scy.actionlogging.Action action = new eu.scy.actionlogging.Action();
            action.setUser(HARD_CODED_USER_NAME);
            action.setType("clear edit area");
            action.addContext(ContextConstants.tool, HARD_CODED_TOOL_NAME);
            action.addContext(ContextConstants.mission, "CO2-House");
			action.addContext(ContextConstants.session, syncSession.getId());
            
            actionLogger.log(action);
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
//            SyncMessageCreateDialog d = new SyncMessageCreateDialog(NutpadDataSyncTestClient.this, HARD_CODED_USER_NAME, HARD_CODED_TOOL_NAME, props.getClientEventSynchronize(),currentSession);            
//            String[] messageStrings = d.showDialog();
//            ISyncMessage syncMessage = SyncMessageHelper.createSyncMessage(messageStrings[0], messageStrings[1], messageStrings[2], messageStrings[3], messageStrings[4], messageStrings[5], messageStrings[6], Long.parseLong(messageStrings[7].trim()));
//            
//            dataSyncService.sendMessage((SyncMessage) syncMessage);
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
//            SyncMessageCreateDialog d = new SyncMessageCreateDialog(NutpadDataSyncTestClient.this, HARD_CODED_USER_NAME, HARD_CODED_TOOL_NAME, props.getClientEventGetSessions(),null);            
//            String[] messageStrings = d.showDialog();
//            ISyncMessage syncMessage = SyncMessageHelper.createSyncMessage(messageStrings[0], messageStrings[1], messageStrings[2], messageStrings[3], messageStrings[4], messageStrings[5], messageStrings[6], Long.parseLong(messageStrings[7].trim()));
//            
//            dataSyncService.sendMessage((SyncMessage) syncMessage);
        }
    }
    
    
    class ExitAction extends AbstractAction {
        
        private static final long serialVersionUID = -7603073618047398002L;
        
        public ExitAction() {
            super("Exit");
            putValue(MNEMONIC_KEY, new Integer('5'));
        }
        
        public void actionPerformed(ActionEvent e) {
        	eu.scy.actionlogging.Action action = new eu.scy.actionlogging.Action();
            action.setUser(HARD_CODED_USER_NAME);
            action.setType("exit_nutpad_session");
            action.addContext(ContextConstants.tool, HARD_CODED_TOOL_NAME);
            action.addContext(ContextConstants.mission, "CO2-House");
            if(syncSession != null) {
            	action.addContext(ContextConstants.session, syncSession.getId());
            } else {
				action.addContext(ContextConstants.session, "nosession");
            }
            
            actionLogger.log(action);
            
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
//            SyncMessageCreateDialog d = new SyncMessageCreateDialog(NutpadDataSyncTestClient.this, HARD_CODED_USER_NAME, HARD_CODED_TOOL_NAME, props.getClientEventCreateData(),currentSession);            
//            String[] messageStrings = d.showDialog();
//            SyncMessage syncMessage = (SyncMessage) SyncMessageHelper.createSyncMessage(currentSession, messageStrings[1], messageStrings[2], messageStrings[3], messageStrings[4], messageStrings[5], messageStrings[6],  Long.parseLong(messageStrings[7].trim()));
//            dataSyncService.sendMessage(syncMessage);
//            
//            eu.scy.actionlogging.logger.Action action = new eu.scy.actionlogging.logger.Action("send_message", HARD_CODED_USER_NAME);
//            action.addContext("tool", HARD_CODED_TOOL_NAME);
//            action.addContext("status", "in session");
//            action.addAttribute("sessionname", "Nutpad Session");
//            action.addAttribute("message", "english", "<text color=\"red\">" + messageStrings[4] + "</text>");
//            
//            actionLogger.log(HARD_CODED_USER_NAME, HARD_CODED_TOOL_NAME, action);
        }        
    }
    
    class CreateSyncObject extends AbstractAction {
    	
		public CreateSyncObject() {
			super("Create Sync Object");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			syncObject = new SyncObject();
			syncObject.setToolname(HARD_CODED_TOOL_NAME);
			syncObject.setProperty("random", Double.toString(Math.random()));
			syncSession.addSyncObject(syncObject);
			editArea.append("SyncObject locally created: " + syncObject + "\n");
			changeSyncObjectAction.setEnabled(true);
    		removeSyncObjectAction.setEnabled(true);
    		
    		eu.scy.actionlogging.Action action = new eu.scy.actionlogging.Action();
            action.setUser(HARD_CODED_USER_NAME);
            action.setType("create object");
            action.addContext(ContextConstants.tool, HARD_CODED_TOOL_NAME);
            action.addContext(ContextConstants.mission, "CO2-House");
			action.addContext(ContextConstants.session, syncSession.getId());
            action.addAttribute("sessionname", syncSession.getId());
            
            actionLogger.log(action);
		}
    	
    }
    
    class ChangeSyncObject extends AbstractAction {
    	
    	public ChangeSyncObject() {
    		super("Change Sync Object");
    	}
    	
    	@Override
    	public void actionPerformed(ActionEvent e) {
    		syncObject.setProperty("random", Double.toString(Math.random()));
    		syncSession.changeSyncObject(syncObject);
    		editArea.append("SyncObject locally changed: " + syncObject + "\n");
    		
    		eu.scy.actionlogging.Action action = new eu.scy.actionlogging.Action();
            action.setUser(HARD_CODED_USER_NAME);
            action.setType("change object");
            action.addContext(ContextConstants.tool, HARD_CODED_TOOL_NAME);
            action.addContext(ContextConstants.mission, "CO2-House");
			action.addContext(ContextConstants.session, syncSession.getId());
            action.addAttribute("sessionname", syncSession.getId());
            
            actionLogger.log(action);
    	}
    	
    }
    
    class RemoveSyncObject extends AbstractAction {
    	
    	public RemoveSyncObject() {
    		super("Remove Sync Object");
    	}
    	
    	@Override
    	public void actionPerformed(ActionEvent e) {
    		syncSession.removeSyncObject(syncObject);
    		editArea.append("SyncObject locally deleted: " + syncObject + "\n");
    		changeSyncObjectAction.setEnabled(false);
    		removeSyncObjectAction.setEnabled(false);
    		
    		eu.scy.actionlogging.Action action = new eu.scy.actionlogging.Action();
            action.setUser(HARD_CODED_USER_NAME);
            action.setType("remove object");
            action.addContext(ContextConstants.tool, HARD_CODED_TOOL_NAME);
            action.addContext(ContextConstants.mission, "CO2-House");
			action.addContext(ContextConstants.session, syncSession.getId());
            action.addAttribute("sessionname", syncSession.getId());
            
            actionLogger.log(action);
    	}
    	
    }
    
}
