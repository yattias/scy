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
import eu.scy.awareness.controller.ChatController;
import java.lang.System;

public class FXChatter extends CustomNode {

    public var buddyNames:String[];
    var chatController: ChatController;

   
    var textPane = new JTextPane();
    var scrollPane = new JScrollPane(textPane);
    var text: SwingComponent;
    var txt: TextBox;

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
        //textPane.setText("Latin (lingua Latīna, pronounced [laˈtiːna]) is an Italic language historically spoken in Latium and Ancient Rome. Through the Roman conquest, Latin spread throughout the Mediterranean and a large part of Europe. Romance languages such as Italian, French, Catalan, Romanian, Spanish, and Portuguese are descended from Latin, while many others, especially European languages, including English, have inherited and acquired much of their vocabulary from Latin. It was the international language of science and scholarship in central and western Europe until the 17th century, then it was gradually replaced by vernacular languages, especially French, which became the new lingua franca of Europe. There are two main varieties of Latin: Classical Latin, the literary dialect used in poetry and prose, and Vulgar Latin, the form of the language spoken by ordinary people. Vulgar Latin was preserved as a spoken language in much of Europe after the decline of the Roman Empire, and by the 9th century diverged into the various Romance languages.");
        text = SwingComponent.wrap(scrollPane);

        chatController = new ChatController(null, null);
        //chatController.populateBuddyList();

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
                                var oldText = textPane.getText();

                                if( list.selectedItem == null ) {
                                    textPane.setText(oldText.concat("me:").concat(txt.text).concat("\n"));
                                    chatController.sendMessage(null, txt.text);
                                } else {
                                    System.out.println(list.selectedItem.toString());
                                    chatController.sendMessage(list.selectedItem, txt.text);
                                    textPane.setText(oldText.concat("me:").concat(txt.text).concat("\n"));
                                } 
                            }
                        }
                    ]
                }
            ]
        };
    }
}
