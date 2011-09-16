package eu.scy.actionlogging.logger;

import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.packet.Message;

import eu.scy.actionlogging.ActionPacketTransformer;
import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.actionlogging.api.IAction;
import eu.scy.actionlogging.api.IActionLogger;
import eu.scy.common.configuration.Configuration;
import eu.scy.common.smack.SmacketExtension;
import org.jivesoftware.smack.util.StringUtils;

public class ActionLogger implements IActionLogger {

    private Connection connection;
    private static Logger debugLogger = Logger.getLogger(ActionLogger.class.getName());
    /**
     * simple constructor for an actionlogger
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
    @Override
    public void log(final IAction action) {

        Runnable r = new Runnable() {

            @Override
            public void run() {
                debugLogger.debug("logging " + action.getType() + "-action for tool " + action.getContext(ContextConstants.tool));

                Message packet = new Message();

                String username = StringUtils.parseBareAddress(connection.getUser());
                packet.setFrom(username);
                packet.setTo(Configuration.getInstance().getSCYHubName() + "." + Configuration.getInstance().getOpenFireHost());
                action.setUser(username);
                // creating new instances of transformer instead of reusing because of racing conditions
                ActionPacketTransformer transformer = new ActionPacketTransformer();
                transformer.setObject(action);

                packet.addExtension(new SmacketExtension(transformer));
                connection.sendPacket(packet);
            }
        };
        if (SwingUtilities.isEventDispatchThread()) {
            Thread t = new Thread(r);
            t.setDaemon(true);
            t.start();
        } else {
            r.run();
        }
    }

    public void init(Connection connection) {
        this.connection = connection;
    }

    @Override
    public String toString() {
        return "eu.scy.actionlogging.logger.ActionLogger connected via XMPP to " + connection.getHost() + ":" + connection.getPort();
    }

}
