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
import eu.scy.client.desktop.scydesktop.mission.MissionLocator;
import eu.scy.client.desktop.scydesktop.mission.MissionRunConfigs;
import eu.scy.client.desktop.scydesktop.LoginType;
import eu.scy.client.desktop.scydesktop.mission.Missions;
import eu.scy.client.desktop.scydesktop.utils.InjectObjectsUtils;
import java.net.URI;

/**
 * @author sikken
 */
// place your code here
package def loginColor = Color.hsb(255, 1, 1.0);
package def successColor = Color.hsb(135, 1, 0.8);
package def failedColor = Color.hsb(360, 1, 0.9);

public class LoginDialog extends CustomNode, TbiReady {

   def logger = Logger.getLogger(this.getClass());
   public var initializer: Initializer;
   public var createScyDesktop: function(missionRunConfigs: MissionRunConfigs): ScyDesktop;
   var loginWindow: StandardScyWindow;
   var loginNode: LoginNode;
   var windowTitle: String = null;
   var userName:String = null;
   var tbi:ToolBrokerAPI = null;

   postinit {

      FX.deferAction(initMouseBlocker);
   }

   function initMouseBlocker(): Void {
      var theStage = scene.stage;
      if (theStage == null) {
         System.err.println("defering initMouseBlocker, because of the missing stage");
         FX.deferAction(initMouseBlocker);
      } else {
         MouseBlocker.initMouseBlocker(scene.stage);
      }
   }

   public override function create(): Node {
      loginNode = LoginNode {
            loginAction: loginAction
            defaultUserName: initializer.defaultUserName
            defaultPassword: initializer.defaultPassword
            autoLogin: initializer.autoLogin
            languages: initializer.languages
         }
      //      loginNode.layout();
      loginWindow = StandardScyWindow {
            title: bind if (windowTitle == null) loginNode.loginTitle else windowTitle
            //         eloIcon: CharacterEloIcon {
            //            iconCharacter: "L"
            //            color: loginColor
            //         }
            windowColorScheme: WindowColorScheme.getWindowColorScheme(ScyColors.green)
            scyContent: EmptyBorderNode {
               content: loginNode
               widthCorrection: 10.0
               heightCorrection: 6.0
            }
            allowClose: false;
            allowResize: false;
            allowRotate: false;
            allowMinimize: false;
            opacity: 0.0;
         };
      loginWindow.windowColorScheme.mainColor = loginColor;
      loginWindow.activated = true;
      loginWindow.eloIcon = LogoEloIcon {
            color: bind loginWindow.windowColorScheme.mainColor
            selected: false
         }

      loginWindow.openWindow(0, 0);
      FX.deferAction(placeWindowCenter);

      return loginWindow;
   }

   function placeWindowCenter(): Void {
      loginWindow.layoutX = this.scene.stage.width / 2 - loginWindow.width / 2;
      loginWindow.layoutY = this.scene.stage.height / 2 - loginWindow.height / 2;
      def window = loginWindow;
      Timeline {
         repeatCount: 1
         keyFrames: [
            at (0ms) {
               window.opacity => 0.0
            }
            at (750ms) {
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
         def loginResult = initializer.toolBrokerLogin.login(userName, password);
         //         var toolBrokerAPI = initializer.toolBrokerLogin.login(userName, password);
         //         logger.info(
         //         "tbi.getLoginUserName() : {toolBrokerAPI.getLoginUserName()}\n""tbi.getMission() : {toolBrokerAPI.getMission()}\n""tbi.getRepository() : {toolBrokerAPI.getRepository()}\n""tbi.getMetaDataTypeManager() : {toolBrokerAPI.getMetaDataTypeManager()}\n""tbi.getExtensionManager() : {toolBrokerAPI.getExtensionManager()}\n""tbi.getELOFactory() : {toolBrokerAPI.getELOFactory()}\n""tbi.getActionLogger() : {toolBrokerAPI.getActionLogger()}\n""tbi.getAwarenessService() : {toolBrokerAPI.getAwarenessService()}\n""tbi.getDataSyncService() : {toolBrokerAPI.getDataSyncService()}\n""tbi.getPedagogicalPlanService() : {toolBrokerAPI.getPedagogicalPlanService()}\n""tbi.getStudentPedagogicalPlanService() : {toolBrokerAPI.getStudentPedagogicalPlanService()}");
         showLoginResult(loginResult, userName);
      //placeScyDescktop(toolBrokerAPI, userName);
      }
      catch (e: LoginFailedException) {
         logger.info("failed to login with {e.getUserName()}");
         showLoginResult(null, userName);
      }
      catch (e: ServerNotRespondingException) {
         logger.info("Could not connect to host {e.getServer()}:{e.getServer()}");
         JOptionPane.showMessageDialog(null as Component, "Could not connect to host {e.getServer()}:{e.getPort()}", "Connection problems", JOptionPane.ERROR_MESSAGE);
      }
   }

   function showLoginResult(loginResult: Object, userName: String) {
      def window = loginWindow;
      def windowColorScheme = window.windowColorScheme;
      if (loginResult != null) {
         // successfull login
         this.userName = userName;
         getReadyForUser(loginResult);
         windowTitle = ##"Welcome to SCY-Lab";
         loginNode.disable = true;
         Timeline {
            repeatCount: 1
            keyFrames: [
               KeyFrame {
                  time: 750ms
                  values: windowColorScheme.mainColor => successColor tween Interpolator.LINEAR;
                  action: function() {
//                     getReadyForUser(loginResult);
                  }
               }
            ]
         }.play();
      }
      else {
         Timeline {
            repeatCount: 6
            autoReverse: true
            keyFrames: [
               KeyFrame {
                  time: 0ms
                  values: windowColorScheme.mainColor => loginColor tween Interpolator.LINEAR;
               }
               KeyFrame {
                  time: 500ms
                  values: windowColorScheme.mainColor => failedColor tween Interpolator.LINEAR;
               }
            ]
         }.play();
         loginNode.loginFailed();
      }
   }

   function getReadyForUser(loginResult: Object): Void {
      if (LoginType.LOCAL_MULTI_USER == initializer.loginTypeEnum){
         initializer.setupLogging(userName);
      }
      def backgroundGetReadyForUser = new BackgroundGetReadyForUser(initializer.toolBrokerLogin,loginResult,this);
      backgroundGetReadyForUser.start();
//      var toolBrokerAPI = initializer.toolBrokerLogin.getReadyForUser(loginResult);
//      logger.info(
//      "tbi.getLoginUserName() : {toolBrokerAPI.getLoginUserName()}\n""tbi.getMission() : {toolBrokerAPI.getMission()}\n""tbi.getRepository() : {toolBrokerAPI.getRepository()}\n""tbi.getMetaDataTypeManager() : {toolBrokerAPI.getMetaDataTypeManager()}\n""tbi.getExtensionManager() : {toolBrokerAPI.getExtensionManager()}\n""tbi.getELOFactory() : {toolBrokerAPI.getELOFactory()}\n""tbi.getActionLogger() : {toolBrokerAPI.getActionLogger()}\n""tbi.getAwarenessService() : {toolBrokerAPI.getAwarenessService()}\n""tbi.getDataSyncService() : {toolBrokerAPI.getDataSyncService()}\n""tbi.getPedagogicalPlanService() : {toolBrokerAPI.getPedagogicalPlanService()}\n""tbi.getStudentPedagogicalPlanService() : {toolBrokerAPI.getStudentPedagogicalPlanService()}");
//      findMission(toolBrokerAPI);
   }

   public override function tbiReady(toolBrokerAPI: ToolBrokerAPI, missions: Missions): Void{
      logger.info(
      "tbi.getLoginUserName() : {toolBrokerAPI.getLoginUserName()}\n""tbi.getMissionSpecificationURI() ) : {toolBrokerAPI.getMissionSpecificationURI()}\n""tbi.getRepository() : {toolBrokerAPI.getRepository()}\n""tbi.getMetaDataTypeManager() : {toolBrokerAPI.getMetaDataTypeManager()}\n""tbi.getExtensionManager() : {toolBrokerAPI.getExtensionManager()}\n""tbi.getELOFactory() : {toolBrokerAPI.getELOFactory()}\n""tbi.getActionLogger() : {toolBrokerAPI.getActionLogger()}\n""tbi.getAwarenessService() : {toolBrokerAPI.getAwarenessService()}\n""tbi.getDataSyncService() : {toolBrokerAPI.getDataSyncService()}\n""tbi.getPedagogicalPlanService() : {toolBrokerAPI.getPedagogicalPlanService()}\n""tbi.getStudentPedagogicalPlanService() : {toolBrokerAPI.getStudentPedagogicalPlanService()}");
      findMission(toolBrokerAPI,missions);
   }

   function findMission(toolBrokerAPI: ToolBrokerAPI, missions: Missions) {
      MissionLocator {
         tbi: toolBrokerAPI
         userName: userName
         initializer: initializer
         missions:missions
         window: loginWindow
         startMission: startMission
         cancelMission: cancelMission
      }.locateMission();

   }

   function cancelMission(): Void {
      FX.exit();
   }

   function startMission(missionRunConfigs: MissionRunConfigs): Void {
      logger.info("start mission with {missionRunConfigs}");
      loginWindow.scyContent = WelcomeNode {
            name: missionRunConfigs.tbi.getLoginUserName();
         }
      FX.deferAction(function(): Void {
         def userName = missionRunConfigs.tbi.getLoginUserName();
         loginWindow.scyContent = WelcomeNode {
               name: userName
            }
         var stage = scene.stage;
         var stageTitle = stage.title;
         stage.title = "{stageTitle} : {userName} in {missionRunConfigs.missionRuntimeModel.getMissionRuntimeElo().getTitle()}";
         FX.deferAction(function():Void{
               finishTbi(missionRunConfigs);
               var scyDesktop = placeScyDescktop(missionRunConfigs);
            });
      });
   }

   function finishTbi(missionRunConfigs: MissionRunConfigs): Void {
      InjectObjectsUtils.injectObjectIfWantedJava(missionRunConfigs.tbi,URI.class,"missionRuntimeURI",missionRunConfigs.missionRuntimeModel.getMissionRuntimeElo().getUriFirstVersion());
      InjectObjectsUtils.injectObjectIfWantedJava(missionRunConfigs.tbi,URI.class,"missionSpecificationURI",missionRunConfigs.missionRuntimeModel.getMissionRuntimeElo().getTypedContent().getMissionSpecificationEloUri());
   }

   function placeScyDescktop(missionRunConfigs: MissionRunConfigs): ScyDesktop {
      // either place the components "static" in the scene in initializer.getScene
      // or do it here "dynamic" (meaning after a succesfull login)
      //     insert ScyDesktop.scyDektopGroup into scene.content;
      //     insert ModalDialogBox.modalDialogGroup into scene.content;
      //     insert SimpleTooltipManager.tooltipGroup into scene.content;
      //     insert MouseBlocker.mouseBlockNode into scene.content;

      var scyDesktop = createScyDesktop(missionRunConfigs);

      // all components are already placed in the scene
      // so we only need to remove this login from the scene
      delete this from scene.content;

      return scyDesktop;
   }

}

function run() {
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
