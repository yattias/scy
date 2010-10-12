/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.corners.elomanagement;
import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.uicontrols.MultiImageButton;
import javafx.scene.CustomNode;
import eu.scy.client.desktop.scydesktop.draganddrop.DropTarget2;
import java.lang.Object;
import roolo.elo.api.IMetadata;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import eu.scy.common.scyelo.ScyElo;
import java.net.URI;

/**
 * @author SikkenJ
 */
public class Searcher extends  CustomNode, DropTarget2 {

   public-init var tbi: ToolBrokerAPI;
   public-init var imageName: String;
   public-init var clickAction: function():Void;
   public-init var dropAction: function(scyElo:ScyElo):Void;
   public var turnedOn = false on replace{
         searchButton.turnedOn = turnedOn
      };

   def identifierKey = tbi.getMetaDataTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.IDENTIFIER);
   def searchButton: MultiImageButton = MultiImageButton {
         imageName: imageName
         action: clickAction
      }

   public override function create(): Node {
      searchButton
   }

    override public function dropEntered (object : Object, canAccept : Boolean) : Void {
      if (canAccept){
         searchButton.turnedOn = true;
      }
    }

    override public function dropLeft (object : Object) : Void {
        searchButton.turnedOn = false;
    }

    override public function canAcceptDrop (object : Object) : Boolean {
       if (object instanceof IMetadata){
          return true;
       }
       return false;
    }

    override public function acceptDrop (object : Object) : Void {
       if (object instanceof IMetadata){
          def metadata = object as IMetadata;
          def scyElo = ScyElo.loadMetadata(metadata.getMetadataValueContainer(identifierKey).getValue() as URI, tbi);
          dropAction(scyElo);
       }
    }

}
