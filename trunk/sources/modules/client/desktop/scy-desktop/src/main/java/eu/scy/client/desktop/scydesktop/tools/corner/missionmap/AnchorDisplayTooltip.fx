/*
 * AnchorDisplayTooltip.fx
 *
 * Created on 27-nov-2009, 10:56:20
 */

package eu.scy.client.desktop.scydesktop.tools.corner.missionmap;

import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;

import roolo.elo.api.IMetadataTypeManager;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.Scene;

import eu.scy.client.desktop.scydesktop.ScyDesktop;

import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;

import javafx.util.Math;

import javafx.scene.layout.VBox;
import javafx.scene.control.ProgressBar;

/**
 * @author sikken
 */

public class AnchorDisplayTooltip extends CustomNode {

   public var anchor:MissionAnchorFX;
   public var scyDesktop:ScyDesktop;
   public var metadataTypeManager:IMetadataTypeManager;

   def contentBorder = 3;

   public override function create(): Node {
      def typeKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT.getId());
      var eloType = anchor.scyElo.getTechnicalFormat();
      var eloTypeName = scyDesktop.newEloCreationRegistry.getEloTypeName(eloType);
      var progress = Math.random();
      var content = VBox{
         spacing:contentBorder

         content:[
            Text {
               font : Font {
                  size: 12
               }
               x: 0, y: 0
               content:
                  "{anchor.title}\n{eloTypeName}"
            }
            ProgressBar {
               progress: progress
            }
         ]
      };
      content.layout();
      var backgroundColor = Color{
         red:anchor.windowColorScheme.mainColor.red
         green:anchor.windowColorScheme.mainColor.green
         blue:anchor.windowColorScheme.mainColor.blue
         opacity:0.9
      }
      return Group{
         cache:true
         blocksMouse:true;
         content:[
            Rectangle {
               x: content.boundsInLocal.minX-contentBorder
               y: content.boundsInLocal.minY-contentBorder
               width: content.boundsInLocal.width+2*contentBorder
               height: content.boundsInLocal.height+2*contentBorder
               fill: backgroundColor
            }
            content
         ]
      }

   }
}

function run(){
   var anchor = MissionAnchorFX{
//      color:Color.AQUAMARINE
      title:"tesing"
   }

   var anchorDisplayTooltip = AnchorDisplayTooltip{
      anchor:anchor
      layoutX:20
      layoutY:20
   }

   Stage {
      title : "AnchorDisplayTooltip test"
      scene: Scene {
         width: 200
         height: 200
         content: [
            anchorDisplayTooltip
         ]
      }
   }


}


