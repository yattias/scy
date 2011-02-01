package eu.scy.core.persistence.hibernate;

import eu.scy.core.model.PedagogicalPlanGroup;
import eu.scy.core.model.SCYGroup;
import eu.scy.core.model.SCYProject;
import eu.scy.core.model.pedagogicalplan.PedagogicalPlan;
import eu.scy.core.persistence.GroupDAO;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import org.hibernate.hql.ast.QuerySyntaxException;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 20.okt.2008
 * Time: 06:30:15
 * To change this template use File | Settings | File Templates.
 */
public class GroupDAOHibernate extends ScyBaseDAOHibernate implements GroupDAO {

    private static Logger log = Logger.getLogger("GroupDAOHibernate.class");

    public SCYGroup createGroup(SCYProject project, String name, SCYGroup parent) {
        if (project == null) {
            throw new RuntimeException("SCYProject not set - cannot create group");
        }
        SCYGroup g = null;//new SCYGroupImpl();

        //g.setProject(project);
        g.setName(name);
        //g.setParentGroup(parent);
        throw new RuntimeException("SOmething wrong here....");
        //return (SCYGroup) save(g);
    }

    public SCYGroup getGroup(String id) {
        return (SCYGroup) getSession().createQuery("from SCYGroupImpl where id = :id")
                .setString("id", id)
                .uniqueResult();
    }

    public SCYGroup getRootGroup(SCYProject project) {
        // System.out.println("GETTING ROOT GROUPS!!");
        return (SCYGroup) getSession().createQuery("From SCYGroupImpl where parentGroup is null and project = :project")
                .setEntity("project", project)
                .setMaxResults(1)
                .uniqueResult();
    }

    public List<SCYGroup> getGroupsForProject(SCYProject project) {
        if (project != null) {
            return getSession().createQuery("from SCYGroupImpl where project = :project order by name")
                    .setEntity("project", project)
                    .list();
        }
        return Collections.EMPTY_LIST;
    }

    public List<PedagogicalPlanGroup> getPedagogicalPlanGroups(PedagogicalPlan plan) {
        return getSession().createQuery("from PedagogicalPlanGroupImpl where plan = :plan order by name")
                .setEntity("project", plan)
                .list();
    }

    public Long getPedagogicalPlanGroupsCount(PedagogicalPlan plan) {
        try {
            return (Long) getSession().createQuery("select distinct count (group) from PedagogicalPlanGroupImpl as group where group.plan = :plan order by name")
                .setEntity("project", plan)
                .uniqueResult();
        } catch (QuerySyntaxException e) {
            return 0l;
        }
    }

    public List getUsers(SCYGroup group) {
        List returnList = getSession().createQuery("select connection.user from UserGroupConnectionImpl connection where connection.group = :group")
                .setEntity("group", group)
                .list();
        log.info("FOUND " + returnList.size() + " USERS FOR GROUP: " + group.getName());
        return returnList;
    }


}
