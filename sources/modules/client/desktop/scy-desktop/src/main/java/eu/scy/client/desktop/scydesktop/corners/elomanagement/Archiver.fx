/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.corners.elomanagement;

import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import eu.scy.client.desktop.scydesktop.draganddrop.DropTarget;
import eu.scy.client.desktop.scydesktop.tools.corner.missionmap.MissionModelFX;
import java.lang.Object;
import roolo.elo.api.IMetadata;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import org.apache.log4j.Logger;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindowControl;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import java.lang.Object;
import java.lang.Void;
import java.net.URI;

/**
 * @author SikkenJ
 */
public class Archiver extends CustomNode, DropTarget {
   def logger = Logger.getLogger(this.getClass());

   public-init var missionMapModel: MissionModelFX;
   public-init var tbi: ToolBrokerAPI;
   public var scyWindowControl: ScyWindowControl;

   def identifierKey = tbi.getMetaDataTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.IDENTIFIER);

   public override function create(): Node {
      Group {
         content: [
            Rectangle {
               x: 0
               y: 0
               width: 50
               height: 25
               fill: Color.LIGHTGRAY
               stroke: Color.BLACK
            }
         ]
      }
   }

   override public function canAcceptDrop(object: Object): Boolean {
      def eloUri = getArchivebleEloUri(object);
      if (eloUri!=null){
         return true;
      }
      if (object instanceof ScyWindow){
         def window = object as ScyWindow;
         return window.eloUri==null
      }
      return false;
   }

   override public function acceptDrop(object: Object): Void {
      def eloUri = getArchivebleEloUri(object);
      if (eloUri!=null){
         archieveElo(eloUri);
         logger.info("archieved elo: {eloUri}");
         return
      }
      if (object instanceof ScyWindow){
         archieveScyWindow(object as ScyWindow);
      }
   }

   function archieveElo(eloUri: URI):Void{
      scyWindowControl.removeOtherScyWindow(eloUri);
   }

   function archieveScyWindow(window: ScyWindow):Void{
      scyWindowControl.removeOtherScyWindow(window);
   }

   function getArchivebleEloUri(object: Object): URI{
      def eloUri = getEloUri(object);
      if (eloUri!=null){
         // there is a elo passed on
         def coreEloUris = missionMapModel.getEloUris(false);
         if (not coreEloUris.contains(eloUri)){
            // it is not one of the core elos
            return eloUri;
         }
      }
      return null;
   }

   function getEloUri(object: Object): URI{
      if (object instanceof IMetadata){
         def metadata = object as IMetadata;
         return metadata.getMetadataValueContainer(identifierKey).getValue() as URI
      }
      return null;
   }


}
