package eu.scy.actionlogging;

import org.springframework.test.AbstractTransactionalSpringContextTests;


/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 14.aug.2009
 * Time: 10:54:30
 * To change this template use File | Settings | File Templates.
 */
public class ActionLoggerHibernateTest  extends AbstractTransactionalSpringContextTests {
    public void testLog() throws Exception {
    }

    protected String[] getConfigLocations() {
        return new String[]{"classpath:/eu/scy/actionlogging/applciationContext-hibernate-OnlyForTesting.xml"};
    }

}
 