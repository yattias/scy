package eu.scy.core.model.impl;

import eu.scy.core.model.SCYProject;
import eu.scy.core.model.SCYGroup;
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

    private SCYProject project;


    @BeforeTest
    private void initializeData() {
        project = new SCYProjectImpl();
    }

    @Test
    public void testAddProjectLevelGroup() {
        SCYGroup group = new SCYGroupImpl();
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
