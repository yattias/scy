package eu.scy.awareness;

/**
 * Temporay user for aware service
 * 
 * @author anthonyp
 *
 */
public interface IAwarenessUser {

    public void setUsername(String username) ;
    public String getUsername() ;
    public void setName(String name) ;
    public String getName() ;
    public void setStatus(String status);
    public String getStatus() ;
    public void setPresence(String presence);
    public String getPresence();
    
}