package eu.scy.awareness.event;

import java.util.Collection;
import java.util.EventObject;
import java.util.StringTokenizer;

import eu.scy.awareness.IAwarenessUser;



public class AwarenessEvent extends EventObject implements IAwarenessEvent {

    private String message;
    protected IAwarenessUser user;
	private String roomId;

    public AwarenessEvent(Object source, IAwarenessUser user, String message){
        super(source);
        this.user = user;
        this.message = message;
    }
    
    public AwarenessEvent(Object source,String message) {
    	super(source);
    	this.setMessage(message);
	}


    public void setMessage(String message) {
		this.message = message;
	}

	@Override
    public String getMessage() {
       return this.message;
    }

    @Override
    public IAwarenessUser getUser() {
        return this.user;
    }
    
    @Override
	public void setUser(IAwarenessUser user) {
		this.user = user;		
	}
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("user: ").append(user);
        sb.append("message: ").append(message);
        return sb.toString();
    }

	@Override
	public void setIAwarenessUser(IAwarenessUser user) {
		this.user = user;
		
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
