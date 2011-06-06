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
import javafx.geometry.HPos;
import java.util.Locale;
import java.util.MissingResourceException;
import eu.scy.client.common.scyi18n.ResourceBundleWrapper;

/**
 * @author sikken
 */
// place your code here
public class LoginNode extends CustomNode {

   def logger = Logger.getLogger(this.getClass());
   public-init var defaultUserName = "name";
   public-init var defaultPassword = "pass";
   public-init var autoLogin = false;
   public-init var languages = ["nl", "en", "et", "fr", "el"];
   public var loginAction: function(userName: String, password: String): Void;
   public-read var language: String = bind languageSelector.language on replace { languageSelected() };
   public-read var loginTitle: String = "SCY-Lab login";
   def spacing = 5;
   def borderSize = 5;
   def entryFieldOffset = 120;
   def labelVOffset = 5;
   def rowHeight = 30;
   def textBoxColumns = 23;
   var userNameLabel: Label;
   var userNameField: TextBox;
   var passwordLabel: Label;
   var passwordField: PasswordBox;
   var loginButton: Button;
   var quitButton: Button;
   var currentLocale = Locale.getDefault();
   def languageSelector = LanguageSelector {
              languages: languages
              language: currentLocale.getLanguage()
           };
   def loginDisabled = bind (userNameField.text.length() == 0 or passwordField.password.length() == 0 or languageSelector.language.length() == 0);
   def loginButtonText = "               ";
   def quitButtonText = "               ";

   postinit {
      if (autoLogin) {
         Timeline {
            repeatCount: 1
            keyFrames: [
               KeyFrame {
                  time: 1000ms
                  action: doAutoLogin
               }
            ]
         }.play();
      }
   }

   function doAutoLogin() {
      if (not loginButton.disabled) {
         loginButton.action();
      } else {
         println("autoLogin, but login is not enabled");
      }
   }

   function languageSelected(): Void {
      logger.info("languageSelected: {language}");
      if (language == null or language == "") {
         return;
      }
      var newLocale = new Locale(language);
      if (newLocale != null) {
         Locale.setDefault(newLocale);
         setLanguageLabels();
      } else {
         logger.error("could not find locale for language {language}");
      }
   }

   function setLanguageLabels() {
      try {
         var bundle = new ResourceBundleWrapper(this);
         loginTitle = bundle.getString("LoginDialog.title");
         userNameLabel.text = bundle.getString("LoginDialog.username");
         passwordLabel.text = bundle.getString("LoginDialog.password");
         loginButton.text = bundle.getString("LoginDialog.login");
         quitButton.text = bundle.getString("LoginDialog.quit");
      } catch (e: MissingResourceException) {
         logger.info("failed to find resource bundle, {e.getMessage()}");
      }
   }

   public override function create(): Node {

      var loginGroup = Group {
                 content: [
                    userNameLabel = Label {
                               layoutY: labelVOffset
                               text: "User name             "
                            }
                    userNameField = TextBox {
                               layoutX: entryFieldOffset;
                               text: defaultUserName
                               columns: textBoxColumns
                               selectOnFocus: true
                               action: function() {
                                  passwordField.requestFocus();
                               }
                            }
                    passwordLabel = Label {
                               layoutY: rowHeight + labelVOffset;
                               text: "Password             "
                            }
                    passwordField = PasswordBox {
                               layoutX: entryFieldOffset;
                               layoutY: rowHeight;
                               text: defaultPassword
                               columns: textBoxColumns
                               selectOnFocus: true
                               action: function() {
                                  if (userNameField.text.length() == 0) {
                                     userNameField.requestFocus();
                                  } else if (passwordField.password.length() > 0) {
                                     loginButton.action();
                                  }
                               }
                            }
                    loginButton = Button {
                               layoutX: entryFieldOffset;
                               layoutY: 2 * rowHeight + spacing;
                               strong: true
                               text: loginButtonText
                               disable: bind loginDisabled
                               action: function() {
                                  loginAction(userNameField.text, passwordField.password);
                               }
                            }
                    quitButton = Button {
                               //layoutX: entryFieldOffset;
                               layoutX: bind passwordField.boundsInParent.maxX - quitButton.layoutBounds.width;
                               layoutY: 2 * rowHeight + spacing;
                               text: quitButtonText
                               action: function() {
                                  FX.exit();
                               }
                            }
                 ]
              };
      languageSelected();
      var vbox = VBox {
                 spacing: spacing;
                 hpos: HPos.RIGHT
                 nodeHPos: HPos.RIGHT
                 content: [
                    languageSelector,
                    loginGroup,
                 ]
              }
      vbox.layout();
      FX.deferAction(function(): Void {
         userNameField.requestFocus();
      });
      return vbox;
   }

   public function loginFailed(): Void {
      def finalUsernameLabel = passwordLabel;
      def finalPasswordLabel = userNameLabel;
      def finalUsernameField = userNameField;
      def finalPasswordField = passwordField;
      def labelColor = finalUsernameLabel.textFill;
      def quaterTime = 25ms;
      def maxShift = 5;
      def nrOfCycles = 5;
      // move the entry field to left and right and back
      Timeline {
         repeatCount: nrOfCycles
         keyFrames: [
            at (0s) {
               finalUsernameField.translateX => 0 tween Interpolator.EASEBOTH;
               finalPasswordField.translateX => 0 tween Interpolator.EASEBOTH
            }
            KeyFrame {
               time: quaterTime
               values: [
                  finalUsernameField.translateX => maxShift tween Interpolator.EASEBOTH,
                  finalPasswordField.translateX => maxShift tween Interpolator.EASEBOTH,
               ]
            }
            KeyFrame {
               time: 3 * quaterTime
               values: [
                  finalUsernameField.translateX => -maxShift tween Interpolator.EASEBOTH,
                  finalPasswordField.translateX => -maxShift tween Interpolator.EASEBOTH
               ]
            }
            KeyFrame {
               time: 4 * quaterTime
               values: [
                  finalUsernameField.translateX => 0 tween Interpolator.EASEBOTH,
                  finalPasswordField.translateX => 0 tween Interpolator.EASEBOTH,
               ]
            }
         ]
      }.play();
      def colorChangeDuration = 500ms;
      def colorShowDuration = 5000ms;
      // change the color of the labels and back
      Timeline {
         repeatCount: 1
         keyFrames: [
            KeyFrame {
               time: colorChangeDuration
               values: [
                  finalUsernameLabel.textFill => Color.RED tween Interpolator.LINEAR,
                  finalPasswordLabel.textFill => Color.RED tween Interpolator.LINEAR
               ]
            }
            KeyFrame {
               time: colorChangeDuration + colorShowDuration
               values: [
                  finalUsernameLabel.textFill => Color.RED tween Interpolator.LINEAR,
                  finalPasswordLabel.textFill => Color.RED tween Interpolator.LINEAR
               ]
            }
            KeyFrame {
               time: colorChangeDuration + colorShowDuration + colorChangeDuration
               values: [
                  finalUsernameLabel.textFill => labelColor tween Interpolator.LINEAR,
                  finalPasswordLabel.textFill => labelColor tween Interpolator.LINEAR
               ]
            }
         ]
      }.play();

//      passwordLabel.textFill = Color.RED;
      passwordField.requestFocus();

   }

}

function run() {
   var logingNode = LoginNode {
              layoutX: 0
              layoutY: 0
              defaultUserName: "123"
              defaultPassword: "321"
              loginAction: function(userName: String, password: String): Void {
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
