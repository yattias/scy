package eu.scy.awareness;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Random;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import eu.scy.toolbroker.ToolBrokerImpl;

public class AwarenessMUCTest {

	private ToolBrokerImpl tbi;
	private IAwarenessService aService;
	private String ELOUri = "BESTCHATEVER";
	private String user1 = "senders11";
	private String pass1 = "senders11";
	private String user2 = "djed11";
	private String pass2 = "djed11";
	private String tail = "@scy.intermedia.uio.no";

	@Ignore
	public void setUp() throws Exception {

		tbi = new ToolBrokerImpl(user2, pass2);
		aService = tbi.getAwarenessService();

		// generate chat room id
		Random r = new Random();
		String token = Long.toString(Math.abs(r.nextLong()), 36);
		ELOUri = ELOUri + "-" + token;
	}

	@Ignore
	public void testGetChatBuddies() {
		aService.joinMUCRoom(ELOUri);

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

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
		awarenessUser.setUsername(user2 + tail);
		awarenessUser.setName(user2);

		// have user1 join the chat
		aService.joinMUCRoom(ELOUri);

		// have user2 join the chat
		aService.addBuddyToMUC(awarenessUser, ELOUri);

		// now check if they are in there
		List<IAwarenessUser> chatBuddies = aService.getChatBuddies(ELOUri);

		boolean foundOne = false;
		boolean foundTwo = false;
		for (IAwarenessUser iAwarenessUser : chatBuddies) {
			if (iAwarenessUser.getName().equals(user1))
				foundOne = true;
			if (iAwarenessUser.getName().equals(user2))
				foundTwo = true;
		}

		assertTrue(foundOne == true && foundTwo == true);

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

		aService.joinMUCRoom("BESTCHATEVER");
		for (;;)
			;

	}

	@Ignore
	public void tearDown() throws Exception {
		// destroy the sucker
		aService.destoryMUCRoom(ELOUri);
	}

}
