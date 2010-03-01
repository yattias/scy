/*
 * Main.fx
 *
 * Created on 18 août 2009, 15:06:48
 */
package eu.scy.client.tools.fxcopex;

import javafx.stage.Stage;
import javafx.scene.Scene;

import eu.scy.client.desktop.scydesktop.ScyDesktopCreator;
import eu.scy.client.desktop.scydesktop.corners.tools.NewScyWindowTool;
import eu.scy.client.tools.fxcopex.registration.CopexToolCreatorFX;

import eu.scy.client.desktop.scydesktop.tools.drawers.xmlviewer.EloXmlViewerCreator;
import eu.scy.client.desktop.scydesktop.Initializer;
import eu.scy.client.desktop.scydesktop.ScyDesktop;
import eu.scy.client.desktop.scydesktop.login.LoginDialog;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import javafx.scene.image.ImageView;

/**
 * @author Marjolaine
 */
var initializer = Initializer {
           scyDesktopConfigFile: "config/scyDesktopCopexTestConfig.xml"
        }

function createScyDesktop(toolBrokerAPI: ToolBrokerAPI, userName: String): ScyDesktop {
//def scyCopexType = "scy/copex";
   def scyCopexId = "copex";

   var scyDesktopCreator = ScyDesktopCreator {
              initializer: initializer;
              toolBrokerAPI: toolBrokerAPI;
              userName: userName;
           }


   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(CopexToolCreatorFX{}, scyCopexId);
   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreator(new EloXmlViewerCreator(), "xmlViewer");

   var scyDesktop = scyDesktopCreator.createScyDesktop();

   scyDesktop.bottomLeftCornerTool = NewScyWindowTool {
      scyDesktop: scyDesktop;
      repository: scyDesktopCreator.config.getRepository();
      titleKey: scyDesktopCreator.config.getTitleKey();
      technicalFormatKey: scyDesktopCreator.config.getTechnicalFormatKey();
   }

   return scyDesktop;
}
var stage: Stage;
var scene: Scene;

stage = Stage {
   title: "SCY desktop with copex"
   width: 400
   height: 300
   scene: scene = Scene {
      content: [
//         initializer.getBackgroundImageView(scene),
          ImageView {
            image: initializer.backgroundImage
            fitWidth: bind scene.width
            fitHeight: bind scene.height
            preserveRatio: false
            cache: true
         }
         LoginDialog {
            createScyDesktop: createScyDesktop
            initializer: initializer;
         }
      ]
   }
}