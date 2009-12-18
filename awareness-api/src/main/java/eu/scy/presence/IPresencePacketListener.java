package eu.scy.presence;


/**
 * Fired when a packet is sent or received
 * 
 * @author thomasd
 *
 */
public interface IPresencePacketListener  {
    
    /**
     * handles packet events
     * 
     * @param e
     */
    public void handlePresencePacketEvent(IPresencePacketEvent e);

}
