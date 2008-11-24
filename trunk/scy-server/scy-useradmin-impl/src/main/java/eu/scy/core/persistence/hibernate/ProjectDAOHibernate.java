package eu.scy.core.persistence.hibernate;

import eu.scy.core.model.Project;
import eu.scy.core.model.Group;
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
        return getSession().createQuery("From ProjectImpl order by name")
                .list();
    }

    public void createProject(Project project) {
        getHibernateTemplate().save(project);
    }

    public List <Project> findProjectsByName(String name) {
        return getSession().createQuery("from ProjectImpl where name like :name")
                .setString("name", name)
                .list();

    }


    public Project getProject(String projectId) {
        return (Project) getSession().createQuery("from ProjectImpl where id like :projectId")
                .setString("projectId", projectId)
                .uniqueResult();
    }

    public Project addGroupToProject(Project project, Group group) {
        getHibernateTemplate().saveOrUpdate(group);
        project.addGroup(group);
        group.setProject(project);
        save(project);
        return project;
    }
}
