/*
 * Main.fx
 *
 * Created on 17 août 2009, 13:49:57
 */

package eu.scy.client.tools.fxfitex;

import javafx.stage.Stage;
import javafx.scene.Scene;
import eu.scy.client.desktop.scydesktop.utils.log4j.InitLog4JFX;

import eu.scy.client.desktop.scydesktop.ScyDesktopCreator;
import eu.scy.client.desktop.scydesktop.corners.tools.NewScyWindowTool;

/**
 * @author Marjolaine
 */


InitLog4JFX.initLog4J();

def scyFitexType = "scy/fitex";

var scyDesktopCreator = ScyDesktopCreator{
    configClassPathConfigLocation:"config/scyDesktopFitexTestConfig.xml";
}

scyDesktopCreator.windowContentCreatorRegistryFX.registerWindowContentCreatorFX(FitexContentCreator{},scyFitexType);
scyDesktopCreator.newEloCreationRegistry.registerEloCreation(scyFitexType,"fitex");

var scyDesktop = scyDesktopCreator.createScyDesktop();

scyDesktop.bottomLeftCornerTool = NewScyWindowTool{
    scyDesktop:scyDesktop;
}


var stage = Stage {
     title: "Data Processing Tool"
     width: 400
     height: 300
     scene: Scene {
         content: [
         scyDesktop
      ]
    }
}

