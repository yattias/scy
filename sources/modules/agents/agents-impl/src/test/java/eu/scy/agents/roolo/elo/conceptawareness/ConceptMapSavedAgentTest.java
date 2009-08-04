package eu.scy.agents.roolo.elo.conceptawareness;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Configuration;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.server.Server;

import java.net.URI;
import java.rmi.dgc.VMID;
import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import roolo.elo.api.IMetadataValueContainer;

public class ConceptMapSavedAgentTest extends ConceptMapAgentsTestFixture {

	private ConceptMapSavedAgent agent;
	private TupleSpace ts;

	@Override
	@Before
	public void setUp() throws Exception {
		if (!Server.isRunning()) {
			Configuration.getConfiguration().setSSLEnabled(false);
			Server.startServer();
		}
		super.setUp();

		IMetadataValueContainer uriContainer = elo.getMetadata()
				.getMetadataValueContainer(
						typeManager.getMetadataKey("identifier"));
		uriContainer.setValue(new URI(
				"http://unittest.conceptmapsavedagent.de/1/bla.scymapping"));
		String agentId = new VMID().toString();
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("id", agentId);
		agent = new ConceptMapSavedAgent(map);
		agent.setMetadataTypeManager(typeManager);
		ts = new TupleSpace("command");
	}

	@After
	public void tearDown() {
		try {
			Server.stopServer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testProcessElo() throws TupleSpaceException {
		agent.processElo(elo);

		Tuple t = ts.waitToTake(new Tuple(String.class, Long.class,
				String.class), 10 * 1000);
		assertNotNull("no tuple written", t);
		assertEquals("scymapper", t.getField(0).getValue());
		assertEquals(
				"http://unittest.conceptmapsavedagent.de/1/bla.scymapping",
				// "roolo://memory/0/testELO1.scy",
				t.getField(2).getValue());
	}
}
