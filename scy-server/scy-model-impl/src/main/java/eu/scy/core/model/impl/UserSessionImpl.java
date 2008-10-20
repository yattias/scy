package eu.scy.core.model.impl;

import eu.scy.core.model.UserSession;
import eu.scy.core.model.User;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.okt.2008
 * Time: 06:18:34
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "usersession")
public class UserSessionImpl extends ScyBaseObject implements UserSession {

    private long sessionStarted;

    private User user;


    public long getSessionStarted() {
        return sessionStarted;
    }

    public void setSessionStarted(long sessionStarted) {
        this.sessionStarted = sessionStarted;
    }

    @ManyToOne(targetEntity = UserImpl.class, cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_primKey")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
