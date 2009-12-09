/*
 * Main.fx
 *
 * Created on 04.09.2009, 12:34:29
 */

package eu.scy.client.tools.fxwebresourcer;





import javafx.scene.Scene;
import javafx.stage.Stage;
import eu.scy.client.desktop.scydesktop.ScyDesktopCreator;
import eu.scy.client.desktop.scydesktop.corners.tools.NewScyWindowTool;
import eu.scy.client.desktop.scydesktop.tools.drawers.xmlviewer.EloXmlViewerCreator;
/**
 * @author pg
 */
//InitLog4JFX.initLog4J();

 //def scyWebType = "scy/webresource";
def scyWebresourceId = "webresource";
//var test = JAXBTest{};
//test.show();

 var scyDesktopCreator = ScyDesktopCreator {
     configClassPathConfigLocation: "config/scyDesktopWebResourceTestConfig.xml";
 }

 scyDesktopCreator.windowContentCreatorRegistryFX.registerWindowContentCreatorFX(WebResourceContentCreator{}, scyWebresourceId);
// scyDesktopCreator.newEloCreationRegistry.registerEloCreation(scyWebType, "webresource");

scyDesktopCreator.drawerContentCreatorRegistryFX.registerDrawerContentCreator(new EloXmlViewerCreator(), "xmlViewer");



 var scyDesktop = scyDesktopCreator.createScyDesktop();
 scyDesktop.bottomLeftCornerTool = NewScyWindowTool {

     scyDesktop:scyDesktop;
     repository:scyDesktopCreator.config.getRepository();
     titleKey:scyDesktopCreator.config.getTitleKey();
     technicalFormatKey:scyDesktopCreator.config.getTechnicalFormatKey();
 }


Stage {
    title: "webresourceR"
    width: 250
    height: 80
    scene: Scene {
        content: [
                scyDesktop
        ]
    }
}
