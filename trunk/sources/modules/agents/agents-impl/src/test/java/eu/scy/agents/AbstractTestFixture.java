package eu.scy.agents;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Configuration;
import info.collide.sqlspaces.commons.Configuration.Database;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.User;
import info.collide.sqlspaces.server.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import roolo.elo.metadata.keys.KeyValuePair;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.impl.ActionConstants;
import eu.scy.agents.impl.AgentProtocol;
import eu.scy.agents.impl.manager.AgentManager;

public class AbstractTestFixture {
	protected static final String MISSION1 = "roolo://memory/0/0/Design+a+CO2-friendly+house.scymissionspecification";
	// private static final String TM_MODEL_NAME = "TopicModel";
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
	// private PersistentStorage storage;
	private TupleSpace actionSpace;
	protected static ClassPathXmlApplicationContext applicationContext;

	public AbstractTestFixture() {
	}

	public AbstractTestFixture(String host, int port) {
		TSHOST = host;
		TSPORT = port;
	}

	@Before
	public void setUp() throws Exception {
		if (Server.isRunning()) {
			tupleSpace = new TupleSpace(new User("test"), TSHOST, TSPORT,
					false, false, AgentProtocol.COMMAND_SPACE_NAME);
			actionSpace = new TupleSpace(new User("test"), TSHOST, TSPORT,
					false, false, AgentProtocol.ACTION_SPACE_NAME);
		}

		agentMap.clear();

		agentList = new ArrayList<String>();

		if (applicationContext == null) {
			readApplicationContext();
		}
		typeManager = (IMetadataTypeManager) applicationContext
				.getBean("metadataTypeManager");
		extensionManager = (IExtensionManager) applicationContext
				.getBean("extensionManager");
		repository = (IRepository) applicationContext
				.getBean("localRepository");

	}

	private static void readApplicationContext() {
		applicationContext = new ClassPathXmlApplicationContext(
				"test-config.xml");
	}

	@After
	public void tearDown() throws AgentLifecycleException {
		if (tupleSpace != null) {
			try {
				tupleSpace.takeAll(new Tuple());
				actionSpace.takeAll(new Tuple());
				tupleSpace.disconnect();
				actionSpace.disconnect();
				System.err
						.println("********** Disconnected from TS ******************");
			} catch (TupleSpaceException e) {
				e.printStackTrace();
			}
		}
	}

	protected void removeTopicModel() {
		// storage.remove(MISSION1, "en", TM_MODEL_NAME);
	}

	protected void removeDFModel() {
		// storage.remove(MISSION1, "en",
		// KeywordWorkflowConstants.DOCUMENT_FREQUENCY_MODEL);
	}

	protected IELO createNewElo() {
		BasicELO elo = new BasicELO();
		return elo;
	}

	protected IELO createNewElo(String title, String type) {
		BasicELO elo = new BasicELO();
		IMetadataValueContainer titleContainer = elo.getMetadata()
				.getMetadataValueContainer(
						typeManager
								.getMetadataKey(CoreRooloMetadataKeyIds.TITLE
										.getId()));
		titleContainer.setValue(title);
		IMetadataValueContainer typeContainer = elo
				.getMetadata()
				.getMetadataValueContainer(
						typeManager
								.getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT
										.getId()));
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
		readApplicationContext();
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
				agentList.add(agentFramework.startAgent(agentName, params)
						.getId());
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

	// protected PersistentStorage getPersistentStorage() {
	// return storage;
	// }

	// protected void initTopicModel() {
	// ObjectInputStream in = null;
	// try {
	// InputStream inStream = this.getClass().getResourceAsStream(
	// "/models/co2_en_tm");
	// // InputStream inStream = this.getClass().getResourceAsStream(
	// // "/models/eco_en_tm");
	// in = new ObjectInputStream(inStream);
	// ParallelTopicModel model = (ParallelTopicModel) in.readObject();
	// in.close();
	// storage.put(MISSION1, "en", TM_MODEL_NAME, model);
	// } catch (IOException e) {
	// e.printStackTrace();
	// } catch (ClassNotFoundException e) {
	// e.printStackTrace();
	// }
	// }

	protected Tuple getTestActionTuple(String eloUri, String type,
			long currentTimeInMillis, String uuid) {
		return new Tuple("action", uuid, currentTimeInMillis,
				ActionConstants.ACTION_ELO_SAVED, "testUser", "SomeTool",
				"SomeMission", "TestSession", eloUri, "type=" + type);
	}

	protected boolean hasItems(List<String> keywords, String... values) {
		for (String value : values) {
			if (!keywords.contains(value)) {
				return false;
			}
		}
		return true;
	}

	protected boolean hasKeywords(List<KeyValuePair> keywords, String... values) {
		// get just the keywords from the list of KeyValuePairs containing Pairs
		// (Keyword, Boost)
		List<String> keywordStrings = new ArrayList<String>();
		for (KeyValuePair keyValuePair : keywords) {
			keywordStrings.add(keyValuePair.getKey());
		}
		for (String value : values) {
			if (!keywordStrings.contains(value)) {
				return false;
			}
		}
		return true;
	}

	protected String readFile(InputStream inStream) throws IOException {
		// reads text from file and creates one String
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				inStream));
		String text = "";
		String line = "";
		while ((line = reader.readLine()) != null) {
			if (line.matches("\\s")) {
				continue;
			}
			text = text + " " + line;
		}
		reader.close();
		return text;
	}

	// protected void initDfModel() throws ClassNotFoundException, IOException {
	// InputStream inStream = this.getClass().getResourceAsStream(
	// "/models/co2_en_DFmodel.dat");
	// ObjectInputStream in = new ObjectInputStream(inStream);
	// DocumentFrequencyModel dfModel = (DocumentFrequencyModel) in
	// .readObject();
	// this.getPersistentStorage().put(MISSION1, "en",
	// KeywordWorkflowConstants.DOCUMENT_FREQUENCY_MODEL, dfModel);
	// }
}
