package eu.scy.client.tools.chattool;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.miginfocom.swing.MigLayout;

import org.apache.log4j.Logger;
import org.jdesktop.swingx.JXTitledPanel;

import eu.scy.awareness.AwarenessUser;
import eu.scy.awareness.IAwarenessService;
import eu.scy.awareness.IAwarenessUser;
import eu.scy.awareness.event.IAwarePresenceEvent;
import eu.scy.awareness.event.IAwarenessPresenceListener;
import eu.scy.awareness.tool.IChatPresenceToolEvent;
import eu.scy.awareness.tool.IChatPresenceToolListener;
import eu.scy.chat.controller.ChatController;
import eu.scy.toolbroker.ToolBrokerImpl;


/**
 * @author jeremyt
 *
 */
public class ChatPresencePanelMain extends JPanel {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(ChatPresencePanelMain.class.getName());

	protected JList buddyList;
	JTextPane chatArea;
	static ChatPresencePanelMain cmp;
	JXTitledPanel chatAreaPanel;
	protected DefaultListModel buddlyListModel;
	protected JTextField sendMessageTextField;
	protected ChatController chatController;
	private IAwarenessService awarenessService;
	private DefaultListModel model = new DefaultListModel();
	private Vector<Object> users = new Vector<Object>();;

	
	public ChatPresencePanelMain(IAwarenessService awarenessService) {
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
		this.add(createBuddyListPanel(), BorderLayout.WEST);
		this.registerChatArea(chatArea);
		initListeners();
	}

	
	protected JPanel createBuddyListPanel() {
		JPanel buddyPanel = new JPanel(new MigLayout("wrap 1"));

		buddyList = new JList(model);
		buddyList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		updateModel();

		JScrollPane buddyListScroll = new JScrollPane(buddyList);
		buddyListScroll.setPreferredSize(new Dimension(150, 100));

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
					
					List<IAwarenessUser> users = new ArrayList<IAwarenessUser>();
					IAwarenessUser iau = null;
					for (int i = 0, n = selections.length; i < n; i++) {
						if(String.valueOf(selectionValues[i]).contains("available")) {
							iau = (IAwarenessUser) model.elementAt(selections[i]);
							users.add(iau);
							
						}// if
					}// for
					
					awarenessService.updateChatTool(users);
					
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


	public static void main(String[] args) {
		JFrame frame = new JFrame("Selecting JList");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		ToolBrokerImpl tbi = new ToolBrokerImpl("senders11@scy.intermedia.uio.no", "senders11");
		IAwarenessService aService = tbi.getAwarenessService();		
		
		cmp = new ChatPresencePanelMain(aService);
		frame.getContentPane().add(cmp);
		frame.setSize(200, 170);
		frame.setVisible(true);
	}

	private void registerChatArea(final JTextPane chatArea) {
		awarenessService.addAwarenessPresenceListener(new IAwarenessPresenceListener() {		
			@Override
			public void handleAwarenessPresenceEvent(IAwarePresenceEvent e) {
				logger.debug("registerChatArea: handleAwarenessPresenceEvent: smthg happened...: " + e);				
				cmp.updateModel();
			}
		});
		
		awarenessService.addChatToolListener(new IChatPresenceToolListener() {		
			@Override
			public void handleChatPresenceToolEvent(IChatPresenceToolEvent event) {
				logger.debug("ChatPresencePanelMain: registerChatArea: handleChatPresenceToolEvent: " + event);
				logger.debug("ChatPresencePanelMain: registerChatArea: handleChatPresenceToolEvent: " + event.getUsers().size());
				AwarenessUser au = (AwarenessUser) event.getUsers().get(0);
				logger.debug("ChatPresencePanelMain: registerChatArea: handleChatPresenceToolEvent: " + au.getUsername());
				for(int i = 0; i<model.getSize(); i++) {
					String mau = ((AwarenessUser) model.get(i)).getUsername();
					if(mau.equals(au.getUsername())) {
						buddyList.setSelectedIndex(i);
					}
				}
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
	
//	protected void selectCorrectChatter(String user) {
//		updateModel();
//		StringTokenizer st = new StringTokenizer(user, "/");
//		String correctUsername = st.nextToken();
//		
//		IAwarenessUser iau;
//		for(int i=0; i<users.size(); i++) {
//			iau = (IAwarenessUser) users.elementAt(i);	
//			if(buddyList.getSelectedIndex() == -1 && iau.getUsername().equals(correctUsername)) {
//				buddyList.setSelectedIndex(i);
//				chatAreaPanel.setTitle("Chatting with: "+iau.getName());
//			}
//		}
//	}
}
