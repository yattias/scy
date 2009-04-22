/*
 * MessageWindow.fx
 *
 * Created on 23.03.2009, 13:54:54
 */

package eu.scy.elobrowser.awareness.contact;

import java.lang.Object;
import javafx.animation.SimpleInterpolator;
import javafx.animation.Timeline;
import javafx.ext.swing.SwingButton;
import javafx.ext.swing.SwingComponent;
import javafx.ext.swing.SwingTextField;
import javafx.scene.Cursor;
import javafx.scene.CustomNode;
import javafx.scene.effect.DropShadow;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.Scene;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import eu.scy.elobrowser.awareness.contact.MessageWindow;

/**
 * @author Sven
 */

public class MessageWindow extends CustomNode, ChatReceiver {


    public var width: Number = 350;
    def initialOpacity: Number = 0.7;
    public var height: Number = 215;
    public var x: Number = 0 ;
    public var y: Number = 0 ;
    public var receiver: String;
    public var sender: String;
    public var masked: Boolean = false;
    var messages: String;
    var tempX: Number;
    var tempY: Number;
    var transparentDragging: Boolean = true;
//    public function closeWindow():Void{};
    public var con: ChatConnector;



//   public def gradient: LinearGradient = LinearGradient{
//        proportional: true;
//        stops: [
//            Stop{
//                color: Color.LIGHTSTEELBLUE;
//                offset: 0.0;
//            },
//            Stop{
//                color: Color.LIGHTBLUE;
//                offset: 1.0;
//            }]
//
//    };

    def frame: Rectangle = Rectangle{
//        stroke: Color.BLACK;
//        fill: gradient;
        stroke: Color.TRANSPARENT;
        fill: Color.TRANSPARENT;
        arcHeight: 20;
        arcWidth: 20;
        opacity: 0.7;
//        effect: windowEffectQueue;
//        stroke: Color.BLACK;
        //        fill: Color.GREEN;
        //        arcHeight: 20;
        //        arcWidth: 20;
        opacity: initialOpacity;
        effect: windowEffectQueue;
        width: bind this.width;
        height: bind this.height;
        x: bind this.x;
        y: bind this.y;

//        onMousePressed: function(evt:MouseEvent) {
//            tempX = frame.x;
//            tempY = frame.y;
//            cursor=Cursor.MOVE;
//
//        };
//
//        onMouseDragged: function(evt:MouseEvent){
//            if (transparentDragging) then {
//                this.opacity=0.4;
//            }
//            this.x = tempX + evt.dragX;
//            this.y = tempY + evt.dragY;
//            textarea.translateX = textField.translateX;
//            textarea.translateY =  20;
//        };
//
//        onMouseReleased: function(evt:MouseEvent):Void{
//            if (transparentDragging) then {
//                this.opacity=1.0;
//                this.frame.opacity=initialOpacity;
//            }
//            else {
//            };
//            cursor=Cursor.DEFAULT;
//        };
    };

    init{
//        effect = windowEffectQueue;
        //        mask();
        blocksMouse = true;
        swingTextArea.setSize(300, 100);
        //        swingTextArea.setContentType("text");
        swingTextArea.setEditable(false);
        swingTextArea.setAutoscrolls(true);
        swingTextArea.setVisible(true);
        textarea.translateX = textField.translateX;
        textarea.translateY = 20;
//        this.effect=opacity;
    };

    postinit {

    };

//    def header: Text = Text{
//        x: bind(this.x + (frame.width - 300) / 2);
//        y: bind (this.y + 20);
//        content: "Chat with {receiver}";
//    }

    public var textField: SwingTextField =  SwingTextField {
        width: 300;
        translateX: bind (this.x + (frame.width - 300) / 2);
        translateY: bind (textarea.translateY + textarea.height + 10);
        columns: 100;
        editable: true;
        action: function():Void{
            sendMessage();
        }
    };

    def sendButton = SwingButton {
        translateX: bind textField.translateX;
        translateY: bind textField.translateY + 26;
//        blocksMouse: true;
        text: "Send!";
        action: function() {
            sendMessage();
        }
    };
    
//    def closeButton: SwingButton = SwingButton {
//        translateX: bind sendButton.translateX + sendButton.width + 20;
//        translateY: bind sendButton.translateY;
//        blocksMouse: true;
//        text: "Close!";
//        action: function() {
//            closeWindow();
//        }
//    };


//    public function closeWindow():Void{
//        Timeline{
//            keyFrames: [at (0.2s){this.opacity => 0.0 tween SimpleInterpolator.LINEAR}];
//
//        }.play();
//            //            this.visible = false;
//        blocksMouse=false;
//        delete this from content;
//        this.unmask();


//    };

    public var swingTextArea: JTextArea  = new JTextArea();
    public var scrollPane: JScrollPane = new JScrollPane(swingTextArea);

    def windowEffectQueue = DropShadow{color: Color.BLACK,
    };


    public var textarea = SwingComponent.wrap(scrollPane);

    function sendMessage(): Void {
        print("--------------- MessageWindow -> sendMessage :");
        if (textField.text != "") {
            println(" {textField.text}");
            if(con == null) {
                println("ALARM!!!!!!!!!!!!!!!!!!!");
            }
            con.sendMessage(receiver, textField.text);
            addChatRow(sender, textField.text);
            textField.text="";
        }
    }

    override public function receiveMessage(user:String, text:String ) : Void {
        FX.deferAction(function() :Void {
            addChatRow(user, text);
        });
    }

    public function addChatRow(chatter:String, message: String):Void{
        messages = swingTextArea.getText();
        if (messages == ""){
            swingTextArea.setText("{chatter}: {message}");
        } else {
            swingTextArea.setText("{messages}\n{chatter}: {message}");
        }
    }

    def maskLayer: Rectangle =
    Rectangle{
        fill: Color.WHITE;
        blocksMouse: true;
        opacity: 0.5;
        width: boundsInScene.width;
        height: boundsInScene.height;
        stroke: Color.TRANSPARENT;
    }


    public function mask():Void{
        if (not masked){
            insert maskLayer before content[0];
            masked = true;
        }
    }


    public function unmask():Void{
        if (masked){
            delete maskLayer from content;
            masked = false;
        }
    };

//    var content: Node[] = [frame,textField,textarea,sendButton, closeButton, header];
    var content: Node[] = [frame,textField,textarea,sendButton];

    
//    public function createNode():
//    Node{
//        textarea.visible = true;
//        swingTextArea.setLineWrap(true);
//        scrollPane.setAutoscrolls(true);
//        textarea.height=300;
//        textarea.width=300;
//        return Group{
//            content: bind content;
//
//        }
//    }
    override public function create():
    Node{
        textarea.visible = true;
        swingTextArea.setLineWrap(true);
        scrollPane.setAutoscrolls(true);
        textarea.height=100;
        textarea.width=300;
        return Group{
            content: bind content;

        }
    }


}

    //Testing
//function run(__ARGS__ : String[]){
//
//    Stage {
//        title: "Contact"
//        width: 620
//        height: 520
//        visible: true;
//        scene: Scene {
//            content: [
//                MessageWindow{
//                    sender: "Sven Manske";
//                    receiver: "Adam G"
//            }];
//        }
//    }
//}
