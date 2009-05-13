/**
 * 
 */
package eu.scy.agents.roolo.impl;

import static org.junit.Assert.assertEquals;
import info.collide.sqlspaces.commons.Configuration;
import info.collide.sqlspaces.server.Server;

import java.util.logging.Level;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eu.scy.agents.api.IPersistentStorage;
import eu.scy.agents.impl.PersistentStorage;

/**
 * @author fschulz
 */
public class PersistentStorageTest {
    
    private IPersistentStorage storage;
    
    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        if (!Server.isRunning()) {
            Configuration.getConfiguration().setDbUser("root");
            Configuration.getConfiguration().setDbPassword("");
            Configuration.getConfiguration().setMysqlSchema("sqlspaces");
            Configuration.getConfiguration().setLogLevel(Level.FINE);
            Server.startServer();
        }
        storage = new PersistentStorage();
    }
    
    @After
    public void tearDown() {
        Server.stopServer();
    }
    
    /**
     * Test method for
     * {@link eu.scy.agents.roolo.impl.PersistentStorage#get(java.lang.String)}.
     */
    @Test
    public void testGetPut() {
        storage.put("Test1", 5);
        Integer five = storage.get("Test1");
        assertEquals(5, five.intValue());
        
        storage.put("Test2", "Testasd adsa dsa");
        String string = storage.get("Test2");
        assertEquals("Testasd adsa dsa", string);
    }
    
}
