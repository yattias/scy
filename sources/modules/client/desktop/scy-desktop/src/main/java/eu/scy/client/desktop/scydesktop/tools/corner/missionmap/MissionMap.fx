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
import javafx.stage.Stage;
import java.net.URI;
import java.util.HashMap;
import eu.scy.client.desktop.scydesktop.utils.log4j.Logger;
import roolo.elo.api.IMetadataTypeManager;
import eu.scy.client.desktop.scydesktop.tooltips.TooltipManager;
import eu.scy.client.desktop.scydesktop.ScyDesktop;
import eu.scy.client.desktop.scydesktop.draganddrop.DragAndDropManager;
import javafx.scene.text.Text;
import javafx.util.Math;
import javafx.scene.text.TextOrigin;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Sequences;
import eu.scy.client.desktop.scydesktop.tools.RuntimeSettingsRetriever;
import eu.scy.common.mission.RuntimeSettingUtils;
import eu.scy.client.desktop.scydesktop.tooltips.TooltipCreator;

/**
 * @author sikken
 */
public class MissionMap extends CustomNode {

   def logger = Logger.getLogger(this.getClass());
   def positionScaleName = "missionMap.positionScale";
   def selectedScaleName = "missionMap.selectedScale";
   def notSelectedScaleName = "missionMap.notSelectedScale";
   public var missionModel: MissionModelFX;
   public var scyWindowControl: ScyWindowControl;
   public var tooltipManager: TooltipManager;
   public var dragAndDropManager: DragAndDropManager;
   public var scyDesktop: ScyDesktop;
   public var metadataTypeManager: IMetadataTypeManager;
   public var runtimeSettingsRetriever: RuntimeSettingsRetriever;
   public var showLasId = false;
   public var selectedScale = 1.5;
   public var notSelectedScale = 1.0;
   public var positionScale = 2.0;
   public var bigMissionMap = false;
   public-read var lasInfoTooltipCreator = LasInfoTooltipCreator {
         scyDesktop: scyDesktop
      }
   var maximumLasXpos = -1e6;
   var minimumLasYPos = 1e6;
   var displayGroup = Group {
         managed: false
      };
   var anchorDisplays: AnchorDisplay[];
   var anchorMap = new HashMap();
   var anchorLinks: AnchorLink[];
   var tooltipCreator: TooltipCreator;

   def activeLas = bind missionModel.activeLas on replace previousActiveLas { activeLasChanged(previousActiveLas) };

   function activeLasChanged(previousActiveLas: LasFX):Void{
      if (previousActiveLas!=null){
         getAnchorDisplay(previousActiveLas).selected = false;
      }
      if (activeLas!=null){
         def anchorDisplay = getAnchorDisplay(activeLas);
         anchorDisplay.selected = true;
      }
   }

   public override function create(): Node {
      fillDisplayGroup();
      displayGroup
   }

   function fillDisplayGroup(): Void {
      if (bigMissionMap) {
         tooltipCreator = lasInfoTooltipCreator
      } else {
         tooltipCreator = AnchorDisplayTooltipCreator {
            }
      }

      anchorDisplays = createAnchorDisplays();
      anchorLinks = createAnchorLinks();
      var lasIdDisplay: Node;
      if (showLasId) {
         lasIdDisplay = createLasIdDisplay();
      }
      displayGroup.content = [lasIdDisplay, anchorLinks, anchorDisplays];
      if (missionModel.activeLas != null) {
         getAnchorDisplay(missionModel.activeLas).selected = true;
      }
   }

   function createLasIdDisplay(): Node {
      //println("maximumLasXpos: {maximumLasXpos}, minimumLasYPos: {minimumLasYPos}");
      var lasIdDisplay = Text {
            font: Font.font("Verdana", FontWeight.BOLD, 12);

            x: maximumLasXpos,
            y: minimumLasYPos - 5
            textOrigin: TextOrigin.BOTTOM
            //         textAlignment:TextAlignment.RIGHT
            content: bind missionModel.activeLas.id
         }
      lasIdDisplay.x -= lasIdDisplay.layoutBounds.width - 20;
      // the las id can change and the width changes then also, until that is handled, give it a fixed x pos.
      lasIdDisplay.x = 0;
      lasIdDisplay
   }

   function createAnchorDisplays(): AnchorDisplay[] {
      def realPositionScale: Number = RuntimeSettingUtils.getFloatValue(runtimeSettingsRetriever.getSetting(positionScaleName), positionScale);
      def realSelectedScale: Number = RuntimeSettingUtils.getFloatValue(runtimeSettingsRetriever.getSetting(selectedScaleName), selectedScale);
      def realNotSelectedScale: Number = RuntimeSettingUtils.getFloatValue(runtimeSettingsRetriever.getSetting(notSelectedScaleName), notSelectedScale);
      logger.info("mission map runtime settings:\n"
      " - missionMap.positionScale: {realPositionScale}\n"
      " - missionMap.selectedScale: {realSelectedScale}\n"
      " - missionMap.notSelectedScale: {realNotSelectedScale}"
      );
      for (las in missionModel.lasses) {
         las.xPos *= positionScale;
         las.yPos *= positionScale;
         var anchorDisplay = AnchorDisplay {
               positionScale: realPositionScale
               las: las,
               selectionAction: anchorSelected;
               dragAndDropManager: dragAndDropManager
               windowStyler: scyDesktop.windowStyler
               selectedScale: realSelectedScale
               notSelectedScale: realNotSelectedScale
            //               positionScale:positionScale
            }
         anchorMap.put(las, anchorDisplay);
         tooltipManager.registerNode(anchorDisplay, tooltipCreator);
         maximumLasXpos = Math.max(maximumLasXpos, las.xPos);
         minimumLasYPos = Math.min(minimumLasYPos, las.yPos);
         anchorDisplay
      }
   }

   function createAnchorLinks(): AnchorLink[] {
      var links: AnchorLink[];
      var processedLasses: LasFX[];
      for (fromAnchor in anchorDisplays) {
         for (toLas in fromAnchor.las.nextLasses) {
            var addLink = true;
            var bidirectional = Sequences.indexOf(toLas.nextLasses, fromAnchor.las) >= 0;
            if (bidirectional) {
               // if the toLas is allready processed, then a bidirectional link as already added
               addLink = Sequences.indexOf(processedLasses, toLas) < 0;
            }
            if (addLink) {
               var anchorLink = AnchorLink {
                     fromAnchor: fromAnchor;
                     toAnchor: getAnchorDisplay(toLas);
                     bidirectional: bidirectional
                  //                  positionScale:positionScale
                  }
               insert anchorLink into links;
            }
            else {
            }
         }
         insert fromAnchor.las into processedLasses;
      }
      links
   }

   function getAnchorDisplay(las: LasFX): AnchorDisplay {
      return anchorMap.get(las) as AnchorDisplay;
   }

   public function anchorSelected(anchorDisplay: AnchorDisplay, anchor: MissionAnchorFX): Void {
      if (missionModel.activeLas != null) {
         var selectedAnchorDisplay = getAnchorDisplay(missionModel.activeLas);
         if (selectedAnchorDisplay == anchorDisplay) {
            // correct anchorDisplay already selected
            if (missionModel.activeLas.selectedAnchor == anchor) {
               // the anchor is allready selected
               return;
            }
            // an other anchor in the las is being selected
            missionModel.anchorSelected(anchorDisplay.las, anchor);
            return;
         }
         selectedAnchorDisplay.selected = false;
      }
      anchorDisplay.selected = true;
      missionModel.anchorSelected(anchorDisplay.las, anchor);
   }

   public function getAnchorAttribute(anchor: MissionAnchorFX): AnchorAttribute {
      AnchorAttribute {
         anchorDisplay: anchorMap.get(anchor) as AnchorDisplay
      }
   }

   public function getAnchorAttribute(eloUri: URI): AnchorAttribute {
      for (las in missionModel.lasses) {
         if (las.mainAnchor.eloUri == eloUri) {
            return AnchorAttribute {
                  anchorDisplay: anchorMap.get(las) as AnchorDisplay
                  missionAnchor: las.mainAnchor
                  mainAnchor: true
                  missionModel: missionModel
                  tooltipManager: tooltipManager
               }
         }
         for (anchor in las.intermediateAnchors) {
            if (anchor.eloUri == eloUri) {
               return AnchorAttribute {
                     anchorDisplay: anchorMap.get(las) as AnchorDisplay
                     missionAnchor: anchor
                     mainAnchor: false
                     missionModel: missionModel
                     tooltipManager: tooltipManager
                  }
            }
         }
      }
      return null;
   }

}

function run() {
   //   var anchor1 = MissionAnchorFX{
   //      iconCharacter: "1";
   //      xPos: 20;
   //      yPos: 20;
   //      color: Color.BLUE;
   //      eloUri: new URI("1");
   //   }
   //   var anchor2 = MissionAnchorFX{
   //      iconCharacter: "2";
   //      xPos: 60;
   //      yPos: 20;
   //      color: Color.GREEN;
   //      eloUri: new URI("2");
   //   }
   //   var anchor3 = MissionAnchorFX{
   //      iconCharacter: "3";
   //      xPos: 20;
   //      yPos: 60;
   //      color: Color.RED;
   //      eloUri: new URI("3");
   //   }
   //   var anchor4 = MissionAnchorFX{
   //      iconCharacter: "4";
   //      xPos: 60;
   //      yPos: 60;
   //      color: Color.ORANGE;
   //      eloUri: new URI("4");
   //   }
   //   anchor1.nextAnchors=[anchor2,anchor3];
   //   anchor2.nextAnchors=[anchor4];
   //   anchor3.nextAnchors=[anchor2];

   var missionModel = MissionModelFX {
      //      anchors: [anchor1,anchor2,anchor3,anchor4]
      }
   //   var missionModelXml = MissionModelXml.convertToXml(missionModel);
   //   var newMissionModel = MissionModelXml.convertToMissionModel(missionModelXml);
   //   var newMissionModelXml = MissionModelXml.convertToXml(newMissionModel);
   //   println(newMissionModelXml);

   var missionMap = MissionMap {
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

