/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.tools.fxchattool.registration;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.layout.Resizable;
import java.lang.String;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.control.ScrollView;
import javafx.scene.control.ScrollBarPolicy;
import javafx.scene.control.TextBox;
import javafx.scene.paint.Color;
import eu.scy.chat.controller.ChatController;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import eu.scy.client.desktop.scydesktop.tools.ScyToolFX;
import javafx.scene.layout.LayoutInfo;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Container;
import javafx.geometry.Insets;
import javafx.geometry.HPos;
import eu.scy.chat.controller.IChat;
import eu.scy.client.desktop.scydesktop.tools.DrawerUIIndicator;

/**
 * @author pg
 */
public class ChatterNode extends CustomNode, Resizable, ScyToolFX, IChat {

    public-init var scyWindow: ScyWindow;
    public var chatController: ChatController on replace {
        if (chatController != null) {
            messageBox.disable = false;
            sendButton.disable = false;
        }
    };
    var sv: ScrollView;
    var messageBox: TextBox;
    var content: VBox;
    var sendButton: Button;
    var chatLines: VBox = VBox {
                spacing: 3
            }
    var lastTime: String;

    public override function create(): Node {
        sv = ScrollView {
                    node: bind chatLines;
                    style: "-fx-background-color: white; -fx-border-color: grey;"
                    vbarPolicy: ScrollBarPolicy.ALWAYS;
                    hbarPolicy: ScrollBarPolicy.NEVER;
                    layoutInfo: LayoutInfo {
                        hfill: true
                        vfill: true
                        hgrow: Priority.ALWAYS
                        vgrow: Priority.ALWAYS
                    }
                }
        messageBox = TextBox {
                    selectOnFocus: true;
                    action: function(): Void {
                        if (messageBox.text.trim().length() != 0) {
                            sendMessage();
                        }
                    }
                    disable: true
                    layoutInfo: LayoutInfo {
                        hfill: true
                        hgrow: Priority.ALWAYS
                    }
                }
        sendButton = Button {
            text: "##Send"
            disable: true
            action: function(): Void {
                if (messageBox.text.trim().length() != 0) {
                    sendMessage();
                }
            }
        }
        content = VBox {
                    managed: false;
                    spacing: 5.0;
                    padding: Insets {
                        top: 5
                        right: 5
                        bottom: 5
                        left: 5
                    }
                    nodeHPos: HPos.CENTER
                    content: [
                        sv,
                        HBox {
                            spacing: 2.0;
                            content: [
                                messageBox,
                                sendButton
                            ]
                            layoutInfo: LayoutInfo {
                                hfill: true
                                hgrow: Priority.ALWAYS
                            }
                        }
                    ]
                }
        return content;
    }

    override function getPrefWidth(height: Number): Number {
        return 200;
    }

    override function getPrefHeight(width: Number): Number {
        return 418;
    }

    postinit {
        sizeChanged();
    }

    public override function getDrawerUIIndicator(): DrawerUIIndicator {
        return DrawerUIIndicator.CHAT;
    }

    function sizeChanged(): Void {
        Container.resizeNode(content, width, height);
    }

    public override var width on replace { sizeChanged() };
    public override var height on replace { sizeChanged() };

    function sendMessage(): Void {
        if (chatController != null) {
            chatController.sendMessage(messageBox.text);
            messageBox.text = "";
        }
    }

    public override function addMessage(time: String, name: String, text: String): Void {
        addMessage(time, name, text, false);
    }

    public function addMessage(time: String, name: String, text: String, isSystemMessage: Boolean): Void {
        if (not time.equalsIgnoreCase(lastTime) and not isSystemMessage) {
            insert SystemLine {
                text: time
                layoutWidth: bind content.layoutBounds.width
                layoutInfo: LayoutInfo {
                    hfill: true
                    vgrow: Priority.ALWAYS
                }
            } into chatLines.content;
            lastTime = time;
        }
        if (isSystemMessage) {
            if (not name.equalsIgnoreCase(chatController.getCurrentUser())) {
                insert SystemLine {
                    text: text
                    layoutWidth: bind content.layoutBounds.width
                    layoutInfo: LayoutInfo {
                        hfill: true
                        vgrow: Priority.ALWAYS
                    }
                } into chatLines.content;
                sv.vvalue = sv.vmax;
            }
            return;
        }

        var color: Color;
        if (name.equalsIgnoreCase(chatController.getCurrentUser())) {
            color = scyWindow.windowColorScheme.mainColor;
        } else {
            color = scyWindow.windowColorScheme.secondColor;
        }

        insert ChatLine {
            name: name
            text: text
            color: color
            textLineWidth: bind content.layoutBounds.width
            isSystemMessage: isSystemMessage;
            layoutInfo: LayoutInfo {
                hfill: false
                vgrow: Priority.NEVER
            }
        } into chatLines.content;
        sv.vvalue = sv.vmax;
    }
}
