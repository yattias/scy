/*
 * ModalDialogBox.fx
 *
 * Created on 18-jan-2010, 11:14:35
 */
package eu.scy.client.desktop.scydesktop.scywindows.scydesktop;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.scywindows.window.StandardScyWindow;
import javafx.scene.Scene;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import eu.scy.client.desktop.scydesktop.scywindows.EloIcon;
import eu.scy.client.desktop.scydesktop.art.WindowColorScheme;
import eu.scy.client.desktop.scydesktop.art.ScyColors;
import javafx.scene.layout.Resizable;
import java.lang.Void;
import eu.scy.client.desktop.scydesktop.scywindows.ModalDialogLayer;

/**
 * @author sikken
 */
// place your code here
public class ModalDialogBox extends CustomNode {

   public var content: Node;
   public var targetScene: Scene; // not used
   public var title: String;
   public var eloIcon: EloIcon;
   public var windowColorScheme: WindowColorScheme;
   public var closeAction: function(): Void;
   var dialogWindow: ScyWindow;

   init {
      FX.deferAction(place);
   }

   public override function create(): Node {
      if (windowColorScheme == null) {
         windowColorScheme = WindowColorScheme.getWindowColorScheme(ScyColors.darkGray);
      }

      dialogWindow = StandardScyWindow {
            visible: false
            scyContent: content;
            title: title
            eloIcon: eloIcon;
            windowColorScheme: windowColorScheme
            closedAction: function(window: ScyWindow) {
               close();
            }
            allowMinimize: false
            allowClose: false
            allowResize: content instanceof Resizable
            allowCenter: false
            activated: true
         }
      dialogWindow.open();
      dialogWindow
   }

   public function close(): Void {
      ModalDialogLayer.removeModalDialog(this);
      closeAction();
   }

   public function place(): Void {
      ModalDialogLayer.addModalDialog(this);
      FX.deferAction(function(): Void {
         center();
         dialogWindow.visible = true;
      });
      this.requestFocus();
   }

   function center(): Void {
      dialogWindow.layoutX = scene.width / 2 - dialogWindow.layoutBounds.width / 2;
      dialogWindow.layoutY = scene.height / 2 - dialogWindow.layoutBounds.height / 2;
   }

}
