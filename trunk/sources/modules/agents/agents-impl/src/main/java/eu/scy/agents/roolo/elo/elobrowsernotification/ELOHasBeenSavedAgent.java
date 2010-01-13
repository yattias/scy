package eu.scy.agents.roolo.elo.elobrowsernotification;

import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.rmi.dgc.VMID;
import java.util.Map;

import roolo.elo.api.IELO;
import eu.scy.agents.impl.elo.AbstractELOAgent;

/**
 * Notifies tools that an elo has been saved.
 * 
 * ELO is saved -> ("notifyEloBrowser":String, <ELOUri>:String)
 * 
 * @author Florian Schulz
 * 
 */
public class ELOHasBeenSavedAgent extends AbstractELOAgent {

	/**
	 * Create a new ELOHasBeenSavedAgent filtering agent. The argument
	 * <code>map</code> is used to initialize special parameters. Never used
	 * here.
	 * 
	 * @param map
	 *            Parameters needed to initialize the agent.
	 */
	public ELOHasBeenSavedAgent() {
		super("NotifiyELOBroserAgent", new VMID().toString());
	}

	@Override
	public void processElo(IELO elo) {
		if (elo == null) {
			return;
		}

		if (elo.getUri() == null) {
			return;
		}

		Tuple tuple = new Tuple("notifyEloBrowser", elo.getUri().toString());

		try {
			getCommandSpace().write(tuple);
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		}
		// sender.send("roolo", "roolo", notification);
	}

}
