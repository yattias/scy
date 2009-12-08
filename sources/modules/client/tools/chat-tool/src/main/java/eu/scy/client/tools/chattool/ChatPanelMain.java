package eu.scy.client.tools.chattool;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.UIManager.LookAndFeelInfo;

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;
import org.jdesktop.swingx.JXTitledPanel;

import eu.scy.awareness.AwarenessUser;
import eu.scy.awareness.IAwarenessService;
import eu.scy.awareness.IAwarenessUser;
import eu.scy.awareness.event.IAwarenessEvent;
import eu.scy.awareness.event.IAwarenessMessageListener;
import eu.scy.awareness.tool.IChatPresenceToolEvent;
import eu.scy.awareness.tool.IChatPresenceToolListener;
import eu.scy.chat.controller.ChatController;
import eu.scy.toolbroker.ToolBrokerImpl;

/**
 * @author jeremyt
 *
 */
public class ChatPanelMain extends JPanel {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(ChatPanelMain.class.getName());

	private JTextPane chatArea;
	private static ChatPanelMain cmp;
	private JXTitledPanel chatAreaPanel;
	protected DefaultListModel buddlyListModel;
	protected JTextField sendMessageTextField;
	protected ChatController chatController;
	private IAwarenessService awarenessService;
	private DefaultListModel model = new DefaultListModel();

	
	public ChatPanelMain(IAwarenessService awarenessService) {
		this.awarenessService = awarenessService;

		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (UnsupportedLookAndFeelException e) {
			logger.error("ChatPanelMain: UnsupportedLookAndFeelException: "+e);
		} catch (ClassNotFoundException e) {
			logger.error("ChatPanelMain: ClassNotFoundException: "+e);
		} catch (InstantiationException e) {
			logger.error("ChatPanelMain: InstantiationException: "+e);
		} catch (IllegalAccessException e) {
			logger.error("ChatPanelMain: IllegalAccessException: "+e);
		}      

	
		chatController = new ChatController(awarenessService);
		chatController.populateBuddyList();
		initGUI();
	}
	
	
	protected void initGUI() {
		chatArea = new JTextPane();
		chatArea.setEditable(false);
		this.registerChatArea(chatArea);
		this.add(createChatArea(), BorderLayout.CENTER);
	}

	
	protected JPanel createChatArea() {
		
		chatAreaPanel = new JXTitledPanel("The mighty scy-chat");
		chatAreaPanel.setLayout(new MigLayout("wrap 1"));
		JScrollPane chatAreaScroll = new JScrollPane(chatArea);
		chatAreaScroll.setPreferredSize(new Dimension(225, 250));
		chatAreaPanel.add(chatAreaScroll);
		
		sendMessageTextField = new JTextField();
		sendMessageTextField.setEditable(true);
		sendMessageTextField.setEnabled(true);
		sendMessageTextField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final String oldText = chatArea.getText();
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						if(!model.isEmpty()) {
							for(int i = 0; i<model.getSize(); i++) {
								chatController.sendMessage(model.elementAt(i), sendMessageTextField.getText());
								chatArea.setText(oldText + "me: " + sendMessageTextField.getText() + "\n");							
							}							
						}
						else {
							JOptionPane.showMessageDialog(chatAreaPanel, "Please select a recipient in the top drawer");
						}
					}
				});
			}
		});

		chatAreaPanel.add(sendMessageTextField, "growx");
		return chatAreaPanel;

	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("Selecting JList");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		ToolBrokerImpl tbi = new ToolBrokerImpl("senders11@scy.intermedia.uio.no", "senders11");
		IAwarenessService aService = tbi.getAwarenessService();
		
		cmp = new ChatPanelMain(aService);
		frame.getContentPane().add(cmp);
		frame.setSize(280, 400);
		frame.setVisible(true);
	}

	private void registerChatArea(final JTextPane chatArea) {
		awarenessService.addAwarenessMessageListener(new IAwarenessMessageListener() {
			@Override
			public void handleAwarenessMessageEvent(final IAwarenessEvent awarenessEvent) {
				//cmp.updateModel();
				final String oldText = chatArea.getText();
				List<IAwarenessUser> users = new ArrayList<IAwarenessUser>();
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						if(awarenessEvent.getMessage() != null) {
							chatArea.setText(oldText + awarenessEvent.getUser() + ": " + awarenessEvent.getMessage() + "\n");							
						}
					}
				});
				logger.debug("registerChatArea: "+awarenessEvent.getMessage());
				users.add(awarenessEvent.getUser());
				awarenessService.updatePresenceTool(users);
			}
		});
		
		awarenessService.addPresenceToolListener(new IChatPresenceToolListener() {		
			@Override
			public void handleChatPresenceToolEvent(IChatPresenceToolEvent event) {
				
				logger.debug("ChatPanelMain: registerChatArea: handleChatPresenceToolEvent " + event.getUsers() + "message " + event.getMessage());
				model.removeAllElements();
				for (IAwarenessUser au : event.getUsers()) {
					model.addElement(au);					
				}
			}
		});
	}
}
