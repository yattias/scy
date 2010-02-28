package eu.scy.core.model;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 03.sep.2008
 * Time: 11:28:13
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "userSession")
public class UserSession extends SCYBaseObject{

    private long sessionStarted;

    private User user;


    public long getSessionStarted() {
        return sessionStarted;
    }

    public void setSessionStarted(long sessionStarted) {
        this.sessionStarted = sessionStarted;
    }

    @ManyToOne(targetEntity = User.class, cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @JoinColumn (name = "user_primKey")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
