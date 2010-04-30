/*
 * DialogBox.fx
 *
 * Created on 18-jan-2010, 11:14:35
 */
package eu.scy.client.desktop.scydesktop.scywindows.scydesktop;

import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import eu.scy.client.desktop.scydesktop.scywindows.window.StandardScyWindow;
import javafx.scene.Scene;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import javafx.scene.input.KeyEvent;
import eu.scy.client.desktop.scydesktop.scywindows.EloIcon;
import eu.scy.client.desktop.scydesktop.art.WindowColorScheme;
import eu.scy.client.desktop.scydesktop.ScyDesktop;

/**
 * @author sikken
 */
// place your code here
public class DialogBox extends CustomNode {

    public   var content: Node;

    public   var targetScene: Scene;
    var addedAsScyWindow:Boolean = false;

    public var title: String;
    public var eloIcon: EloIcon;
    public var windowColorScheme: WindowColorScheme;
    public var closeAction: function(): Void;
    public-init var modal: Boolean = true;
    public-init var scyDesktop:ScyDesktop;
    var dialogWindow: ScyWindow;

    init {
        FX.deferAction(place);
    }

    public override function create(): Node {
        dialogWindow = StandardScyWindow {
            eloUri:null;
            eloType:null;
            scyContent: content;
            title: title
            eloIcon: eloIcon;
            windowColorScheme: windowColorScheme
            closedAction: function (window: ScyWindow) {
                close();
            }
            allowMinimize: false
            activated: true
        }
        dialogWindow.open();
        return Group {
                    content: [
                        if (modal) {
                            Rectangle {
                                blocksMouse: true
                                x: 0, y: 0
                                width: bind scene.width, height: bind scene.height
                                fill: Color.color(1.0, 1.0, 1.0, 0.5)
                                onKeyPressed: function (e: KeyEvent): Void {
                                }
                                onKeyReleased: function (e: KeyEvent): Void {
                                }
                                onKeyTyped: function (e: KeyEvent): Void {
                                } }
                        } else []//,
                        //dialogWindow
                    ]
                };
    }

    public function close(): Void {
        if (not addedAsScyWindow){
            var sceneContentList = scene.content;
            delete this from sceneContentList;
            delete dialogWindow from sceneContentList;
            scene.content = sceneContentList;
            closeAction();
        } else {
            scyDesktop.windows.removeScyWindow(dialogWindow);
            closeAction();
        }
    }

    public function place(): Void {
        var sceneContentList = targetScene.content;
        if (modal) {
            insert this into sceneContentList;
            insert dialogWindow into sceneContentList;
            targetScene.content = sceneContentList;
            this.requestFocus();
        } else {
            if (not(scyDesktop==null)){
            targetScene.content = sceneContentList;
            scyDesktop.windows.addScyWindow(dialogWindow);
            scyDesktop.windows.activateScyWindow(dialogWindow);
            addedAsScyWindow = true;
            }
        }
        center();
    }

    function center() {
        if (modal) {
            dialogWindow.layoutX = scene.width / 2 - dialogWindow.layoutBounds.width / 2;
            dialogWindow.layoutY = scene.height / 2 - dialogWindow.layoutBounds.height / 2;
        } else if (not (scyDesktop == null)) {
            dialogWindow.layoutX = scyDesktop.scene.width / 2 - dialogWindow.layoutBounds.width / 2;
            dialogWindow.layoutY = scyDesktop.scene.height / 2 - dialogWindow.layoutBounds.height / 2;
        }
    }
}

public function placeInDialogBox(content: Node, scene: Scene): DialogBox {
    var DialogBox = DialogBox {
                content: content;
            }
    var sceneContentList = scene.content;
    insert DialogBox into sceneContentList;
    scene.content = sceneContentList;
    return DialogBox;
}

public function placeInDialogBox(content: Node[], scene: Scene): DialogBox {
    return placeInDialogBox(Group { content: content }, scene);
}
