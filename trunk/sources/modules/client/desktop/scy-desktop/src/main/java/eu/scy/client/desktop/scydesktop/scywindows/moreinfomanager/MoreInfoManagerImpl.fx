/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.scywindows.moreinfomanager;

import eu.scy.client.desktop.scydesktop.scywindows.MoreInfoManager;
import javafx.scene.Node;
import javafx.scene.Scene;
import eu.scy.client.desktop.scydesktop.scywindows.WindowStyler;
import eu.scy.client.desktop.scydesktop.art.ScyColors;
import eu.scy.client.desktop.scydesktop.art.WindowColorScheme;
import org.apache.log4j.Logger;
import eu.scy.client.desktop.scydesktop.scywindows.MoreInfoToolFactory;
import eu.scy.client.desktop.scydesktop.scywindows.ShowInfoUrl;
import java.net.URI;
import eu.scy.client.desktop.scydesktop.scywindows.MoreInfoTypes;
import eu.scy.client.common.scyi18n.UriLocalizer;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.client.desktop.scydesktop.scywindows.EloIcon;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import eu.scy.client.desktop.scydesktop.scywindows.ModalDialogLayer;
import eu.scy.client.desktop.scydesktop.art.javafx.MoreAssignmentTypeIcon;
import eu.scy.client.desktop.scydesktop.art.javafx.MoreResourcesTypeIcon;
import eu.scy.client.desktop.scydesktop.art.javafx.InstructionTypesIcon;
import eu.scy.client.common.scyi18n.ResourceBundleWrapper;

/**
 * @author SikkenJ
 */
public class MoreInfoManagerImpl extends MoreInfoManager {

   def logger = Logger.getLogger(this.getClass());
   public override var activeLas on replace { activeLasChanged() };
   public-init var scene: Scene;
   public var windowStyler: WindowStyler;
   public var moreInfoToolFactory: MoreInfoToolFactory on replace { moreInfoToolFactoryChanged() };
   public var tbi: ToolBrokerAPI;
   def noLasColorScheme = WindowColorScheme.getWindowColorScheme(ScyColors.darkGray);
   var colorScheme = noLasColorScheme;
   def relativeWindowScreenBoder = 0.2;
   def sceneWidth = bind scene.width on replace { sceneSizeChanged() };
   def sceneHeight = bind scene.height on replace { sceneSizeChanged() };
   def instructionWindow: MoreInfoWindow = MoreInfoWindow {
         title: "Instruction"
         infoTypeIcon: InstructionTypesIcon {}
         openAction: showInstructionWindow;
         closeAction: hideInstructionWindow;
         hideCloseButton: true
         visible: false
      }
   var instructionTool: ShowInfoUrl;
   def moreInfoWindow: MoreInfoWindow = MoreInfoWindow {
         title: "More info"
         closeAction: hideMoreInfoWindow
         hideCloseButton: false
         visible: false
      }
   var moreInfoTool: ShowInfoUrl;
   def resourceBundleWrapper = new ResourceBundleWrapper(this.getClass());
   def uriLocalizer = new UriLocalizer();
   var runPhase = false;

   init {
      runPhase = true;
      activeLasChanged();
      sceneSizeChanged();
   }

   function sceneSizeChanged() : Void {
      if (instructionWindow.visible) {
         instructionWindow.width = (1 - 2 * relativeWindowScreenBoder) * scene.width;
         instructionWindow.height = (1 - 1 * relativeWindowScreenBoder) * scene.height;
         instructionWindow.layoutX = relativeWindowScreenBoder * scene.width;
         instructionWindow.layoutY = 0.0;
         instructionWindow.curtainControl.layoutX = instructionWindow.width / 2.0;
         instructionWindow.curtainControl.layoutY = instructionWindow.height + 8;
      }
      if (moreInfoWindow.visible) {
         moreInfoWindow.width = (1 - 2 * relativeWindowScreenBoder) * scene.width;
         moreInfoWindow.height = (1 - 2 * relativeWindowScreenBoder) * scene.height;
      }
   }

   public override function getControlNode(): Node {
      return null;
   }

   function activeLasChanged() {
      //      logger.info("active las changed: {activeLas.id}");
      if (activeLas == null) {
         colorScheme = noLasColorScheme;
      } else {
         colorScheme = windowStyler.getWindowColorScheme(activeLas.mainAnchor.scyElo);
         if (runPhase and activeLas.nrOfTimesInstructionShowed < 1) {
            //            FX.deferAction(showInstructionWindow);
            showInstructionWindow();
            ++activeLas.nrOfTimesInstructionShowed;
         }
      }
   }

   function showInstructionWindow(): Void {
      initInstructionWindow();
      instructionWindow.windowColorScheme = colorScheme;
      instructionWindow.eloIcon = windowStyler.getScyEloIcon(activeLas.mainAnchor.scyElo);
      //      instructionWindow.infoTypeIcon = CharacterEloIcon {
      //            color: colorScheme.mainColor
      //            iconCharacter: "I"
      //            selected: false
      //         }
      instructionWindow.title = activeLas.title;
      instructionTool.showInfoUrl(uriLocalizer.localizeUrlwithChecking(activeLas.instructionUri.toURL()));
      instructionWindow.visible = true;
      instructionWindow.setControlFunctionClose();
      sceneSizeChanged();
      ModalDialogLayer.addModalDialog(instructionWindow, false, true);
   }

   function hideInstructionWindow(): Void {
      if (instructionWindow.visible) {
         instructionWindow.setControlFunctionOpen();
         ModalDialogLayer.removeModalDialog(instructionWindow, true, false);
      }
   }

   function initInstructionWindow(): Void {
      if (instructionWindow.content == null) {
         instructionWindow.content = moreInfoToolFactory.createMoreInfoTool();
         if (instructionWindow.content instanceof ShowInfoUrl) {
            instructionTool = instructionWindow.content as ShowInfoUrl
         }
      }
   }

   function moreInfoToolFactoryChanged(){
      if (instructionWindow.content != null) {
         instructionWindow.content = moreInfoToolFactory.createMoreInfoTool();
         if (instructionWindow.content instanceof ShowInfoUrl) {
            instructionTool = instructionWindow.content as ShowInfoUrl;
         }
         if (instructionWindow.visible){
            showInstructionWindow()
         }
      }
   }

   public override function showMoreInfo(infoUri: URI, type: MoreInfoTypes, eloUri: URI): Void {
      showMoreInfo(infoUri, type, ScyElo.loadMetadata(eloUri, tbi));
   }

   public override function showMoreInfo(infoUri: URI, type: MoreInfoTypes, scyElo: ScyElo): Void {
      def moreInfoColorScheme = windowStyler.getWindowColorScheme(scyElo);
      def title = scyElo.getTitle();
      def eloIcon = windowStyler.getScyEloIcon(scyElo);
      //      def infoTypeIcon = CharacterEloIcon {
      //            color: colorScheme.mainColor
      //            iconCharacter: getMoreInfoTitle(type).substring(0, 1)
      //            selected: false
      //         }
      def infoTypeIcon = getMoreInfoTypeIcon(type);
      showMoreInfoWindow(infoUri, title, eloIcon, infoTypeIcon, moreInfoColorScheme);
   }

   function getMoreInfoTitle(type: MoreInfoTypes): String {
      if (MoreInfoTypes.ASSIGNMENT == type) {
         return ##"Assignment"
      }
      if (MoreInfoTypes.RESOURCES == type) {
         return ##"Resources"
      }
      return ##"Unknonw type"
   }

   function getMoreInfoTypeIcon(type: MoreInfoTypes): Node {
      if (MoreInfoTypes.ASSIGNMENT == type) {
         return MoreAssignmentTypeIcon {}
      }
      if (MoreInfoTypes.RESOURCES == type) {
         return MoreResourcesTypeIcon {}
      }
      return null
   }

   function showMoreInfoWindow(infoUri: URI, title: String, eloIcon: EloIcon, infoTypeIcon: Node, moreInfoColorScheme: WindowColorScheme): Void {
      initMoreInfoWindow();
      moreInfoWindow.title = title;
      moreInfoWindow.eloIcon = eloIcon;
      moreInfoWindow.infoTypeIcon = infoTypeIcon;
      moreInfoWindow.windowColorScheme = moreInfoColorScheme;
      moreInfoTool.showInfoUrl(uriLocalizer.localizeUrlwithChecking(infoUri.toURL()));
      sceneSizeChanged();
      ModalDialogLayer.addModalDialog(moreInfoWindow, true, true);
   }

   function hideMoreInfoWindow(): Void {
      ModalDialogLayer.removeModalDialog(moreInfoWindow);
   }

   function initMoreInfoWindow(): Void {
      if (moreInfoWindow.content == null) {
         moreInfoWindow.content = moreInfoToolFactory.createMoreInfoTool();
         if (moreInfoWindow.content instanceof ShowInfoUrl) {
            moreInfoTool = moreInfoWindow.content as ShowInfoUrl
         }
      }
   }

}
