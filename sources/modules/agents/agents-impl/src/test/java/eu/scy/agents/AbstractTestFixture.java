package eu.scy.agents;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Configuration;
import info.collide.sqlspaces.commons.User;
import info.collide.sqlspaces.commons.Configuration.Database;
import info.collide.sqlspaces.server.Server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import roolo.api.IExtensionManager;
import roolo.api.IRepository;
import roolo.elo.BasicELO;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.IMetadataValueContainer;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import cc.mallet.topics.TopicModelParameter;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.impl.AgentProtocol;
import eu.scy.agents.impl.PersistentStorage;
import eu.scy.agents.impl.manager.AgentManager;

public class AbstractTestFixture {

	protected static final String TSHOST = "localhost";
	// protected static String TSHOST = "scy.collide.info";

	protected static final int TSPORT = 2525;

	public static final boolean STANDALONE = true;

	protected IMetadataTypeManager typeManager;

	protected IExtensionManager extensionManager;

	protected IRepository repository;

	protected Map<String, Map<String, Object>> agentMap = new HashMap<String, Map<String, Object>>();

	private AgentManager agentFramework;

	private ArrayList<String> agentList;

	private TupleSpace tupleSpace;

	protected void setUp() throws Exception {
		tupleSpace = new TupleSpace(new User("test"), TSHOST, TSPORT, false, false,
				AgentProtocol.COMMAND_SPACE_NAME);

		agentMap.clear();

		agentList = new ArrayList<String>();

		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"test-config.xml");

		typeManager = (IMetadataTypeManager) applicationContext.getBean("metadataTypeManager");
		extensionManager = (IExtensionManager) applicationContext.getBean("extensionManager");
		repository = (IRepository) applicationContext.getBean("localRepository");
	}

	protected IELO createNewElo() {
		BasicELO elo = new BasicELO();
		elo
				.setIdentifierKey(typeManager.getMetadataKey(CoreRooloMetadataKeyIds.IDENTIFIER
						.getId()));
		return elo;
	}

	protected IELO createNewElo(String title, String type) {
		BasicELO elo = new BasicELO();
		elo
				.setIdentifierKey(typeManager.getMetadataKey(CoreRooloMetadataKeyIds.IDENTIFIER
						.getId()));
		IMetadataValueContainer titleContainer = elo.getMetadata().getMetadataValueContainer(
				typeManager.getMetadataKey(CoreRooloMetadataKeyIds.TITLE.getId()));
		titleContainer.setValue(title);
		IMetadataValueContainer typeContainer = elo.getMetadata().getMetadataValueContainer(
				typeManager.getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT.getId()));
		typeContainer.setValue(type);
		return elo;
	}

	protected static void startTupleSpaceServer() {
		if (!Server.isRunning()) {
			Configuration conf = Configuration.getConfiguration();
			conf.setNonSSLPort(TSPORT);
			conf.setSSLEnabled(false);
			conf.setDbType(Database.HSQL);
			conf.setWebEnabled(false);
			conf.setWebServicesEnabled(false);
			Server.startServer();
		}
	}

	protected static void stopTupleSpaceServer() {
		// if (STANDALONE) {
		Server.stopServer();
		// }
	}

	public void startAgentFramework(Map<String, Map<String, Object>> agents) {
		agentList.clear();
		agentFramework = new AgentManager(TSHOST, TSPORT);
		agentFramework.setRepository(repository);
		agentFramework.setMetadataTypeManager(typeManager);
		for (String agentName : agents.keySet()) {
			Map<String, Object> params = agents.get(agentName);
			try {
				agentList.add(agentFramework.startAgent(agentName, params).getId());
			} catch (AgentLifecycleException e) {
				// TODO what to do with these exception.
				e.printStackTrace();
			}
		}
	}

	public TupleSpace getTupleSpace() {
		return tupleSpace;
	}

	protected void stopAgentFrameWork() throws AgentLifecycleException {
		for (String agentId : agentList) {
			agentFramework.killAgent(agentId);
		}
	}

	protected PersistentStorage getPersistentStorage() {
		return new PersistentStorage(TSHOST, TSPORT);
	}

	protected void initModel() {
		ObjectInputStream in = null;
		try {
			InputStream inStream = this.getClass().getResourceAsStream("/model.dat");
			in = new ObjectInputStream(inStream);
			TopicModelParameter model = (TopicModelParameter) in.readObject();
			in.close();
			PersistentStorage storage = getPersistentStorage();
			storage.put("co2_scy_english", model);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
