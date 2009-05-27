package eu.scy.agents.roolo.elo.misspelling;

import info.collide.sqlspaces.commons.Tuple;
import eu.scy.agents.api.IAgentFactory;
import eu.scy.agents.api.IParameter;
import eu.scy.agents.api.IThreadedAgent;
import eu.scy.agents.impl.AbstractCommunicationAgent;
import eu.scy.agents.roolo.elo.elobrowsernotification.NotifyEloBrowserAgent;
import eu.scy.notification.Notification;
import eu.scy.notification.api.INotification;

/**
 * ("misspellings":String, <URI>:String, <TS>:Long, <NumberOfErrors>:Integer,
 * <User>:String) -> Notifies client.
 * 
 * @author fschulz_2
 * 
 */
public class MisspellingNotificationAgent extends AbstractCommunicationAgent
		implements IAgentFactory {

	private static final String NOTIFY_ABOUT_MISSPELLINGS_NAME = "NotifyAboutMisspellings";

	public MisspellingNotificationAgent() {
		super(NOTIFY_ABOUT_MISSPELLINGS_NAME);
	}

	@Override
	protected void doRun(Tuple trigger) {
		String uri = (String) trigger.getField(1).getValue();
		Integer numberOfErrors = (Integer) trigger.getField(3).getValue();
		String user = (String) trigger.getField(4).getValue();

		INotification notification = new Notification();
		notification.addProperty("errors", "" + numberOfErrors);
		notification.addProperty("target", "misspellings");
		notification.addProperty("eloURI", uri);
		getNotificationSender().send(user, "textpad", notification);
	}

	@Override
	protected Tuple getTemplateTuple() {
		return new Tuple("misspellings", String.class, Long.class,
				Integer.class, String.class);
	}

	@Override
	public IThreadedAgent create(IParameter params) {
		return new NotifyEloBrowserAgent();
	}

	@Override
	public String getAgentName() {
		return NOTIFY_ABOUT_MISSPELLINGS_NAME;
	}

}
