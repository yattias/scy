/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tools.corner.missionmap;

import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.scywindows.moreinfomanager.MoreInfoWindow;
import eu.scy.client.desktop.scydesktop.scywindows.ModalDialogLayer;
import javafx.scene.input.MouseEvent;
import eu.scy.client.desktop.scydesktop.art.javafx.MissionMapWindowIcon;
import eu.scy.client.desktop.scydesktop.uicontrols.MultiImageButton;
import eu.scy.client.desktop.scydesktop.imagewindowstyler.ImageWindowStyler;
import eu.scy.client.desktop.scydesktop.scywindows.WindowStyler;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindowControl;

/**
 * @author SikkenJ
 */
public class BigMissionMapControl extends CustomNode {

   public var bigMissionMap: BigMissionMap;
   public var missionModel: MissionModelFX;
   public var windowStyler: WindowStyler;
   public var scyWindowControl: ScyWindowControl;
   def missionMapWindow: MoreInfoWindow = MoreInfoWindow {
         title: ##"Mission navigation"
         eloIcon: MissionMapWindowIcon {}
         windowColorScheme: windowStyler.getWindowColorScheme(ImageWindowStyler.generalNavigation)
         content: bigMissionMap
         closeAction: hideBigMissionMap
         mouseClickedAction: mouseClickedInMissionWindowWindow
      }
   def sceneWidth = bind scene.width on replace { sceneSizeChanged() };
   def sceneHeight = bind scene.height on replace { sceneSizeChanged() };
   def relativeWindowScreenBoder = 0.2;
   var bigMissionMapVisible = false;

   init {
      FX.deferAction(function(): Void {
         showBigMissionMap();
      })
   }

   public override function create(): Node {
      bigMissionMap.anchorClicked = hideBigMissionMap;
      bigMissionMap.lasInfoTooltipCreator.openElo = openElo;
      Group {
         content: [
            MultiImageButton {
               imageName: "missionMap"
               action: showBigMissionMap
            }
         ]
      }
   }

   function sceneSizeChanged() {
      if (bigMissionMapVisible) {
         missionMapWindow.width = (1 - 2 * relativeWindowScreenBoder) * scene.width;
         missionMapWindow.height = (1 - 2 * relativeWindowScreenBoder) * scene.height;
         bigMissionMap.tooltipManager.removeTooltip();
      }
   }

   function showBigMissionMap(): Void {
      bigMissionMapVisible = true;
      sceneSizeChanged();
      FX.deferAction(function(): Void {
//         bigMissionMap.adjustSize();
         missionMapWindow.resizeTheContent();
      });
      ModalDialogLayer.addModalDialog(missionMapWindow, true);
   }

   function hideBigMissionMap(): Void {
      if (bigMissionMapVisible) {
         bigMissionMapVisible = false;
         ModalDialogLayer.removeModalDialog(missionMapWindow);
         bigMissionMap.tooltipManager.removeTooltip();
      }
   }

   function mouseClickedInMissionWindowWindow(e: MouseEvent): Void {
      bigMissionMap.tooltipManager.removeTooltip();
   }

   function openElo(scyElo: ScyElo): Void {
      scyWindowControl.makeMainScyWindow(scyElo.getUri());
      hideBigMissionMap();
   }

}
