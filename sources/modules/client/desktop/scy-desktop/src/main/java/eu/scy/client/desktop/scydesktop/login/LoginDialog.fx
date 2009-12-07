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
import eu.scy.client.desktop.scydesktop.ScyDesktop;
import eu.scy.client.desktop.scydesktop.scywindows.window.MouseBlocker;
import eu.scy.client.desktop.scydesktop.dummy.DummyLoginValidator;

/**
 * @author sikken
 */
// place your code here
public class LoginDialog extends CustomNode {


      def userNameName =   "username";



   def passwordName = "password";
   public var windowStyler: WindowStyler;
   public var createScyDesktop: function(sessionId:String): ScyDesktop;
   public var loginValidator: LoginValidator;
   var loginWindow: StandardScyWindow;
   var loginNode: LoginNode;
   var defaultUserName = "";
   var defaultPassword = "";

   init {
      FX.deferAction(function () {
         MouseBlocker.initMouseBlocker(scene.stage);
      });
      if (loginValidator==null){
         loginValidator = new DummyLoginValidator();
      }
   }

   public override function create(): Node {
      setDefaultLoginEntries();
      loginNode = LoginNode {
                 loginAction: loginAction
                 defaultUserName: defaultUserName
                 defaultPassword: defaultPassword
              }
      //TODO, why Math.abs????
      var loginWidth = loginNode.boundsInLocal.width + Math.abs(loginNode.boundsInLocal.minX);
      var loginHeight = loginNode.boundsInLocal.height + Math.abs(loginNode.boundsInLocal.minY);
      println("loginNode.boundsInLocal: {loginNode.boundsInLocal}");
      loginWindow = StandardScyWindow {
         title: "SCY-LAB login"
         color: Color.GREEN
         drawerColor: Color.color(0.3, 0.7, 0.3);
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
//      placeWindowCenter(loginWindow);
      windowStyler.style(loginWindow);

      return loginWindow;
   }

   function setDefaultLoginEntries() {
      // try application parameters
      defaultUserName = FX.getArgument(userNameName) as String;
      defaultPassword = FX.getArgument(passwordName) as String;
      if (defaultUserName == null) {
         // try web start parameters
         defaultUserName = getApplicationArgument(userNameName);
         defaultPassword = getApplicationArgument(passwordName);
      }
      if (defaultUserName == null) {
         // try system properties
         // TODO does not seem to work
         defaultUserName = FX.getProperty(userNameName) as String;
         defaultPassword = FX.getProperty(passwordName) as String;
      }
      return defaultUserName != null;
   }

   function getApplicationArgument(name: String): String {
      var args = FX.getArguments();
      var i = 0;
      while (i < sizeof args) {
         if (args[i].startsWith("-")) {
            var lcArg = args[i].toLowerCase();
            if (lcArg == "-{name}") {
               i++;
               if (i < sizeof args) {
                  return args[i];
               }
            }
         }
         i++;
      }
      return null;
   }

   function placeWindowCenter(): Void {
      loginWindow.layoutX = this.scene.stage.width / 2 - loginWindow.width / 2;
      loginWindow.layoutY = this.scene.stage.height / 2 - loginWindow.height / 2;
      println("placeWindowCenter: {scene.width}, {scene.stage.width}, {loginWindow.width}");
      println("placeWindowCenter: {scene.height}, {scene.stage.height}, {loginWindow.height}");
   }

   function loginAction(userName: String, password: String): Void {
      println("userName: {userName}, password: {password}");
      try{
         var sessionId = loginValidator.login(userName,password);
         placeScyDescktop(sessionId);
      }
      catch (e:LoginFailedException){
         loginNode.loginFailed();
      }
   }

   function placeScyDescktop(sessionId:String){
      // using the sceneContent, with a copy of scene.content, does work
      // directly adding scyDesktop to scene.content does not seem to work
      var sceneContent = scene.content;
      var scyDesktop = createScyDesktop(sessionId);
      delete this from sceneContent;
      insert scyDesktop into sceneContent;
      scene.content = sceneContent;
   }

}

function run()  {
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
