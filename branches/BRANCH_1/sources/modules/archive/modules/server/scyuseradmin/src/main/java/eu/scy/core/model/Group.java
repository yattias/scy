package eu.scy.core.model;

import javax.persistence.*;
import java.util.List;
import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.jun.2008
 * Time: 23:08:45
 * A group by which users can be organised. Groups can be organised in an hierarchical fashion
 */
@Entity
@Table (name = "scygroup")
public class Group extends SCYBaseObject {

    private List<User> users;
    private List <Group> children = new LinkedList<Group>();
    private Group parentGroup;
    private Project project;

    @OneToMany(targetEntity = User.class, mappedBy = "group", cascade = {CascadeType.REFRESH, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    public List<User> getUsers() {
        if(users == null) {
            users = new LinkedList<User>();
        }
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @OneToMany(targetEntity = Group.class, mappedBy = "parentGroup", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    public List<Group> getChildren() {
        if(children == null) {
            children = new LinkedList<Group>();
        }
        return children;
    }

    public void setChildren(List<Group> children) {
        this.children = children;
    }

    public void addChild(Group group) {
        if(group != null) {
            getChildren().add(group);    
        }

    }

    @ManyToOne(targetEntity = Group.class)
    public Group getParentGroup() {
        return parentGroup;
    }

    public void setParentGroup(Group parentGroup) {
        this.parentGroup = parentGroup;
    }

    @ManyToOne(targetEntity = Project.class, cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @JoinColumn (name = "project_primKey")
    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public void addUser(User user) {
        if(user != null) {
            getUsers().add(user);    
        }

    }
}
