/**
 * 
 */
package eu.scy.client.notification;

import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.provider.ProviderManager;

import eu.scy.common.packetextension.SCYPacketTransformer;
import eu.scy.common.smack.SmacketExtension;
import eu.scy.common.smack.SmacketExtensionProvider;
import eu.scy.notification.NotificationPacketTransformer;
import eu.scy.notification.api.INotifiable;
import eu.scy.notification.api.INotification;

/**
 * @author giemza
 */
public class NotificationReceiver {
	
	private Connection connection;
	
	private SCYPacketTransformer transformer;
	
	private List<INotifiable> notifiables;
	
	public NotificationReceiver() {
		notifiables = new ArrayList<INotifiable>();
	}

	public void init(Connection connection) {
		this.connection = connection;
		
		transformer = new NotificationPacketTransformer();
		
		// add extenison provider
		SmacketExtensionProvider.registerExtension(transformer);

		ProviderManager providerManager = ProviderManager.getInstance();
		providerManager.addExtensionProvider(transformer.getElementname(), transformer.getNamespace(), new SmacketExtensionProvider());
		
		connection.addPacketListener(new PacketListener(){
		
			@Override
			public void processPacket(Packet packet) {
				if(packet instanceof Message) {
					SmacketExtension packetExtension = (SmacketExtension) packet.getExtension(transformer.getElementname(), transformer.getNamespace());
					INotification notification = (INotification) packetExtension.getTransformer().getObject();
					notification.setUserId(packet.getTo());
					notifyNotifiables(notification);
				}
			}
		}, new PacketFilter() {
		
			@Override
			public boolean accept(Packet packet) {
				return packet.getExtension(transformer.getElementname(), transformer.getNamespace()) != null;
			}
		});
	}


	protected void notifyNotifiables(final INotification notification) {
		for (final INotifiable notifiable : notifiables) {
                    Thread t = new Thread(new Runnable() {

                        @Override
                        public void run() {
                                notifiable.processNotification(notification);
                        }
                    });
                    t.start();
		}
	}

	public void addNotifiable(INotifiable notifiable) {
		notifiables.add(notifiable);
	}

}
