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
import eu.scy.toolbrokerapi.ServerNotRespondingException;
import eu.scy.toolbrokerapi.LoginFailedException;
import javax.swing.JOptionPane;
import java.awt.Component;
import java.lang.System;
import eu.scy.client.desktop.scydesktop.art.WindowColorScheme;
import eu.scy.client.desktop.scydesktop.art.ScyColors;
import eu.scy.client.desktop.scydesktop.utils.EmptyBorderNode;
import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.ModalDialogBox;

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

   postinit {
      
      FX.deferAction(initMouseBlocker);
//      FX.deferAction(function () {
//         MouseBlocker.initMouseBlocker(scene.stage);
//      });
   }
   
   function initMouseBlocker():Void{
      var theStage = scene.stage;
      if (theStage==null){
         System.err.println("defering initMouseBlocker, because of the missing stage");
         FX.deferAction(initMouseBlocker);
      }
      else{
         MouseBlocker.initMouseBlocker(scene.stage);
      }
   }
   

   public override function create(): Node {
      loginNode = LoginNode {
                 loginAction: loginAction
                 defaultUserName: initializer.defaultUserName
                 defaultPassword: initializer.defaultPassword
                 autoLogin:initializer.autoLogin
                 languages: initializer.languages
              }
      loginWindow = StandardScyWindow {
         title: bind loginNode.loginTitle
         eloIcon: CharacterEloIcon {
            iconCharacter: "L"
            color: loginColor
         }
         windowColorScheme:WindowColorScheme.getWindowColorScheme(ScyColors.green)
         scyContent: EmptyBorderNode{
            content:loginNode
         }
         allowClose: false;
         allowResize: true;
         allowRotate: false;
         allowMinimize: false;
         opacity:0.0;
      };
      loginWindow.windowColorScheme.mainColor = loginColor;
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

         logger.info(
         "tbi.getLoginUserName() : {toolBrokerAPI.getLoginUserName()}\n"
         "tbi.getMission() : {toolBrokerAPI.getMission()}\n"
         "tbi.getRepository() : {toolBrokerAPI.getRepository()}\n"
         "tbi.getMetaDataTypeManager() : {toolBrokerAPI.getMetaDataTypeManager()}\n"
         "tbi.getExtensionManager() : {toolBrokerAPI.getExtensionManager()}\n"
         "tbi.getELOFactory() : {toolBrokerAPI.getELOFactory()}\n"
         "tbi.getActionLogger() : {toolBrokerAPI.getActionLogger()}\n"
         "tbi.getAwarenessService() : {toolBrokerAPI.getAwarenessService()}\n"
         "tbi.getDataSyncService() : {toolBrokerAPI.getDataSyncService()}\n"
         "tbi.getPedagogicalPlanService() : {toolBrokerAPI.getPedagogicalPlanService()}\n"
         "tbi.getStudentPedagogicalPlanService() : {toolBrokerAPI.getStudentPedagogicalPlanService()}");
         showLoginResult(toolBrokerAPI,userName);
         //placeScyDescktop(toolBrokerAPI, userName);
      } catch (e: LoginFailedException) {
         logger.info("failed to login with {e.getUserName()}");
         showLoginResult(null,userName);
      } catch (e: ServerNotRespondingException) {
         logger.info("Could not connect to host {e.getServer()}:{e.getServer()}");
         JOptionPane.showMessageDialog(null as Component,"Could not connect to host {e.getServer()}:{e.getPort()}","Connection problems",JOptionPane.ERROR_MESSAGE);
      }
   }

   function showLoginResult(toolBrokerAPI:ToolBrokerAPI, userName: String){
      def window = loginWindow;
      def windowColorScheme = window.windowColorScheme;
      if (toolBrokerAPI!=null){
         // successfull login
         Timeline {
            repeatCount: 1
            keyFrames : [
               KeyFrame {
                  time : 750ms
                  values:windowColorScheme.mainColor => successColor tween Interpolator.LINEAR;
                  action:function(){
                     loginWindow.scyContent = WelcomeNode{
                        name:userName;
                     }
                  }
               }
               KeyFrame {
                  time : 1000ms
                  action:function(){
                     var stage = scene.stage;
                     var stageTitle = stage.title;
                     stage.title = "{stageTitle} : {userName}";
                     var scyDesktop = placeScyDescktop(toolBrokerAPI, userName);
                     stage.title = "{stageTitle} : {userName} in {scyDesktop.missionModelFX.name}";
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
                  values:windowColorScheme.mainColor => loginColor tween Interpolator.LINEAR;
               }
               KeyFrame {
                  time : 500ms
                  values:windowColorScheme.mainColor => failedColor tween Interpolator.LINEAR;
               }
            ]
         }.play();
         loginNode.loginFailed();
      }
   }


   function placeScyDescktop(toolBrokerAPI: ToolBrokerAPI, userName: String):ScyDesktop {
      // using the sceneContent, with a copy of scene.content, does work
      // directly adding scyDesktop to scene.content does not seem to work
      var scyDesktop = createScyDesktop(toolBrokerAPI, userName);
      var sceneContent = scene.content;
      delete this from sceneContent;
      insert scyDesktop into sceneContent;
      insert ModalDialogBox.modalDialogGroup into sceneContent;
      insert SimpleTooltipManager.tooltipGroup into sceneContent;
      insert MouseBlocker.mouseBlockNode into sceneContent;
      scene.content = sceneContent;
      return scyDesktop;
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
