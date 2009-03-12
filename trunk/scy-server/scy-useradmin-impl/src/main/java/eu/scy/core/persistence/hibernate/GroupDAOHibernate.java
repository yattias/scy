package eu.scy.core.persistence.hibernate;

import eu.scy.core.model.SCYGroup;
import eu.scy.core.model.SCYProject;
import eu.scy.core.model.impl.SCYGroupImpl;
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

    public SCYGroup createGroup(SCYProject project, String name, SCYGroup parent) {
        if (project == null) {
            throw new RuntimeException("SCYProject not set - cannot create group");
        }
        SCYGroup g = new SCYGroupImpl();
        g.setProject(project);
        g.setName(name);
        g.setParentGroup(parent);
        return (SCYGroup) save(g);
    }

    public SCYGroup getGroup(String id) {
        return (SCYGroup) getSession().createQuery("from SCYGroupImpl where id = :id")
                .setString("id", id)
                .uniqueResult();
    }

    public SCYGroup getRootGroup(SCYProject project) {
        System.out.println("GETTING ROOT GROUPS!!");
        return (SCYGroup) getSession().createQuery("From SCYGroupImpl where parentGroup is null and project = :project")
                .setEntity("project", project)
                .setMaxResults(1)
                .uniqueResult();
    }

    public List<SCYGroup> getGroupsForProject(SCYProject project) {
        if(project != null) {
            return getSession().createQuery("from SCYGroupImpl where project = :project order by name")
                    .setEntity("project", project)
                    .list();
        }
        return Collections.EMPTY_LIST;
    }


}
