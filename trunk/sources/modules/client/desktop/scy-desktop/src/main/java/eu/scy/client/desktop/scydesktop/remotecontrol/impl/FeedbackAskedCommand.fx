package eu.scy.client.desktop.scydesktop.remotecontrol.impl;

import java.net.URI;
import eu.scy.notification.api.INotification;
import eu.scy.client.desktop.scydesktop.remotecontrol.api.ScyDesktopRemoteCommand;
import javafx.stage.Alert;

public class FeedbackAskedCommand extends ScyDesktopRemoteCommand {

    override public function getActionName(): String {
        "feedback_asked"
    }

    override public function executeRemoteCommand(notification: INotification): Void {
        logger.debug("*****************feedback_asked*Notification*********************");
        def mission = new URI(notification.getFirstProperty("mission"));
        if (mission.equals(scyDesktop.missionRunConfigs.missionRuntimeModel.getMissionRuntimeElo().getUri())) {
            scyDesktop.scyFeedbackGiveButton.eloIcon = scyDesktop.windowStyler.getScyEloIcon("give_feedback_new");
        } else {
            logger.debug("feedback_asked in other mission: {mission.toString()}");
            Alert.inform("Feedback asked in some other mission.");
        }
    }

}
