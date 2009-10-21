package eu.scy.agents.topics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import roolo.elo.api.IELO;
import roolo.elo.api.IMetadata;
import eu.scy.agents.AbstractTestFixture;

public class TextForTopicSavedTest extends AbstractTestFixture {

	@BeforeClass
	public static void startTS() {
		startTupleSpaceServer();
	}

	@AfterClass
	public static void stopTS() {
		stopTupleSpaceServer();
	}

	private TextForTopicSaved agent;
	private IELO elo;

	@Before
	public void setUp() throws Exception {
		super.setUp();

		agent = new TextForTopicSaved();
		agent.setMetadataTypeManager(typeManager);

		elo = createNewElo("testElo", "scy/text");
		IMetadata data = repository.addNewELO(elo);
		elo.setMetadata(data);
	}

	@Test
	public void testProcessElo() throws TupleSpaceException {
		agent.processElo(elo);

		Tuple tuple = getTupleSpace().waitToTake(
				new Tuple("topicDetector", String.class), 5000);
		if (tuple == null) {
			fail("no tuple sent");
		}
		assertEquals("Uri not the same", elo.getUri().toString(), tuple
				.getField(1).getValue());
	}
}
