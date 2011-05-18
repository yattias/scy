package eu.scy.client.desktop.scydesktop.remotecontrol.impl;

import java.net.URI;
import eu.scy.notification.api.INotification;
import eu.scy.client.desktop.scydesktop.remotecontrol.api.ScyDesktopRemoteCommand;
import javafx.stage.Alert;

public class EloAssessmentFinishedCommand extends ScyDesktopRemoteCommand {

    override public function getActionName(): String {
        "elo_assessment_finished"
    }

    override public function executeRemoteCommand(notification: INotification): Void {
        logger.debug("*****************elo_assessment_finished*Notification*********************");
        def mission = new URI(notification.getFirstProperty("mission"));
        if (mission.equals(scyDesktop.missionRunConfigs.missionRuntimeModel.getMissionRuntimeElo().getUri())) {
            scyDesktop.eportfolioButton.eportfolioButton.eloIcon = scyDesktop.windowStyler.getScyEloIcon("e_portfolio_new");
        } else {
            logger.debug("elo_assessment_finished in other mission: {mission.toString()}");
            Alert.inform("ELO assessment finished in some other mission.");
        }
    }

}
