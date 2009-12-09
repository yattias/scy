package eu.scy.awareness;

import java.util.StringTokenizer;

import eu.scy.awareness.IAwarenessUser;

public class AwarenessUser implements IAwarenessUser {
    
    private String username;
    private String name;
    private String status;
    private String presence;
	private String correctUsername;
    
    public void setUsername(String username) {
        this.username = username;
    }
    public String getUsername() {
        return username;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getStatus() {
        return status;
    }
    public String getPresence() {
        return presence;
    }
    public void setPresence(String presence) {
        this.presence = presence;
    }
    
    @Override
    public String toString() {
        return trimIt(username) + " [" + presence + "]";
    }
    
	private String trimIt(String username2) {
		StringTokenizer st = new StringTokenizer(username2, "@");
		return st.nextToken();
	}
	@Override
	public String getCorrectUsername() {
		return this.correctUsername;
	}
	@Override
	public void setCorrectUsername(String uc) {
		this.correctUsername = trimIt(uc);
	}


}
