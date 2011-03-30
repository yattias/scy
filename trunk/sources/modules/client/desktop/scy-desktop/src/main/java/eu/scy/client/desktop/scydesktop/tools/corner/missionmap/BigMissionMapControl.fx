/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tools.corner.missionmap;

import javafx.scene.CustomNode;
import eu.scy.client.desktop.scydesktop.scywindows.WindowStyler;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindowControl;
import eu.scy.client.desktop.scydesktop.Initializer;
import eu.scy.client.desktop.scydesktop.ScyDesktop;
import eu.scy.client.desktop.scydesktop.tooltips.TooltipManager;
import eu.scy.client.desktop.scydesktop.uicontrols.EloIconButton;
import javafx.lang.FX;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import eu.scy.client.desktop.scydesktop.art.javafx.InstructionTypesIcon;
import eu.scy.client.desktop.scydesktop.imagewindowstyler.ImageWindowStyler;
import eu.scy.client.desktop.scydesktop.scywindows.ModalDialogLayer;
import eu.scy.client.desktop.scydesktop.scywindows.MoreInfoToolFactory;
import eu.scy.client.desktop.scydesktop.scywindows.ShowInfoUrl;
import eu.scy.client.desktop.scydesktop.scywindows.moreinfomanager.MoreInfoWindow;
import eu.scy.client.desktop.scydesktop.tools.corner.missionmap.LasFX;
import eu.scy.common.scyelo.ScyElo;
import java.lang.Void;
import eu.scy.client.desktop.scydesktop.scywindows.MoreInfoManager;
import eu.scy.client.common.scyi18n.UriLocalizer;
import javafx.scene.CacheHint;

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
   public var moreInfoToolFactory: MoreInfoToolFactory on replace { moreInfoToolFactoryChanged() };
   public var moreInfoManager: MoreInfoManager;
   var eloIcon = windowStyler.getScyEloIcon("mission_map");
   def missionMapWindow: MoreInfoWindow = MoreInfoWindow {
              title: ##"Mission navigation"
              eloIcon: eloIcon.clone()
              windowColorScheme: windowStyler.getWindowColorScheme(ImageWindowStyler.generalNavigation)
              content: bigMissionMap
              closeAction: hideBigMissionMap
              mouseClickedAction: mouseClickedInMissionWindowWindow
              cacheHint: CacheHint.SPEED
              cache: true
           }
   def missionMapButton = EloIconButton {
              size: buttonSize
              actionScheme: buttonActionScheme
              eloIcon: eloIcon.clone()
              action: showBigMissionMap
              tooltipManager: tooltipManager
              tooltip: bind "{navigationTooltip}{missionModel.activeLas.title}"
           }
   def relativInstructioneWindowScreenBoder = 0.2;
   def sceneWidth = bind scene.width on replace { sceneSizeChanged() };
   def sceneHeight = bind scene.height on replace { sceneSizeChanged() };
   def relativeWindowScreenBoder = 0.0;
   def navigationTooltip = "navigation\nnow working on: ";
   var bigMissionMapVisible = false;
   var initPhase = true;
   var deferLoadTimerCount = 5;
   def instructionWindow: MoreInfoWindow = MoreInfoWindow {
              title: "Instruction"
              eloIcon: windowStyler.getScyEloIcon("assignment")
              infoTypeIcon: InstructionTypesIcon {}
              openAction: showInstructionWindow;
              closeAction: hideInstructionWindow;
              hideCloseButton: true
              visible: false
              tooltipManager: tooltipManager;
              cacheHint: CacheHint.SPEED
              cache: true
           }
   var instructionTool: ShowInfoUrl;
   def uriLocalizer = new UriLocalizer();
   def missionMapInstructionAvailable = missionModel.missionMapInstructionUri != null or missionModel.missionMapInstructionUri.toString() != "";
   var missionMapInstructionShown = false;
   var nrOfTimesInstructionShowed = 0;

   init {
      FX.deferAction(function(): Void {
         initializer.loadTimer.startActivity("show big mission map");
         showBigMissionMap();
      });
      println("missionModel.missionMapInstructionUri: {missionModel.missionMapInstructionUri} -> {missionMapInstructionAvailable}");
   }

   public override function create(): Node {
      if (missionModel.missionMapButtonIconType != "") {
         def newEloIcon = windowStyler.getScyEloIcon(missionModel.missionMapButtonIconType);
         //         println("missionModel.missionMapButtonIconType: {missionModel.missionMapButtonIconType} -> {newEloIcon}");
         newEloIcon.windowColorScheme = eloIcon.windowColorScheme;
         missionMapButton.eloIcon = newEloIcon;
         missionMapWindow.eloIcon = newEloIcon.clone();
      }

      bigMissionMap.anchorClicked = hideBigMissionMap;
      bigMissionMap.lasInfoTooltipCreator.openElo = openElo;
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
      if (instructionWindow.visible) {
         instructionWindow.width = (1 - 2 * relativInstructioneWindowScreenBoder) * scene.width;
         instructionWindow.height = (1 - 1 * relativInstructioneWindowScreenBoder) * scene.height;
         instructionWindow.layoutX = relativInstructioneWindowScreenBoder * scene.width;
         instructionWindow.layoutY = 0.0;
         instructionWindow.curtainControl.layoutX = instructionWindow.width / 2.0;
         instructionWindow.curtainControl.layoutY = instructionWindow.height + 8;
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
         if (missionMapInstructionAvailable and not missionMapInstructionShown) {
            addInstructionWindow();
            missionMapInstructionShown = true;
         }
      }
   }

   function deferLoadTimer(): Void {
      if (deferLoadTimerCount <= 0) {
         initializer.loadTimer.endActivity();
      } else {
         initializer.loadTimer.startActivity("deferLoadTimer {deferLoadTimerCount}");
         --deferLoadTimerCount;
         FX.deferAction(deferLoadTimer);
      }
   }

   function hideBigMissionMap(): Void {
      if (bigMissionMapVisible) {
         bigMissionMapVisible = false;
         ModalDialogLayer.removeModalDialog(missionMapWindow);
         if (missionMapInstructionAvailable) {
            hideInstructionWindow();
            ModalDialogLayer.removeModalDialog(instructionWindow, false, true);
            missionMapInstructionShown = false;
         }
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

   function addInstructionWindow(): Void {
      initInstructionWindow();
      if (nrOfTimesInstructionShowed > 0) {
         instructionWindow.visible = true;
         instructionWindow.setControlFunctionOpen();
         ModalDialogLayer.addHiddenNode(instructionWindow);
         sceneSizeChanged();
         ModalDialogLayer.resize();
      } else {
         showInstructionWindow()
      }
   }

   function showInstructionWindow(): Void {
      initInstructionWindow();
      instructionWindow.setControlFunctionClose();
      ModalDialogLayer.addModalDialog(instructionWindow, false, true, false);
      sceneSizeChanged();
      ++nrOfTimesInstructionShowed
   }

   function hideInstructionWindow(): Void {
      if (instructionWindow.visible) {
         instructionWindow.setControlFunctionOpen();
         ModalDialogLayer.removeModalDialog(instructionWindow, true, false);
      }
   }

   function initInstructionWindow(): Void {
      if (instructionWindow.content == null) {
         instructionWindow.content = moreInfoToolFactory.createMoreInfoTool(moreInfoManager);
         instructionWindow.windowColorScheme = eloIcon.windowColorScheme;
         if (instructionWindow.content instanceof ShowInfoUrl) {
            instructionTool = instructionWindow.content as ShowInfoUrl;
            instructionTool.showInfoUrl(uriLocalizer.localizeUrlwithChecking(missionModel.missionMapInstructionUri.toURL()));
         }
      }
   }

   function moreInfoToolFactoryChanged() {
      if (instructionWindow.content != null) {
         instructionWindow.content = moreInfoToolFactory.createMoreInfoTool(moreInfoManager);
         if (instructionWindow.content instanceof ShowInfoUrl) {
            instructionTool = instructionWindow.content as ShowInfoUrl;
         }
      }
   }

}
