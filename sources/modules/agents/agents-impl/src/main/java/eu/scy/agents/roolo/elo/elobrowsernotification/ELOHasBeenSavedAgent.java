package eu.scy.agents.roolo.elo.elobrowsernotification;

import java.util.Map;

import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;
import eu.scy.agents.impl.elo.AbstractELOAgent;

/**
 * Notifies tools that an elo has been saved.
 * 
 * ELO is saved -> ("notifyEloBrowser":String, <ELOUri>:String)
 * 
 * @author fschulz_2
 * 
 * @param <T>
 * @param <K>
 */
public class ELOHasBeenSavedAgent<T extends IELO<K>, K extends IMetadataKey>
		extends AbstractELOAgent<T, K> {

	public ELOHasBeenSavedAgent(Map<String, Object> map) {
		super("NotifiyELOBroserAgent", (String) map.get("id"));
	}

	@Override
	public void processElo(T elo) {

		if (elo == null) {
			return;
		}

		if (elo.getUri() == null) {
			return;
		}

		Tuple tuple = new Tuple("notifyEloBrowser", elo.getUri().toString());
		

		try {
			getTupleSpace().write(tuple);
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		}
		// sender.send("roolo", "roolo", notification);
	}

}
