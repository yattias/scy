package eu.scy.core.persistence.hibernate;

import eu.scy.core.model.Group;
import eu.scy.core.model.Project;
import eu.scy.core.model.impl.GroupImpl;
import eu.scy.core.persistence.GroupDAO;

import java.util.List;
import java.util.Collections;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 20.okt.2008
 * Time: 06:30:15
 * To change this template use File | Settings | File Templates.
 */
public class GroupDAOHibernate extends ScyBaseDAOHibernate implements GroupDAO {

    public Group createGroup(Project project, String name, Group parent) {
        if (project == null) {
            throw new RuntimeException("Project not set - cannot create group");
        }
        Group g = new GroupImpl();
        g.setProject(project);
        g.setName(name);
        g.setParentGroup(parent);
        return (Group) save(g);
    }

    public Group getGroup(String id) {
        return (Group) getSession().createQuery("from GroupImpl where id = :id")
                .setString("id", id)
                .uniqueResult();
    }

    public Group getRootGroup(Project project) {
        System.out.println("GETTING ROOT GROUPS!!");
        return (Group) getSession().createQuery("From GroupImpl where parentGroup is null and project = :project")
                .setEntity("project", project)
                .setMaxResults(1)
                .uniqueResult();
    }

    public List<Group> getGroupsForProject(Project project) {
        if(project != null) {
            return getSession().createQuery("from GroupImpl where project = :project order by name")
                    .setEntity("project", project)
                    .list();
        }
        return Collections.EMPTY_LIST;
    }


}
