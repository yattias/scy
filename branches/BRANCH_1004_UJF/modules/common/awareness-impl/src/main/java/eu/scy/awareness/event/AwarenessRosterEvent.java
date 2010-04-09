package eu.scy.awareness.event;

import java.util.Collection;

import eu.scy.awareness.AwarenessUser;
import eu.scy.awareness.IAwarenessUser;


public class AwarenessRosterEvent extends AwarenessEvent implements IAwarenessRosterEvent {

    private Collection<String> addresses;
    private String roomId;
    
    public AwarenessRosterEvent(Object source, IAwarenessUser user, String message, Collection<String> addresses) {
        super(source, user, message);
        setAddresses(addresses);
    }
    
    public AwarenessRosterEvent(Object source, String user, String message, Collection<String> addresses) {
        super(source, message);
        setAddresses(addresses);
        IAwarenessUser a = new AwarenessUser();
        a.setJid(user);
        setUser(a);
    }

    @Override
    public Collection<String> getAddresses() {
        return addresses;
    }

    @Override
    public void setAddresses(Collection<String> addresses) {
        this.addresses = addresses;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString()).append("\n");
        sb.append("addresses: ").append(addresses);
        return sb.toString();
    }
    
    
    @Override
	public String getRoomId() {
		return this.roomId;
	}

	@Override
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}


}
