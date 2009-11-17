package eu.scy.client.tools.chattool;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;

import roolo.elo.api.IMetadataKey;
import eu.scy.awareness.IAwarenessService;
import eu.scy.awareness.event.IAwarePresenceEvent;
import eu.scy.awareness.event.IAwarenessEvent;
import eu.scy.awareness.event.IAwarenessMessageListener;
import eu.scy.awareness.event.IAwarenessPresenceListener;
import eu.scy.chat.controller.ChatController;
import eu.scy.toolbroker.ToolBrokerImpl;


public class ChatPanelMain extends JPanel {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(ChatPanelMain.class.getName());

	protected JList buddyList;
	JTextPane chatArea;
	protected DefaultListModel buddlyListModel;
	protected JTextField sendMessageTextField;
	protected ChatController chatController;
	private IAwarenessService awarenessService;
	private DefaultListModel model = new DefaultListModel();

	public ChatPanelMain() {
		

		try {
			// Set cross-platform Java L&F (also called "Metal")
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
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
		this.add(createBuddyListPanel(), BorderLayout.WEST);
		chatArea = new JTextPane();
		chatArea.setEditable(false);
		this.registerChatArea(chatArea);
		this.add(createChatArea(), BorderLayout.CENTER);
		initListeners();
	}

	protected JPanel createChatArea() {

		JPanel chatAreaPanel = new JPanel(new MigLayout("wrap 1"));
		JScrollPane chatAreaScroll = new JScrollPane(chatArea);
		chatAreaScroll.setPreferredSize(new Dimension(225, 250));
		chatAreaPanel.add(chatAreaScroll);
		
		sendMessageTextField = new JTextField();
		sendMessageTextField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JTextField textfield = (JTextField) e.getSource();
				String oldText = chatArea.getText();
				
				if (buddyList.getSelectedValue() == null) {
					JOptionPane.showMessageDialog(null, "Please select a recipient before submitting the text ...");
//					chatArea.setText(oldText + "me: " + textfield.getText() + "\n");
//					chatController.sendMessage(null, textfield.getText());
				}
				else {
					chatController.sendMessage(buddyList.getSelectedValue(), textfield.getText());
					chatArea.setText(oldText + "me: " + textfield.getText() + "\n");
				}
			}
		});

		chatAreaPanel.add(sendMessageTextField, "growx");
		return chatAreaPanel;

	}

	protected JPanel createBuddyListPanel() {
		JPanel buddyPanel = new JPanel(new MigLayout("wrap 1"));

		buddyList = new JList(model);
		buddyList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		
		for(int i=0; i<chatController.getBuddyListArray().size(); i++) {
			model.addElement(chatController.getBuddyListArray().elementAt(i));
		}

		JScrollPane buddyListScroll = new JScrollPane(buddyList);
		buddyListScroll.setPreferredSize(new Dimension(150, 250));

		buddyPanel.add(buddyListScroll);
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
		buddyList.addListSelectionListener(listSelectionListener);

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
		buddyList.addMouseListener(mouseListener);
	}

	public JFrame runInFrame() {

		JFrame frame = new JFrame("my awareness");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().add(this);
		frame.setSize(450, 350);
		frame.setVisible(true);
		return frame;
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("Selecting JList");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ChatPanelMain cmp = new ChatPanelMain();
		frame.getContentPane().add(cmp);
		frame.setSize(500, 500);
		frame.setVisible(true);
	}

	private void registerChatArea(final JTextPane chatArea) {
		awarenessService.addAwarenessMessageListener(new IAwarenessMessageListener() {
			@Override
			public void handleAwarenessMessageEvent(
					IAwarenessEvent awarenessEvent) {
				String oldText = chatArea.getText();
	
				chatArea.setText(oldText + awarenessEvent.getUser()
						+ ": " + awarenessEvent.getMessage() + "\n");
	
			}
		});
		
		awarenessService.addAwarenessPresenceListener(new IAwarenessPresenceListener() {
			
			@Override
			public void handleAwarenessPresenceEvent(IAwarePresenceEvent e) {
				logger.debug("registerChatArea: handleAwarenessPresenceEvent: smthg happened...: " + e);
				model.removeAllElements();
				for(int i=0; i<chatController.getBuddyListArray().size(); i++) {
					model.addElement(chatController.getBuddyListArray().elementAt(i));						
				}
			}
		});
	}
}
