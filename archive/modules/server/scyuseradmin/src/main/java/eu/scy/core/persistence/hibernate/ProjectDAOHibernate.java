package eu.scy.core.persistence.hibernate;

import eu.scy.core.model.Project;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 07.aug.2008
 * Time: 13:54:05
 * To change this template use File | Settings | File Templates.
 */
public class ProjectDAOHibernate extends BaseDAOHibernate{

    public List getAllProjects() {
        return getSession().createQuery("From Project order by name")
                .list();
    }

    public void createProject(Project project) {
        getHibernateTemplate().save(project);        
    }

    public List <Project> findProjectsByName(String name) {
        return getSession().createQuery("from Project where name like :name")
                .setString("name", name)
                .list();

    }


    public Project getProject(String projectId) {
        return (Project) getSession().createQuery("from Project where id like :projectId")
                .setString("projectId", projectId)
                .uniqueResult();
    }
}
