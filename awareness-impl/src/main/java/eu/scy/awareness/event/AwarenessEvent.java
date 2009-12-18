package eu.scy.awareness.event;

import java.util.Collection;
import java.util.EventObject;
import java.util.StringTokenizer;

import eu.scy.awareness.IAwarenessUser;



public class AwarenessEvent extends EventObject implements IAwarenessEvent {

    protected String message;
    protected IAwarenessUser user;

    public AwarenessEvent(Object source, IAwarenessUser user, String message){
        super(source);
        this.user = user;
        this.user.setCorrectUsername(correctName(user.getUsername()));
        this.message = message;
    }
    
    public AwarenessEvent(Object source,String user) {
    	super(source);
		// TODO Auto-generated constructor stub
	}

	private String correctName(String username) {
		StringTokenizer st = new StringTokenizer(username, "/");
		return st.nextToken();
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
    

   
}
