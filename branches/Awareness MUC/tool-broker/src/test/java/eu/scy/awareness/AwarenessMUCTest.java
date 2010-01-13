package eu.scy.awareness;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Random;

import junit.framework.Assert;

import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import eu.scy.toolbroker.ToolBrokerImpl;

public class AwarenessMUCTest {

	private ToolBrokerImpl tbi;
	private IAwarenessService aService;
	private String ELOUri = "1rxodm8f80qcb";
	private String user1 = "senders11";
	private String pass1 = "senders11";
	private String user2 = "djed11";
	private String pass2 = "djed11";
	private String tail = "@scy.intermedia.uio.no";
	private String conferenceExtension = "conference.scy.intermedia.uio.no";
	@Ignore
	public void setUp() throws Exception {

		tbi = new ToolBrokerImpl(user2, pass2);
		aService = tbi.getAwarenessService();
		//should be in the toolbroker
		aService.setMUCConferenceExtension(conferenceExtension);
		// generate chat room id
		Random r = new Random();
		String token = Long.toString(Math.abs(r.nextLong()), 36);
		//ELOUri = token;
	}
	
	@Test
	public void testDefault () {
		System.out.println("this is the MUC test");
	}

	
	@Ignore
	public void testSendMUCMessage() {
		aService.joinMUCRoom(ELOUri);
		
		String messageToSend = "hei hei you, satan is your leader";
		
		try {
			aService.sendMUCMessage(ELOUri, messageToSend);
		} catch (AwarenessServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		MultiUserChat muc = aService.getMultiUserChat(ELOUri);
		
		Message messageSent = muc.nextMessage();
	
		
		assertEquals(messageToSend, messageSent.getBody());
	}
	
	
	
	@Ignore
	public void testGetChatBuddies() {
		aService.joinMUCRoom(ELOUri);

//		try {
//			Thread.sleep(5000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}

		List<IAwarenessUser> chatBuddies = aService.getChatBuddies(ELOUri);

		assertTrue(!chatBuddies.isEmpty());

	}

	@Ignore
	public void testDestroyRoom() {
		aService.joinMUCRoom(ELOUri);
		// now lets destroy the sucker
		aService.destoryMUCRoom(ELOUri);
		// Pause for 4 seconds
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		boolean doesRoomExist = aService.doesRoomExist(ELOUri);
		assert (doesRoomExist == false);
	}

	@Ignore
	public void testADDBuddytoChat() {
		IAwarenessUser awarenessUser = new AwarenessUser();
		awarenessUser.setJid(user2 + tail);
		awarenessUser.setNickName(user2);

		// have user1 join the chat
		aService.joinMUCRoom(ELOUri);

		// have user2 join the chat
		aService.addBuddyToMUC(awarenessUser, ELOUri);

		// now check if they are in there
		List<IAwarenessUser> chatBuddies = aService.getChatBuddies(ELOUri);

		boolean foundOne = false;
		boolean foundTwo = false;
		for (IAwarenessUser iAwarenessUser : chatBuddies) {
			if (iAwarenessUser.getNickName().equals(user1))
				foundOne = true;
			if (iAwarenessUser.getNickName().equals(user2))
				foundTwo = true;
		}

		assertTrue(foundOne == true && foundTwo == true);
		this.forEver();
	}

	@Ignore
	public void testDoesNOTRoomExist() {
		// create the chat
		// aService.joinMUCRoom(ELOUri);

		boolean doesRoomExist = aService.doesRoomExist(ELOUri);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertTrue(doesRoomExist == false);
	}

	@Ignore
	public void testDoesRoomExist() {
		// create the chat
		aService.joinMUCRoom(ELOUri);
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		boolean doesRoomExist = aService.doesRoomExist(ELOUri);
		assertTrue(doesRoomExist == true);
	}

	@Ignore
	public void testHasJoinedRoom() {
		aService.joinMUCRoom(ELOUri);
		boolean hasJoinedRoom = aService.hasJoinedRoom(ELOUri, user1 + tail);
		assertTrue(hasJoinedRoom == true);
	}

	@Ignore
	public void testHasNOTJoinedRoom() {
		boolean hasJoinedRoom = aService.hasJoinedRoom(ELOUri, user1);
		assertTrue(hasJoinedRoom == false);
	}

	@Ignore
	public void joinMUC() {

		aService.joinMUCRoom(ELOUri);
		this.forEver();
	}

	@Ignore
	public void tearDown() throws Exception {
		// destroy the sucker
		aService.destoryMUCRoom(ELOUri);
	}

	public void forEver() {
		for (;;);
	}
}
