package eu.scy.agents.roolo.elo.elobrowsernotification;

import eu.scy.agents.impl.AbstractELOSavedAgent;
import eu.scy.agents.impl.ActionConstants;
import eu.scy.agents.impl.AgentProtocol;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.io.InputStream;
import java.rmi.dgc.VMID;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * Notifies tools that an elo has been saved. ELO is saved ->
 * ("notifyEloBrowser":String, <ELOUri>:String)
 * 
 * @author Florian Schulz
 */
public class ELOHasBeenSavedAgent extends AbstractELOSavedAgent {

	private static final String NAME = ELOHasBeenSavedAgent.class.getName();
	private static final Object TYPE = "type=elo_show";
	private Set<String> whitelist;

	/**
	 * Create a new ELOHasBeenSavedAgent filtering agent. The argument
	 * <code>map</code> is used to initialize special parameters. Never used
	 * here.
	 * 
	 * @param params
	 *            Parameters needed to initialize the agent.
	 */
	public ELOHasBeenSavedAgent(Map<String, Object> params) {
		super(NAME, (String) params.get(AgentProtocol.PARAM_AGENT_ID));
		if (params.containsKey(AgentProtocol.TS_HOST)) {
			this.host = (String) params.get(AgentProtocol.TS_HOST);
		}
		if (params.containsKey(AgentProtocol.TS_PORT)) {
			this.port = (Integer) params.get(AgentProtocol.TS_PORT);
		}
		readWhitelist();
	}

	private void readWhitelist() {
		whitelist = new HashSet<String>();
		InputStream blacklistAsStream = getClass().getResourceAsStream(
				"/elo_type.whitelist");
		Scanner scanner = new Scanner(blacklistAsStream);
		while (scanner.hasNext()) {
			whitelist.add(scanner.next().trim());
		}
	}

	@Override
	public void processELOSavedAction(String actionId, String user,
			long timeInMillis, String tool, String mission, String session,
			String eloUri, String eloType) {
		if (!whitelist.contains(eloType)) {
			return;
		}

		Tuple notificationTuple = new Tuple(AgentProtocol.NOTIFICATION,
				new VMID().toString(), user, tool, NAME, mission, session,
				ActionConstants.ACTIONLOG_ELO_URI + "=" + eloUri,
				ActionConstants.ACTIONLOG_ELO_TYPE + "=" + eloType, TYPE);

		try {
			this.getCommandSpace().write(notificationTuple);
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		}
	}
}
