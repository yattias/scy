package eu.scy.core.persistence.hibernate;

import org.springframework.test.AbstractDependencyInjectionSpringContextTests;
import org.springframework.test.AbstractTransactionalSpringContextTests;
import org.testng.annotations.Test;
import eu.scy.core.persistence.ProjectDAO;
import eu.scy.core.model.Project;
import eu.scy.core.model.Group;
import eu.scy.core.model.impl.ProjectImpl;
import eu.scy.core.model.impl.GroupImpl;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 24.okt.2008
 * Time: 12:34:47
 * To change this template use File | Settings | File Templates.
 */
@Test
public class ProjectDAOHibernateTest extends AbstractTransactionalSpringContextTests {

    private ProjectDAO projectDAO;

    protected String[] getConfigLocations() {
        return new String[]{"classpath:/eu/scy/core/persistence/hibernate/applciationContext-hibernate-OnlyForTesting.xml"};
    }

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
        Project p = new ProjectImpl();
        p.setName("test projejcct");
        assertNull(p.getId());
        p = (Project) getProjectDAO().save(p);
        assertNotNull(p.getId());
    }

    //Test
    public void testFindProjectsByName() {
        String projectName = "Henrik";
        Project testProject = new ProjectImpl();
        testProject.setName(projectName);
        getProjectDAO().save(testProject);

        List projects = getProjectDAO().findProjectsByName(projectName);
        assert(projects.size() == 1);

    }

    @Test
    public void testAddGroupToProject() {
        Project testProject = new ProjectImpl();
        getProjectDAO().save(testProject);

        Group g = new GroupImpl();
        g = (Group) getProjectDAO().save(g);
        //testProject.addGroup(g);
        //g.setProject(testProject);
        getProjectDAO().addGroupToProject(testProject, g);
        assertTrue(g.getId() != null);
        assertTrue(testProject.getGroups().contains(g));
    }

}


