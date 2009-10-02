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
import eu.scy.client.tools.fxsimulator.registration.SimulatorContentCreator;
import eu.scy.client.tools.fxscydynamics.registration.ScyDynamicsContentCreator;
import eu.scy.client.tools.fxfitex.FitexContentCreator;
import eu.scy.client.tools.fxcopex.CopexContentCreator;

import eu.scy.client.desktop.scydesktop.tools.drawers.xmlviewer.EloXmlViewerCreator;

/**
 * @author sikkenj
 */

InitLog4JFX.initLog4J();

//def scyDrawingType = "scy/drawing";
def scyDrawingId = "drawing";
//def scyFitexType = "scy/data processing";
def scyFitexId = "fitex";
//def scyCopexType = "scy/copex";
def scyCopexId = "copex";
//def scySimulatorType = "scy/simconfig";
def scySimulatorId = "simulator";
//def scyModelType = "scy/model";
def scyModelId = "scy-dynamics";

var scyDesktopCreator = ScyDesktopCreator {
   configClassPathConfigLocation:"config/scyLabLocalConfig.xml";
}

scyDesktopCreator.windowContentCreatorRegistryFX.registerWindowContentCreatorFX(DrawingtoolContentCreator{},scyDrawingId);
//scyDesktopCreator.newEloCreationRegistry.registerEloCreation(scyDrawingType,"drawing");

scyDesktopCreator.windowContentCreatorRegistryFX.registerWindowContentCreatorFX(FitexContentCreator{},scyFitexId);
//scyDesktopCreator.newEloCreationRegistry.registerEloCreation(scyFitexType,"pds");

scyDesktopCreator.windowContentCreatorRegistryFX.registerWindowContentCreatorFX(CopexContentCreator{},scyCopexId);
//scyDesktopCreator.newEloCreationRegistry.registerEloCreation(scyCopexType,"xproc");

var simulationUriString:String = "http://www.scy-lab.eu/sqzx/HouseNew.sqzx";
//var simulationUriString:String = "http://www.scy-lab.eu/sqzx/balance.sqzx";
scyDesktopCreator.windowContentCreatorRegistryFX.registerWindowContentCreatorFX(SimulatorContentCreator{simulationUriString:simulationUriString},scySimulatorId);
//scyDesktopCreator.newEloCreationRegistry.registerEloCreation(scySimulatorType,"simulator");

scyDesktopCreator.windowContentCreatorRegistryFX.registerWindowContentCreatorFX(ScyDynamicsContentCreator{},scyModelId);
//scyDesktopCreator.newEloCreationRegistry.registerEloCreation(scyModelType,"model");

scyDesktopCreator.drawerContentCreatorRegistryFX.registerDrawerContentCreator(new EloXmlViewerCreator(), "xmlViewer");

var scyDesktop = scyDesktopCreator.createScyDesktop();

scyDesktop.bottomLeftCornerTool = NewScyWindowTool{
      scyDesktop:scyDesktop;
      repository:scyDesktopCreator.config.getRepository();
      titleKey:scyDesktopCreator.config.getTitleKey();
      technicalFormatKey:scyDesktopCreator.config.getTechnicalFormatKey();
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
