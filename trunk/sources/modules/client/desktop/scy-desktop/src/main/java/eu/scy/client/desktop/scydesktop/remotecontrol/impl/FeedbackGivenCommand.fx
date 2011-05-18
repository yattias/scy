package eu.scy.client.desktop.scydesktop.remotecontrol.impl;

import java.net.URI;
import eu.scy.notification.api.INotification;
import eu.scy.client.desktop.scydesktop.remotecontrol.api.ScyDesktopRemoteCommand;
import javafx.stage.Alert;

public class FeedbackGivenCommand extends ScyDesktopRemoteCommand {

    override public function getActionName(): String {
        "feedback_given"
    }

    override public function executeRemoteCommand(notification: INotification): Void {
        logger.debug("*****************feedback_given*Notification*********************");
        def mission = new URI(notification.getFirstProperty("mission"));
        if (mission.equals(scyDesktop.missionRunConfigs.missionRuntimeModel.getMissionRuntimeElo().getUri())) {
            scyDesktop.scyFeedbackGetButton.eloIcon = scyDesktop.windowStyler.getScyEloIcon("get_feedback_new");
        } else {
            logger.debug("feedback_given in other mission: {mission.toString()}");
            Alert.inform("Feedback given in some other mission.");
        }
    }

}
