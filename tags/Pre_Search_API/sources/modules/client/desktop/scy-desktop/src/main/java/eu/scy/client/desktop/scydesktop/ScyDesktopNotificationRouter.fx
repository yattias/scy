package eu.scy.client.desktop.scydesktop;

import eu.scy.notification.api.INotifiable;
import java.lang.UnsupportedOperationException;
import eu.scy.notification.api.INotification;
import eu.scy.client.desktop.scydesktop.utils.log4j.Logger;
import java.net.URI;
import java.net.URISyntaxException;
import eu.scy.actionlogging.api.IAction;
import eu.scy.actionlogging.Action;
import eu.scy.actionlogging.api.ContextConstants;

/**
 * @author lars
 */
public class ScyDesktopNotificationRouter extends INotifiable {

    public-init var scyDesktop: ScyDesktop;
    def logger = Logger.getLogger(this.getClass());
    var notificationProcessor: GenericNotificationProcessor;

    postinit {
        logger.debug("ScyDesktopNotificationRouter created with {scyDesktop}");
        notificationProcessor = GenericNotificationProcessor {
            scyDesktop: scyDesktop;}
    }

    override public function processNotification(notification: INotification): Boolean {
        logger.debug("received notification for {notification.getToolId()} from {notification.getSender()}");
        var success: Boolean = false;

        // is this notification handled by the commandregistry?
        success = scyDesktop.remoteCommandRegistryFX.processNotification(notification);
        if (success) {
            // yep, has been handled by commandregistry
            logger.debug("notification successfully handled by RemoteCommandRegistry");
            return true;
        }

        if (notification.getToolId().equals("scylab") and (notification.getFirstProperty("type") != null) and notification.getFirstProperty("type").equals("collaboration_response")) {
            // special case, handled by ToolBrokerImpl itself
            logger.debug("received collaboration_response notification (which is handled elsewhere).");
            return true;
        }

        // no, has not handled by commandregistry, go on...
        logger.debug("received notification for tool with eloURI {notification.getToolId()}");
        try {
            var eloUri = new URI(notification.getToolId());
            var window = scyDesktop.windows.findScyWindow(eloUri);
           
            if ((window != null) and (window.scyContent instanceof INotifiable)) {
                // INotifiable tool/window found, forward the notification
                success = (window.scyContent as INotifiable).processNotification(notification);
            }

            if (not success) {
                // no fitting tool found, or tool didn't accept, try to process it myself
                logger.debug("notification could not be routed to specific tool; trying to handle it myself.");
                success = notificationProcessor.processNotification(notification);
            } else {
                logger.debug("notification successfully routed to specific tool.");
            }

        } catch (ex: URISyntaxException) {
            logger.debug("notification could not be routed to specific tool because uri was not wellformed; trying to handle it myself.");
            success = notificationProcessor.processNotification(notification);
        }

        if (success) {
            logNotificationAccepted(notification);
        } else {
            logNotificationRejected(notification);
        }
        return true;
    }

    function logNotificationRejected(notification: INotification): Void {
        def action: IAction = new Action();
        action.setType("notification_rejected");
        action.setUser(scyDesktop.config.getToolBrokerAPI().getLoginUserName());
        action.addContext(ContextConstants.tool, "scy-lab");
        action.addAttribute("notificationId", notification.getUniqueID());
        action.addAttribute("toolId", notification.getToolId());
        scyDesktop.config.getToolBrokerAPI().getActionLogger().log(action);
        logger.info("logged notificaton_rejected-action: {action}");
    }

    function logNotificationAccepted(notification: INotification): Void {
        def action: IAction = new Action();
        action.setType("notification_accepted");
        action.setUser(scyDesktop.config.getToolBrokerAPI().getLoginUserName());
        action.addContext(ContextConstants.tool, "scy-lab");
        action.addAttribute("notificationId", notification.getUniqueID());
        action.addAttribute("toolId", notification.getToolId());
        scyDesktop.config.getToolBrokerAPI().getActionLogger().log(action);
        logger.info("logged notificaton_accepted-action: {action}");
    }

}
