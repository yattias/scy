package eu.scy.sessionmanager.impl;

import junit.framework.TestCase;
import org.springframework.test.AbstractTransactionalSpringContextTests;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 11.aug.2009
 * Time: 10:49:09
 * To change this template use File | Settings | File Templates.
 */
public class SessionManagerImplTest extends AbstractTransactionalSpringContextTests {
    protected String[] getConfigLocations() {
        return new String[]{"classpath:/eu/scy/sessionmanager/impl/applicationContext-hibernate-OnlyForTesting.xml"};
    }

    public void testLogin() {
        fail();
    }

}
