/*
 * CollaborationResponseAction.fx
 *
 * Created on 22.04.2010, 13:00:24
 */
package eu.scy.client.desktop.scydesktop.remotecontrol.impl;

import java.lang.String;
import eu.scy.notification.api.INotification;
import eu.scy.client.desktop.scydesktop.remotecontrol.api.ScyDesktopRemoteCommand;

/**
 * @author sven
 */
public class AgendaNotificationCommand extends ScyDesktopRemoteCommand {

    override public function getActionName(): String {
        "agenda_notify"
    }

    override public function executeRemoteCommand(notification: INotification): Void {
        logger.debug("*****************agenda_notify*Notification*********************");
        def text = notification.getFirstProperty("text");
        def timestamp = notification.getFirstProperty("timestamp");
        def done = notification.getFirstProperty("done");
        def elouri = notification.getFirstProperty("elouri");
        logger.debug("agenda_notify: {timestamp}-{text}-{done}");
        def agenda = scyDesktop.moreInfoManager.agendaNode;
        agenda.addLogEntry(Long.parseLong(timestamp), text, Boolean.parseBoolean(done), elouri);
    }

}
