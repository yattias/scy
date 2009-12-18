package eu.scy.agents.topics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import roolo.elo.api.IELO;
import roolo.elo.api.IMetadata;
import eu.scy.agents.AbstractTestFixture;

public class TextForTopicSavedTest extends AbstractTestFixture {

	private TextForTopicSaved agent;
	private IELO elo;

	@BeforeClass
	public static void startTS() {
	}

	@AfterClass
	public static void stopTS() {
//		stopTupleSpaceServer();
	}

	@Override
	@Before
	public void setUp() throws Exception {
                startTupleSpaceServer();
		super.setUp();

		agent = new TextForTopicSaved(TSHOST, TSPORT);

		agent.setMetadataTypeManager(typeManager);

		elo = createNewElo("testElo", "scy/text");
		IMetadata data = repository.addNewELO(elo);
		elo.setMetadata(data);
	}

	@Test
	public void testProcessElo() throws TupleSpaceException {
		agent.processElo(elo);

		Tuple tuple = getTupleSpace().waitToRead(new Tuple("topicDetector", String.class), 5000);

		assertNotNull("no tuple sent", tuple);
		getTupleSpace().takeTupleById(tuple.getTupleID());
		assertEquals("Uri not the same", elo.getUri().toString(), tuple.getField(1).getValue());
	}

	@After
	public void tearDown() {
		super.stopTupleSpaceServer();
	}
}
