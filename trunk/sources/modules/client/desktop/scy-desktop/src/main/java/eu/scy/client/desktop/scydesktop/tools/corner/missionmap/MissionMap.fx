/*
 * MissionMap.fx
 *
 * Created on 13-okt-2009, 11:58:31
 */

package eu.scy.client.desktop.scydesktop.tools.corner.missionmap;

import eu.scy.client.desktop.scydesktop.scywindows.ScyWindowControl;
import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;

import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.net.URI;
import java.util.HashMap;

import org.apache.log4j.Logger;
import roolo.elo.api.IMetadataTypeManager;

import eu.scy.client.desktop.scydesktop.tooltips.TooltipManager;

import eu.scy.client.desktop.scydesktop.ScyDesktop;
import eu.scy.client.desktop.scydesktop.draganddrop.DragAndDropManager;


/**
 * @author sikken
 */

public class MissionMap extends CustomNode {
   def logger = Logger.getLogger(this.getClass());

   public var missionModel: MissionModelFX;
   public var scyWindowControl: ScyWindowControl;
   public var tooltipManager: TooltipManager;
   public var dragAndDropManager: DragAndDropManager;
   public var scyDesktop: ScyDesktop;
   public var metadataTypeManager:IMetadataTypeManager;

   var anchorDisplays: AnchorDisplay[];
   var anchorMap = new HashMap();
   var anchorLinks: AnchorLink[];

   def anchorDisplayTooltipCreator = AnchorDisplayTooltipCreator{
      scyDesktop: scyDesktop
      metadataTypeManager: metadataTypeManager
   }

   postinit {
      if (missionModel.activeAnchor != null){
         if (missionModel.activeAnchor.exists){
            getAnchorDisplay(missionModel.activeAnchor).selected = true;
         }
         else{
            // the active anchor elo does not exist
            logger.error("the specified active anchor elo does not exists: {missionModel.activeAnchor.eloUri}");
            missionModel.activeAnchor = null;
         }

      }
   }

   public override function create(): Node {
      anchorDisplays = createAnchorDisplays();
      anchorLinks = createAnchorLinks();
      return Group {
         content: [
            anchorLinks,
            anchorDisplays
         ]
      };
   }

   function createAnchorDisplays():AnchorDisplay[]{
      for (anchor in missionModel.anchors){
         var anchorDisplay = AnchorDisplay{
            anchor: anchor,
            selectionAction: anchorSelected;
            dragAndDropManager:dragAndDropManager
         }
         anchorMap.put(anchor,anchorDisplay);
         tooltipManager.registerNode(anchorDisplay, anchorDisplayTooltipCreator);
         anchorDisplay
      }
   }

   function createAnchorLinks():AnchorLink[]{
      for (fromAnchor in anchorDisplays){
         for (toAnchor in fromAnchor.anchor.nextAnchors){
            AnchorLink{
               fromAnchor: fromAnchor;
               toAnchor: getAnchorDisplay(toAnchor);
            }
         }
      }
   }

   function getAnchorDisplay(anchor:MissionAnchorFX):AnchorDisplay{
      return
      anchorMap.get(anchor) as AnchorDisplay;
   }


   public function anchorSelected(anchorDisplay:AnchorDisplay){
      if (missionModel.activeAnchor != null){
         var selectedAnchorDisplay = getAnchorDisplay(missionModel.activeAnchor);
         if (selectedAnchorDisplay == anchorDisplay){
            // already selected, but do reposition the windows again
//            scyWindowControl.positionWindows();
            return;
         }
         selectedAnchorDisplay.selected = false;
      }
      anchorDisplay.selected = true;
      missionModel.activeAnchor = anchorDisplay.anchor;
   }

   public function getAnchorAttribute(anchor:MissionAnchorFX):AnchorAttribute{
      AnchorAttribute{
         anchorDisplay:
         anchorMap.get(anchor) as AnchorDisplay
      }

   }


}

function run(){

   var anchor1 = MissionAnchorFX{
      iconCharacter: "1";
      xPos: 20;
      yPos: 20;
      color: Color.BLUE;
      eloUri: new URI("1");
   }
   var anchor2 = MissionAnchorFX{
      iconCharacter: "2";
      xPos: 60;
      yPos: 20;
      color: Color.GREEN;
      eloUri: new URI("2");
   }
   var anchor3 = MissionAnchorFX{
      iconCharacter: "3";
      xPos: 20;
      yPos: 60;
      color: Color.RED;
      eloUri: new URI("3");
   }
   var anchor4 = MissionAnchorFX{
      iconCharacter: "4";
      xPos: 60;
      yPos: 60;
      color: Color.ORANGE;
      eloUri: new URI("4");
   }
   anchor1.nextAnchors=[anchor2,anchor3];
   anchor2.nextAnchors=[anchor4];
   anchor3.nextAnchors=[anchor2];

   var missionModel = MissionModelFX{
      anchors: [anchor1,anchor2,anchor3,anchor4]
   }

//   var missionModelXml = MissionModelXml.convertToXml(missionModel);
//   var newMissionModel = MissionModelXml.convertToMissionModel(missionModelXml);
//   var newMissionModelXml = MissionModelXml.convertToXml(newMissionModel);
//   println(newMissionModelXml);

   var missionMap = MissionMap{
      missionModel: missionModel
   }


   Stage {
      title: "Test MissionMap"
      scene: Scene {
         width: 200
         height: 200
         content: [
            missionMap
         ]
      }
   }
}

