package eu.scy.client.desktop.scydesktop;

import eu.scy.notification.api.INotifiable;
import eu.scy.notification.api.INotification;
import eu.scy.client.desktop.desktoputils.log4j.Logger;
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
                    scyDesktop: scyDesktop; }
    }

    override public function processNotification(notification: INotification): Boolean {
        logger.info("received notification for {notification.getToolId()} from {notification.getSender()}");
        logger.info("notification type: {notification.getFirstProperty("type")}");
		var success: Boolean = false;

        // is this notification handled by the commandregistry?
        success = scyDesktop.remoteCommandRegistryFX.processNotification(notification);
        if (success) {
            // yep, has been handled by commandregistry
            logger.info("notification successfully handled by RemoteCommandRegistry");
            logNotificationAccepted(notification);
            return true;
        }

        if (notification.getToolId().equals("scylab") and (notification.getFirstProperty("type") != null) and notification.getFirstProperty("type").equals("collaboration_response")) {
            // special case, handled by ToolBrokerImpl itself
            logger.info("received collaboration_response notification (which is handled elsewhere).");
            logNotificationAccepted(notification);
            return true;
        }

        // no, has not been handled by commandregistry, go on...
        logger.info("received notification for tool with eloURI {notification.getToolId()}");
        try {
            //FIXME the eloUri must not be equals to the tool id
            var eloUri = new URI(notification.getToolId());
            var window = scyDesktop.windows.findScyWindow(eloUri);
            //XXX <noGoodIdea> this isnt a good idea in general, but somehow there is a misuse of notification attributes
            if (window == null) {
                //try another URI
                def uriString: String = notification.getFirstProperty("elo_uri");
                if ((not (uriString == null)) and (not(uriString.equals("")))){
                    eloUri = new URI(uriString);
                    window = scyDesktop.windows.findScyWindow(eloUri);
                }
            }
            //</noGoodIdea>

            if ((window != null) and (window.scyContent instanceof INotifiable)) {
                // INotifiable tool/window found, forward the notification
                success = (window.scyContent as INotifiable).processNotification(notification);
            }

            if (not success) {
                // no fitting tool found, or tool didn't accept, try to process it myself
                logger.info("notification could not be routed to specific tool; trying to handle it myself.");
                success = notificationProcessor.processNotification(notification);
            } else {
                logger.debug("notification successfully routed to specific tool.");
            }

        } catch (ex: URISyntaxException) {
            logger.info("notification could not be routed to specific tool because uri was not wellformed; trying to handle it myself.");
            success = notificationProcessor.processNotification(notification);
        }

        if (success) {
            logNotificationAccepted(notification);
            return true;
        } else {
            logNotificationRejected(notification);
            return false;
        }
    }

    function logNotificationRejected(notification: INotification): Void {
        def action: IAction = new Action();
        action.setType("notification_rejected");
        action.setUser(scyDesktop.config.getToolBrokerAPI().getLoginUserName());
        action.addContext(ContextConstants.mission, scyDesktop.missionRunConfigs.missionRuntimeModel.getMissionRuntimeElo().getUri().toString());
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
		action.addContext(ContextConstants.mission, scyDesktop.missionRunConfigs.missionRuntimeModel.getMissionRuntimeElo().getUri().toString());
        action.addContext(ContextConstants.tool, "scy-lab");
        action.addAttribute("notificationId", notification.getUniqueID());
        action.addAttribute("toolId", notification.getToolId());
        scyDesktop.config.getToolBrokerAPI().getActionLogger().log(action);
        logger.info("logged notificaton_accepted-action: {action}");
    }

}
