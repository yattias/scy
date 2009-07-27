package chatter;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.ext.swing.SwingComponent;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;
import java.awt.Dimension;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.TextBox;
import java.lang.System;
import javax.swing.AbstractListModel;

import eu.scy.awareness.IAwarenessService;
import eu.scy.awareness.IAwarenessUser;
import eu.scy.chat.controller.ChatController;
import eu.scy.toolbroker.ToolBrokerImpl;
import java.util.Vector;

public class FXChatter extends CustomNode {

    public var buddyNames:String[];
    public var chatController: ChatController;

   
    var textPane = new JTextPane();
    var scrollPane = new JScrollPane(textPane);
    var text: SwingComponent;
    var txt: TextBox;
    var names:AbstractListModel;
    
    var list: ListView = ListView {
        translateX: 0

        items: bind
        for(str in buddyNames) {
            str;
        }
        onMouseClicked: function (me: MouseEvent) {
        }

        onKeyPressed: function (ke: KeyEvent) {
        }
    }




    override function create() : Node {
        scrollPane.setPreferredSize(new Dimension(300, 250));
        text = SwingComponent.wrap(scrollPane);

        var tbi:ToolBrokerImpl = new ToolBrokerImpl();
        var awarenessService:IAwarenessService = tbi.getAwarenessService();
        awarenessService.init(tbi.getConnection("obama", "obama"));

        chatController = new ChatController(awarenessService);        
        chatController.populateBuddyList();
        
        var au:IAwarenessUser;
        var users:Vector = chatController.getBuddyListArray();
        for(str in users) {
            au = str as IAwarenessUser;
            insert au.getName() into buddyNames;
        }

        return HBox {
            spacing : 10;
            content: [
                list,

                VBox {
                    spacing : 3;
                    content: [
                        text,
                        txt = TextBox {
                            text: "";
                            columns: 40;
                        },
                        Button {
                            text: "Send";
                            action: function() {
                                var oldText:String = textPane.getText();

                                if( list.selectedIndex == -1 ) {
                                    textPane.setText(oldText.concat("me: ").concat(txt.text).concat("\n"));
                                    chatController.sendMessage(null, txt.text);
                                } else {
                                    chatController.sendMessage(users.elementAt(list.selectedIndex) as IAwarenessUser, txt.text);
                                    textPane.setText(oldText.concat("me: ").concat(txt.text).concat("\n"));
                                }
                            }
                        }
                    ]
                }
            ]
        };
    }

}
