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
import eu.scy.client.desktop.scydesktop.tooltips.impl.SimpleTooltipManager;
import eu.scy.client.desktop.scydesktop.utils.log4j.Logger;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.animation.Interpolator;
import javafx.scene.shape.Rectangle;

/**
 * @author sikken
 */
// place your code here
package def loginColor = Color.hsb(255, 1, 1.0);
package def successColor = Color.hsb(135, 1, 0.8);
package def failedColor = Color.hsb(360, 1, 0.9);


public class LoginDialog extends CustomNode {
   def logger = Logger.getLogger(this.getClass());

   public var initializer   :  Initializer ;
   public var createScyDesktop: function( tbi:  ToolBrokerAPI,userName: String): ScyDesktop;

   var loginWindow: StandardScyWindow;
   var loginNode: LoginNode;

//   def loginColor = Color.web("#0ea7bf");
   init {
      FX.deferAction(function () {
         MouseBlocker.initMouseBlocker(scene.stage);
//         FX.deferAction(function(){
//            if (initializer.autoLogin){
//               if (loginNode.loginEnabled){
//                  loginNode.login();
//               }
//               else{
//                  println("autoLogin, but login is not enabled");
//               }
//            }
//         });

      });
   }

   public override function create(): Node {
      loginNode = LoginNode {
                 loginAction: loginAction
                 defaultUserName: initializer.defaultUserName
                 defaultPassword: initializer.defaultPassword
                 autoLogin:initializer.autoLogin
              }
      loginWindow = StandardScyWindow {
         title: "SCY-Lab login"
         eloIcon: CharacterEloIcon {
            iconCharacter: "L"
            color: loginColor
         }
         color: loginColor
         drawerColor: loginColor;
//         height: loginHeight;
//         width: loginWidth;
         scyContent: loginNode
         allowClose: false;
         allowResize: false;
         allowRotate: false;
         allowMinimize: false;
         opacity:0.0;
      };
      loginWindow.openWindow(0, 0);
      loginWindow.activated = true;
      FX.deferAction(placeWindowCenter);

      return loginWindow;
   }

   function placeWindowCenter(): Void {
      loginWindow.layoutX = this.scene.stage.width / 2 - loginWindow.width / 2;
      loginWindow.layoutY = this.scene.stage.height / 2 - loginWindow.height / 2;
      def window = loginWindow;
      Timeline {
         repeatCount: 1
         keyFrames : [
            at(0ms){
               window.opacity => 0.0
            }
            at(750ms){
               window.opacity => 1.0
            }
         ]
      }.play();
//      println("placeWindowCenter: {scene.width}, {scene.stage.width}, {loginWindow.width}");
//      println("placeWindowCenter: {scene.height}, {scene.stage.height}, {loginWindow.height}");
   }

   function loginAction(userName: String, password: String): Void {
      println("userName: {userName}, password: {password}");
      try {
         var toolBrokerAPI = initializer.toolBrokerLogin.login(userName, password);

         logger.info("tbi.getRepository() : {toolBrokerAPI.getRepository()}\n"
         "tbi.getMetaDataTypeManager() : {toolBrokerAPI.getMetaDataTypeManager()}\n"
         "tbi.getExtensionManager() : {toolBrokerAPI.getExtensionManager()}\n"
         "tbi.getELOFactory() : {toolBrokerAPI.getELOFactory()}\n"
         "tbi.getActionLogger() : {toolBrokerAPI.getActionLogger()}\n"
         "tbi.getAwarenessService() : {toolBrokerAPI.getAwarenessService()}\n"
         "tbi.getDataSyncService() : {toolBrokerAPI.getDataSyncService()}\n"
         "tbi.getPedagogicalPlanService() : {toolBrokerAPI.getPedagogicalPlanService()}");
         showLoginResult(toolBrokerAPI,userName);
         //placeScyDescktop(toolBrokerAPI, userName);
      } catch (e: LoginFailedException) {
         showLoginResult(null,userName);
      }
   }

   function showLoginResult(toolBrokerAPI:ToolBrokerAPI, userName: String){
      def window = loginWindow;
      if (toolBrokerAPI!=null){
         // successfull login
         Timeline {
            repeatCount: 1
            keyFrames : [
               KeyFrame {
                  time : 750ms
                  values:window.color => successColor tween Interpolator.LINEAR;
                  action:function(){
                     loginWindow.scyContent = WelcomeNode{
                        name:userName;
                     }
                  }
               }
               KeyFrame {
                  time : 1000ms
                  action:function(){
                     placeScyDescktop(toolBrokerAPI, userName);
                  }
               }
            ]
         }.play();
      }
      else{
         Timeline {
            repeatCount: 6
            autoReverse:true
            keyFrames : [
               KeyFrame {
                  time : 0ms
                  values:window.color => loginColor tween Interpolator.LINEAR;
               }
               KeyFrame {
                  time : 500ms
                  values:window.color => failedColor tween Interpolator.LINEAR;
               }
            ]
         }.play();
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
      insert SimpleTooltipManager.tooltipGroup into sceneContent;
      scene.content = sceneContent;
   }
}

function run()    {
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
            Rectangle {
               x: 10, y: 10
               width: 10, height: 10
               fill: loginColor
            }
            Rectangle {
               x: 30, y: 10
               width: 10, height: 10
               fill: successColor
            }
            Rectangle {
               x: 50, y: 10
               width: 10, height: 10
               fill: failedColor
            }

            loginDialog
         ]
      }
   }

}
