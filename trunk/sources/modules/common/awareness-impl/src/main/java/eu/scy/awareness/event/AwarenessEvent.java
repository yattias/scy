package eu.scy.awareness.event;

import java.util.EventObject;
import java.util.StringTokenizer;



public class AwarenessEvent extends EventObject implements IAwarenessEvent {

    protected String message;
    protected String user;

    public AwarenessEvent(Object source, String user, String message){
        super(source);
        this.user = correctName(user);
        this.message = message;
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
    public String getUser() {
        return this.user;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("user: ").append(user);
        sb.append("message: ").append(message);
        return sb.toString();
    }
    

   
}
