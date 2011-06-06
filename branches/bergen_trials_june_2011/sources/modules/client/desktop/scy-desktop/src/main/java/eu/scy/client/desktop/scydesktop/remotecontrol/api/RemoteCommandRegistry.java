/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.remotecontrol.api;

import eu.scy.client.desktop.desktoputils.log4j.Logger;
import eu.scy.notification.api.INotification;
import java.util.HashMap;

/**
 * This class holds all RemoteCommands as a Registry. 
 * @author sven, lars
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

    public static boolean unregisterRemoteCommand(String remoteCommandName){
        if(actionsToClasses.containsKey(remoteCommandName)){
            actionsToClasses.remove(remoteCommandName);
            return true;
        } 
        return false;
    }

    public static void registerremoteCommand(IRemoteCommand remoteCommand) {
        actionsToClasses.put(remoteCommand.getActionName(), remoteCommand);
        logger.debug("registered IremoteCommand: " + remoteCommand);
    }

    public static void registerRemoteCommands(IRemoteCommand... remoteCommands) {
        for (IRemoteCommand remoteCommand : remoteCommands) {
            actionsToClasses.put(remoteCommand.getActionName(), remoteCommand);
            logger.debug("registered IremoteCommand: " + remoteCommand);
        }
    }

    public boolean acceptsNotification(INotification notification) {
        String[] notificationTypes = notification.getPropertyArray("type");
        boolean success = false;
        for (String notificationType: notificationTypes) {
            if (actionsToClasses.containsKey(notificationType)) {
                success = true;
            }
        }
        return success;
    }

    public boolean processNotification(INotification notification) {
        String[] notificationTypes = notification.getPropertyArray("type");
        boolean success = false;
        for (String notificationType : notificationTypes) {
            if (actionsToClasses.containsKey(notificationType)) {
                actionsToClasses.get(notificationType).executeRemoteCommand(notification);
                logger.debug("performed remote action for " + actionsToClasses.get(notificationType));
                success = true;
            } else {
                logger.debug("no remote action is registered for type " + notificationType);
                success = false;
            }
        }
        return success;
    }
}
