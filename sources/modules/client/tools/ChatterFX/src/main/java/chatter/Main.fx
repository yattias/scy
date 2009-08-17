package chatter;

import java.lang.Object;
import javafx.ext.swing.SwingButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;

import eu.scy.chat.controller.ChatController;
import eu.scy.awareness.IAwarenessService;
import eu.scy.toolbroker.ToolBrokerImpl;
import eu.scy.awareness.event.IAwarenessPresenceListener;
import eu.scy.awareness.event.IAwarenessMessageListener;
import javafx.ext.swing.SwingTextField;
import javafx.scene.Group;
import eu.scy.awareness.IAwarenessUser;
import javax.swing.event.ListSelectionListener;
import javafx.stage.Alert;
import org.jivesoftware.smack.XMPPConnection;


var chatController: ChatController;
var awarenessService:IAwarenessService;
var buddyNames:ListInput;
var selectedRow:Integer = -1;
var recipient:String;

class Contact{
    var userName: String;
    var realName: String;
}


public var contacts: Contact[];
public var selection: Integer;


var messListener:IAwarenessMessageListener = IAwarenessMessageListener {
    override function handleAwarenessMessageEvent(event): Void {
        println("\n-------- IAwarenessMessageListener --------- {event}");
        var creader: ChatReader = ChatReader {
            chatHistoryBox: chatHistory;
            messageEvent: event;
        }
        creader.start();
    }
};

var presenceListener:IAwarenessPresenceListener = IAwarenessPresenceListener {
    override function handleAwarenessPresenceEvent(event): Void {
        println("\n-------- IAwarenessPresenceListener --------- {event}");
        chatController.populateBuddyList();
        contacts = [];
        var au:IAwarenessUser;
        var avail:String;
        
        for(str in chatController.getBuddyListArray()) {
            au = str as IAwarenessUser;
            if(au.getPresence() == "unavailable") {
                avail = "(x)";
            }
            else {
                avail = "(o)";
            }

            var cont:Contact = Contact {
                userName: "{avail} {au.getName()}";
                realName: au.getUsername();
            }
            
            insert cont into contacts;
        }
    }
};

var listListener: ListSelectionListener = ListSelectionListener{
    override function valueChanged(e) {
        selectedRow = (customTable.table).getSelectedRow();
        println("\n-------- selection --------- {selectedRow}");
    }
};

var inputText: SwingTextField = SwingTextField {
    columns: 40;
    text: "";
    editable: true;
};

var serverInput: SwingTextField = SwingTextField {
    columns: 15;
    text: "wiki.intermedia.uio.no";
    editable: true;
    disable: true;
};

var nameInput: SwingTextField = SwingTextField {
    columns: 15;
    text: "";
    editable: true;
};

var passInput: SwingTextField = SwingTextField {
    columns: 15;
    text: "";
    editable: true;
};

var connectButton: SwingButton = SwingButton {
    text: "Connect"
    action: function() {
        if(serverInput.text != "" and nameInput.text != "" and passInput.text != "") {
           var tbi:ToolBrokerImpl = new ToolBrokerImpl();
           awarenessService = tbi.getAwarenessService();
           var connection:XMPPConnection = tbi.getConnection(nameInput.text, passInput.text);
           if (connection != null) {
               startListeners(connection);
               customTable.disable = false;
               inputText.disable = false;
               chatHistory.disable = false;
               nameInput.disable = true;
               passInput.disable = true;
           }
           else {
               Alert.inform("The connection with the credentials provided failed");
           }
        }
        else {
            Alert.inform("One of the field server, name or password is wrong or missing");
        }

    }
};


var sendButton: SwingButton = SwingButton {
    text: "Send"
    action: function() {
        if(inputText.text != "") {
            if(selectedRow > -1) {
                recipient = (contacts[selectedRow] as Contact).realName;
                println("\n-------- recipient --------- {recipient}");
                send (inputText.text, recipient);
                chatHistory.addText("me -> {recipient}: {inputText.text}");
                inputText.text = "";
            }
            else {
                Alert.inform("Please select a recipient in the list of users before submitting.");
            }
        }
        else {
            Alert.inform("Please write some text before submitting.");
        }

    }
};

function startListeners(connection: XMPPConnection) :Void {
    awarenessService.init(connection);

    chatController = new ChatController(awarenessService);
    chatController.populateBuddyList();
    awarenessService.addAwarenessMessageListener(messListener);
    awarenessService.addAwarenessPresenceListener(presenceListener);

    var selectionModel = (customTable.table).getSelectionModel();
    selectionModel.addListSelectionListener(listListener);
}




var chatHistory: ChatHistoryBox = ChatHistoryBox {
};


var customTable: TableComponent;


function send (entry : String, recpt: String) : Void {
    var cwriter: ChatWriter = ChatWriter {
        txt: entry;
        ccontroller: chatController;
        recpt: recpt;
    }
    cwriter.start();

}

function run () {
    Stage {
        title: "Jer's ChatFX",
        width: 650
        height: 600
        scene: Scene {
            content: [
                VBox {
                    spacing: 10
                    content: [
                        HBox {
                            spacing: 5
                            content: [
                                serverInput,
                                nameInput,
                                passInput,
                                connectButton
                            ]
                        },
                        HBox {
                            spacing: 10
                            content: [
                                VBox {
                                    spacing: 10
                                    content: [
                                        Group{
                                            content: [
                                                customTable = TableComponent{
                                                    width: 100;
                                                    height: 500;

                                                    columns: [
                                                        TableComponent.TableColumn {
                                                            text: "Users"
                                                        }
                                                    ]

                                                    rows: bind for(p in contacts)
                                                        TableComponent.TableRow{
                                                            cells: [
                                                                TableComponent.TableCell {
                                                                    text:bind p.userName;
                                                                }
                                                            ]
                                                        }

                                                    selection: bind selection with inverse

                                                }
                                            ]
                                        }
                                    ]
                                },
                                VBox {
                                    spacing: 10
                                    content: [
                                        HBox {
                                            spacing: 10
                                            content: [
                                                inputText,
                                                sendButton
                                            ]
                                        }
                                        chatHistory

                                    ]
                                }
                           ]
                        }
                    ]
                }

            ]
        }
    }
    customTable.disable = true;
    inputText.disable = true;
    chatHistory.disable = true;
}
