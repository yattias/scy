package eu.scy.chat.controller;

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

	public void registerChatArea(final JTextArea chatArea) {

		awarenessService
				.addAwarenessMessageListener(new IAwarenessMessageListener() {
					@Override
					public void handleAwarenessMessageEvent(
							final IAwarenessEvent awarenessEvent) {

						System.out.println("calling message event");

						SwingUtilities.invokeLater(new Runnable() {
							public void run() {

								String oldText = chatArea.getText();
								List<IAwarenessUser> users = new ArrayList<IAwarenessUser>();

								if (awarenessEvent.getMessage() != null) {
									chatArea.setText(oldText
											+ awarenessEvent.getUser()
													.getNickName() + ": "
											+ awarenessEvent.getMessage()
											+ "\n");
									logger.debug("message sent from: "
											+ awarenessEvent.getUser()
													.getNickName()
											+ " message: "
											+ awarenessEvent.getMessage());
								}

							}
						});

					}
				});

		awarenessService
				.addAwarenessPresenceListener(new IAwarenessPresenceListener() {
					@Override
					public void handleAwarenessPresenceEvent(
							final IAwarePresenceEvent e) {
						logger
								.debug("registerChatArea: handleAwarenessPresenceEvent: smthg happened...: "
										+ e.getUser().getNickName()
										+ " : "
										+ e.getUser().getPresence());

						SwingUtilities.invokeLater(new Runnable() {
							public void run() {

								IAwarenessUser awarenessUser = e.getUser();

								boolean isFound = false;
								IAwarenessUser iau;
								for (int i = 0; i < buddyListModel.getSize(); i++) {
									iau = (IAwarenessUser) buddyListModel
											.elementAt(i);
									logger
											.debug("registerChatArea: handleAwarenessPresenceEvent: "
													+ iau.getNickName());
									if (iau.getNickName().equals(
											awarenessUser.getNickName())) {
										((IAwarenessUser) buddyListModel
												.elementAt(i))
												.setPresence(awarenessUser
														.getPresence());
										isFound = true;
									}
								}
							}
						});

					}
				});

		awarenessService
				.addAwarenessRosterListener(new IAwarenessRosterListener() {

					@Override
					public void handleAwarenessRosterEvent(
							IAwarenessRosterEvent e) {
						Collection<String> addresses = e.getAddresses();
						for (String address : addresses) {
							final IAwarenessUser a = new AwarenessUser();
							a.setNickName(address);
							if (e.getMessage()
									.equals(IAwarenessRosterEvent.ADD)) {

								SwingUtilities.invokeLater(new Runnable() {
									@Override
									public void run() {

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
								});

							} else if (e.getMessage().equals(
									IAwarenessRosterEvent.REMOVE)) {

								SwingUtilities.invokeLater(new Runnable() {
									@Override
									public void run() {

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
											a
													.setPresence(IPresenceEvent.UNAVAILABLE);
											buddyListModel.addElement(a);
										}
									}
								});
							}
						}

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
}
