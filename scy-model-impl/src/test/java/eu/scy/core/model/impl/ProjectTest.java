package eu.scy.core.model.impl;

import eu.scy.core.model.Project;
import eu.scy.core.model.Group;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.okt.2008
 * Time: 06:32:10
 */
@Test
public class ProjectTest {

    private Project project;


    @BeforeTest
    private void initializeData() {
        project = new ProjectImpl();
    }

    @Test
    public void testAddProjectLevelGroup() {
        Group group = new GroupImpl();
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
