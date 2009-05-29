package eu.scy.agents.roolo.elo.conceptawareness;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataValueContainer;

public class EnrichConceptMapAgentTest extends ConceptMapAgentsTestFixture {

	private EnrichConceptMapAgent<IELO<IMetadataKey>, IMetadataKey> agent;

	@Before
	public void setUp() throws Exception {
		super.setUp();

		agent = new EnrichConceptMapAgent<IELO<IMetadataKey>, IMetadataKey>();
		agent.setMetadataTypeManager(typeManager);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testProcessElo() {
		agent.processElo(elo);
		IMetadataValueContainer nodeLabelsContainer = elo.getMetadata()
				.getMetadataValueContainer(
						typeManager.getMetadataKey("nodeLabel"));
		List<String> nodeLabels = (List<String>) nodeLabelsContainer
				.getValueList();
		assertEquals(8, nodeLabels.size());
		assertEquals("total moment", nodeLabels.get(0));
		assertEquals("moment child 1", nodeLabels.get(1));
		assertEquals("force", nodeLabels.get(2));
		assertEquals("moment child 2", nodeLabels.get(3));
		assertEquals("mass", nodeLabels.get(4));
		assertEquals("moment", nodeLabels.get(5));
		assertEquals("balance state", nodeLabels.get(6));
		assertEquals("distance", nodeLabels.get(7));

		IMetadataValueContainer linkLabelsContainer = elo.getMetadata()
				.getMetadataValueContainer(
						typeManager.getMetadataKey("linkLabel"));
		List<String> linkLabels = (List<String>) linkLabelsContainer
				.getValueList();
		assertEquals(5, linkLabels.size());
		assertEquals("-", linkLabels.get(0));
		assertEquals("+", linkLabels.get(1));
		assertEquals("is a", linkLabels.get(2));
		assertEquals("13768021-13768021", linkLabels.get(3));
		assertEquals("Determines", linkLabels.get(4));
	}
}
