package eu.scy.agents;

import info.collide.sqlspaces.commons.Configuration;
import info.collide.sqlspaces.commons.Configuration.Database;
import info.collide.sqlspaces.server.Server;

import java.util.Map;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import roolo.api.IExtensionManager;
import roolo.api.IRepository;
import roolo.elo.BasicELO;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.impl.manager.AgentManager;

public class AbstractTestFixture {

	protected IMetadataTypeManager typeManager;
	protected IExtensionManager extensionManager;
	protected IRepository repository;
	private static Configuration conf;

	protected void setUp() throws Exception {
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"test-config.xml");

		typeManager = (IMetadataTypeManager) applicationContext
				.getBean("metadataTypeManager");
		extensionManager = (IExtensionManager) applicationContext
				.getBean("extensionManager");
		repository = (IRepository) applicationContext
				.getBean("localRepository");
	}

	protected IELO createNewElo() {
		BasicELO elo = new BasicELO();
		elo.setIdentifierKey(typeManager
				.getMetadataKey(CoreRooloMetadataKeyIds.IDENTIFIER.getId()));
		return elo;
	}

	protected static void startTupleSpaceServer() {
		if (!Server.isRunning()) {
			conf = Configuration.getConfiguration();
			conf.setSSLEnabled(false);
			conf.setDbType(Database.HSQL);
			conf.setWebEnabled(false);
			conf.setWebServicesEnabled(false);

			Server.startServer();
		}
	}

	protected static void stopTupleSpaceServer() {
		if (Server.isRunning()) {
			Server.stopServer();
		}
	}

	public static void startAgentFramework(
			Map<String, Map<String, Object>> agents) {
		AgentManager agentFramework = new AgentManager("localhost", conf
				.getNonSSLPort());
		for (String agentName : agents.keySet()) {
			Map<String, Object> params = agents.get(agentName);
			try {
				agentFramework.startAgent(agentName, params);
			} catch (AgentLifecycleException e) {
				// TODO what to do with these exception.
				e.printStackTrace();
			}
		}

	}
}
