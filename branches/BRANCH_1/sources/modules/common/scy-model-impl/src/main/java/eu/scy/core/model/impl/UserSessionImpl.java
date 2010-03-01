package eu.scy.core.model.impl;

import eu.scy.core.model.UserSession;
import eu.scy.core.model.User;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.okt.2008
 * Time: 06:18:34
 * A session is a unit of work from a user first logs in to the system until the user logs out or is logged out
 */
@Entity
@Table(name = "usersession")
public class UserSessionImpl extends ScyBaseObject implements UserSession {

    private long sessionStarted;
    private long sessionEnded;
    private Boolean isSessionActive;

    private String sessionId;
    private User user;


    public long getSessionStarted() {
        return sessionStarted;
    }

    public void setSessionStarted(long sessionStarted) {
        this.sessionStarted = sessionStarted;
    }

    @ManyToOne(targetEntity = SCYUserImpl.class, cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_primKey")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getSessionEnded() {
        return sessionEnded;
    }

    public void setSessionEnded(long sessionEnded) {
        this.sessionEnded = sessionEnded;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    
    public Boolean getSessionActive() {
        return isSessionActive;
    }

    public void setSessionActive(Boolean sessionActive) {
        isSessionActive = sessionActive;
    }
}
