/**
 * 
 */
package eu.scy.scyhub;

import org.xmpp.component.Component;
import org.xmpp.component.ComponentException;
import org.xmpp.component.ComponentManager;
import org.xmpp.component.ComponentManagerFactory;
import org.xmpp.packet.Packet;
import org.xmpp.packet.PacketExtension;

import eu.scy.common.packetextension.SCYPacketTransformer;
import eu.scy.commons.whack.WhacketExtension;

/**
 * All SCY-Hub modules should subclass this abstract class in order to
 * receive messages from SCY-Hub and to have the ability to send messages
 * back to the OpenFire server.
 * 
 * @author giemza
 */
public abstract class SCYHubModule {
	
	// Reference to scyhub for sending messages
	private Component scyhub;
	
	// SCYPacketTransformer for deciding acceptence
	protected SCYPacketTransformer transformer;
	
	/**
	 * All subclasses must call the constructor to set the
	 * {@link Component} reference for sending messages.
	 * 
	 * @param scyhub the {@link Component}
	 * @param transformer
	 */
	protected SCYHubModule(Component scyhub, SCYPacketTransformer transformer) {
		this.scyhub = scyhub;
		this.transformer = transformer;
		WhacketExtension.registerExtension(transformer);
	}
	
	/**
	 * Sends a {@link Packet} using the {@link ComponentManager} and
	 * the {@link SCYHubComponent}.
	 * 
	 * @param packet the packet to be send
	 */
	protected void send(Packet packet) {
		try {
			ComponentManagerFactory.getComponentManager().sendPacket(scyhub, packet);
		} catch (ComponentException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * SCY-Hub modules will either accept packages for processing or not. If
	 * the module returns true, the process method will be called and the packet
	 * will be passed again for processing ({@link SCYHubModule#process(Packet)}).
	 * 
	 * @param packet the {@link Packet} to be accepted
	 * @return true, if accepted, false if not
	 */
	protected final boolean accept(Packet packet) {
		return packet.getExtension(transformer.getElementname(), transformer.getNamespace()) != null;
	}
	
	/**
	 * {@link SCYHubComponent} will pass the {@link Packet} for processing if
	 * it has been accepted by the module in the {@link SCYHubModule#accept(Packet)}
	 * method. The {@link PacketExtension} will be extracted form the packet and passed
	 * to the {@link SCYHubModule#process(PacketExtension)} method.
	 * 
	 * @param packet the {@link Packet} to be processed
	 */
	protected final void process(Packet packet) {
		PacketExtension extension = packet.getExtension(transformer.getElementname(), transformer.getNamespace());
		if(extension != null && extension instanceof WhacketExtension) {
			process(packet, (WhacketExtension)extension);
		}
	}
	
	/**
	 * Subclasses of {@link SCYHubModule} have to process the {@link WhacketExtension}
	 * passed over by this method. The packet can be used to extract further information.
	 * 
	 * @param packet the {@link Packet} to be processed
	 * @param extension the {@link WhacketExtension} to be processed
	 */
	protected abstract void process(Packet packet, WhacketExtension extension);

	/**
	 * Notifies the module to shutdown.
	 */
	public abstract void shutdown();

}
