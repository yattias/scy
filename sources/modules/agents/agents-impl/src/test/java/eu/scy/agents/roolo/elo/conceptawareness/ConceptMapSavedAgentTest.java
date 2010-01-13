package eu.scy.agents.roolo.elo.conceptawareness;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.net.URI;
import java.rmi.dgc.VMID;
import java.util.HashMap;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataValueContainer;
import eu.scy.agents.AbstractTestFixture;

public class ConceptMapSavedAgentTest extends AbstractTestFixture {

	private ConceptMapSavedAgent agent;
	private IELO elo;

	@BeforeClass
	public static void beforeAll() {
		startTupleSpaceServer();
	}

	@AfterClass
	public static void afterAll() {
		stopTupleSpaceServer();
	}

	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();

		elo = createNewElo("TestELO", "scy/scymapping");

		IMetadataValueContainer uriContainer = elo.getMetadata().getMetadataValueContainer(
				typeManager.getMetadataKey("identifier"));
		uriContainer.setValue(new URI("http://unittest.conceptmapsavedagent.de/1/bla.scymapping"));
		String agentId = new VMID().toString();
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("id", agentId);
		map.put("tsHost", TSHOST);
		map.put("tsPort", TSPORT);
		agent = new ConceptMapSavedAgent(map);
		// agent.setTuplespaceFactory(tuplespaceFactory);
		agent.setMetadataTypeManager(typeManager);
	}

	@Test
	public void testProcessElo() throws TupleSpaceException {
		agent.processElo(elo);

		Tuple t = getTupleSpace().waitToTake(new Tuple("scymapper", Long.class, String.class), 5 * 1000);
		assertNotNull("no tuple written", t);
		assertEquals("scymapper", t.getField(0).getValue());
		assertEquals("http://unittest.conceptmapsavedagent.de/1/bla.scymapping",
		// "roolo://memory/0/testELO.scy",
				t.getField(2).getValue());
	}
}
