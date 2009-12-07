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

/**
 * @author sikken
 */
// place your code here
public class LoginNode extends CustomNode {

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
                       text: ""
                       columns: textBoxColumns
                       selectOnFocus: true
                    }
                    Label {
                       layoutY: rowHeight;
                       text: "Password"
                    }
                    passwordField = PasswordBox {
                       layoutX: entryFieldOffset;
                       layoutY: rowHeight;
                       text: ""
                       columns: textBoxColumns
                       selectOnFocus: true
                    }
                    loginButton = Button {
                       layoutX: entryFieldOffset;
                       layoutY: 2 * rowHeight + spacing;
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

}

function run()    {
   var logingNode = LoginNode {
              layoutX: 10
              layoutY: 10
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
