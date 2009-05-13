package eu.scy.core.model;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 17.sep.2008
 * Time: 12:42:10
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(name="buddyConnection")
public class BuddyConnection extends SCYBaseObject{

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
