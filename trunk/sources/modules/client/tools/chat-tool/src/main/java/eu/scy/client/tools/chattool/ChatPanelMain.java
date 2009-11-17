package eu.scy.client.tools.chattool;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;
import org.jdesktop.swingx.JXTitledPanel;

import roolo.elo.api.IMetadataKey;
import eu.scy.awareness.IAwarenessService;
import eu.scy.awareness.IAwarenessUser;
import eu.scy.awareness.event.IAwarePresenceEvent;
import eu.scy.awareness.event.IAwarenessEvent;
import eu.scy.awareness.event.IAwarenessMessageListener;
import eu.scy.awareness.event.IAwarenessPresenceListener;
import eu.scy.chat.controller.ChatController;
import eu.scy.toolbroker.ToolBrokerImpl;


/**
 * @author jeremyt
 *
 */
public class ChatPanelMain extends JPanel {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(ChatPanelMain.class.getName());

	//protected JList buddyList;
	JTextPane chatArea;
	static ChatPanelMain cmp;
	JXTitledPanel chatAreaPanel;
	protected DefaultListModel buddlyListModel;
	protected JTextField sendMessageTextField;
	protected ChatController chatController;
	private IAwarenessService awarenessService;
	private DefaultListModel model = new DefaultListModel();
	private Vector<Object> users = new Vector<Object>();;

	
	public ChatPanelMain() {
		

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

		ToolBrokerImpl<IMetadataKey> tbi = new ToolBrokerImpl<IMetadataKey>();
		awarenessService = tbi.getAwarenessService();
		awarenessService.init(tbi.getConnection("jt11@scy.intermedia.uio.no", "jt11"));

		chatController = new ChatController(awarenessService);
		chatController.populateBuddyList();
		initGUI();
	}
	
	
	protected void initGUI() {
//		this.add(createBuddyListPanel(), BorderLayout.WEST);
		chatArea = new JTextPane();
		chatArea.setEditable(false);
		this.registerChatArea(chatArea);
		this.add(createChatArea(), BorderLayout.CENTER);
		initListeners();
	}

	
	protected JPanel createChatArea() {
		
		chatAreaPanel = new JXTitledPanel("Welcome");
		chatAreaPanel.setLayout(new MigLayout("wrap 1"));
		JScrollPane chatAreaScroll = new JScrollPane(chatArea);
		chatAreaScroll.setPreferredSize(new Dimension(225, 250));
		chatAreaPanel.add(chatAreaScroll);
		
		sendMessageTextField = new JTextField();
		sendMessageTextField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JTextField textfield = (JTextField) e.getSource();
				String oldText = chatArea.getText();
				
//				if (buddyList.getSelectedValue() == null) {
//					JOptionPane.showMessageDialog(null, "Please select a recipient before submitting the text ...");
//				}
//				else {
//					chatController.sendMessage(buddyList.getSelectedValue(), textfield.getText());
					chatController.sendMessage(model.elementAt(0), textfield.getText());
					chatArea.setText(oldText + "me: " + textfield.getText() + "\n");
//				}
			}
		});

		chatAreaPanel.add(sendMessageTextField, "growx");
		return chatAreaPanel;

	}

	
	protected JPanel createBuddyListPanel() {
		JPanel buddyPanel = new JPanel(new MigLayout("wrap 1"));

//		buddyList = new JList(model);
//		buddyList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		updateModel();

//		JScrollPane buddyListScroll = new JScrollPane(buddyList);
//		buddyListScroll.setPreferredSize(new Dimension(150, 250));
//
//		buddyPanel.add(buddyListScroll);
		return buddyPanel;
	}

	protected void initListeners() {
		ListSelectionListener listSelectionListener = new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent listSelectionEvent) {
				logger.debug("initListeners: First index: " + listSelectionEvent.getFirstIndex());
				logger.debug("initListeners: Last index: " + listSelectionEvent.getLastIndex());
				boolean adjust = listSelectionEvent.getValueIsAdjusting();
				logger.debug("initListeners: Adjusting? " + adjust);
				
				if (!adjust) {
					JList list = (JList) listSelectionEvent.getSource();
					int selections[] = list.getSelectedIndices();
					Object selectionValues[] = list.getSelectedValues();
					for (int i = 0, n = selections.length; i < n; i++) {
						if (i == 0) {
							logger.debug("initListeners: Selections:");
						}
						logger.debug("initListeners: "+selections[i] + "/" + selectionValues[i] + " ");
					}
				}
			}
		};
//		buddyList.addListSelectionListener(listSelectionListener);

		MouseListener mouseListener = new MouseAdapter() {
			public void mouseClicked(MouseEvent mouseEvent) {
				JList theList = (JList) mouseEvent.getSource();
				if (mouseEvent.getClickCount() == 2) {
					int index = theList.locationToIndex(mouseEvent.getPoint());
					if (index >= 0) {
						Object o = theList.getModel().getElementAt(index);
						logger.debug("initListeners: mouseClicked: Double-clicked on: " + o.toString());
					}
				}
			}
		};
//		buddyList.addMouseListener(mouseListener);
	}


	public static void main(String[] args) {
		JFrame frame = new JFrame("Selecting JList");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		cmp = new ChatPanelMain();
		frame.getContentPane().add(cmp);
		frame.setSize(280, 400);
		frame.setVisible(true);
	}

	private void registerChatArea(final JTextPane chatArea) {
		awarenessService.addAwarenessMessageListener(new IAwarenessMessageListener() {
			@Override
			public void handleAwarenessMessageEvent(IAwarenessEvent awarenessEvent) {
				String oldText = chatArea.getText();
				chatArea.setText(oldText + awarenessEvent.getUser() + ": " + awarenessEvent.getMessage() + "\n");
				logger.debug("registerChatArea: "+awarenessEvent.getMessage());
				cmp.selectCorrectChatter(awarenessEvent.getUser());
			}

		});
		
		awarenessService.addAwarenessPresenceListener(new IAwarenessPresenceListener() {
			
			@Override
			public void handleAwarenessPresenceEvent(IAwarePresenceEvent e) {
				logger.debug("registerChatArea: handleAwarenessPresenceEvent: smthg happened...: " + e);
				
				cmp.updateModel();
			}
		});
	}

	protected void updateModel() {
		model.removeAllElements();
		users.removeAllElements();
		chatController.populateBuddyList();
		IAwarenessUser iau;
		
		for(int i=0; i<chatController.getBuddyList().getSize(); i++) {
			iau = (IAwarenessUser) chatController.getBuddyListArray().elementAt(i);
			model.addElement(iau);
			users.addElement(iau);
		}
	}

	protected void selectCorrectChatter(String user) {
		updateModel();
		StringTokenizer st = new StringTokenizer(user, "/");
		String correctUsername = st.nextToken();
		
		IAwarenessUser iau;
		for(int i=0; i<users.size(); i++) {
			iau = (IAwarenessUser) users.elementAt(i);
//			
		}
	}
}
