package eu.scy.presence;


public class PresenceUser implements IPresenceUser {
    
    private String username;
    private String name;
    private String status;
    private String presence;
    
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
        return name + " [" + presence + "]";
    }


}
