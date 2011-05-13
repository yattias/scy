package eu.scy.core.persistence.hibernate;

import eu.scy.core.model.SCYGroup;
import eu.scy.core.model.SCYProject;
import eu.scy.core.model.impl.SCYGroupImpl;
import eu.scy.core.model.impl.SCYProjectImpl;
import eu.scy.core.persistence.ProjectDAO;
import org.junit.Test;


import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 24.okt.2008
 * Time: 12:34:47
 * To change this template use File | Settings | File Templates.
 */

public class ProjectDAOHibernateTest extends AbstractDAOTest {

    private ProjectDAO projectDAO;

    public ProjectDAO getProjectDAO() {
        return projectDAO;
    }

    public void setProjectDAO(ProjectDAO projectDAO) {
        this.projectDAO = projectDAO;
    }


    @Test
    public void testBeanDefined() {
        assert (getProjectDAO() != null);
    }

    @Test
    public void testSaveProject() {
        SCYProject p = new SCYProjectImpl();
        p.setName("test projejcct");
        assertNull(p.getId());
        p = (SCYProject) getProjectDAO().save(p);
        assertNotNull(p.getId());
    }

    @Test
    public void testFindProjectsByName() {
        String projectName = "Henrik";
        SCYProject testProject = new SCYProjectImpl();
        testProject.setName(projectName);
        getProjectDAO().save(testProject);

        List projects = getProjectDAO().findProjectsByName(projectName);
        assert(projects.size() == 1);

    }

    @Test
    public void testAddGroupToProject() {
        SCYProject testProject = new SCYProjectImpl();
        getProjectDAO().save(testProject);

        SCYGroup g = new SCYGroupImpl();
        g = (SCYGroup) getProjectDAO().save(g);
        //testProject.addGroup(g);
        //g.setProject(testProject);
        getProjectDAO().addGroupToProject(testProject, g);
        assertTrue(g.getId() != null);
        //assertTrue(testProject.getGroups().contains(g));
    }
 
}


