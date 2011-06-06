package eu.scy.core.model.impl;

import eu.scy.core.model.UserGroupConnection;
import eu.scy.core.model.User;
import eu.scy.core.model.SCYGroup;

import javax.persistence.ManyToOne;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.CascadeType;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 27.mai.2009
 * Time: 20:08:54
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "usergroupconnection")
//org.hibernate.annotations.Proxy (proxyClass = UserGroupConnection.class )
public class UserGroupConnectionImpl extends ScyBaseObject implements UserGroupConnection {

    private User user;
    private SCYGroup group;

    public UserGroupConnectionImpl() {
    }

    public UserGroupConnectionImpl(User user, SCYGroup group) {
        this.user = user;
        this.group = group;
    }

    @ManyToOne(targetEntity = SCYUserImpl.class, cascade = CascadeType.ALL)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne(targetEntity = SCYGroupImpl.class, cascade = CascadeType.ALL)
    public SCYGroup getGroup() {
        return group;
    }

    public void setGroup(SCYGroup group) {
        this.group = group;
    }
}
