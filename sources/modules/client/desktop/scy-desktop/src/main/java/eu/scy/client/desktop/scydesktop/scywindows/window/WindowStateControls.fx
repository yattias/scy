/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.scywindows.window;

import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import javafx.stage.Stage;
import javafx.scene.Scene;
import eu.scy.client.desktop.scydesktop.art.eloicons.EloIconFactory;
import eu.scy.client.desktop.scydesktop.imagewindowstyler.JavaFxWindowStyler;
import eu.scy.client.desktop.scydesktop.scywindows.EloIcon;
import eu.scy.client.desktop.scydesktop.scywindows.WindowStyler;
import eu.scy.client.desktop.scydesktop.tooltips.TooltipManager;
import eu.scy.client.desktop.scydesktop.tooltips.impl.SimpleTooltipManager;
import eu.scy.client.desktop.scydesktop.uicontrols.EloIconButton;

/**
 * @author SikkenJ
 */
public class WindowStateControls extends TitleBarItemList {

   public-init var tooltipManager: TooltipManager;
   public-init var windowStyler: WindowStyler;
   public var window: ScyWindow;
   public var rotateNormalAction: function(): Void;
   public var minimizeAction: function(): Void;
   public var centerAction: function(): Void;
   public var maximizeAction: function(): Void;
   public var enableRotateNormal = true;
   public var enableMinimize = true;
   public var enableCenter = true;
   public var enableMaximize = true;
   def actionScheme = 1;

   override function updateItems(): Void {
      delete  displayBox.content;
      displayBox.content = [
                 EloIconButton {
                    eloIcon: createEloIcon("unrotate")
                    size: itemHeight + 4
                    action: rotateNormalAction
                    disableButton: bind not enableRotateNormal
                    tooltipManager: tooltipManager
                    actionScheme: actionScheme
                    hideBackground: true
                 }
                 EloIconButton {
                    eloIcon: createEloIcon("minimize")
                    size: itemHeight + 4
                    action: minimizeAction
                    disableButton: bind not enableMinimize
                    tooltipManager: tooltipManager
                    actionScheme: actionScheme
                    hideBackground: true
                 }
                 EloIconButton {
                    eloIcon: createEloIcon("center")
                    size: itemHeight + 4
                    action: centerAction
                    disableButton: bind not enableCenter
                    tooltipManager: tooltipManager
                    actionScheme: actionScheme
                    hideBackground: true
                 }
                 EloIconButton {
                    eloIcon: createEloIcon("maximize")
                    size: itemHeight + 4
                    action: maximizeAction
                    disableButton: bind not enableMaximize
                    tooltipManager: tooltipManager
                    actionScheme: actionScheme
                    hideBackground: true
                 }
              ];
      itemListChanged();
   }

   function createEloIcon(name: String): EloIcon {
      def eloIcon = windowStyler.getScyEloIcon(name);
      eloIcon.windowColorScheme = windowColorScheme;
      eloIcon.selected = false;
      eloIcon
   }
}

function run() {
   def tooltipManager = SimpleTooltipManager{};
   def windowStyler= JavaFxWindowStyler {
                     eloIconFactory: EloIconFactory{}
                  }
   def scale = 1.0;
   Stage {
      title: "MyApp"
      onClose: function() {
      }
      scene: Scene {
         width: 200
         height: 200
         content: [
            WindowStateControls {
               tooltipManager: tooltipManager
               windowStyler: windowStyler
               layoutX: 10
               layoutY: 10
            }
            WindowStateControls {
               tooltipManager: tooltipManager
               windowStyler: windowStyler
               layoutX: 10
               layoutY: 50
               enableRotateNormal: false
               enableMinimize: false
               enableCenter: false
               enableMaximize: false
            }
         ]
      }
   }
}
