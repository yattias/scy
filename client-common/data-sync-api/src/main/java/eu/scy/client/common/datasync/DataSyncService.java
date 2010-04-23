package eu.scy.client.common.datasync;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.MultiUserChat;

import eu.scy.common.configuration.Configuration;
import eu.scy.common.message.DataSyncMessagePacketTransformer;
import eu.scy.common.message.SyncMessage;
import eu.scy.common.message.SyncMessage.Event;
import eu.scy.common.message.SyncMessage.Response;
import eu.scy.common.message.SyncMessage.Type;
import eu.scy.common.packetextension.SCYPacketTransformer;
import eu.scy.common.smack.SmacketExtension;
import eu.scy.common.smack.SmacketExtensionProvider;

public class DataSyncService implements IDataSyncService {

	private ReentrantLock lock = new ReentrantLock();
	
	private Condition condition = lock.newCondition();
	
	private Queue<Packet> queue;
	
	private XMPPConnection xmppConnection;

	private SCYPacketTransformer transformer;

	public DataSyncService() {
		
		queue = new LinkedList<Packet>();
	}
	
	public void init(XMPPConnection xmppConnection) {
		
		this.xmppConnection = xmppConnection;
		transformer = new DataSyncMessagePacketTransformer();
		
		// add extenison provider
		SmacketExtensionProvider.registerExtension(transformer);

		ProviderManager providerManager = ProviderManager.getInstance();
		providerManager.addExtensionProvider(transformer.getElementname(), transformer
				.getNamespace(), new SmacketExtensionProvider());
		
		xmppConnection.addPacketListener(new PacketListener(){
		
			@Override
			public void processPacket(Packet packet) {
				queue.add(packet);
				
				lock.lock();
				condition.signalAll();
				lock.unlock();
			}
		}, new PacketFilter() {
		
			@Override
			public boolean accept(Packet packet) {
				return packet.getExtension(transformer.getElementname(), transformer.getNamespace()) != null;
			}
		});
	}
	
	@Override
	public ISyncSession createSession(ISyncListener listener) throws Exception {
		
		SyncMessage command = new SyncMessage(Type.command);
		
		command.setEvent(Event.create);
		command.setUserId(xmppConnection.getUser());
		command.setToolId("nutpad");
		transformer.setObject(command);
		
		Packet sentPacket = new Message();
		sentPacket.setFrom(xmppConnection.getUser());
		sentPacket.setTo(Configuration.getInstance().getSCYHubName() + "." + Configuration.getInstance().getOpenFireHost());
		
		SmacketExtension extension = new SmacketExtension(transformer);
		sentPacket.addExtension(extension);
		
		xmppConnection.sendPacket(sentPacket);
		
		Message receivedPacket = null;
		ISyncSession newSession = null;
		
		lock.lock();
		try {
			do {
				condition.await();//10, TimeUnit.SECONDS);
				Packet peek = queue.peek();
				// check IDs for request and response
				if (peek.getPacketID().equals(sentPacket.getPacketID())){
					receivedPacket = (Message) peek;
					queue.remove(peek);
				}
			} while (receivedPacket == null);
			extension = (SmacketExtension) receivedPacket.getExtension(transformer.getElementname(), transformer.getNamespace());
			SyncMessage message = (SyncMessage) extension.getTransformer().getObject();
			
			if(message.getResponse().equals(Response.success)) {
				String mucID = message.getMessage(); // defined by xmpp response
				MultiUserChat muc = new MultiUserChat(xmppConnection, mucID);
				muc.join(xmppConnection.getUser());
				newSession = new SyncSession(xmppConnection, muc);
				newSession.addSyncListener(listener);
				
			} else if (message.getResponse().equals(Response.failure)) {
				// TODO through exception
				System.err.println("Failure during session creation");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw new IOException("Something really bad happened ...", e);
		} finally {
			lock.unlock();
		}
		return newSession;
	}

	@Override
	public ISyncSession joinSession(String mucID, ISyncListener listener) {
		MultiUserChat muc = new MultiUserChat(xmppConnection, mucID);
		try {
			muc.join(xmppConnection.getUser());
			ISyncSession joinedSession = new SyncSession(xmppConnection, muc);
			joinedSession.addSyncListener(listener);
			return joinedSession;
		} catch (XMPPException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void leaveSession(ISyncSession iSyncSession,ISyncListener iSyncListener) {
		// TODO Auto-generated method stub
	}

}