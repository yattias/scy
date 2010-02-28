package eu.scy.actionlogging;

import org.springframework.test.AbstractTransactionalSpringContextTests;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.junit.Test;

import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.core.model.impl.PersistentAction;


/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 14.aug.2009
 * Time: 10:54:30
 * To change this template use File | Settings | File Templates.
 */
public class ActionLoggerHibernateTest  extends AbstractTransactionalSpringContextTests {

    private ActionLoggerHibernate actionLogger;
    private PersistentAction action;

    private DriverManagerDataSource driverManagerDataSource;



    public ActionLoggerHibernate getActionLogger() {
        return actionLogger;
    }

    public void setActionLogger(ActionLoggerHibernate actionLogger) {
        this.actionLogger = actionLogger;
    }

    public DriverManagerDataSource getDriverManagerDataSource() {
        return driverManagerDataSource;
    }

    public void setDriverManagerDataSource(DriverManagerDataSource driverManagerDataSource) {
        this.driverManagerDataSource = driverManagerDataSource;
    }

    protected String[] getConfigLocations() {
        return new String[]{"classpath:/eu/scy/actionlogging/applciationContext-hibernate-OnlyForTesting.xml"};
    }

    /*@Test
    public void testEchoTestDataSourceValues() {
        DriverManagerDataSource datasource = getDriverManagerDataSource();
        assertNotNull(datasource);
        System.out.println("****************************************************************");
        System.out.println(datasource.getUrl());
        System.out.println("****************************************************************");
        try {
            throw new RuntimeException("DATA SOURCE URL: " + datasource.getUrl() + " " + datasource.getUsername());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }  */

    @Test
    public void testAssertThatLoggerIsCreated() {
        assertNotNull(getActionLogger());
    }

    @Test(expected=NullPointerException.class)
    public void testLogAction() {
        action = new PersistentAction();
        action.setName("Test action");
        getActionLogger().log("Hen", "hei", action);
        assertNotNull(action.getId());
    }

    @Test
    public void testAddAttribute() {
        String key = "HENRIK_THE_COOL_ONE";
        String value = "YEAH_THAT_IS TRUE MAN";
        action = new PersistentAction();
        action.addAttribute(key, value);
        getActionLogger().log("Hen", "hei", action);

        String id = action.getId();

        action = null;

        PersistentAction action2 = (PersistentAction) getActionLogger().getObject(PersistentAction.class, id);
        assertNotNull(action2);
        assertEquals(action2.getAttribute(key), value);




    }

    public void testAddContextAttribute() {
        ContextConstants key = ContextConstants.tool;
        String value = "YODA_COMPARES_TO_NOTHING";
        action = new PersistentAction();
        action.addContext(key, value);
        getActionLogger().log("Hen", "hei", action);
        String id = action.getId();
        action = null;
        assertNull(action);
        PersistentAction action2 = (PersistentAction) getActionLogger().getObject(PersistentAction.class, id);
        assertNotNull(action2);
        assertEquals (action2.getContext(key), value);
    }




}
 