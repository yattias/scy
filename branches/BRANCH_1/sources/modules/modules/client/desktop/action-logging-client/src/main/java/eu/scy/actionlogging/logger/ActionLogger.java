package eu.scy.actionlogging.logger;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Message;

import eu.scy.actionlogging.ActionPacketTransformer;
import eu.scy.actionlogging.api.IAction;
import eu.scy.actionlogging.api.IActionLogger;
import eu.scy.common.configuration.Configuration;
import eu.scy.commons.smack.SmacketExtension;

public class ActionLogger /* extends ScyBaseDAOHibernate */implements
		IActionLogger {

	private XMPPConnection connection;

	/**
	 * simple constructor for an actionlogger
	 * 
	 * @param user
	 *            user throwing actions (NOT! the tool)
	 */
	public ActionLogger() {
		
	}

	/**
	 * logs an action
	 * 
	 * @param tool
	 *            the tool throwing the action
	 * @param action
	 *            IAction thrown
	 */
	public void log(IAction action) {
		Message packet = new Message();

		packet.setFrom(connection.getUser());
		packet.setTo(Configuration.getInstance().getSCYHubName() + "." + Configuration.getInstance().getOpenFireHost());

		action.setUser(connection.getUser());
		// creating new instances of transformer instead of reusing because of racing conditions
		ActionPacketTransformer transformer = new ActionPacketTransformer();
		transformer.setObject(action);

		packet.addExtension(new SmacketExtension(transformer));
		connection.sendPacket(packet);
	}

	public void init(XMPPConnection connection) {
		this.connection = connection;
	}

	@Override
	@Deprecated
	public void log(String username, String source, IAction action) {
		log(action);
	}

}
