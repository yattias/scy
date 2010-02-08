package eu.scy.chat.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.AbstractListModel;
import javax.swing.DefaultListModel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;
import org.jivesoftware.smack.util.StringUtils;

import eu.scy.awareness.AwarenessServiceException;
import eu.scy.awareness.AwarenessUser;
import eu.scy.awareness.IAwarenessService;
import eu.scy.awareness.IAwarenessUser;
import eu.scy.awareness.event.IAwarePresenceEvent;
import eu.scy.awareness.event.IAwarenessEvent;
import eu.scy.awareness.event.IAwarenessMessageListener;
import eu.scy.awareness.event.IAwarenessPresenceListener;
import eu.scy.awareness.event.IAwarenessRosterEvent;
import eu.scy.awareness.event.IAwarenessRosterListener;
import eu.scy.presence.IPresenceEvent;

public class MUCChatController implements ChatController {

	private static final Logger logger = Logger
			.getLogger(MUCChatController.class.getName());
	private DefaultListModel buddyListModel = new DefaultListModel();
	private IAwarenessService awarenessService;
	private String ELOUri;
	private boolean isTempMUCRoom;
	private JTextArea chatArea;
	private JTextField textField;

	public MUCChatController(IAwarenessService awarenessService, String ELOUri) {
		logger.debug("MUC ChatController: starting ... ");
		logger.debug("MUC ChatController: awarenessService.isConnected(): "
				+ awarenessService.isConnected());
		this.ELOUri = ELOUri;
		this.awarenessService = awarenessService;
	}

	public String getCurrentUser() {
		String user = getAwarenessService().getConnection().getUser();
		if (user != null)
			return StringUtils.parseName(user);
		return "NO ONE";
	}

	public void sendMessage(final String ELOUri, final String message) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					awarenessService.sendMUCMessage(ELOUri, message);
				} catch (AwarenessServiceException e) {
					logger
							.error("ChatController: sendMessageMUC: AwarenessServiceException for ELOUri:"
									+ ELOUri + " " + e);
				}
			}
		});

	}

	public void addBuddy(final AwarenessUser user) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				buddyListModel.addElement(user);
			}
		});
	}

	public void removeBuddy(final AwarenessUser user) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				buddyListModel.removeElement(user);
				// kick that fucker away call handler
			}
		});
	}

	public void registerChatArea(JTextArea registerArea) {
		this.chatArea = registerArea;

		awarenessService.addAwarenessMessageListener(new IAwarenessMessageListener() {
					@Override
					public void handleAwarenessMessageEvent(
							final IAwarenessEvent awarenessEvent) {

				
						logger.debug( "calling message event the new chat");

						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								System.out.println( "Checking room id" );
								
								
								String awarenessEventRoomId = awarenessEvent.getRoomId();
								if( awarenessEventRoomId != null && awarenessEventRoomId.contains("@")) {
									//need to parse it text@conference.org
									awarenessEventRoomId = StringUtils.parseName(awarenessEventRoomId);
									logger.debug( "NEW ROOMID " + awarenessEventRoomId);
								}
								
								if( org.apache.commons.lang.StringUtils.equalsIgnoreCase(ELOUri, awarenessEventRoomId) ) {									
									logger.debug( "MATCHED ELOURI " + ELOUri + " roomid " + awarenessEventRoomId );
									
									String oldText = chatArea.getText();
									List<IAwarenessUser> users = new ArrayList<IAwarenessUser>();

									if (awarenessEvent.getMessage() != null) {
										chatArea.setText(oldText
												+ awarenessEvent.getUser()
														.getNickName() + ": "
												+ awarenessEvent.getMessage()
												+ "\n");
										logger.debug("text set in chatarea: "
												+ awarenessEvent.getUser()
														.getNickName()
												+ " message: "
												+ awarenessEvent.getMessage());
										logger.debug("chat area refreshing");
										chatArea.revalidate();
									}
								} else {
									logger.debug( "ELOURI MISS MATCH " + ELOUri + " roomid " + awarenessEventRoomId );
								}
								

							}
						});

					}
				});

		awarenessService.addAwarenessPresenceListener(new IAwarenessPresenceListener() {
					@Override
					public void handleAwarenessPresenceEvent(final IAwarePresenceEvent awarenessPresenceEvent) {
						logger.debug("registerChatArea: handleAwarenessPresenceEvent: smthg happened...: " + awarenessPresenceEvent.getUser().getNickName() + " : "	+ awarenessPresenceEvent.getUser().getPresence());

						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								
								String awarenessEventRoomId = awarenessPresenceEvent.getRoomId();
								logger.debug( "NEW awarenessEventRoomId ROOMID " + awarenessEventRoomId);
								if( awarenessEventRoomId != null && awarenessEventRoomId.contains("@")) {
									//need to parse it text@conference.org
									awarenessEventRoomId = StringUtils.parseName(awarenessEventRoomId);
									logger.debug( "NEW awarenessEventRoomId ROOMID " + awarenessEventRoomId);
								}
								
								if( org.apache.commons.lang.StringUtils.equalsIgnoreCase(ELOUri, awarenessEventRoomId) ) {									
									logger.debug( "MATCHED awarenessEventRoomId ELOURI " + ELOUri + " roomid " + awarenessEventRoomId );
									
									IAwarenessUser awarenessUser = awarenessPresenceEvent.getUser();
	
									boolean isFound = false;
									IAwarenessUser iau;
									for (int i = 0; i < buddyListModel.getSize(); i++) {
										iau = (IAwarenessUser) buddyListModel.elementAt(i);
										logger.debug("registerChatArea: handleAwarenessPresenceEvent: awarenessEventRoomId: "	+ iau.getNickName());
										if (iau.getNickName().equals(awarenessUser.getNickName())) {
											((IAwarenessUser) buddyListModel.elementAt(i)).setPresence(awarenessUser.getPresence());
											isFound = true;
										}
									}
								}
							}
						});

					}
				});

		awarenessService.addAwarenessRosterListener(new IAwarenessRosterListener() {

					@Override
					public void handleAwarenessRosterEvent(
							final IAwarenessRosterEvent awarenessRosterEvent) {
						Collection<String> addresses = awarenessRosterEvent.getAddresses();
						for (String address : addresses) {
							final IAwarenessUser a = new AwarenessUser();
							a.setNickName(address);
							if (awarenessRosterEvent.getMessage().equals(IAwarenessRosterEvent.ADD)) {

								SwingUtilities.invokeLater(new Runnable() {
									@Override
									public void run() {
										
										String awarenessEventRoomId = awarenessRosterEvent.getRoomId();
										logger.debug( "NEW awarenessRosterEventRoomId ROOMID " + awarenessEventRoomId);
										if( awarenessEventRoomId != null && awarenessEventRoomId.contains("@")) {
											//need to parse it text@conference.org
											awarenessEventRoomId = StringUtils.parseName(awarenessEventRoomId);
											logger.debug( "NEW awarenessRosterEventRoomId ROOMID " + awarenessEventRoomId);
										}
										
										if( org.apache.commons.lang.StringUtils.equalsIgnoreCase(ELOUri, awarenessEventRoomId) ) {									
											logger.debug( "MATCHED awarenessRosterEventRoomId ELOURI " + ELOUri + " roomid " + awarenessEventRoomId );
											
											int indexOfBuddy = getIndexOfBuddy(a);
											if (indexOfBuddy > -1) {
												IAwarenessUser elementAt = (IAwarenessUser) buddyListModel
												.elementAt(indexOfBuddy);
												elementAt
												.setPresence(IPresenceEvent.AVAILABLE);
												buddyListModel.remove(indexOfBuddy);
												buddyListModel.add(indexOfBuddy,
														elementAt);
											} else {
												a
												.setPresence(IPresenceEvent.AVAILABLE);
												buddyListModel.addElement(a);
											}
											
										}


									}
								});

							} else if (awarenessRosterEvent.getMessage().equals(IAwarenessRosterEvent.REMOVE)) {

								SwingUtilities.invokeLater(new Runnable() {
									@Override
									public void run() {
										
										String awarenessEventRoomId = awarenessRosterEvent.getRoomId();
										logger.debug( "NEW awarenessRosterEventRoomId ROOMID " + awarenessEventRoomId);
										
										if( awarenessEventRoomId != null && awarenessEventRoomId.contains("@")) {
											//need to parse it text@conference.org
											awarenessEventRoomId = StringUtils.parseName(awarenessEventRoomId);
											logger.debug( "NEW awarenessRosterEventRoomId ROOMID " + awarenessEventRoomId);
										}
										
										if( org.apache.commons.lang.StringUtils.equalsIgnoreCase(ELOUri, awarenessEventRoomId) ) {									
											logger.debug( "MATCHED awarenessRosterEventRoomId ELOURI " + ELOUri + " roomid " + awarenessEventRoomId );

											int indexOfBuddy = getIndexOfBuddy(a);
											if (indexOfBuddy > -1) {
												IAwarenessUser elementAt = (IAwarenessUser) buddyListModel
														.elementAt(indexOfBuddy);
												elementAt
														.setPresence(IPresenceEvent.UNAVAILABLE);
												buddyListModel.remove(indexOfBuddy);
												buddyListModel.add(indexOfBuddy,
														elementAt);
											} else {
												a.setPresence(IPresenceEvent.UNAVAILABLE);
												buddyListModel.addElement(a);
											}
										}
									}
								});
							}
						}

					}
				});

	}

	@Override
	public void registerTextField(JTextField sendMessageTextField) {
		this.textField = sendMessageTextField;
		this.textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final JTextField sourceTextField  = (JTextField) e.getSource();
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						MUCChatController.this.sendMessage( ELOUri,sourceTextField.getText());
						sourceTextField.setText("");
						chatArea.revalidate();
					}
				});
			}
		});
		
	}
	
	protected int getIndexOfBuddy(IAwarenessUser awarenessUser) {
		Enumeration<?> elements = buddyListModel.elements();
		while (elements.hasMoreElements()) {
			IAwarenessUser auser = (IAwarenessUser) elements.nextElement();

			if (auser.getNickName() != null
					&& auser.getNickName().equals(awarenessUser.getNickName())) {
				return buddyListModel.indexOf(auser);
			}
		}
		return -1;
	}

	public void connectToRoom() {
		logger
				.debug("ChatController: Joining room with ELOUri: "
						+ getELOUri());
		try {
			this.getAwarenessService().joinMUCRoom(this.ELOUri);
		} catch (AwarenessServiceException e) {
			e.printStackTrace();
		}
	}

	public void setAwarenessService(IAwarenessService awarenessService) {
		this.awarenessService = awarenessService;
	}

	public IAwarenessService getAwarenessService() {
		return awarenessService;
	}

	public void setELOUri(String eLOUri) {
		ELOUri = eLOUri;
	}

	public String getELOUri() {
		return ELOUri;
	}

	public void setBuddyListModel(DefaultListModel buddyListModel) {
		this.buddyListModel = buddyListModel;
	}

	public DefaultListModel getBuddyListModel() {
		return buddyListModel;
	}

	public void sendMessage(IAwarenessUser recipient, String message) {
		// for OOO chat
	}

	public void setChatArea(JTextArea chatArea) {
		this.chatArea = chatArea;
	}

	public JTextArea getChatArea() {
		return chatArea;
	}


	public void setTextField(JTextField textField) {
		this.textField = textField;
	}

	public JTextField getTextField() {
		return textField;
	}
}
