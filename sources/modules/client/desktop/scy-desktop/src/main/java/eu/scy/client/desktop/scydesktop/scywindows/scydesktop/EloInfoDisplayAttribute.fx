/*
 * EloInfoDisplayAttribute.fx
 *
 * Created on 6-mrt-2010, 15:32:15
 */
package eu.scy.client.desktop.scydesktop.scywindows.scydesktop;

import eu.scy.client.desktop.scydesktop.scywindows.ScyWindowAttribute;
import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import eu.scy.client.desktop.scydesktop.tooltips.TooltipCreator;
import eu.scy.client.desktop.scydesktop.tooltips.impl.ColoredTextTooltip;
import javafx.scene.Group;
import javafx.scene.shape.Circle;
import roolo.api.IRepository;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import eu.scy.common.scyelo.ScyRooloMetadataKeyIds;
import eu.scy.client.desktop.scydesktop.tooltips.TooltipManager;
import javafx.scene.input.MouseEvent;
import java.net.URI;

/**
 * @author sikken
 */
public class EloInfoDisplayAttribute extends ScyWindowAttribute, TooltipCreator {

   public var window: ScyWindow;
   public var repository:IRepository;
   public var metadataTypeManager:IMetadataTypeManager;
   public var tooltipManager:TooltipManager;
   override public var priority = 1;
   def circleRadius = 6.0;
   def identifierKey=metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.IDENTIFIER.getId());
   def technicalFormatKey=metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT.getId());
   def isForkOfKey=metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.IS_FORK_OF.getId());
   def lasKey=metadataTypeManager.getMetadataKey(ScyRooloMetadataKeyIds.LAS.getId());
   def anchorIdKey=metadataTypeManager.getMetadataKey(ScyRooloMetadataKeyIds.ANCHOR_ID.getId());
   def functionalTypeKey=metadataTypeManager.getMetadataKey(ScyRooloMetadataKeyIds.FUNCTIONAL_TYPE.getId());

   init{
      tooltipManager.registerNode(this, this);
   }

   public override function clone():EloInfoDisplayAttribute{
      EloInfoDisplayAttribute{
         window: window
         repository: repository
         metadataTypeManager: metadataTypeManager
         tooltipManager: tooltipManager
         priority: priority
      }
   }

   override protected function create(): Node {
      Group {
//         blocksMouse:true
         content: [
            Circle {
               centerX: circleRadius
               centerY: -circleRadius
               radius: circleRadius
               fill: bind window.windowColorScheme.mainColor
            }
         ]
//         onMouseEntered: function (e: MouseEvent): Void {
//            println("mouse entered EloInfoDisplayAttribute")
//         }
//         onMouseExited: function (e: MouseEvent): Void {
//            println("mouse exited EloInfoDisplayAttribute")
//         }

      }
   }

   override public function createTooltipNode(sourceNode: Node): Node {
      ColoredTextTooltip {
         content: getEloInfo()
         color: window.windowColorScheme.mainColor
      }
   }

   function getEloInfo(): String {
      if (window.eloUri==null){
         return "no elo uri"
      }
      var metadata = repository.retrieveMetadata(window.eloUri);
      var technicalFormat = metadata.getMetadataValueContainer(technicalFormatKey).getValue() as String;
      var functionalType = metadata.getMetadataValueContainer(functionalTypeKey).getValue() as String;
      var lasId = metadata.getMetadataValueContainer(lasKey).getValue() as String;
      var anchorId = metadata.getMetadataValueContainer(anchorIdKey).getValue() as String;
      var rootUri = getRootUri(window.eloUri);
      if (window.eloUri==rootUri){
         // most likely a static elo, so resource. So the las and anchor ids are probably from specification creation time
         // thus ignore them
         lasId = "";
         anchorId = "";
      }

      "uri: {window.eloUri}\n"
      "tech type: {technicalFormat}\n"
      "func type: {functionalType}\n"
      "las: {lasId}\n"
      "anchorId: {anchorId}\n"
      "def uri: {if (window.eloUri==rootUri) 'same as uri' else rootUri}"
   }

   function getRootUri(uri:URI):URI{
      var metadata = repository.retrieveMetadataFirstVersion(uri);
      var uriFirstVersion = metadata.getMetadataValueContainer(identifierKey).getValue() as URI;
      var isForkOfUri = metadata.getMetadataValueContainer(isForkOfKey).getValue() as URI;
      if (isForkOfUri==null){
         return uriFirstVersion
      }
      return getRootUri(isForkOfUri);
   }


}
