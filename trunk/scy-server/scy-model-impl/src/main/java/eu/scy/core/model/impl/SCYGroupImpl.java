package eu.scy.core.model.impl;

import eu.scy.core.model.SCYGroup;
import eu.scy.core.model.User;
import eu.scy.core.model.SCYProject;

import javax.persistence.*;
import java.util.List;
import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.okt.2008
 * Time: 06:00:11
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table (name = "scygroup")
@org.hibernate.annotations.Proxy (proxyClass = SCYGroup.class )
public class SCYGroupImpl extends ScyBaseObject implements SCYGroup {

    private List<User> users;
    private List <SCYGroup> children = new LinkedList<SCYGroup>();
    private SCYGroup parentGroup;
    private SCYProject project;


    //OneToMany(targetEntity = SCYUserImpl.class, mappedBy = "group", cascade = {CascadeType.REFRESH, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @Transient
    public List<User> getUsers() {
        if(users == null) {
            users = new LinkedList<User>();
        }
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }


    @OneToMany(targetEntity = SCYGroupImpl.class, mappedBy = "parentGroup", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    public List<SCYGroup> getChildren() {
        if(children == null) {
            children = new LinkedList<SCYGroup>();
        }
        return children;
    }

    public void setChildren(List<SCYGroup> children) {
        this.children = children;
    }

    public void addChild(SCYGroup group) {
        if(group != null) {
            getChildren().add(group);
        }

    }

    @ManyToOne(targetEntity = SCYGroupImpl.class)
    public SCYGroup getParentGroup() {
        return parentGroup;
    }

    public void setParentGroup(SCYGroup parentGroup) {
        this.parentGroup = parentGroup;
    }

    @ManyToOne(targetEntity = SCYProjectImpl.class, cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @JoinColumn(name = "project_primKey")
    public SCYProject getProject() {
        return project;
    }

    public void setProject(SCYProject project) {
        this.project = project;
    }
    public void addUser(User user) {
        if(user != null) {
            getUsers().add(user);
        }
    }

}
