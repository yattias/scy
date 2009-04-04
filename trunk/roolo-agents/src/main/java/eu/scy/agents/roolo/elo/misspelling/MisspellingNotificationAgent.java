package eu.scy.agents.roolo.elo.misspelling;

import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import roolo.elo.api.IMetadataKey;
import eu.scy.agents.impl.AbstractCommunicationAgent;
import eu.scy.notification.Notification;
import eu.scy.notification.NotificationSender;
import eu.scy.notification.api.INotification;

public class MisspellingNotificationAgent<K extends IMetadataKey> extends
		AbstractCommunicationAgent<K> {

	private NotificationSender sender;

	public MisspellingNotificationAgent() {
		sender = new NotificationSender();
	}

	@Override
	protected void doRun() {
		Tuple t;
		try {
			t = getTupleSpace().waitToTake(
					new Tuple("misspellings", String.class, Long.class,
							Integer.class));
			String uri = (String) t.getField(1).getValue();
			Integer numberOfErrors = (Integer) t.getField(3).getValue();
			String user = (String) t.getField(4).getValue();
			getTupleSpace().write(
					new Tuple("notification", "misspelling", uri,
							numberOfErrors));
			System.out
					.println("***************** your document " + uri + " has "
							+ numberOfErrors + " spelling errors ********** ");

			INotification notification = new Notification();
			notification.addProperty("errors", "" + numberOfErrors);
			notification.addProperty("target", "misspellings");
			sender.send(user, "textpad", notification);
		} catch (TupleSpaceException e) {
			throw new RuntimeException(e);
		}
	}

}
