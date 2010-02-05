package eu.scy.agents.roolo.elo.elobrowsernotification;

import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.util.Map;

import eu.scy.actionlogging.api.IAction;
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
			host = (String) params.get(AgentProtocol.TS_HOST);
		}
		if (params.containsKey(AgentProtocol.TS_PORT)) {
			port = (Integer) params.get(AgentProtocol.TS_PORT);
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

	@Override
	protected void processELOSavedAction(IAction action) {
		String eloUri = action.getAttribute("elouri");
		Tuple tuple = new Tuple("notifyEloBrowser", eloUri);

		try {
			getCommandSpace().write(tuple);
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		}

	}
}
