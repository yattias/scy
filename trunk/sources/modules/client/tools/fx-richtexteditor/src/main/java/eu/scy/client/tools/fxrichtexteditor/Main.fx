/*
 * Main.fx
 *
 * Created on 27.01.2010, 17:04:55
 */

package eu.scy.client.tools.fxrichtexteditor;

import javafx.scene.Scene;
import javafx.stage.Stage;
import eu.scy.client.desktop.scydesktop.Initializer;
import eu.scy.client.desktop.scydesktop.ScyDesktop;
import eu.scy.client.desktop.scydesktop.ScyDesktopCreator;
import eu.scy.client.desktop.scydesktop.tools.drawers.xmlviewer.EloXmlViewerCreator;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import eu.scy.client.tools.fxrichtexteditor.registration.RichTextEditorContentCreatorFX;
import eu.scy.client.desktop.scydesktop.corners.elomanagement.EloManagement;


/**
 * @author kaido
 */
var initializer = Initializer {
           scyDesktopConfigFile: "config/scyDesktopFxRichTextEditorConfig.xml"
           authorMode:true;
        }

function createScyDesktop(toolBrokerAPI: ToolBrokerAPI, userName: String): ScyDesktop {
   def scyRichTextId = "richtext";
   var scyDesktopCreator = ScyDesktopCreator {
              initializer: initializer;
              toolBrokerAPI: toolBrokerAPI;
              userName: userName;
           }
   scyDesktopCreator.windowContentCreatorRegistryFX.registerWindowContentCreatorFX(RichTextEditorContentCreatorFX{},scyRichTextId);
   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreator(new EloXmlViewerCreator(), "xmlViewer");
   var scyDesktop = scyDesktopCreator.createScyDesktop();
   scyDesktop.bottomLeftCornerTool = EloManagement {
      scyDesktop: scyDesktop;
      repository: scyDesktopCreator.config.getRepository();
      metadataTypeManager:scyDesktopCreator.config.getMetadataTypeManager();
      titleKey: scyDesktopCreator.config.getTitleKey();
      technicalFormatKey: scyDesktopCreator.config.getTechnicalFormatKey();
      userId:userName
   }
   return scyDesktop;
}
var stage: Stage;
var scene: Scene;
//var myLocale : java.util.Locale = new java.util.Locale("et", "EE");
//var myLocale : java.util.Locale = new java.util.Locale("en", "US");
//java.util.Locale.setDefault(myLocale);

var title = ##"Formatted text editor";

stage = Stage {
   title: "SCY-Lab with rich-text-editor"
   width: 800
   height: 600
	scene: initializer.getScene(createScyDesktop);
}
