package eu.scy.agents;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;

import net.sf.cglib.core.Local;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import roolo.elo.api.IELO;
import roolo.elo.api.IELOFactory;

import eu.scy.common.mission.AgentModelElo;
import eu.scy.common.mission.AgentModelEloContent;
import eu.scy.common.mission.MissionSpecificationElo;

public class AgentModelEloTest extends AbstractTestFixture {

	private TestRooloServiceImpl rooloServices;

	@Before
	public void setup() {
		rooloServices = new TestRooloServiceImpl(repository);
		rooloServices.setMetadataTypeManager(typeManager);
		rooloServices.setExtensionManager(extensionManager);
		IELOFactory eloFactory = (IELOFactory) applicationContext
				.getBean("localEloFactory");
		rooloServices.setEloFactory(eloFactory);
	}

	@After
	public void tearDown() {

	}

	@Test
	public void testGetMissionElo() throws URISyntaxException {
		MissionSpecificationElo missionSpecificationElo = MissionSpecificationElo
				.loadElo(
						new URI(
								"roolo://memory/0/0/Design+a+CO2-friendly+house.scymissionspecification"),
						rooloServices);
		assertNotNull(missionSpecificationElo);
		assertNotNull(missionSpecificationElo.getTypedContent()
				.getAgentModelsEloUri());
		AgentModelElo modelElo = AgentModelElo.loadElo(missionSpecificationElo
				.getTypedContent().getAgentModelsEloUri(), rooloServices);
		assertNotNull(modelElo);
		AgentModelEloContent agentModelElos = modelElo.getTypedContent();
		assertNotNull(agentModelElos);
		assertFalse(agentModelElos.getKeys().isEmpty());
		assertNotNull(agentModelElos.getModelEloUri("topicModel",
				Locale.ENGLISH));
		assertNotNull(agentModelElos.getModelEloUri("topicModel", new Locale(
				"et", "EE")));
		
		URI modelEloUri = agentModelElos.getModelEloUri("topicModel", "en");
		IELO modelElo2 = rooloServices.getRepository().retrieveELO(modelEloUri);
		assertNotNull(modelElo2);
	}
}
