package eu.scy.agents.roolo.elo.elobrowsernotification;

import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.rmi.dgc.VMID;
import java.util.Map;

import eu.scy.agents.impl.AbstractELOSavedAgent;
import eu.scy.agents.impl.AgentProtocol;

/**
 * Notifies tools that an elo has been saved. ELO is saved -> ("notifyEloBrowser":String, <ELOUri>:String)
 * 
 * @author Florian Schulz
 */
public class ELOHasBeenSavedAgent extends AbstractELOSavedAgent {

	private static final String NAME = ELOHasBeenSavedAgent.class.getName();

	/**
	 * Create a new ELOHasBeenSavedAgent filtering agent. The argument <code>map</code> is used to initialize special
	 * parameters. Never used here.
	 * 
	 * @param map Parameters needed to initialize the agent.
	 */
	public ELOHasBeenSavedAgent(Map<String, Object> params) {
		super(NAME, (String) params.get(AgentProtocol.PARAM_AGENT_ID));
		if (params.containsKey(AgentProtocol.TS_HOST)) {
			this.host = (String) params.get(AgentProtocol.TS_HOST);
		}
		if (params.containsKey(AgentProtocol.TS_PORT)) {
			this.port = (Integer) params.get(AgentProtocol.TS_PORT);
		}
	}

	// @Override
	// public void processElo(IELO elo) {
	// if (elo == null) {
	// return;
	// }
	//
	// if (elo.getUri() == null) {
	// return;
	// }
	//
	// Tuple tuple = new Tuple("notifyEloBrowser", elo.getUri().toString());
	//
	// try {
	// getCommandSpace().write(tuple);
	// } catch (TupleSpaceException e) {
	// e.printStackTrace();
	// }
	// // sender.send("roolo", "roolo", notification);
	// }

	// @Override
	// protected void processELOSavedAction(IAction action) {
	//
	// }

	@Override
	protected void processELOSavedAction(String actionId, String user, long timeInMillis, String tool, String mission,
			String session, String eloUri, String eloType) {

		Tuple notificationTuple = new Tuple(AgentProtocol.NOTIFICATION, new VMID().toString(), user, tool, NAME,
				mission, session, AgentProtocol.ACTIONLOG_ELO_URI + "=" + eloUri, AgentProtocol.ACTIONLOG_ELO_TYPE
						+ "=" + eloType);

		try {
			this.getCommandSpace().write(notificationTuple);
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		}
	}
}
