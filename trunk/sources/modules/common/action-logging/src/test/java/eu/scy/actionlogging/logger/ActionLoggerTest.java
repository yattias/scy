package eu.scy.actionlogging.logger;

import org.springframework.test.AbstractTransactionalSpringContextTests;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 12.mai.2009
 * Time: 20:20:25
 * To change this template use File | Settings | File Templates.
 */
public class ActionLoggerTest extends AbstractTransactionalSpringContextTests {

     protected String[] getConfigLocations() {
        return new String[]{"classpath:/eu/scy/actionlogging/logger/applicationContext-hibernate-OnlyForTesting.xml"};
    }

    public void testExists() {
        
    }
}
