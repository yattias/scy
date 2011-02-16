package eu.scy.core.model.impl;

import eu.scy.core.model.Group;
import eu.scy.core.model.User;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.okt.2008
 * Time: 06:00:11
 */
@Entity
@Table(name = "groups")
@DiscriminatorColumn(name = "type")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class GroupImpl extends ScyBaseObject implements Group {

    private Set<User> members = new HashSet<User>();
    private String type = null;

    @Column(insertable = false, updatable = false)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public void addMember(User member) {
        if (this.members.contains(member)) {
            return;
        }
        this.members.add(member);
    }

    @Override
    @ManyToMany(targetEntity = SCYUserImpl.class, fetch = FetchType.EAGER)
    @JoinTable(name = "group_user_connection", joinColumns = { @JoinColumn(name = "group_fk", nullable = false) }, inverseJoinColumns = @JoinColumn(name = "user_fk", nullable = false))
    public Set<User> getMembers() {
        return members;
    }

    @Override
    public void setMembers(Set<User> members) {
        this.members = members;
    }
}