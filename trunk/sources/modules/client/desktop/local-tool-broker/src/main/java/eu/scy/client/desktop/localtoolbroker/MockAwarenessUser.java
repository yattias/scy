package eu.scy.client.desktop.localtoolbroker;

import eu.scy.awareness.IAwarenessUser;
import org.jivesoftware.smack.util.StringUtils;

public class MockAwarenessUser implements IAwarenessUser {

    private String status;
    private String presence;
    private String jid;
    private String nickName;
    private String mode;

    public MockAwarenessUser(String status, String presence, String jid, String nickName) {
        this.status = status;
        this.presence = presence;
        this.jid = jid;
        this.nickName = nickName;
    }

    public MockAwarenessUser() {
        this("online", "online", "sven@scy.collide.info", "sven");
    }

    @Override
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public String getPresence() {
        return presence;
    }

    @Override
    public void setPresence(String presence) {
        this.presence = presence;
    }

    @Override
    public String toString() {
        return this.nickName + " [" + presence + "] jid: " + this.jid;
    }

    @Override
    public String getJid() {
        return this.jid;
    }

    @Override
    public String getNickName() {
        return this.nickName;
    }

    @Override
    public void setJid(String jid) {
        this.jid = jid;
        //create nick name
        this.nickName = StringUtils.parseName(jid);
        if (this.nickName == null) {
            this.nickName = this.jid;
        }
    }

    @Override
    public void setNickName(String nickName) {
        if (nickName != null && nickName.contains("@")) {
            nickName = StringUtils.parseName(nickName);
        }
        this.nickName = nickName;
    }

   @Override
   public void setMode(String mode)
   {
      this.mode = mode;
   }

   @Override
   public String getMode()
   {
      return mode;
   }
}
