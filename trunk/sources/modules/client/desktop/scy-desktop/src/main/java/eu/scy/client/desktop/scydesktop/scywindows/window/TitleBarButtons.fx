/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.scywindows.window;

import eu.scy.client.desktop.scydesktop.tools.TitleBarButtonManager;
import eu.scy.client.desktop.scydesktop.tooltips.TooltipManager;
import eu.scy.client.desktop.scydesktop.uicontrols.EloIconButton;
import eu.scy.client.desktop.scydesktop.scywindows.WindowStyler;
import javafx.stage.Stage;
import javafx.scene.Scene;
import eu.scy.client.desktop.scydesktop.imagewindowstyler.JavaFxWindowStyler;
import eu.scy.client.desktop.scydesktop.tools.TitleBarButton;
import eu.scy.client.desktop.scydesktop.art.eloicons.EloIconFactory;

/**
 * @author SikkenJ
 */
public class TitleBarButtons extends TitleBarItemList, TitleBarButtonManager {

   public-init var tooltipManager: TooltipManager;
   public-init var windowStyler: WindowStyler;
   public override var titleBarButtons on replace { updateItems() };
   var eloIconButtons: EloIconButton[];

   override function updateItems(): Void {
      if (windowStyler==null){
         return
      }
      delete  displayBox.content;
//      for (eloIconButton in eloIconButtons) {
//         tooltipManager.unregisterNode(eloIconButton);
//      }
      delete  eloIconButtons;
      eloIconButtons =
              for (titleBarButton in titleBarButtons) {
                 def eloIcon = windowStyler.getScyEloIcon(titleBarButton.iconType);
                 eloIcon.windowColorScheme = windowColorScheme;
                 eloIcon.selected = false;
                 def eloIconButton = EloIconButton {
                            eloIcon: eloIcon
                            size: itemHeight+4
                            action: bind titleBarButton.action
                            tooltip: bind titleBarButton.tooltip
                            tooltipManager: tooltipManager
                         }
//                 tooltipManager.registerNode(eloIconButton, eloIconButton);
                 eloIconButton
              }
      displayBox.content = eloIconButtons;
      itemListChanged();

   }

}

function run() {
   Stage {
      title: "MyApp"
      onClose: function() {
      }
      scene: Scene {
         width: 200
         height: 200
         content: [
            TitleBarButtons {
               layoutX: 10
               layoutY: 10
               windowStyler: JavaFxWindowStyler {
                     eloIconFactory: EloIconFactory{}
                  }
               titleBarButtons: [
                  TitleBarButton {
                     iconType: "pizza"
                     enabled: true
                  }
                  TitleBarButton {
                     iconType: "drawing"
                     enabled: true
                  }
               ]
            }
         ]
      }
   }

}

