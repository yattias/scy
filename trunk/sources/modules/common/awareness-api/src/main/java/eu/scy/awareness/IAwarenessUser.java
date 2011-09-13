package eu.scy.awareness;

/**
 * Temporay user for aware service
 * 
 * @author anthonyp
 *
 */
public interface IAwarenessUser {

    public void setStatus(String status);
    public String getStatus() ;
    public void setPresence(String presence);
    public String getPresence();
    
    public void setJid(String jid);
    public String getJid();
    
    public void setNickName(String nickName);
    public String getNickName();

    public void setMode(String mode);
    public String getMode();
}