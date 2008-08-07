package eu.scy.core.model;

import javax.persistence.*;
import java.util.List;

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

    @OneToMany(targetEntity = Group.class, mappedBy = "project", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }
}
