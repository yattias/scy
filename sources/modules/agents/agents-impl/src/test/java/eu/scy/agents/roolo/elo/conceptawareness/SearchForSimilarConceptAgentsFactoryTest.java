package eu.scy.agents.roolo.elo.conceptawareness;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import info.collide.sqlspaces.commons.Configuration;
import info.collide.sqlspaces.server.Server;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import roolo.cms.repository.mock.MockRepository;
import roolo.elo.api.IMetadataKey;
import eu.scy.agents.api.IThreadedAgent;
import eu.scy.agents.impl.Parameter;

public class SearchForSimilarConceptAgentsFactoryTest extends
		ConceptMapAgentsTestFixture {

	private SearchForSimilarConceptAgentsFactory factory;
	private Parameter params;
	private MockRepository<IMetadataKey> repo;

	@Before
	public void setUp() {
		// if (!Server.isRunning()) {
		// Configuration.getConfiguration().setSSLEnabled(false);
		// Server.startServer();
		// }

		params = new Parameter();
		params.set(Parameter.METADATA_TYPE_MANAGER, typeManager);
		repo = new MockRepository<IMetadataKey>();
		params.set(Parameter.ELO_REPOSITORY, repo);
		factory = new SearchForSimilarConceptAgentsFactory();
	}

	@After
	public void tearDown() {
		// Server.stopServer();
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testCreate() {
		IThreadedAgent agent = factory.create(params);
		assertNotNull("Agent is null", agent);
		assertTrue("not right instance",
				agent instanceof SearchForSimilarConceptsAgent);
		assertSame(repo, ((SearchForSimilarConceptsAgent<IMetadataKey>) agent)
				.getRepository());
		assertSame(typeManager,
				((SearchForSimilarConceptsAgent<IMetadataKey>) agent)
						.getMetadataTypeManager());

	}

	@Test
	public void testGetAgentName() {
		assertEquals(
				SearchForSimilarConceptsAgent.SEARCH_FOR_SIMILAR_CONCEPTS_AGENT_NAME,
				factory.getAgentName());
	}
}
