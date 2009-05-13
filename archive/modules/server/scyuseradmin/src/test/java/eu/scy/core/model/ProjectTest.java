package eu.scy.core.model;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeTest;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 05.okt.2008
 * Time: 14:36:19
 * To change this template use File | Settings | File Templates.
 */
@Test
public class ProjectTest {

    private Project project;


    @BeforeTest
    private void initializeData() {
        project = new Project();
    }

    @Test
    public void testAddProjectLevelGroup() {
        Group group = new Group();
        project.addGroup(group);
        assert(project.getGroups().contains(group));
    }

    @Test
    public void testAddNullGroupToProject() {
        int size = project.getGroups().size();
        project.addGroup(null);
        assert(size == project.getGroups().size());
    }

}
