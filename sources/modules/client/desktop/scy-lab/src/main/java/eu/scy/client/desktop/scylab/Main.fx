/*
 * Main.fx
 *
 * Created on 9-jul-2009, 15:06:15
 */

package eu.scy.client.desktop.scylab;

import javafx.scene.Scene;
import javafx.stage.Stage;
import eu.scy.client.desktop.scydesktop.utils.log4j.InitLog4JFX;

import eu.scy.client.desktop.scydesktop.ScyDesktopCreator;
import eu.scy.client.desktop.scydesktop.corners.tools.NewScyWindowTool;
import eu.scy.client.tools.fxdrawingtool.registration.DrawingtoolContentCreator;
import eu.scy.client.tools.fxfitex.FitexContentCreator;
import eu.scy.client.tools.fxcopex.CopexContentCreator;

/**
 * @author sikkenj
 */

InitLog4JFX.initLog4J();

def scyDrawingType = "scy/drawing";
def scyFitexType = "scy/data processing";
def scyCopexType = "scy/copex";


var scyDesktopCreator = ScyDesktopCreator{
   configClassPathConfigLocation:"config/scyLabLocalConfig.xml";
}

scyDesktopCreator.windowContentCreatorRegistryFX.registerWindowContentCreatorFX(DrawingtoolContentCreator{},scyDrawingType);
scyDesktopCreator.newEloCreationRegistry.registerEloCreation(scyDrawingType,"drawing");

scyDesktopCreator.windowContentCreatorRegistryFX.registerWindowContentCreatorFX(FitexContentCreator{},scyFitexType);
scyDesktopCreator.newEloCreationRegistry.registerEloCreation(scyFitexType,"pds");

scyDesktopCreator.windowContentCreatorRegistryFX.registerWindowContentCreatorFX(CopexContentCreator{},scyCopexType);
scyDesktopCreator.newEloCreationRegistry.registerEloCreation(scyCopexType,"xproc");


var scyDesktop = scyDesktopCreator.createScyDesktop();

scyDesktop.bottomLeftCornerTool = NewScyWindowTool{
   scyDesktop:scyDesktop;
}


var stage = Stage {
    title: "SCY Lab"
    width: 400
    height: 300
    scene: Scene {
        content: [
            scyDesktop
        ]
    }
}
