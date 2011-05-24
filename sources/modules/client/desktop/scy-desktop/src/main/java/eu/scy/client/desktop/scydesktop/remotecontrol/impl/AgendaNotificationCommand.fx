/*
 * CollaborationResponseAction.fx
 *
 * Created on 22.04.2010, 13:00:24
 */
package eu.scy.client.desktop.scydesktop.remotecontrol.impl;

import java.lang.String;
import eu.scy.notification.api.INotification;
import eu.scy.client.desktop.scydesktop.remotecontrol.api.ScyDesktopRemoteCommand;
import eu.scy.client.desktop.scydesktop.scywindows.moreinfomanager.AgendaEntryState;

/**
 * @author stefan
 */
public class AgendaNotificationCommand extends ScyDesktopRemoteCommand {

    override public function getActionName(): String {
        "agenda_notify"
    }

    override public function executeRemoteCommand(notification: INotification): Void {
        logger.debug("*****************agenda_notify*Notification*********************");
        def text = notification.getFirstProperty("text");
        var timestamp = notification.getFirstProperty("timestamp");
        if (timestamp == null or timestamp == "") {
            timestamp = "0";
        }
        def state = notification.getFirstProperty("state");
        def elouri = notification.getFirstProperty("elouri");
        logger.debug("agenda_notify: {timestamp}-{text}-{state}-{elouri}");
        def agenda = scyDesktop.moreInfoManager.agendaNode;
        if (state != null) {
            agenda.addLogEntry(Long.parseLong(timestamp), text, AgendaEntryState.valueOf(state), elouri);
        } else {
            agenda.addMessageEntry(Long.parseLong(timestamp), text);
        }
    }

}
