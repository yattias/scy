// package eu.scy.collaborationservice.tuplespaceconnector;
package eu.scy.openfire.plugin;

import java.io.File;

import org.apache.log4j.Logger;
import org.jivesoftware.openfire.PacketRouter;
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.container.Plugin;
import org.jivesoftware.openfire.container.PluginManager;
import org.jivesoftware.openfire.interceptor.InterceptorManager;
import org.jivesoftware.openfire.interceptor.PacketInterceptor;
import org.jivesoftware.openfire.interceptor.PacketRejectedException;
import org.jivesoftware.openfire.session.Session;
import org.jivesoftware.smack.provider.ProviderManager;
import org.xmpp.component.Component;
import org.xmpp.component.ComponentException;
import org.xmpp.component.ComponentManager;
import org.xmpp.component.ComponentManagerFactory;
import org.xmpp.packet.JID;
import org.xmpp.packet.Packet;
import org.xmpp.packet.PacketExtension;
import org.xmpp.packet.Presence;

import eu.scy.communications.adapter.IScyCommunicationAdapter;
import eu.scy.communications.adapter.IScyCommunicationListener;
import eu.scy.communications.adapter.ScyCommunicationAdapter;
import eu.scy.communications.adapter.ScyCommunicationEvent;
import eu.scy.communications.packet.extension.ScyObjectExtensionProvider;
import eu.scy.communications.packet.extension.ScyObjectPacketExtension;
import eu.scy.core.model.impl.ScyBaseObject;
import eu.scy.openfire.plugin.botz.BotzConnection;
import eu.scy.openfire.plugin.botz.BotzPacketReceiver;

public class ScyOpenFirePlugin implements Plugin, PacketInterceptor, Component {
    private static final Logger logger = Logger.getLogger(ScyOpenFirePlugin.class.getName());
    private static IScyCommunicationAdapter communicationsAdapter;
    private static PluginManager pluginManager;
    private String serviceName;
    private XMPPServer xmppServer;
    private PacketRouter packetRouter;
    private JID componentJID;
    private ComponentManager componentManager;
    private boolean isComponentReady;
    private InterceptorManager interceptorManager;
    
    public ScyOpenFirePlugin() {
        xmppServer = XMPPServer.getInstance();
        
        ProviderManager providerManager = ProviderManager.getInstance();
        this.componentManager = ComponentManagerFactory.getComponentManager();
        this.interceptorManager = InterceptorManager.getInstance();
        
        providerManager.addExtensionProvider(ScyObjectPacketExtension.ELEMENT_NAME, ScyObjectPacketExtension.NAMESPACE, new ScyObjectExtensionProvider());
        
        this.communicationsAdapter = ScyCommunicationAdapter.getInstance();
        this.communicationsAdapter.addScyCommunicationListener(new IScyCommunicationListener() {
            
            @Override
            public void handleCommunicationEvent(ScyCommunicationEvent e) {
                System.out.println("---------- handleCommunicationEvent ----------");
                org.xmpp.packet.Message m = new org.xmpp.packet.Message();
                m.setTo("biden@imediamac09.uio.no/Smack");
                m.setFrom(componentJID.toString());
                m.setSubject("Message to set");
                m.setBody("i is watching you; callback happened: " + e.getMessage());
                ScyBaseObject scyObject = new ScyBaseObject();
                scyObject.setId("mom");
                scyObject.setName("mom");
                scyObject.setDescription("what");
                PacketExtension scye = new ScyObjectPacketExtension();
                ((ScyObjectPacketExtension) scye).setScyBase(scyObject);
                m.addExtension(scye);
                sendPacket(m);
                System.out.println("---------- sent ----------");
            }
        });
        
    }
    
    /**
     * Initializes the component.
     * 
     * @param jid
     *            the jid of the component
     * @param componentManager
     *            instance of the componentManager
     * @throws ComponentException
     *             thrown if there are issues initializing this component
     */
    public void initialize(JID jid, ComponentManager componentManager) throws ComponentException {
        this.componentJID = jid;
    }
    
    public void initializePlugin(PluginManager pluginManager, File pluginDirectory) {
        
        this.pluginManager = pluginManager;
        
        this.interceptorManager.addInterceptor(this);
        System.out.println("register component name");
        System.out.println("Connections make you happy.");
        // this.interceptorManager.e(this);
        
       
        System.out.println("TSC: " + communicationsAdapter);
        
        try {
            componentManager.addComponent(getName(), this);
        } catch (ComponentException e1) {
            e1.printStackTrace();
        }
    }
    
    /**
     * Used to send a packet with this component
     * 
     * @param packet
     *            the package to send
     */
    public void sendPacket(Packet packet) {
        try {
            componentManager.sendPacket(this, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void destroyPlugin() {
        interceptorManager.removeInterceptor(this);
        try {
            componentManager.removeComponent(this.getName());
            componentManager = null;
        } catch (Exception e) {
            componentManager.getLog().error(e);
        }
    }
    
    public String getName() {
        return "collaboration-service-plugin";
    }
    
    public static PluginManager getPluginManager() {
        return pluginManager;
    }
    
    /**
     * Returns JID for this component
     * 
     * @return the jid for this component
     */
    public JID getComponentJID() {
        return componentJID;
    }
    
    @Override
    public String getDescription() {
        return "collaboration service plugin";
    }
    
    @Override
    public void processPacket(Packet packet) {
        System.out.println("collaboration-service-processing-packet");
        System.out.println(packet.toXML());
        System.out.println("============== instance of message =============");
        System.out.println("extracting element");
        PacketExtension extension = (PacketExtension) packet.getExtension(ScyObjectPacketExtension.ELEMENT_NAME, ScyObjectPacketExtension.NAMESPACE);
        // logger.debug("extension; " + extension.toString());
        System.out.println("processing SCY Packet");
        this.processScyPacket(extension);
    
        
    }
    
    @Override
    public void shutdown() {
        isComponentReady = false;
    }
    
    @Override
    public void start() {
        isComponentReady = true;
    }

    @Override
    public void interceptPacket(Packet packet, Session session, boolean incoming, boolean processed) throws PacketRejectedException {
        if (!processed && incoming) {
            System.out.println("====== intercepting-packet ============");
            System.out.println("====== packet ============");
            System.out.println(packet.toXML());
            PacketExtension extension = (PacketExtension) packet.getExtension(ScyObjectPacketExtension.ELEMENT_NAME, ScyObjectPacketExtension.NAMESPACE);
            this.processScyPacket(extension);
        }
        
    }
    
    
    public void processScyPacket(PacketExtension extension){
        System.out.println("checking if scy packet");
        if (extension != null && extension instanceof ScyObjectPacketExtension) {
            System.out.println("processing SCY Packet");
            ScyObjectPacketExtension scyExt = (ScyObjectPacketExtension) extension;
            System.out.println("============== scy message packet =============");
            System.out.println("name " + scyExt.getName());
            System.out.println("description " + scyExt.getDescription());
            System.out.println("id " + scyExt.getId());
            communicationsAdapter.sendCallBack("send something");
        }
    }
    
    
}
