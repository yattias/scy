/*
 * Main.fx
 *
 * Created on 14-mei-2009, 17:41:07
 */
package eu.scy.client.desktop.scydesktop;

import javafx.stage.Stage;
import javafx.scene.Scene;

import eu.scy.client.desktop.scydesktop.tools.drawers.xmlviewer.EloXmlViewerCreator;
import eu.scy.client.desktop.scydesktop.tools.content.text.TextEditorScyToolContentCreator;

import eu.scy.client.desktop.scydesktop.corners.tools.NewScyWindowTool;
import eu.scy.client.desktop.scydesktop.utils.log4j.InitLog4JFX;
import eu.scy.client.desktop.scydesktop.login.LoginDialog;
import eu.scy.client.desktop.scydesktop.scywindows.window.MouseBlocker;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * @author sikkenj
 */
InitLog4JFX.initLog4J();

def scyTextId = "text";
def eloXmlViewerId = "xmlViewer";
var scyDesktopCreator = ScyDesktopCreator {
           servicesClassPathConfigLocation: "config/localWrappedRooloConfig.xml";
           configClassPathConfigLocation: "config/scyDesktopTestConfig.xml";
        }

scyDesktopCreator.windowContentCreatorRegistryFX.registerWindowContentCreatorFX(TextEditorScyToolContentCreator {}, scyTextId);
scyDesktopCreator.drawerContentCreatorRegistryFX.registerDrawerContentCreator(new EloXmlViewerCreator(), "eloXmlViewerId");

function createScyDesktop(): ScyDesktop {
   var scyDesktop = scyDesktopCreator.createScyDesktop();

   scyDesktop.bottomLeftCornerTool = NewScyWindowTool {
      scyDesktop: scyDesktop;
      repository: scyDesktopCreator.config.getRepository();
      titleKey: scyDesktopCreator.config.getTitleKey();
      technicalFormatKey: scyDesktopCreator.config.getTechnicalFormatKey();
   }
   return scyDesktop;
}
var loginDialog = LoginDialog {
           createScyDesktop: createScyDesktop
        }
var stage: Stage;

var backgroundImage = ImageView {
           image: Image {
              url: "{__DIR__}bckgrnd2.jpg"
           }
           fitWidth: bind stage.scene.width
           fitHeight: bind stage.scene.height
           preserveRatio: false
           cache: true
        }

stage = Stage {
   title: "SCY Desktop"
   width: 400
   height: 300
   scene: Scene {
      content: [
         backgroundImage,
         loginDialog
      ]
   }
}
