package eu.scy.core.model.impl;

import eu.scy.core.model.BuddyConnection;
import eu.scy.core.model.User;

import javax.persistence.ManyToOne;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.okt.2008
 * Time: 06:26:40
 * To change this template use File | Settings | File Templates.
 */
public class BuddyConnectionImpl extends ScyBaseObject implements BuddyConnection {

    private User myself;
    private User buddy;

    @ManyToOne(targetEntity = User.class, cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    public User getMyself() {
        return myself;
    }

    public void setMyself(User myself) {
        this.myself = myself;
    }

    @ManyToOne(targetEntity = User.class, cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    public User getBuddy() {
        return buddy;
    }

    public void setBuddy(User buddy) {
        this.buddy = buddy;
    }

}
