/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.fxformauthor;


import eu.scy.client.desktop.scydesktop.*;
import eu.scy.client.desktop.scydesktop.corners.elomanagement.EloManagement;
import eu.scy.client.desktop.scydesktop.tools.drawers.xmlviewer.EloXmlViewerCreator;
import javafx.scene.Scene;
import javafx.stage.Stage;
import eu.scy.client.desktop.scydesktop.mission.MissionRunConfigs;

/**
 * @author pg
 TODO:
    - save AS / save
 */
var initializer = Initializer {
           scyDesktopConfigFile: "config/scyDesktopFormAuthorTestConfig.xml"
           //loginType: "remote"
           loginType: "local"
           authorMode:true
           //autoLogin: true
           //defaultUserName: "phil"
           //defaultPassword: "phil"
}
function createScyDesktop(missionRunConfigs: MissionRunConfigs): ScyDesktop {
   def scyFormAuthorId = "formauthor";

   var scyDesktopCreator = ScyDesktopCreator {
              initializer: initializer;
              missionRunConfigs: missionRunConfigs;
   }

    scyDesktopCreator.scyToolCreatorRegistryFX.registerScyToolCreatorFX(FormAuthorContentCreator {}, scyFormAuthorId);
    //scyDesktopCreator.windowContentCreatorRegistryFX.registerWindowContentCreatorFX(FormAuthorViewContentCreator {}, scyFormAuthorId);
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

var stage: Stage;
var scene: Scene;
stage = Stage {
   title: "SCY-Lab with FormAuthor"
   width: 400
   height: 300
   scene: initializer.getScene(createScyDesktop);
}