package eu.scy.core.persistence.hibernate;

import org.springframework.test.AbstractTransactionalSpringContextTests;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 09.des.2009
 * Time: 05:55:18
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractDAOTest extends AbstractTransactionalSpringContextTests {
    protected String[] getConfigLocations() {
        return new String[]{"classpath:/eu/scy/core/persistence/hibernate/applciationContext-hibernate-OnlyForTesting.xml"};
    }
}
