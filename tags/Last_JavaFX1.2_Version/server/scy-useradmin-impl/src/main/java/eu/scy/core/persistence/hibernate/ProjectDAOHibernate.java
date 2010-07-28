package eu.scy.core.persistence.hibernate;

import eu.scy.core.model.SCYProject;
import eu.scy.core.model.SCYGroup;
import eu.scy.core.persistence.ProjectDAO;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 20.okt.2008
 * Time: 05:47:36
 * To change this template use File | Settings | File Templates.
 */
public class ProjectDAOHibernate extends ScyBaseDAOHibernate implements ProjectDAO {

    public List getAllProjects() {
        return getSession().createQuery("From SCYProjectImpl order by name")
                .list();
    }

    public void createProject(SCYProject project) {
        getHibernateTemplate().save(project);
    }

    public List <SCYProject> findProjectsByName(String name) {
        return getSession().createQuery("from SCYProjectImpl where name like :name")
                .setString("name", name)
                .list();

    }


    public SCYProject getProject(String projectId) {
        return (SCYProject) getSession().createQuery("from SCYProjectImpl where id like :projectId")
                .setString("projectId", projectId)
                .uniqueResult();
    }

    public SCYGroup addGroupToProject(SCYProject project, SCYGroup group) {
        getHibernateTemplate().saveOrUpdate(group);
        //project.addGroup(group);
        //group.setProject(project);
        save(project);
        return group;
    }
}
