/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.remotecontrol.api;

import eu.scy.client.desktop.scydesktop.utils.log4j.Logger;
import eu.scy.notification.api.INotification;
import java.util.HashMap;

/**
 * This class holds all RemoteCommands as a Registry. 
 * @author sven
 */
public class RemoteCommandRegistry {

    private static HashMap<String, IRemoteCommand> actionsToClasses;
    private static final Logger logger = Logger.getLogger(RemoteCommandRegistry.class.getName());
    private static RemoteCommandRegistry instance;

    private RemoteCommandRegistry() {
        actionsToClasses = new HashMap<String, IRemoteCommand>();
    }

    public synchronized static RemoteCommandRegistry getInstance() {
        if (instance == null) {
            instance = new RemoteCommandRegistry();
        }
        return instance;
    }

    public static void registerRemoteAction(IRemoteCommand remoteAction) {
        actionsToClasses.put(remoteAction.getActionName(), remoteAction);
        logger.debug("registered IRemoteAction: " + remoteAction);
    }

    public static void registerRemoteCommands(IRemoteCommand... remoteActions) {
        for (IRemoteCommand remoteAction : remoteActions) {
            actionsToClasses.put(remoteAction.getActionName(), remoteAction);
            logger.debug("registered IRemoteAction: " + remoteAction);
        }
    }

    public void processNotification(INotification notification) {
        String[] notificationTypes = notification.getPropertyArray("type");
        for (String notificationType : notificationTypes) {
            if (actionsToClasses.containsKey(notificationType)) {
                actionsToClasses.get(notificationType).executeRemoteCommand(notification);
                logger.debug("performed remote action for " + actionsToClasses.get(notificationType));
            } else {
                logger.debug("no remote action is registered for type " + notificationType);
            }
        }
    }
}
