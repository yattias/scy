package eu.scy.agents.groupformation.strategies;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import roolo.elo.api.IELO;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import roolo.elo.metadata.keys.Contribute;
import eu.scy.agents.AbstractTestFixture;
import eu.scy.agents.AgentRooloServiceImpl;
import eu.scy.agents.groupformation.GroupFormationStrategy;
import eu.scy.agents.groupformation.cache.Group;
import eu.scy.agents.groupformation.cache.GroupCache;
import eu.scy.agents.helper.ELOFiller;

public class ClusterStrategyTest extends AbstractTestFixture {

	private GroupFormationStrategy strategy;
	private IELO referenceElo1;
	private Set<String> users;

	private void addEloToRepository(String eloPath, String eloType, String eloTitle, String user) throws IOException {
		IELO elo1 = this.loadElo(eloPath, eloType, eloTitle);
		ELOFiller eloFiller = new ELOFiller(elo1, this.typeManager);
		eloFiller.fillValue(CoreRooloMetadataKeyIds.AUTHOR.getId(), new Contribute(user, 1234L));
		this.repository.addNewELO(elo1);
	}

	@Before
	public void setup() throws IOException {
		this.referenceElo1 = this.loadElo("/eco_reference_concept_map.scymapper.xml", "scy/mapping", "referenceMap");
		this.strategy = new ClusterStrategy();
		this.users = new HashSet<String>();
		this.users.add("TestUser1@example.com");
		this.users.add("TestUser2@example.com");
		this.users.add("TestUser3@example.com");
		this.users.add("TestUser4@example.com");
		this.users.add("TestUser5@example.com");
		this.users.add("TestUser6@example.com");
		this.users.add("TestUser7@example.com");
		this.users.add("TestUser8@example.com");
		AgentRooloServiceImpl agentRooloService = new AgentRooloServiceImpl(this.repository);
		agentRooloService.setMetadataTypeManager(this.typeManager);
		this.strategy.setRooloServices(agentRooloService);
		// no need to add extractors, because they are added in the strategy constructor!
		// CMapFeatureExtractor cMapFeatureExtractor = new CMapFeatureExtractor();
		// cMapFeatureExtractor.setRepository(agentRooloService);
		// ((ClusterStrategy) this.strategy).addFeatureExtractor(cMapFeatureExtractor);
		// HypothesisFeatureExtractor hypothesisFeatureExtractor = new HypothesisFeatureExtractor();
		// hypothesisFeatureExtractor.setRepository(agentRooloService);
		// ((ClusterStrategy) this.strategy).addFeatureExtractor(hypothesisFeatureExtractor);
		this.strategy.setAvailableUsers(this.users);
		this.strategy.setMinimumGroupSize(2);
		this.strategy.setMaximumGroupSize(3);
		GroupCache groupFormationCache = new GroupCache();
		this.strategy.setGroupFormationCache(groupFormationCache);

		// add some SCYMapper ELOs that are stored as XML files:
		this.addEloToRepository("/ecoExpertMaps/expertMap1.scymapper.xml", "scy/mapping", "cmap1", "TestUser1");
		this.addEloToRepository("/ecoExpertMaps/expertMap1b.scymapper.xml", "scy/mapping", "cmap2", "TestUser2");
		this.addEloToRepository("/ecoExpertMaps/expertMap2.scymapper.xml", "scy/mapping", "cmap3", "TestUser3");
		this.addEloToRepository("/ecoExpertMaps/expertMap3.scymapper.xml", "scy/mapping", "cmap4", "TestUser4");
		this.addEloToRepository("/ecoSimpleMaps/simpleMap1.scymapper.xml", "scy/mapping", "cmap5", "TestUser5");
		this.addEloToRepository("/ecoSimpleMaps/simpleMap2.scymapper.xml", "scy/mapping", "cmap6", "TestUser6");
		// TestUser7 does not have a concept map!
		this.addEloToRepository("/ecoSimpleMaps/simpleMap3.scymapper.xml", "scy/mapping", "cmap8", "TestUser8");

		// some Copex ELOs are already in the database. The following is a test to check if all user ELOS can be
		// retrieved:
		try {
			IELO retrieveELO = this.repository.retrieveELO(new URI("roolo://memory/1/0/TestCopex1.scyxproc"));
			assertNotNull(retrieveELO);
			retrieveELO = this.repository.retrieveELO(new URI("roolo://memory/2/0/TestCopex2.scyxproc"));
			assertNotNull(retrieveELO);
			retrieveELO = this.repository.retrieveELO(new URI("roolo://memory/3/0/TestCopex3.scyxproc"));
			assertNotNull(retrieveELO);
			retrieveELO = this.repository.retrieveELO(new URI("roolo://memory/4/0/TestCopex4.scyxproc"));
			assertNotNull(retrieveELO);
			retrieveELO = this.repository.retrieveELO(new URI("roolo://memory/5/0/TestCopex5.scyxproc"));
			assertNotNull(retrieveELO);
			retrieveELO = this.repository.retrieveELO(new URI("roolo://memory/6/0/TestCopex6.scyxproc"));
			assertNotNull(retrieveELO);
			retrieveELO = this.repository.retrieveELO(new URI("roolo://memory/7/0/TestCopex7.scyxproc"));
			assertNotNull(retrieveELO);
			// TetUser8 does not have a Copex ELO!
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testFormGroup() {
		Collection<Group> groups = this.strategy.formGroup(this.referenceElo1);
		Group group = new Group();
		group.add("TestUser5@example.com");
		group.add("TestUser6@example.com");
		group.add("TestUser8@example.com");
		assertTrue(groups.contains(group));
		group.clear();
		group.add("TestUser1@example.com");
		group.add("TestUser2@example.com");
		group.add("TestUser3@example.com");
		group.add("TestUser4@example.com");
		group.add("TestUser7@example.com");
		assertTrue(groups.contains(group));
	}

}
