package eu.scy.agents;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Configuration;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.User;
import info.collide.sqlspaces.commons.Configuration.Database;
import info.collide.sqlspaces.server.Server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
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

	private static final String TM_MODEL_NAME = "co2_scy_english";

	protected static String TSHOST = "localhost";
	// protected static String TSHOST = "scy.collide.info";

	protected static int TSPORT = 2525;

	public static final boolean STANDALONE = true;

	protected IMetadataTypeManager typeManager;

	protected IExtensionManager extensionManager;

	protected IRepository repository;

	protected Map<String, Map<String, Object>> agentMap = new HashMap<String, Map<String, Object>>();

	private AgentManager agentFramework;

	private ArrayList<String> agentList;

	private TupleSpace tupleSpace;

	private PersistentStorage storage;

	private TupleSpace actionSpace;

	public AbstractTestFixture() {
	}

	public AbstractTestFixture(String host, int port) {
		TSHOST = host;
		TSPORT = port;
	}

	@Before
	public void setUp() throws Exception {
		tupleSpace = new TupleSpace(new User("test"), TSHOST, TSPORT, false, false,
				AgentProtocol.COMMAND_SPACE_NAME);
		actionSpace = new TupleSpace(new User("test"), TSHOST, TSPORT, false, false,
				AgentProtocol.ACTION_SPACE_NAME);

		agentMap.clear();

		agentList = new ArrayList<String>();

		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("test-config.xml");

		typeManager = (IMetadataTypeManager) applicationContext.getBean("metadataTypeManager");
		extensionManager = (IExtensionManager) applicationContext.getBean("extensionManager");
		repository = (IRepository) applicationContext.getBean("localRepository");

		storage = new PersistentStorage(TSHOST, TSPORT);
	}

	@SuppressWarnings("unused")
	@After
	public void tearDown() throws AgentLifecycleException {
		System.err.println("super.tearDown");
		if (tupleSpace != null) {
			try {
				tupleSpace.takeAll(new Tuple());
				actionSpace.takeAll(new Tuple());
				tupleSpace.disconnect();
				actionSpace.disconnect();
				System.err.println("********** Disconnected from TS ******************");
			} catch (TupleSpaceException e) {
				e.printStackTrace();
			}
		}
		storage.close();
	}

	protected void removeTopicModel() {
		storage.remove(TM_MODEL_NAME);
	}

	protected IELO createNewElo() {
		BasicELO elo = new BasicELO();
		elo.setIdentifierKey(typeManager.getMetadataKey(CoreRooloMetadataKeyIds.IDENTIFIER.getId()));
		return elo;
	}

	protected IELO createNewElo(String title, String type) {
		BasicELO elo = new BasicELO();
		elo.setIdentifierKey(typeManager.getMetadataKey(CoreRooloMetadataKeyIds.IDENTIFIER.getId()));
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
			// conf.setWebServicesEnabled(false);
			conf.setShellEnabled(false);
			Server.startServer();
		}
	}

	protected static void stopTupleSpaceServer() {
		Server.stopServer();
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

	public TupleSpace getCommandSpace() {
		return tupleSpace;
	}

	public TupleSpace getActionSpace() {
		return actionSpace;
	}

	protected void stopAgentFrameWork() throws AgentLifecycleException {
		for (String agentId : agentList) {
			agentFramework.killAgent(agentId);
		}
	}

	protected PersistentStorage getPersistentStorage() {
		return storage;
	}

	protected void initTopicModel() {
		ObjectInputStream in = null;
		try {
			InputStream inStream = this.getClass().getResourceAsStream("/model.dat");
			in = new ObjectInputStream(inStream);
			TopicModelParameter model = (TopicModelParameter) in.readObject();
			in.close();
			storage.put(TM_MODEL_NAME, model);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	protected Tuple getTestActionTuple(String eloUri, String type, long currentTimeInMillis, String uuid) {
		return new Tuple("action", uuid, currentTimeInMillis, AgentProtocol.ACTION_ELO_SAVED, "testUser", "SomeTool",
				"SomeMission", "TestSession", eloUri, "type=" + type);
	}
}
