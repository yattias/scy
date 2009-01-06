package eu.scy.agents.roolo.elo.misspelling;

import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import roolo.elo.api.IMetadataKey;
import eu.scy.agents.impl.AbstractCommunicationAgent;

public class MisspellingCommunicationAgent<K extends IMetadataKey> extends
		AbstractCommunicationAgent<K> {

	@Override
	protected void doRun() {
		Tuple t;
		try {
			t = this.getTupleSpace().waitToTake(
					new Tuple("misspellings", String.class, Long.class, Integer.class));
		} catch (TupleSpaceException e) {
			throw new RuntimeException(e);
		}

		String uri = (String) t.getField(1).getValue();
		Integer numberOfErrors = (Integer) t.getField(3).getValue();
		// INotification notification = new Notification();
		// notification.addProperty("errors", "" + numberOfErrors);
		// notification.addProperty("target", "misspellings");
		// getToolBrokerAPI().getNotificationService().notifyCallbacks(notification);
		System.out.println("***************** your document " + uri + " has " + numberOfErrors
				+ " spelling errors ********** ");
	}
}
