/*
 * LoginDialog.fx
 *
 * Created on 7-dec-2009, 10:07:06
 */
package eu.scy.client.desktop.scydesktop.login;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import eu.scy.client.desktop.scydesktop.scywindows.window.StandardScyWindow;
import eu.scy.client.desktop.scydesktop.scywindows.WindowStyler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Math;

/**
 * @author sikken
 */
// place your code here
class LoginDialog extends CustomNode {


   public var windowStyler: WindowStyler;

   public override function create(): Node {
      var loginNode = LoginNode{
         loginAction:loginAction
      }
      //TODO, why Math.abs????
      var loginWidth = loginNode.boundsInLocal.width + Math.abs(loginNode.boundsInLocal.minX);
      var loginHeight = loginNode.boundsInLocal.height+ Math.abs(loginNode.boundsInLocal.minY);
      println("loginNode.boundsInLocal: {loginNode.boundsInLocal}");
      var loginWindow = StandardScyWindow {
                 title: "SCY-LAB login"
                 color: Color.GREEN
                 drawerColor: Color.color(0.3, 0.7, 0.3);
                 height: loginHeight;
                 width:loginWidth;
                 scyContent:loginNode
                 allowClose: false;
                 allowResize: false;
                 allowRotate: false;
                 allowMinimize: false;
              };
      loginWindow.openWindow(loginWidth, loginHeight);
      windowStyler.style(loginWindow);
      return loginWindow;
   }

   function loginAction(userName:String,password:String):Void{

   }

}

function run(){
   var loginDialog = LoginDialog {
              layoutX: 10
              layoutY: 10
           }

   Stage {
      title: "Login dialog test"
      scene: Scene {
         width: 300
         height: 200
         content: [
            loginDialog
         ]
      }
   }

}
