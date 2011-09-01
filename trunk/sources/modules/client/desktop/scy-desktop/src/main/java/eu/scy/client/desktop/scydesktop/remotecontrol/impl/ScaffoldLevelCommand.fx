package eu.scy.client.desktop.scydesktop.remotecontrol.impl;

import eu.scy.client.desktop.scydesktop.remotecontrol.api.ScyDesktopRemoteCommand;
import eu.scy.notification.api.INotification;
import eu.scy.client.desktop.scydesktop.scywindows.scaffold.ScaffoldManager;

public class ScaffoldLevelCommand extends ScyDesktopRemoteCommand {

    override public function getActionName(): String {
        "scaffold"
    }

    override public function executeRemoteCommand(notification: INotification): Void {
        logger.debug("******************** scaffold notification *************************");
        ScaffoldManager.getInstance().setScaffoldLevel(notification.getFirstProperty("level"));
    }

}
