package eu.scy.awareness;

import java.util.StringTokenizer;

import org.jivesoftware.smack.util.StringUtils;

import eu.scy.awareness.IAwarenessUser;

public class AwarenessUser implements IAwarenessUser {
    
    private String username;
    private String name;
    private String status;
    private String presence;
	private String correctUsername;
	private String jid;
	private String nickName;
    
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
        return this.nickName + " [" + presence + "] jid: " + this.jid ;
    }

	@Override
	public String getJid() {
		return this.jid;
	}
	@Override
	public String getNickName() {
		
		if( this.nickName != null && this.nickName.contains("@") )
			this.nickName = StringUtils.parseName(nickName);
		
		return this.nickName;
	}
	@Override
	public void setJid(String jid) {
		this.jid = jid;
		//create nick name
		this.nickName = StringUtils.parseName(jid);
		if( this.nickName == null ){
			this.nickName = this.jid;
		}
	}
	@Override
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}


}
