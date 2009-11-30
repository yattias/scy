/*
 * Main.fx
 *
 * Created on 04.09.2009, 12:34:29
 */

package eu.scy.client.tools.fxvideo;





import javafx.scene.Scene;
import javafx.stage.Stage;
import eu.scy.client.desktop.scydesktop.ScyDesktopCreator;
import eu.scy.client.desktop.scydesktop.corners.tools.NewScyWindowTool;
import eu.scy.client.desktop.scydesktop.tools.drawers.xmlviewer.EloXmlViewerCreator;
/**
 * @author pg
 */
//InitLog4JFX.initLog4J();

 //def scyWebType = "scy/webressource";
def scyVideoId = "video";
//var test = JAXBTest{};
//test.show();

 var scyDesktopCreator = ScyDesktopCreator {
     configClassPathConfigLocation: "config/scyDesktopVideoTestConfig.xml";
 }

 scyDesktopCreator.windowContentCreatorRegistryFX.registerWindowContentCreatorFX(VideoContentCreator{}, scyVideoId);
// scyDesktopCreator.newEloCreationRegistry.registerEloCreation(scyWebType, "webressource");

scyDesktopCreator.drawerContentCreatorRegistryFX.registerDrawerContentCreator(new EloXmlViewerCreator(), "xmlViewer");



 var scyDesktop = scyDesktopCreator.createScyDesktop();
 scyDesktop.bottomLeftCornerTool = NewScyWindowTool {

     scyDesktop:scyDesktop;
     repository:scyDesktopCreator.config.getRepository();
     titleKey:scyDesktopCreator.config.getTitleKey();
     technicalFormatKey:scyDesktopCreator.config.getTechnicalFormatKey();
 }


Stage {
    title: "Video"
    width: 250
    height: 80
    scene: Scene {
        content: [
                scyDesktop
        ]
    }
}
