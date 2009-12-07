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
import java.lang.System;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.animation.Interpolator;

/**
 * @author sikken
 */
// place your code here
public class LoginNode extends CustomNode {

   public-init
      var defaultUserName =   "name";

                    public-init var defaultPassword = "pass";

   public
      var loginAction   :


     function(userName:String,password:String): Void;
   def spacing = 5;
   def borderSize = 5;
   def entryFieldOffset = 100;
   def rowHeight = 30;
   def textBoxColumns = 15;
   var userNameField: TextBox;
   var passwordField: PasswordBox;
   var loginButton: Button;
   var quitButton: Button;

   public override function create(): Node {
      var loginGroup = Group {
                 layoutX: borderSize
                 layoutY: borderSize
                 content: [
                    Label {
                       text: "User name"
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
                    Label {
                       layoutY: rowHeight;
                       text: "Password"
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
                       text: "Login"
                       disable: bind (userNameField.text.length() == 0 or passwordField.password.length() == 0)
                       action: function () {
                          loginAction(userNameField.text,passwordField.password);
                       }
                    }
                    quitButton = Button {
                       layoutX: entryFieldOffset;
                       layoutY: 2 * rowHeight + spacing;
                       text: "Quit"
                       action: function () {
                          System.exit(0);
                       }
                    }
                 ]
              };
      quitButton.layoutX = passwordField.boundsInParent.maxX - quitButton.boundsInLocal.width;
      return Group {
                 content: [
                    Rectangle {
                       x: 0, y: 0
                       width: loginGroup.boundsInLocal.width + 2 * borderSize
                       height: loginGroup.boundsInLocal.height + 2 * borderSize
                       fill: Color.WHITE
                    }
                    loginGroup
                 ]
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
passwordField.requestFocus();

   }


}

function run()    {
   var logingNode = LoginNode {
              layoutX: 10
              layoutY: 10
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
