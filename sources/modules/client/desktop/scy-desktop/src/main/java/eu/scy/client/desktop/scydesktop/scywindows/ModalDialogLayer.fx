/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.scywindows;

import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.animation.Timeline;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.util.Sequences;
import javafx.scene.CacheHint;

/**
 * @author SikkenJ
 */
def modalDialogLayer = ModalDialogLayer {};
public def layer: Node = modalDialogLayer;

public function addModalDialog(node: Node, center: Boolean, animated: Boolean, fullscreen: Boolean): Void {
    modalDialogLayer.addModalDialogNode(node, center, animated, fullscreen);
}

public function addModalDialog(node: Node, center: Boolean): Void {
    modalDialogLayer.addModalDialogNode(node, center, false, false);
}

public function addModalDialog(node: Node): Void {
    modalDialogLayer.addModalDialogNode(node, false, false, false);
}

public function removeModalDialog(node: Node): Void {
    removeModalDialog(node, false, false);
}

public function removeModalDialog(node: Node, animated: Boolean, reallyRemove: Boolean): Void {
    modalDialogLayer.removeModalDialogNode(node, animated, reallyRemove);
}

public class ModalDialogLayer extends CustomNode {

    def sceneWidth = bind scene.width on replace { sceneSizeChanged() };
    def sceneHeight = bind scene.height on replace { sceneSizeChanged() };
    def modalDialogGroup = Group {
                visible: false
            }
    def backgroundBlocker = Rectangle {
                blocksMouse: true
                x: 0, y: 0
                width: 100, height: 100
                fill: Color.color(0.0, 0.0, 0.0, 0.3)
                onKeyPressed: function(e: KeyEvent): Void {
                }
                onKeyReleased: function(e: KeyEvent): Void {
                }
                onKeyTyped: function(e: KeyEvent): Void {
                }
            }
    var centeredNodes: Node[];

    var fullscreenNodes: Node[];

    function sceneSizeChanged() {
        backgroundBlocker.width = scene.width;
        backgroundBlocker.height = scene.height;
        if (backgroundBlocker.visible) {
            for (node in centeredNodes) {
                node.layoutX = -node.layoutBounds.minX + scene.width / 2 - node.layoutBounds.width / 2;
                node.layoutY = -node.layoutBounds.minY + scene.height / 2 - node.layoutBounds.height / 2;
            }
        } else {
            for (node in modalDialogGroup.content) {
                if (node != backgroundBlocker) {
                    node.translateY = -node.layoutBounds.height + 26;
                }
            }
        }
    }

    public override function create(): Node {
        modalDialogGroup.content = backgroundBlocker;
        modalDialogGroup
    }

    function addModalDialogNode(node: Node, center: Boolean, animated: Boolean, fullscreen: Boolean): Void {
        if (center) {
            insert node into centeredNodes;
            sceneSizeChanged();
        }
        if (fullscreen) {
            insert node into fullscreenNodes;
        }

        if (Sequences.indexOf(modalDialogGroup.content, backgroundBlocker) < 0) {
            insert backgroundBlocker into modalDialogGroup.content;
        }
        if (Sequences.indexOf(modalDialogGroup.content, node) < 0) {
            insert node into modalDialogGroup.content;
        }
        modalDialogGroup.visible = true;
        node.visible = true;
        backgroundBlocker.visible = true;
        if (animated) {
            backgroundBlocker.opacity = 0.0;
            node.translateY = -node.layoutBounds.height + 26;
            def nodeTemp = node;
            nodeTemp.cache = true;
            nodeTemp.cacheHint = CacheHint.SPEED;
            def addDialogTimeline1 = Timeline {
                        keyFrames: [
                            KeyFrame {
                                time: 100ms
                                values: [
                                    backgroundBlocker.opacity => 1.0 tween Interpolator.LINEAR
                                ]
                                action: function() {
                                    addDialogTimeline2.playFromStart();
                                }
                            }
                        ];
                    };
            def addDialogTimeline2 = Timeline {
                        keyFrames: [
                            KeyFrame {
                                time: 400ms
                                values: [
                                    nodeTemp.translateY => 2.0 tween Interpolator.EASEOUT,
                                ]
                                action: function() {
                                    nodeTemp.cache = false;
                                }
                            }
                        ];
                    };
            addDialogTimeline1.playFromStart();
        } else {
            backgroundBlocker.opacity = 1.0;
        }

    }

    function removeModalDialogNode(node: Node): Void {
        removeModalDialogNode(node, false, false);
    }

    function removeModalDialogNode(node: Node, animated: Boolean, reallyRemove: Boolean): Void {
        if (animated) {
            def nodeTemp = node;
            nodeTemp.cache = true;
            nodeTemp.cacheHint = CacheHint.SPEED;
            def removeDialogTimeline1 = Timeline {
                        keyFrames: [
                            KeyFrame {
                                time: 400ms
                                values: [
                                    nodeTemp.translateY => -nodeTemp.layoutBounds.height + 26 tween Interpolator.EASEIN,
                                ]
                                action: function() {
                                    if (Sequences.indexOf(fullscreenNodes, nodeTemp) != -1) {
                                        delete nodeTemp from fullscreenNodes;
                                    } else {
                                        removeDialogTimeline2.playFromStart();
                                    }
                                }
                            }
                        ]
                    };
            def removeDialogTimeline2 = Timeline {
                        keyFrames: [
                            KeyFrame {
                                time: 100ms
                                values: [
                                    backgroundBlocker.opacity => 0.0 tween Interpolator.LINEAR
                                ]
                                action: function() {
                                    if (reallyRemove) {
                                        node.visible = false;
                                        delete node from centeredNodes;
                                        delete node from modalDialogGroup.content;
                                        modalDialogGroup.visible = sizeof modalDialogGroup.content > 1;
                                    }
                                    backgroundBlocker.visible = false;
                                    nodeTemp.cache = false;
                                }
                            }
                        ];
                    };
            removeDialogTimeline1.playFromStart();
        } else {
            delete node from modalDialogGroup.content;
            delete node from centeredNodes;
            node.visible = false;
            if (Sequences.indexOf(fullscreenNodes, node) == -1) {
                backgroundBlocker.visible = false;
                modalDialogGroup.visible = false;
            } else {
                delete node from fullscreenNodes;
            }

        }
    }

}
