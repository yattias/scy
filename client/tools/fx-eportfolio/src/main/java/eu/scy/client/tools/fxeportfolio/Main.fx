package eu.scy.client.tools.fxeportfolio;

import javafx.scene.Scene;
import javafx.stage.Stage;
import eu.scy.client.desktop.scydesktop.Initializer;
import eu.scy.client.desktop.scydesktop.ScyDesktop;
import eu.scy.client.desktop.scydesktop.ScyDesktopCreator;
import eu.scy.client.tools.fxeportfolio.registration.EportfolioContentCreatorFX;
import eu.scy.client.desktop.scydesktop.corners.elomanagement.EloManagement;
import eu.scy.client.desktop.scydesktop.mission.MissionRunConfigs;
import java.lang.String;

var initializer = Initializer {
    scyDesktopConfigFile: "config/scyDesktopFxEportfolioConfig.xml"
    authorMode:true;
}

var stage: Stage;
var scene: Scene;
var title = "EPortfolio Tool";

function createScyDesktop(missionRunConfigs: MissionRunConfigs): ScyDesktop {
    def scyEportfolioId = "eportfoliotool";
    var scyDesktopCreator = ScyDesktopCreator {
        initializer: initializer;
        missionRunConfigs: missionRunConfigs;
    }

    var userName:String = missionRunConfigs.tbi.getLoginUserName();

    scyDesktopCreator.windowContentCreatorRegistryFX.registerWindowContentCreatorFX(EportfolioContentCreatorFX{},scyEportfolioId);

    var scyDesktop = scyDesktopCreator.createScyDesktop();
    scyDesktop.bottomLeftCornerTool = EloManagement {
        scyDesktop: scyDesktop;
        repository: scyDesktopCreator.config.getRepository();
        metadataTypeManager:scyDesktopCreator.config.getMetadataTypeManager();
        titleKey: scyDesktopCreator.config.getTitleKey();
        technicalFormatKey: scyDesktopCreator.config.getTechnicalFormatKey();
    }
    return scyDesktop;
}



stage = Stage {
    title: "SCY-Lab with eportfolio"
    width: 800
    height: 600
    scene: initializer.getScene(createScyDesktop);
}
