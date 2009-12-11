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
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Math;
import eu.scy.client.desktop.scydesktop.ScyDesktop;
import eu.scy.client.desktop.scydesktop.scywindows.window.MouseBlocker;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import eu.scy.client.desktop.scydesktop.Initializer;
import eu.scy.client.desktop.scydesktop.scywindows.window.CharacterEloIcon;

/**
 * @author sikken
 */
// place your code here
public class LoginDialog extends CustomNode {

   public var initializer: Initializer;
   public var createScyDesktop: function(tbi:ToolBrokerAPI, userName : String): ScyDesktop;
   var loginWindow: StandardScyWindow;
   var loginNode: LoginNode;

   def loginColor = Color.web("#0ea7bf");

   init {
      FX.deferAction(function () {
         MouseBlocker.initMouseBlocker(scene.stage);
      });
   }

   public override function create(): Node {
      loginNode = LoginNode {
                 loginAction: loginAction
                 defaultUserName: initializer.defaultUserName
                 defaultPassword: initializer.defaultPassword
              }
      //TODO, why Math.abs????
      var loginWidth = loginNode.boundsInLocal.width + Math.abs(loginNode.boundsInLocal.minX);
      var loginHeight = loginNode.boundsInLocal.height + Math.abs(loginNode.boundsInLocal.minY);
      println("loginNode.boundsInLocal: {loginNode.boundsInLocal}");
      loginWindow = StandardScyWindow {
         title: "SCY-Lab login"
         eloIcon: CharacterEloIcon {
            iconCharacter: "L"
            color: loginColor
         }
         color: loginColor
         drawerColor: loginColor;
         height: loginHeight;
         width: loginWidth;
         scyContent: loginNode
         allowClose: false;
         allowResize: false;
         allowRotate: false;
         allowMinimize: false;
      };
      loginWindow.openWindow(loginWidth, loginHeight);
      loginWindow.activated = true;
      FX.deferAction(placeWindowCenter);

      return loginWindow;
   }

   function placeWindowCenter(): Void {
      loginWindow.layoutX = this.scene.stage.width / 2 - loginWindow.width / 2;
      loginWindow.layoutY = this.scene.stage.height / 2 - loginWindow.height / 2;
//      println("placeWindowCenter: {scene.width}, {scene.stage.width}, {loginWindow.width}");
//      println("placeWindowCenter: {scene.height}, {scene.stage.height}, {loginWindow.height}");
   }

   function loginAction(userName: String, password: String): Void {
      println("userName: {userName}, password: {password}");
      try {
         var toolBrokerAPI = initializer.toolBrokerLogin.login(userName, password);
         placeScyDescktop(toolBrokerAPI, userName);
      } catch (e: LoginFailedException) {
         loginNode.loginFailed();
      }
   }

   function placeScyDescktop(toolBrokerAPI: ToolBrokerAPI, userName: String) {
      // using the sceneContent, with a copy of scene.content, does work
      // directly adding scyDesktop to scene.content does not seem to work
      var sceneContent = scene.content;
      var scyDesktop = createScyDesktop(toolBrokerAPI, userName);
      delete this from sceneContent;
      insert scyDesktop into sceneContent;
      scene.content = sceneContent;
   }
}

function run()   {
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
