package eu.scy.agents.groupformation.strategies;

import eu.scy.agents.AbstractTestFixture;
import eu.scy.agents.groupformation.GroupFormationStrategy;
import eu.scy.agents.groupformation.cache.Group;
import eu.scy.agents.groupformation.cache.GroupCache;
import eu.scy.agents.groupformation.strategies.features.CMapFeatureExtractor;
import eu.scy.agents.helper.ELOFiller;
import eu.scy.agents.impl.AgentRooloServiceImpl;
import eu.scy.common.scyelo.RooloServices;
import org.junit.Before;
import org.junit.Test;
import roolo.elo.api.IELO;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import roolo.elo.metadata.keys.Contribute;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertTrue;

public class ClusterStrategyTest extends AbstractTestFixture {

    private GroupFormationStrategy strategy;
    private IELO referenceElo;
    private Set<String> users;

    private void addEloToRepository(String eloPath, String eloType, String eloTitle, String user) throws IOException {
        IELO elo1 = loadElo(eloPath, eloType, eloTitle);
        ELOFiller eloFiller = new ELOFiller(elo1, this.typeManager);
        eloFiller.fillValue(CoreRooloMetadataKeyIds.AUTHOR.getId(), new Contribute(user, 1234L));
        repository.addNewELO(elo1);
    }

    @Before
    public void setup() throws IOException {
        this.referenceElo = this.loadElo("/eco_reference_concept_map.scymapper.xml", "TestInterview", "scy/interview");
        strategy = new ClusterStrategy();
        users = new HashSet<String>();
        users.add("TestUser1");
        users.add("TestUser2");
        users.add("TestUser3");
        users.add("TestUser4");
        users.add("TestUser5");
        users.add("TestUser6");
        users.add("TestUser7");
        ( (ClusterStrategy) strategy ).addFeatureExtractor(new CMapFeatureExtractor());
        strategy.setAvailableUsers(users);
        strategy.setMinimumGroupSize(2);
        strategy.setMaximumGroupSize(3);
        GroupCache groupFormationCache = new GroupCache();
        strategy.setGroupFormationCache(groupFormationCache);
        RooloServices rooloServices = new AgentRooloServiceImpl(repository);
        strategy.setRooloServices(rooloServices);


        addEloToRepository("/ecoExpertMaps/expertMap1.scymapper.xml", "scy/mapping", "cmap1", "TestUser1");
        addEloToRepository("/ecoExpertMaps/expertMap1b.scymapper.xml", "scy/mapping", "cmap2", "TestUser2");
        addEloToRepository("/ecoExpertMaps/expertMap2.scymapper.xml", "scy/mapping", "cmap3", "TestUser3");
        addEloToRepository("/ecoExpertMaps/expertMap3.scymapper.xml", "scy/mapping", "cmap4", "TestUser4");
        addEloToRepository("/ecoSimpleMaps/simpleMap1.scymapper.xml", "scy/mapping", "cmap5", "TestUser5");
        addEloToRepository("/ecoSimpleMaps/simpleMap2.scymapper.xml", "scy/mapping", "cmap6", "TestUser6");
        addEloToRepository("/ecoSimpleMaps/simpleMap3.scymapper.xml", "scy/mapping", "cmap7", "TestUser7");
    }

    @Test
    public void testFormGroup() {
        Collection<Group> groups = strategy.formGroup(referenceElo);
        Group group = new Group();
        group.add("TestUser5");
        assertTrue(groups.contains(group));
        group.clear();
        group.add("TestUser1");
        group.add("TestUser2");
        group.add("TestUser3");
        group.add("TestUser4");
        assertTrue(groups.contains(group));
        group.clear();
        group.add("TestUser6");
        group.add("TestUser7");
        assertTrue(groups.contains(group));
    }

}
