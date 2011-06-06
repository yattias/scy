/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.scywindows.moreinfomanager;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import java.net.URI;
import eu.scy.client.desktop.scydesktop.scywindows.MoreInfoTypes;
import eu.scy.client.desktop.scydesktop.tools.ScyToolFX;
import eu.scy.client.desktop.scydesktop.scywindows.ShowMoreInfo;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import javafx.scene.control.Label;
import roolo.api.IRepository;

/**
 * @author SikkenJ
 */
public class TestMoreInfoNode extends CustomNode, ScyToolFX {

   public var showMoreInfo: ShowMoreInfo;
   public var repository: IRepository;
   public var toolBrokerAPI: ToolBrokerAPI on replace { repository = toolBrokerAPI.getRepository() };
   def spacing = 5.0;
   var eloUri: URI;
   var scyElo: ScyElo;

   public override function create(): Node {
      VBox {
         spacing: spacing
         content: [
            Label {
               text: "Test more info\nnot part of mission"
            }
            Button {
               text: "More assignment"
               action: function() {
                  showMoreInfo.showMoreInfo(new URI("http://www.scy-lab.eu/content/en/mission1/LAS_Build_design/Assignments/A_House_drawings.html"), MoreInfoTypes.ASSIGNMENT, scyElo);
                  }
            }
            Button {
               text: "More resources"
               action: function() {
                  showMoreInfo.showMoreInfo(new URI("http://www.scy-lab.eu/content/en/mission1/LAS_Conceptualization_design/Resources/R_CO2_houses.html"), MoreInfoTypes.RESOURCES, scyElo);
                  }
            }
         ]
      }
       }

   public override function loadElo(eloUri: URI): Void {
      eloUriChanged(eloUri)

       }

   public override function loadedEloChanged(eloUri: URI): Void {
      eloUriChanged(eloUri)
       }

   function eloUriChanged(eloUri: URI) {
      this.eloUri = eloUri;
      if (eloUri != null) {
         scyElo = ScyElo.loadMetadata(eloUri, toolBrokerAPI);
          } else {
         scyElo = null;
          }

       }

}
