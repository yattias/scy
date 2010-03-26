package eu.scy.core.model.impl;

import eu.scy.core.model.SCYProject;
import eu.scy.core.model.SCYGroup;
import junit.framework.TestCase;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 16.okt.2008
 * Time: 06:32:10
 */
public class ProjectTest extends TestCase {

    private SCYProject project;


    protected void setUp() throws Exception {
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
