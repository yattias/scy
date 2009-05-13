package eu.scy.awareness.event;


public class AwarenessListEvent extends AwarenessEvent implements IAwarenessListEvent {

    public AwarenessListEvent(Object source, String user, String message) {
        super(source, user, message);
    }

}
