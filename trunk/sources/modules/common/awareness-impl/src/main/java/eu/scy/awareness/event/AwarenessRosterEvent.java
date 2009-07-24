package eu.scy.awareness.event;

import java.util.Collection;


public class AwarenessRosterEvent extends AwarenessEvent implements IAwarenessRosterEvent {

    private Collection<String> addresses;
    
    public AwarenessRosterEvent(Object source, String user, String message, Collection<String> addresses) {
        super(source, user, message);
        setAddresses(addresses);
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

}
