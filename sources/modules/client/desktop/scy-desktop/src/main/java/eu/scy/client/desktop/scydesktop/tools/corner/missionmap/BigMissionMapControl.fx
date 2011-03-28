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
import eu.scy.client.desktop.scydesktop.imagewindowstyler.ImageWindowStyler;
import eu.scy.client.desktop.scydesktop.scywindows.WindowStyler;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindowControl;
import eu.scy.client.desktop.scydesktop.Initializer;
import eu.scy.client.desktop.scydesktop.ScyDesktop;
import eu.scy.client.desktop.scydesktop.tooltips.TooltipManager;
import eu.scy.client.desktop.scydesktop.tooltips.impl.ColoredTextTooltipCreator;
import eu.scy.client.desktop.scydesktop.uicontrols.EloIconButton;

/**
 * @author SikkenJ
 */
public class BigMissionMapControl extends CustomNode {

   public var bigMissionMap: BigMissionMap;
   public var missionModel: MissionModelFX;
   public var windowStyler: WindowStyler;
   public var scyWindowControl: ScyWindowControl;
   public var initializer: Initializer;
   public var tooltipManager: TooltipManager;
   public var scyDesktop: ScyDesktop;
   public var buttonSize = 30.0;
   public var buttonActionScheme = 1;
   def missionMapWindow: MoreInfoWindow = MoreInfoWindow {
         title: ##"Mission navigation"
         eloIcon: MissionMapWindowIcon {}
         windowColorScheme: windowStyler.getWindowColorScheme(ImageWindowStyler.generalNavigation)
         content: bigMissionMap
         closeAction: hideBigMissionMap
         mouseClickedAction: mouseClickedInMissionWindowWindow
      }
//   def missionMapButton =  MultiImageButton {
//               imageName: "missionMap"
//               action: showBigMissionMap
//            }
   def eloIcon = windowStyler.getScyEloIcon("mission_map");
   def missionMapButton = EloIconButton{
      size: buttonSize
      actionScheme: buttonActionScheme
      eloIcon: eloIcon
      action: showBigMissionMap
      tooltipManager: tooltipManager
      tooltip: bind "{navigationTooltip}{missionModel.activeLas.title}"
   }

   def sceneWidth = bind scene.width on replace { sceneSizeChanged() };
   def sceneHeight = bind scene.height on replace { sceneSizeChanged() };
   def relativeWindowScreenBoder = 0.0;
   def navigationTooltip = "navigation\nnow working on: ";
   var bigMissionMapVisible = false;
   var initPhase = true;
   var deferLoadTimerCount = 5;

   init {
      FX.deferAction(function(): Void {
         initializer.loadTimer.startActivity("show big mission map");
         showBigMissionMap();
      })
   }

   public override function create(): Node {
      bigMissionMap.anchorClicked = hideBigMissionMap;
      bigMissionMap.lasInfoTooltipCreator.openElo = openElo;
//      tooltipManager.registerNode(missionMapButton, ColoredTextTooltipCreator {
//         content: bind "{navigationTooltip}{missionModel.activeLas.title}"
//         windowColorScheme: windowStyler.getWindowColorScheme(ImageWindowStyler.generalNavigation)
//      });
      Group {
         content: [
            missionMapButton
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
      if (not bigMissionMapVisible) {
         if (initPhase) {
            missionModel.initActiveLas();
         }
         bigMissionMapVisible = true;
         sceneSizeChanged();
         FX.deferAction(function(): Void {
            if (initPhase) {
               missionMapWindow.resizeTheContent();
               FX.deferAction(missionMapWindow.resizeTheContent);
               FX.deferAction(scyDesktop.showContent);
               deferLoadTimer();
               initPhase = false;
            }

         });
         ModalDialogLayer.addModalDialog(missionMapWindow, true, false, true);
      }
   }

   function deferLoadTimer(): Void {
      if (deferLoadTimerCount <= 0) {
         initializer.loadTimer.endActivity();
      }
      else {
         initializer.loadTimer.startActivity("deferLoadTimer {deferLoadTimerCount}");
         --deferLoadTimerCount;
         FX.deferAction(deferLoadTimer);
      }
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

   function openElo(scyElo: ScyElo, las: LasFX): Void {
      missionModel.selectLas(las);

      scyWindowControl.makeMainScyWindow(scyElo.getUri());
      hideBigMissionMap();
   }

}
