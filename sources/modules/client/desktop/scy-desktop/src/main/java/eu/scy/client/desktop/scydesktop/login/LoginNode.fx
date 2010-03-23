/*
 * LoginNode.fx
 *
 * Created on 4-dec-2009, 14:47:04
 */
package eu.scy.client.desktop.scydesktop.login;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.TextBox;
import javafx.scene.control.Button;

import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.animation.Interpolator;
import eu.scy.client.desktop.scydesktop.uicontrols.LanguageSelector;
import org.apache.log4j.Logger;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.geometry.HPos;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.MissingResourceException;

/**
 * @author sikken
 */
// place your code here
public class LoginNode extends CustomNode {
   def logger = Logger.getLogger(this.getClass());

   public-init var defaultUserName = "name";
   public-init var defaultPassword = "pass";
   public-init var autoLogin = false;
   public-init var languages = ["nl","en","et","fr","el"];
   public var loginAction  : function(userName:String,password:String): Void;
   public-read var language:String = bind languageSelector.language on replace {languageSelected()};
   public-read var loginTitle:String = "SCY-Lab login";

   def spacing = 5;
   def borderSize = 5;
   def entryFieldOffset = 120;
   def labelVOffset = 5;
   def rowHeight = 30;
   def textBoxColumns = 20;
   var userNameLabel:Label;
   var userNameField: TextBox;
   var passwordLabel:Label;
   var passwordField: PasswordBox;
   var loginButton: Button;
   var quitButton: Button;
   var currentLocale = Locale.getDefault();
   def languageSelector = LanguageSelector{
         languages: languages
         language: currentLocale.getLanguage()
      };
   def loginEnabled = bind (userNameField.text.length() == 0 or passwordField.password.length() == 0);
   def loginButtonText = "               ";
   def quitButtonText  = "               ";

   postinit{
      if (autoLogin){
         Timeline {
            repeatCount: 1
            keyFrames : [
               KeyFrame {
                  time : 1000ms
                  action:doAutoLogin
               }
            ]
         }.play();
      }
   }

   function doAutoLogin(){
      if (not loginButton.disabled){
         loginButton.action();
      }
      else{
         println("autoLogin, but login is not enabled");
      }
   }

   function languageSelected():Void{
      logger.info("languageSelected: {language}");
      if (language==null or language==""){
         return;
      }
      var newLocale = new Locale(language);
      if (newLocale!=null){
         Locale.setDefault(newLocale);
         setLanguageLabels();
      }
      else{
         logger.error("could not find locale for language {language}");
      }
    }

    function setLanguageLabels(){
       try{
          var bundle = ResourceBundle.getBundle("languages/scydesktop");
          loginTitle = bundle.getString("LoginDialog.title");
          userNameLabel.text = bundle.getString("LoginDialog.username");
          passwordLabel.text = bundle.getString("LoginDialog.password");
          loginButton.text = bundle.getString("LoginDialog.login");
          quitButton.text = bundle.getString("LoginDialog.quit");
       }
       catch (e:MissingResourceException){
          logger.info("failed to find resource bundle, {e.getMessage()}");
       }
    }

   public override function create(): Node {

      var loginGroup = Group {
                 layoutX: 3*borderSize
                 layoutY: 3*borderSize
                 content: [
                    userNameLabel = Label {
                       layoutY:labelVOffset
                       text: "User name             "
                    }
                    userNameField = TextBox {
                       layoutX: entryFieldOffset;
                       text: defaultUserName
                       columns: textBoxColumns
                       selectOnFocus: true
                       action:function(){
                          passwordField.requestFocus();
                       }
                    }
                    passwordLabel = Label {
                       layoutY: rowHeight+labelVOffset;
                       text: "Password             "
                    }
                    passwordField = PasswordBox {
                       layoutX: entryFieldOffset;
                       layoutY: rowHeight;
                       text: defaultPassword
                       columns: textBoxColumns
                       selectOnFocus: true
                       action:function(){
                          if (userNameField.text.length()==0){
                           userNameField.requestFocus();
                          }
                          else if (passwordField.password.length()>0){
                             loginButton.action();
                          }
                       }
                    }
                    loginButton = Button {
                       layoutX: entryFieldOffset;
                       layoutY: 2 * rowHeight + spacing;
                       strong:true
                       text: loginButtonText
                       disable: bind loginEnabled
                       action: function () {
                          loginAction(userNameField.text,passwordField.password);
                       }
                    }
                    quitButton = Button {
                       layoutX: entryFieldOffset;
                       layoutY: 2 * rowHeight + spacing;
                       text: quitButtonText
                       action: function () {
                          FX.exit();
                       }
                    }
                 ]
              };
      languageSelected();
      quitButton.layoutX = passwordField.boundsInParent.maxX - quitButton.boundsInLocal.width;
      var vbox = VBox{
         layoutX:spacing+borderSize;
         layoutY:spacing+borderSize;
         spacing:spacing;
         hpos:HPos.RIGHT
         nodeHPos:HPos.RIGHT
         content:[
            languageSelector,
            loginGroup,
            Rectangle {
               x: 0, y: 0
               width: 1, height: 1
               fill: Color.TRANSPARENT
            }
         ]
      }
      vbox.layout();
      Group{
         content:vbox
      }
   }


   public function loginFailed():Void{
      def shiftField = passwordField;
      def quaterTime = 25ms;
      def maxShift = 5;
      Timeline {
         repeatCount: 5
         keyFrames : [
            at(0s){
               shiftField.translateX => 0 tween Interpolator.EASEBOTH
            }
            KeyFrame {
               time : quaterTime
               values: shiftField.translateX => maxShift tween Interpolator.EASEBOTH
            }
            KeyFrame {
               time : 3*quaterTime
               values: shiftField.translateX => -maxShift tween Interpolator.EASEBOTH
            }
            KeyFrame {
               time : 4*quaterTime
               values: shiftField.translateX => 0 tween Interpolator.EASEBOTH
            }
         ]
      }.play();
      passwordLabel.textFill = Color.RED;
      passwordField.requestFocus();

   }


}

function run()    {
   var logingNode = LoginNode {
              layoutX: 0
              layoutY: 0
              defaultUserName:"123"
              defaultPassword:"321"
              loginAction:function(userName:String, password:String):Void{
                 println("user name: {userName}, password: {password}");
                 }
              }

      Stage {
         title: "Login node test"
         scene: Scene {
            width: 300
            height: 200
            content: [
               logingNode
            ]
         }
      }

   }
