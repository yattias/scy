package eu.scy.core.model.impl;

import eu.scy.core.model.*;

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
public class SCYProjectImpl extends ScyBaseObject implements SCYProject {


    private List<SCYGroup> groups;
    private List<User>  users;

    @Transient
    //OneToMany(targetEntity = SCYGroupImpl.class, mappedBy = "project", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    public List<SCYGroup> getGroups() {
        if(groups == null) {
            groups = new LinkedList<SCYGroup>();
        }
        return groups;
    }

    public void setGroups(List<SCYGroup> groups) {
        this.groups = groups;
    }


    //OneToMany(targetEntity = SCYUserImpl.class, mappedBy = "project", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @Transient
    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public void addGroup(SCYGroup group) {
        if(group != null) {
            getGroups().add(group);
            //group.setProject(this);
        }

    }

}
