/*
 * Main.fx
 *
 * Created on 8-jul-2009, 17:21:12
 */

package eu.scy.client.tools.studentplanningtool;

import javafx.scene.Scene;
import javafx.stage.Stage;
import eu.scy.client.desktop.scydesktop.utils.log4j.InitLog4JFX;

import eu.scy.client.desktop.scydesktop.ScyDesktopCreator;
import eu.scy.client.desktop.scydesktop.corners.tools.NewScyWindowTool;
import eu.scy.client.tools.studentplanningtool.registration.StudenPlanningToolContentCreator.StudentPlanningToolContentCreator;


/**
 * @author jeremyt
 */

InitLog4JFX.initLog4J();

//def scychatType = "scy/chat";
def scystudenplanningId = "studentplanningtool";

var scyDesktopCreator = ScyDesktopCreator {
    configClassPathConfigLocation:"config/scyDesktopStudentPlanningToolConfig.xml";
}

scyDesktopCreator.windowContentCreatorRegistryFX.registerWindowContentCreatorFX(StudentPlanningToolContentCreator{},scystudenplanningId);


var scyDesktop = scyDesktopCreator.createScyDesktop();

scyDesktop.bottomLeftCornerTool = NewScyWindowTool {
    scyDesktop:scyDesktop;
    repository:scyDesktopCreator.config.getRepository();
    titleKey:scyDesktopCreator.config.getTitleKey();
    technicalFormatKey:scyDesktopCreator.config.getTechnicalFormatKey();
}


var stage = Stage {
    title: "SCY desktop with StudenPlanningTool"
    width: 400
    height: 300
    scene: Scene {
        content: [
            scyDesktop
        ]
    }
}
