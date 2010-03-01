package eu.scy.core.model.impl;

import eu.scy.core.model.SCYGroup;
import eu.scy.core.model.User;
import eu.scy.core.model.SCYProject;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.LinkedList;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.okt.2008
 * Time: 06:00:11
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "groups")
@org.hibernate.annotations.Proxy (proxyClass = SCYGroup.class )
public class SCYGroupImpl extends ScyBaseObject implements SCYGroup {

    private Set<User> members = new HashSet<User>();

    private SCYGroup parent;

    @Override
    public void addMember(User member) {
        if (this.members.contains(member)) {
            return;
        }
        this.members.add(member);
    }

    @Override
    @ManyToMany(targetEntity = SCYUserImpl.class, fetch = FetchType.EAGER)
    @JoinTable(name = "groups_related_to_users", joinColumns = { @JoinColumn(name = "group_fk", nullable = false) }, inverseJoinColumns = @JoinColumn(name = "user_fk", nullable = false))
    public Set<User> getMembers() {
        return members;
    }

    @Override
    public void setMembers(Set<User> members) {
        this.members = members;
    }

    @Override
    @ManyToOne(targetEntity = SCYGroupImpl.class, fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @Cascade( { org.hibernate.annotations.CascadeType.SAVE_UPDATE })
    @JoinColumn(name = "parent_fk")
    public SCYGroup getParent() {
        return parent;
    }

    @Override
    public void setParent(SCYGroup parent) {
        this.parent = parent;
    }
}
