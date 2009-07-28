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
import eu.scy.awareness.event.IAwarePresenceEvent;
import eu.scy.awareness.event.IAwarenessEvent;
import eu.scy.awareness.event.IAwarenessRosterEvent;
import eu.scy.awareness.event.IAwarenessRosterListener;
import eu.scy.awareness.event.IAwarenessMessageListener;
import eu.scy.awareness.event.IAwarenessPresenceListener;
import eu.scy.chat.controller.ChatController;
import eu.scy.toolbroker.ToolBrokerImpl;
import java.util.Vector;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.scene.text.Text;
import javafx.scene.control.ScrollBar;
import javafx.scene.layout.LayoutInfo;

public class FXChatter extends CustomNode {

    var output:String = "";
    var time:String="";
    var formatter:java.text.SimpleDateFormat=new java.text.SimpleDateFormat("HH:mm:ss S");
    var buddyNames:String[];
    var chatController: ChatController;
    var oldText:String;
    var awarenessService:IAwarenessService;
    var textPane = new JTextPane();
    var scrollPane = new JScrollPane(textPane);
    var text: SwingComponent;
    var txt: TextBox;
    var names:AbstractListModel;
    var users:Vector;
    var txtArea:Text;

    var messageListener = IAwarenessMessageListener {
        override function handleAwarenessMessageEvent(event) : Void {
            println("\n-------- IAwarenessMessageListener --------- {event}");
            //oldText = textPane.getText();
            oldText = output;
            updateTextArea("{event.getUser()}: {event.getMessage()}\n{oldText}");
        }
    }

    /*var presenceListener = IAwarenessPresenceListener {
        override function handleAwarenessPresenceEvent(event): Void {
            println("\n-------- IAwarenessPresenceListener --------- {event}");
        }
    }*/


    
    var list: ListView = ListView {
        items: bind
        for(str in buddyNames) {
        str;
        }
    }

    var vScroll = ScrollBar {
        min: 0
        max: txtArea.boundsInParent.height;
        value: 0
        vertical: true
        translateX: 10
        layoutInfo: LayoutInfo
        {
            height: 200;
        }
    }


    var tl:Timeline = Timeline {
        repeatCount:Timeline.INDEFINITE;
        keyFrames:KeyFrame {
            time:0.09s
            action:function():Void {
                time = "{formatter.format(new java.util.Date())}";}
        };
    }

    public function doIt(): Void {
        tl.play();
        //textPane.setText("Please select a buddy before chatting ...\n");
        output = "Please select a buddy before chatting ...\n";
        var tbi:ToolBrokerImpl = new ToolBrokerImpl();
        awarenessService = tbi.getAwarenessService();
        awarenessService.init(tbi.getConnection("obama", "obama"));
        awarenessService.addAwarenessMessageListener(messageListener);
        //awarenessService.addAwarenessPresenceListener(presenceListener);

        chatController = new ChatController(awarenessService);
        chatController.populateBuddyList();

        var au:IAwarenessUser;
        users = chatController.getBuddyListArray();
        for(str in users) {
            au = str as IAwarenessUser;
            insert "({au.getPresence()}) {au.getName()}" into buddyNames;
        }
    }



    override function create() : Node {
        scrollPane.setPreferredSize(new Dimension(300, 250));
        text = SwingComponent.wrap(scrollPane);

        return HBox {
            spacing : 10;
            content: [
                list,

                VBox {
                    spacing : 3;
                    content: [                     
                        TextBox {
                            text: bind time;
                            translateX: 100;
                        },
                        HBox {
                            spacing : 3;
                            content: [
                                txt = TextBox {
                                    text: "";
                                    columns: 40;
                                },
                                Button {
                                    text: "Send";
                                    action: function() {
                                        //oldText = textPane.getText();
                                        oldText = output;

                                        if( list.selectedIndex == -1 ) {
                                            updateTextArea("me: {txt.text}\n{oldText}");
                                            chatController.sendMessage(null, txt.text);
                                        } else {
                                            chatController.sendMessage(users.elementAt(list.selectedIndex) as IAwarenessUser, txt.text);
                                            updateTextArea("me: {txt.text}\n{oldText}");
                                        }
                                    }
                                }
                            ]
                        },
                        //text,
                        HBox {
                            content: [
                                txtArea = Text {
                                    wrappingWidth: 300;
                                    content: bind output;
                                }
                            ]
                        }
                    ]
                }
            ]
        };
    }

    function updateTextArea(s:String): Void {
        output = s;
    }
}
