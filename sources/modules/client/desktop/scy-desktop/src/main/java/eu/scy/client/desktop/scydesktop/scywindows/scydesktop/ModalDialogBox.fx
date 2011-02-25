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
import eu.scy.client.desktop.scydesktop.utils.XFX;
import java.lang.Thread;
import javafx.animation.Timeline;
import javafx.animation.Interpolator;

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

   postinit {
      FX.deferAction(place);
   }

   public override function create(): Node {
      if (windowColorScheme == null) {
         windowColorScheme = WindowColorScheme.getWindowColorScheme(ScyColors.darkGray);
      }

      dialogWindow = StandardScyWindow {
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
            allowMaximize: false
            activated: true
            opacity: 0
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
      });
      this.requestFocus();
   }

   function center(): Void {
       // this is clearly a quick hack to wait manually for the window to redender itself, so
       // that we have the final dialogWindow.layoutBounds that we can take to calculate the
       // correct center position (if someone has a better idea, please remove and make it right)
       // the hack is: wait 500 ms in the background to have the UI thread free to render
       // then content then show it, i.e., make opacity = 1
       XFX.runActionInBackgroundAndCallBack(function() { Thread.sleep(500); return null }, function(o): Void {
           dialogWindow.layoutX = scene.width / 2 - dialogWindow.layoutBounds.width / 2;
           dialogWindow.layoutY = scene.height / 2 - dialogWindow.layoutBounds.height / 2;
           def finalDialogWindow = dialogWindow;
           Timeline {
               keyFrames: [
                   at (500ms) {finalDialogWindow.opacity => 1 tween Interpolator.LINEAR}
               ];
           }.playFromStart();
       });
   }
}