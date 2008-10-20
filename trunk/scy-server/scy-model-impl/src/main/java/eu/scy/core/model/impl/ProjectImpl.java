package eu.scy.core.model.impl;

import eu.scy.core.model.Project;
import eu.scy.core.model.Group;
import eu.scy.core.model.User;

import javax.persistence.*;
import java.util.List;
import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.okt.2008
 * Time: 06:16:40
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "project")
public class ProjectImpl extends ScyBaseObject implements Project {


    private List<Group> groups;
    private List<User>  users;

    @OneToMany(targetEntity = GroupImpl.class, mappedBy = "project", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    public List<Group> getGroups() {
        if(groups == null) {
            groups = new LinkedList<Group>();
        }
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }


    @OneToMany(targetEntity = UserImpl.class, mappedBy = "project", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
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
