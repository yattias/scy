/*
 * Main.fx
 *
 * Created on 27.01.2010, 17:04:55
 */

package eu.scy.client.tools.fxrichtexteditor;

import javafx.stage.Stage;
import eu.scy.client.desktop.scydesktop.Initializer;
import eu.scy.client.desktop.scydesktop.ScyDesktop;
import eu.scy.client.desktop.scydesktop.ScyDesktopCreator;
import eu.scy.client.desktop.scydesktop.tools.drawers.xmlviewer.EloXmlViewerCreator;
import eu.scy.client.tools.fxrichtexteditor.registration.RichTextEditorContentCreatorFX;
import eu.scy.client.desktop.scydesktop.corners.elomanagement.EloManagement;
import eu.scy.client.desktop.scydesktop.mission.MissionRunConfigs;


/**
 * @author kaido
 */
var initializer = Initializer {
           scyDesktopConfigFile: "config/scyDesktopFxRichTextEditorConfig.xml"
           authorMode:true;
        }

function createScyDesktop(missionRunConfigs: MissionRunConfigs): ScyDesktop {
   def scyRichTextId = "richtext";
   var scyDesktopCreator = ScyDesktopCreator {
              initializer: initializer;
              missionRunConfigs: missionRunConfigs;
           }
   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(RichTextEditorContentCreatorFX{},scyRichTextId);
   scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreator(new EloXmlViewerCreator(), "xmlViewer");
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

Stage {
   title: "##SCY-Lab with rich-text-editor"
   width: 800
   height: 600
	scene: initializer.getScene(createScyDesktop);
}
