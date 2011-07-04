package eu.scy.agents.groupformation.strategies;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import roolo.elo.api.IELO;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import roolo.elo.metadata.keys.Contribute;
import roolo.search.ISearchResult;
import roolo.search.MetadataQueryComponent;
import roolo.search.Query;
import eu.scy.agents.AbstractTestFixture;
import eu.scy.agents.groupformation.GroupFormationStrategy;
import eu.scy.agents.groupformation.cache.GroupCache;
import eu.scy.agents.groupformation.strategies.features.CMapFeatureExtractor;
import eu.scy.agents.helper.ELOFiller;

public class ClusterStrategyTest extends AbstractTestFixture {

	private GroupFormationStrategy strategy;
    private IELO referenceElo;
    private Set<String> users;
    
    private void addEloToRepository(String eloPath, String eloType, String eloTitle, String user) throws IOException{
        IELO elo1 = loadElo(eloPath, eloType, eloTitle);
        ELOFiller eloFiller = new ELOFiller(elo1, this.typeManager);
        eloFiller.fillValue(CoreRooloMetadataKeyIds.AUTHOR.getId(), new Contribute(user,1234L));
        repository.addNewELO(elo1);
    }

    private ISearchResult retrieveEloFromRepository(String user){
        MetadataQueryComponent mcq = new MetadataQueryComponent(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT.getId(),"scy/mapping");
        Query q = new Query(mcq);
        HashSet <String> allowedUsers = new HashSet <String>();
        allowedUsers.add(user);
        q.setAllowedUsers(allowedUsers);
        List<ISearchResult> res = repository.search(q);
        assertEquals(1, res.size());
        return res.get(0);
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
		((ClusterStrategy) strategy).addFeatureExtractor(new CMapFeatureExtractor());
		strategy.setAvailableUsers(users);
		strategy.setMinimumGroupSize(2);
		strategy.setMaximumGroupSize(3);
		GroupCache groupFormationCache = new GroupCache();
		strategy.setGroupFormationCache(groupFormationCache);
		strategy.setRepository(repository);


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
//	    Set<String> availableUsers = ((ClusterStrategy) strategy).getAvailableUsers();
//	    for (Iterator uIt = availableUsers.iterator(); uIt.hasNext();) {
//            String user = (String) uIt.next();
//            ISearchResult eloUri = retrieveEloFromRepository(user);
//            IELO elo = repository.retrieveELO(eloUri.getUri());
//            elo.getXml();
//        }
	    Collection<Set<String>> groups = strategy.formGroup(referenceElo) ;
	    Set<String> group = new HashSet<String>();
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
