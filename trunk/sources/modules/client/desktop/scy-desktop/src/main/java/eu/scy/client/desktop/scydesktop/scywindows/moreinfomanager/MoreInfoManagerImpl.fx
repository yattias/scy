/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.scywindows.moreinfomanager;

import eu.scy.client.desktop.scydesktop.scywindows.MoreInfoManager;
import javafx.scene.Node;
import javafx.scene.Scene;
import eu.scy.client.desktop.scydesktop.scywindows.WindowStyler;
import eu.scy.client.desktop.desktoputils.art.ScyColors;
import eu.scy.client.desktop.desktoputils.art.WindowColorScheme;
import org.apache.log4j.Logger;
import eu.scy.client.desktop.scydesktop.scywindows.MoreInfoToolFactory;
import eu.scy.client.desktop.scydesktop.scywindows.ShowInfoUrl;
import java.net.URI;
import eu.scy.client.desktop.scydesktop.scywindows.MoreInfoTypes;
import eu.scy.client.common.scyi18n.UriLocalizer;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.client.desktop.desktoputils.art.EloIcon;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import eu.scy.client.desktop.scydesktop.scywindows.ModalDialogLayer;
import eu.scy.client.desktop.desktoputils.art.javafx.MoreAssignmentTypeIcon;
import eu.scy.client.desktop.desktoputils.art.javafx.MoreResourcesTypeIcon;
import eu.scy.client.desktop.desktoputils.art.javafx.InstructionTypesIcon;
import eu.scy.client.desktop.scydesktop.tooltips.TooltipManager;
import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

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
   public var tooltipManager: TooltipManager;
   def noLasColorScheme = WindowColorScheme.getWindowColorScheme(ScyColors.darkGray);
   var colorScheme = noLasColorScheme;
   def relativInstructioneWindowScreenBoder = 0.2;
   def relativeMoreInfoWindowScreenBoder = 0.15;
   def sceneWidth = bind scene.width on replace { sceneSizeChanged() };
   def sceneHeight = bind scene.height on replace { sceneSizeChanged() };
   def instructionWindow: MoreInfoWindow = MoreInfoWindow {
         title: ##"Instruction"
         infoTypeIcon: InstructionTypesIcon {}
         openAction: showInstructionWindow;
         closeAction: hideInstructionWindow;
         hideCloseButton: true
         visible: false
         tooltipManager: tooltipManager;
      }
   var instructionTool: ShowInfoUrl;
   def moreInfoWindow: MoreInfoWindow = MoreInfoWindow {
         title: "More info"
         closeAction: hideMoreInfoWindow
         hideCloseButton: false
         visible: false
      }
   def agendaWindow: MoreInfoWindow = MoreInfoWindow {
         title: "Agenda"
         infoTypeIcon: InstructionTypesIcon {}
         closeAction: hideAgendaWindow
         openAction: showAgendaWindow;
         windowColorScheme: bind colorScheme;
         hideCloseButton: true
         visible: false
      }
   public-read var agendaNode: AgendaNode;
   var moreInfoTool: ShowInfoUrl;
   def uriLocalizer = new UriLocalizer();
   var runPhase = false;
   var showingMoreInfoWindow = false;

   init {
      runPhase = true;
      activeLasChanged();
      if (Boolean.getBoolean("agenda")) {
        showAgendaWindow();
      }
      sceneSizeChanged();
   }

   function sceneSizeChanged() : Void {
      if (instructionWindow.visible) {
         instructionWindow.width = (1 - 2 * relativInstructioneWindowScreenBoder) * scene.width;
         instructionWindow.height = (1 - 1 * relativInstructioneWindowScreenBoder) * scene.height;
         instructionWindow.layoutX = relativInstructioneWindowScreenBoder * scene.width;
         instructionWindow.layoutY = 0.0;
         instructionWindow.curtainControl.layoutX = instructionWindow.width / 2.0;
         instructionWindow.curtainControl.layoutY = instructionWindow.height + 8;
      }
      if (agendaWindow.visible) {
         agendaWindow.width = (1 - 2 * relativInstructioneWindowScreenBoder) * scene.width;
         agendaWindow.height = (1 - 1 * relativInstructioneWindowScreenBoder) * scene.height;
         agendaWindow.layoutX = relativInstructioneWindowScreenBoder * scene.width;
         agendaWindow.layoutY = 0.0;
         agendaWindow.curtainControl.layoutX = agendaWindow.width / 2.0;
         agendaWindow.curtainControl.layoutY = agendaWindow.height + 8;
      }
      if (moreInfoWindow.visible) {
         moreInfoWindow.width = (1 - 2 * relativeMoreInfoWindowScreenBoder) * scene.width;
         moreInfoWindow.height = (1 - 2 * relativeMoreInfoWindowScreenBoder) * scene.height;
      }
      ModalDialogLayer.resize();
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

   function showAgendaWindow(): Void {
      initAgendaWindow();
      agendaWindow.eloIcon = windowStyler.getScyEloIcon(activeLas.mainAnchor.scyElo);
      agendaWindow.eloIcon.windowColorScheme = colorScheme;
      agendaWindow.title = "Agenda";
      agendaWindow.visible = true;
      agendaWindow.setControlFunctionClose();
      ModalDialogLayer.addModalDialog(agendaWindow, false, true, false, 20);
      sceneSizeChanged();
   }

   function showInstructionWindow(): Void {
      initInstructionWindow();
      instructionWindow.windowColorScheme = colorScheme;
      instructionWindow.eloIcon = windowStyler.getScyEloIcon(activeLas.mainAnchor.scyElo);
      instructionWindow.eloIcon.windowColorScheme = colorScheme;
      instructionWindow.title = activeLas.title;
      instructionTool.showInfoUrl(uriLocalizer.localizeUrlwithChecking(activeLas.instructionUri.toURL()));
      instructionWindow.visible = true;
      instructionWindow.setControlFunctionClose();
      ModalDialogLayer.addModalDialog(instructionWindow, false, true, false, 0);
      sceneSizeChanged();
   }

   function hideInstructionWindow(): Void {
      if (instructionWindow.visible) {
         instructionWindow.setControlFunctionOpen();
         ModalDialogLayer.removeModalDialog(instructionWindow, true, false);
      }
   }

   function hideAgendaWindow(): Void {
      if (agendaWindow.visible) {
         agendaWindow.setControlFunctionOpen();
         ModalDialogLayer.removeModalDialog(agendaWindow, true, false);
      }
   }

   function initAgendaWindow(): Void {
      if (agendaWindow.content == null) {
         agendaNode = AgendaNode {
             };
         agendaWindow.content = agendaNode
      }
   }

   function initInstructionWindow(): Void {
      if (instructionWindow.content == null) {
         instructionWindow.content = moreInfoToolFactory.createMoreInfoTool(this);
         if (instructionWindow.content instanceof ShowInfoUrl) {
            instructionTool = instructionWindow.content as ShowInfoUrl
         }
      }
   }

   function moreInfoToolFactoryChanged(){
      if (instructionWindow.content != null) {
         instructionWindow.content = moreInfoToolFactory.createMoreInfoTool(this);
         if (instructionWindow.content instanceof ShowInfoUrl) {
            instructionTool = instructionWindow.content as ShowInfoUrl;
         }
         if (instructionWindow.visible and not runPhase){
            showInstructionWindow();
         }
      }
   }

   public override function showMoreInfo(infoUri: URI, type: MoreInfoTypes, eloUri: URI): Void {
      var scyElo = activeLas.mainAnchor.scyElo;
      if (eloUri!=null){
         scyElo = ScyElo.loadMetadata(eloUri, tbi);
      }
      showMoreInfo(infoUri, type, scyElo);
   }

   public override function showMoreInfo(infoUri: URI, type: MoreInfoTypes, scyElo: ScyElo): Void {
      def moreInfoColorScheme = windowStyler.getWindowColorScheme(scyElo);
      def title = scyElo.getTitle();
      def eloIcon = windowStyler.getScyEloIcon(scyElo);
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
      return ##"Unknown type"
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
      if (not showingMoreInfoWindow){
         ModalDialogLayer.addModalDialog(moreInfoWindow, true, true, false, 0);
         showingMoreInfoWindow = true
      }
      sceneSizeChanged();
   }

   function hideMoreInfoWindow(): Void {
      ModalDialogLayer.removeModalDialog(moreInfoWindow);
      showingMoreInfoWindow = false;
   }

   function initMoreInfoWindow(): Void {
      if (moreInfoWindow.content == null) {
         moreInfoWindow.content = moreInfoToolFactory.createMoreInfoTool(this);
         if (moreInfoWindow.content instanceof ShowInfoUrl) {
            moreInfoTool = moreInfoWindow.content as ShowInfoUrl
         }
      }
   }

}
