package eu.scy.core.model;

import javax.persistence.*;
import java.util.List;
import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 07.aug.2008
 * Time: 13:01:55
 * A SCY project
 */

@Entity
@Table(name = "project")

public class Project extends SCYBaseObject {

    private List<Group> groups;
    private List<User>  users;

    @OneToMany(targetEntity = Group.class, mappedBy = "project", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    public List<Group> getGroups() {
        if(groups == null) {
            groups = new LinkedList<Group>();
        }
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }


    @OneToMany(targetEntity = User.class, mappedBy = "project", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public void addGroup(Group group) {
        if(group != null) {
            getGroups().add(group);    
        }

    }
}
