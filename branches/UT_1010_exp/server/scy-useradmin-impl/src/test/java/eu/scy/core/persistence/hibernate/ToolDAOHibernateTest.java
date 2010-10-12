package eu.scy.core.persistence.hibernate;

import eu.scy.core.model.impl.pedagogicalplan.ToolImpl;
import eu.scy.core.model.pedagogicalplan.Tool;
import eu.scy.core.persistence.ToolDAO;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 20.jan.2010
 * Time: 11:14:14
 * To change this template use File | Settings | File Templates.
 */
public class ToolDAOHibernateTest extends AbstractDAOTest {

    private ToolDAO toolDAO;

    public ToolDAO getToolDAO() {
        return toolDAO;
    }

    public void setToolDAO(ToolDAO toolDAO) {
        this.toolDAO = toolDAO;
    }

    @Test
    public void testSetup() {
        assertNotNull(getToolDAO());
    }

    public void testCreateNewTool() {
        ToolImpl tool = (ToolImpl) createTool("Colemo");
        getToolDAO().save(tool);
        assertNotNull(tool.getId());
    }

    private Tool createTool(String toolName) {
        Tool tool = new ToolImpl();
        tool.setName(toolName);
        return tool;
    }

    public void testGetToolByName() {
        String NAME ="FreakinCoolTool";
        ToolImpl tool = (ToolImpl) createTool(NAME);
        getToolDAO().save(tool);
        assertNotNull(tool.getId());
        assertEquals(tool, getToolDAO().findToolByName(NAME));
    }


}
