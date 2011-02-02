package eu.scy.elobrowser.tool.chat;

import eu.scy.elobrowser.main.Roolo;
import eu.scy.elobrowser.tool.chat.ChatNode;
import eu.scy.elobrowser.tool.chat.ChatPanel;
import eu.scy.elobrowser.ui.CommandText;
import eu.scy.scywindows.ScyWindow;
import java.lang.Object;
import java.net.URI;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.ext.swing.SwingComponent;
import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.stage.Stage;

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
    var chatnode : ChatNode;
    Stage {
        title: "Chat"
        width: 250
        height: 250
        scene: Scene {
            content:chatnode = ChatNode{
                
            }

        }
    }
    Timeline {
        keyFrames: [
            KeyFrame {
                time: 30s
                action: function() {
                chatnode.chatpanel.DELETE_ME_QUICKLY();
            }

            }
        ]
}.play();

}
