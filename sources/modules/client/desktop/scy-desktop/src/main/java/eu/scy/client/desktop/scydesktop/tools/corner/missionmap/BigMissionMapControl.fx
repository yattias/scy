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
import eu.scy.client.desktop.desktoputils.art.javafx.InstructionTypesIcon;
import eu.scy.client.desktop.scydesktop.imagewindowstyler.ImageWindowStyler;
import eu.scy.client.desktop.scydesktop.scywindows.ModalDialogLayer;
import eu.scy.client.desktop.scydesktop.scywindows.MoreInfoToolFactory;
import eu.scy.client.desktop.scydesktop.scywindows.ShowInfoUrl;
import eu.scy.client.desktop.scydesktop.scywindows.moreinfomanager.MoreInfoWindow;
import eu.scy.common.scyelo.ScyElo;
import java.lang.Void;
import eu.scy.client.desktop.scydesktop.scywindows.MoreInfoManager;
import eu.scy.client.common.scyi18n.UriLocalizer;
import javafx.scene.CacheHint;
import eu.scy.client.desktop.scydesktop.tooltips.BubbleManager;
import eu.scy.client.desktop.scydesktop.tooltips.BubbleLayer;
import eu.scy.client.desktop.scydesktop.tooltips.BubbleKey;

/**
 * @author SikkenJ
 */
public def defaultMissionMapIconName = "mission_map";

public class BigMissionMapControl extends CustomNode {

   public var bigMissionMap: BigMissionMap;
   public var missionModel: MissionModelFX;
   public var windowStyler: WindowStyler;
   public var scyWindowControl: ScyWindowControl;
   public var initializer: Initializer;
   public var tooltipManager: TooltipManager;
   public var bubbleManager: BubbleManager;
   public var scyDesktop: ScyDesktop;
   public var buttonSize = 30.0;
   public var buttonActionScheme = 1;
   public var moreInfoToolFactory: MoreInfoToolFactory on replace { moreInfoToolFactoryChanged() };
   public var moreInfoManager: MoreInfoManager;
   var eloIcon = windowStyler.getScyEloIcon(defaultMissionMapIconName);
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
   def relativInstructioneWindowScreenBoder = 0.2;
   def sceneWidth = bind scene.width on replace { sceneSizeChanged() };
   def sceneHeight = bind scene.height on replace { sceneSizeChanged() };
   def relativeWindowScreenBoder = 0.0;
   public var bigMissionMapVisible = false on replace {
              scyDesktop.config.getToolBrokerAPI().getAwarenessService().setUserPresence(not bigMissionMapVisible);
           };
   var initPhase = true;
   var deferLoadTimerCount = 5;
   def instructionWindow: MoreInfoWindow = MoreInfoWindow {
              title: ##"Instruction"
              eloIcon: windowStyler.getScyEloIcon("assignment")
              infoTypeIcon: InstructionTypesIcon {}
              openAction: showInstructionWindow;
              closeAction: hideInstructionWindow;
              hideCloseButton: true
              visible: false
              tooltipManager: tooltipManager;
              cacheHint: CacheHint.SPEED
              cache: true
              bubbleManager: bubbleManager
              bubbleLayerId: BubbleLayer.MISSION_MAP_CURTAIN
              closeBubbleKey:BubbleKey.MISSION_MAP_CLOSE
              openCloseBubbleKey:BubbleKey.MISION_MAP_CURTAIN_CONTROL
           }
   var instructionTool: ShowInfoUrl;
   def uriLocalizer = new UriLocalizer();
   def missionMapInstructionAvailable = missionModel.missionMapInstructionUri != null or missionModel.missionMapInstructionUri.toString() != "";
   var missionMapInstructionShown = false;
   var nrOfTimesInstructionShowed = 0;
   def navigationTooltip = ##"navigation\nnow working on: ";
   def navigationNoMissionTooltip = ##"navigation is only available when there is a mission";
   def missionPresent = missionModel != null and sizeof missionModel.lasses > 0;
   def missionMapButton = EloIconButton {
              size: buttonSize
              actionScheme: buttonActionScheme
              eloIcon: eloIcon.clone()
              action: showBigMissionMap
              disableButton: not missionPresent
              tooltipManager: tooltipManager
              tooltip: bind if (missionPresent) "{navigationTooltip}{missionModel.activeLas.title}" else navigationNoMissionTooltip
           }

   init {
      if (missionPresent) {
         FX.deferAction(function(): Void {
            initializer.loadTimer.startActivity("show big mission map");
            showBigMissionMap();
         });
         println("missionModel.missionMapInstructionUri: {missionModel.missionMapInstructionUri} -> {missionMapInstructionAvailable}");
      } else {
         showDesktopContent();
      }
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
      bubbleManager.createBubble(missionMapButton, BubbleLayer.DESKTOP, BubbleKey.MISSION_MAP_CONTROL, missionMapWindow.eloIcon.windowColorScheme);
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
               showDesktopContent();
            }
         });
         ModalDialogLayer.addModalDialog(missionMapWindow, true, false, true, 0);
         bubbleManager.showingLayer(BubbleLayer.MISSION_MAP);
         if (missionMapInstructionAvailable and not missionMapInstructionShown) {
            addInstructionWindow();
            missionMapInstructionShown = true;
         }
      }
   }

   function showDesktopContent(): Void {
      FX.deferAction(scyDesktop.showContent);
      deferLoadTimer();
      initPhase = false;
   }

   function deferLoadTimer(): Void {
      if (deferLoadTimerCount <= 0) {
         scyDesktop.bubbleManager.start();
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
         bubbleManager.hidingLayer(BubbleLayer.MISSION_MAP);
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
      ModalDialogLayer.addModalDialog(instructionWindow, false, true, false, 0);
      sceneSizeChanged();
      ++nrOfTimesInstructionShowed;
      bubbleManager.showingLayer(BubbleLayer.MISSION_MAP_CURTAIN);
   }

   function hideInstructionWindow(): Void {
      if (instructionWindow.visible) {
         instructionWindow.setControlFunctionOpen();
         ModalDialogLayer.removeModalDialog(instructionWindow, true, false);
         bubbleManager.hidingLayer(BubbleLayer.MISSION_MAP_CURTAIN);
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
