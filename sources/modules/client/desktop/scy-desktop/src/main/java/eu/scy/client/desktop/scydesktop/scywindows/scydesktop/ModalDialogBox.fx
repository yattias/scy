/*
 * ModalDialogBox.fx
 *
 * Created on 18-jan-2010, 11:14:35
 */

package eu.scy.client.desktop.scydesktop.scywindows.scydesktop;

import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import eu.scy.client.desktop.scydesktop.scywindows.window.StandardScyWindow;
import javafx.scene.Scene;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import javafx.scene.input.KeyEvent;
import eu.scy.client.desktop.scydesktop.scywindows.EloIcon;
import eu.scy.client.desktop.scydesktop.art.WindowColorScheme;

/**
 * @author sikken
 */

// place your code here

public def modalDialogGroup = Group{
   visible: false
}

public class ModalDialogBox extends CustomNode {

   public var content:Node;
   public var targetScene:Scene;
   public var title:String;
   public var eloIcon : EloIcon;
   public var windowColorScheme:WindowColorScheme;
   public var closeAction:function():Void;

   var dialogWindow:ScyWindow;

   init{
      FX.deferAction(place);
   }


   public override function create(): Node {
      dialogWindow = StandardScyWindow{
         scyContent:content;
         title:title
         eloIcon:eloIcon;
         windowColorScheme:windowColorScheme
//         layoutX:scene.width-content.layoutBounds.width/2
//         layoutY:scene.height-content.layoutBounds.height/2
         closedAction:function(window:ScyWindow){
            close();
         }
         allowMinimize:false
         activated:true
      }
      dialogWindow.open();
      return Group {
                 content: [
                    Rectangle {
                       blocksMouse:true
                        x: 0, y: 0
                        width: bind scene.width, height: bind scene.height
                        fill: Color.color(1.0, 1.0, 1.0, 0.5)
                        onKeyPressed: function (e: KeyEvent): Void {
                        }
                        onKeyReleased: function (e: KeyEvent): Void {
                        }
                        onKeyTyped: function (e: KeyEvent): Void {
                        }
                     }
                     dialogWindow
                 ]
              };
   }

   public function close():Void{
//      var sceneContentList = scene.content;
//      delete this from sceneContentList;
//      scene.content = sceneContentList;
      delete this from modalDialogGroup.content;
      modalDialogGroup.visible = false;
      closeAction();
   }

   public function place():Void{
//      var sceneContentList = targetScene.content;
//      insert this into sceneContentList;
//      targetScene.content = sceneContentList;
      insert this into modalDialogGroup.content;
      modalDialogGroup.visible = true;
      this.requestFocus();
      center();
   }

   function center(){
      dialogWindow.layoutX = scene.width/2-content.layoutBounds.width/2;
      dialogWindow.layoutY = scene.height/2-content.layoutBounds.height/2;
   }


}

public function placeInModalDialogBox(content:Node,scene:Scene):ModalDialogBox{
   var modalDialogBox = ModalDialogBox{
      content:content;
   }
   var sceneContentList = scene.content;
   insert modalDialogBox into sceneContentList;
   scene.content = sceneContentList;
   return modalDialogBox;
}

public function placeInModalDialogBox(content:Node[],scene:Scene):ModalDialogBox{
   return placeInModalDialogBox(Group{content:content},scene);
}