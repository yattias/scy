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
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.net.URI;
import eu.scy.client.desktop.scydesktop.scywindows.MoreInfoTypes;
import eu.scy.client.common.scyi18n.UriLocalizer;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.client.desktop.scydesktop.scywindows.EloIcon;
import eu.scy.client.desktop.scydesktop.scywindows.window.CharacterEloIcon;
import eu.scy.toolbrokerapi.ToolBrokerAPI;

/**
 * @author SikkenJ
 */
public class MoreInfoManagerImpl extends MoreInfoManager {

   def logger = Logger.getLogger(this.getClass());
   public override var activeLas on replace { activeLasChanged() };
   public-init var scene: Scene;
   public var windowStyler: WindowStyler;
   public var moreInfoToolFactory: MoreInfoToolFactory;
   public var tbi: ToolBrokerAPI;
   def noLasColorScheme = WindowColorScheme.getWindowColorScheme(ScyColors.darkGray);
   var colorScheme = noLasColorScheme;
   def moreInfoControl = InstructionWindowControl {
         windowColorScheme: bind colorScheme
         clickAction: showInstructionWindow
         layoutX: bind scene.width / 2.0
         layoutY: 0
      }
   def relativeWindowScreenBoder = 0.2;
   def instructionWindow: MoreInfoWindow = MoreInfoWindow {
         title: "Instruction"
         closeAction: hideInstructionWindow
      }
   var instructionTool: ShowInfoUrl;
   def moreInfoWindow: MoreInfoWindow = MoreInfoWindow {
         title: "More info"
         closeAction: hideMoreInfoWindow
      }
   var moreInfoTool: ShowInfoUrl;
   def modalLayer = Rectangle {
         blocksMouse: true
         x: 0, y: 0
         width: bind scene.width, height: bind scene.height
         fill: Color.color(1.0, 1.0, 1.0, 0.5)
         onKeyPressed: function(e: KeyEvent): Void {
         }
         onKeyReleased: function(e: KeyEvent): Void {
         }
         onKeyTyped: function(e: KeyEvent): Void {
         }
      }
   def uriLocalizer = new UriLocalizer();

   init {
      activeLasChanged();
   }

   public override function getControlNode(): Node {
      return moreInfoControl
   }

   function activeLasChanged() {
//      logger.info("active las changed: {activeLas.id}");
      if (activeLas == null) {
         colorScheme = noLasColorScheme;
      } else {
         colorScheme = windowStyler.getWindowColorScheme(activeLas.mainAnchor.eloUri);
      }
   }

   function showInstructionWindow(): Void {
      initInstructionWindow();
      instructionWindow.windowColorScheme = colorScheme;
      instructionWindow.width = (1 - 2 * relativeWindowScreenBoder) * scene.width;
      instructionWindow.height = (1 - 1 * relativeWindowScreenBoder) * scene.height;
      instructionWindow.layoutX = relativeWindowScreenBoder * scene.width;
      instructionWindow.layoutY = 0.0;
      instructionWindow.eloIcon = windowStyler.getScyEloIcon(activeLas.mainAnchor.eloUri);
      instructionWindow.infoTypeIcon = CharacterEloIcon {
            color: colorScheme.mainColor
            iconCharacter: "I"
            selected: false
         }
      instructionWindow.title = activeLas.mainAnchor.scyElo.getTitle();
      instructionTool.showInfoUrl(uriLocalizer.localizeUrlwithChecking(activeLas.instructionUri.toURL()));
      insert modalLayer into scene.content;
      insert instructionWindow into scene.content;
   }

   function hideInstructionWindow(): Void {
      delete modalLayer from scene.content;
      delete instructionWindow from scene.content;
   }

   function initInstructionWindow(): Void {
      if (instructionWindow.content == null) {
         instructionWindow.content = moreInfoToolFactory.createMoreInfoTool();
         if (instructionWindow.content instanceof ShowInfoUrl) {
            instructionTool = instructionWindow.content as ShowInfoUrl
         }
      }
   }

   public override function showMoreInfo(infoUri: URI, type: MoreInfoTypes, eloUri: URI): Void {
      showMoreInfo(infoUri, type, ScyElo.loadMetadata(eloUri, tbi));
   }

   public override function showMoreInfo(infoUri: URI, type: MoreInfoTypes, scyElo: ScyElo): Void {
      def moreInfoColorScheme = windowStyler.getWindowColorScheme(scyElo.getUri());
      def title = scyElo.getTitle();
      def eloIcon = windowStyler.getScyEloIcon(scyElo.getUri());
      def infoTypeIcon = CharacterEloIcon {
            color: colorScheme.mainColor
            iconCharacter: getMoreInfoTitle(type).substring(0, 1)
            selected: false
         }
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

   function showMoreInfoWindow(infoUri: URI, title: String, eloIcon: EloIcon, infoTypeIcon: EloIcon, moreInfoColorScheme: WindowColorScheme): Void {
      initMoreInfoWindow();
      moreInfoWindow.title = title;
      moreInfoWindow.eloIcon = eloIcon;
      moreInfoWindow.infoTypeIcon = infoTypeIcon;
      moreInfoWindow.windowColorScheme = moreInfoColorScheme;
      moreInfoWindow.width = (1 - 2 * relativeWindowScreenBoder) * scene.width;
      moreInfoWindow.height = (1 - 2 * relativeWindowScreenBoder) * scene.height;
      moreInfoWindow.layoutX = relativeWindowScreenBoder * scene.width;
      moreInfoWindow.layoutY = relativeWindowScreenBoder * scene.height;
      moreInfoTool.showInfoUrl(uriLocalizer.localizeUrlwithChecking(infoUri.toURL()));
      insert modalLayer into scene.content;
      insert moreInfoWindow into scene.content;
   }

   function hideMoreInfoWindow(): Void {
      delete modalLayer from scene.content;
      delete moreInfoWindow from scene.content;
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
