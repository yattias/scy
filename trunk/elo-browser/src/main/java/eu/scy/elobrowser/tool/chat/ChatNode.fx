package eu.scy.elobrowser.tool.chat;

import eu.scy.elobrowser.main.Roolo;
import eu.scy.scywindows.ScyWindow;
import javafx.ext.swing.SwingComponent;
import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.net.URI;

/**
 * @author weinbrenner
 */

public class ChatNode extends CustomNode {

    var chatpanel:ChatPanel;

	public var scyWindow:ScyWindow on replace {
		setScyWindowTitle()
    };

     public function loadElo(uri:URI){
        //eloTextpadActionWrapper.loadElo(uri);
        setScyWindowTitle();
     }

	function setScyWindowTitle(){
		if (scyWindow == null)
		return;
		scyWindow.title = "Chat";
		//var eloUri = eloTextpadActionWrapper.getEloUri();
		//if (eloUri != null)
        //scyWindow.id = eloUri.toString()
		//else
        //scyWindow.id = "";

    }

    public override function create(): Node {
        var g = Group {
            blocksMouse:true;
            content: [
                VBox{
                    translateY:5;
                    spacing:5;
                    content:[
                        HBox{
                            translateX:5;
                            spacing:5;
                            content:[
                                CommandText{
                                    label:"New"
                                    clickAction:function( e: MouseEvent ):Void {
                                        //eloTextpadActionWrapper.newTextAction();
										setScyWindowTitle();
                                    }
                                }
                                CommandText{
                                    label:"Load"
                                    clickAction:function( e: MouseEvent ):Void {
                                        //eloTextpadActionWrapper.loadTextAction();
										setScyWindowTitle();
                                    }
                                }
                                CommandText{
                                    label:"Save"
                                    clickAction:function( e: MouseEvent ):Void {
                                        //eloTextpadActionWrapper.saveTextpadAction();
                                        setScyWindowTitle();
                                    }
                                }
                                CommandText{
                                    label:"Save as"
                                    clickAction:function( e: MouseEvent ):Void {
                                        //eloTextpadActionWrapper.saveAsTextpadAction();
										setScyWindowTitle();
                                    }
                                }
                            ]
                        },
                        SwingComponent.wrap(chatpanel)
                    ]
                }
            ]
        };
    }
}

    class CommandText extends CustomNode {
        public var label="label";
        public var clickAction:function(e: MouseEvent):Void;
        var color = Color.color(0.34,0.34,0.34);
        var hoverColor = Color.BLACK;
        var textFont =  Font {
            size: 11}
        var text:Text;

        public override function create(): Node {
            return Group {
                content: [
                    Rectangle {
                        x: 0,
                        y: 0
                        width: 55,
                        height: 17
                        arcHeight:5
                        arcWidth:5
                        fill: Color.color(0.9,0.9,0.9)
                    }
                    text = Text{
                        translateX:8;
                        translateY:12;
                        font:textFont
                        content: bind label
                        fill:color
                    }
                ]
                onMouseEntered: function( e: MouseEvent ):Void {
                    text.fill = hoverColor;
                }
                onMouseExited: function( e: MouseEvent ):Void {
                    text.fill = color;
                }
                onMouseClicked: function( e: MouseEvent ):Void {
                    if (clickAction != null) clickAction(e);
                }
        };
        }
    }

    public function createChatNode(roolo:Roolo):ChatNode{
        var chatpanel = new ChatPanel();
        return ChatNode{
            chatpanel : chatpanel
        }
    }

    public function createChatWindow(roolo:Roolo):ScyWindow {
    return createChatWindow(ChatNode.createChatNode(roolo));
    }

    public function createChatWindow(chatNode:ChatNode):ScyWindow{
        var chatWindow = ScyWindow{
            color:Color.RED;
            title:"Chat"
            scyContent: chatNode;
            minimumWidth:320;
            minimumHeight:100;
            cache:true;
            allowResize:false
      }
        chatNode.scyWindow = chatWindow;
        chatWindow.openWindow(320, 250);
    return chatWindow;
    }

function run(){
    Stage {
        title: "Chat"
        width: 250
        height: 250
        scene: Scene {
            content:ChatNode{
            }

        }
    }

}
