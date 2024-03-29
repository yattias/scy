/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.scywindows;

import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Rectangle;
import javafx.animation.Timeline;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.util.Sequences;
import javafx.scene.CacheHint;
import eu.scy.client.desktop.desktoputils.art.ArtSource;

/**
 * @author SikkenJ
 */
def modalDialogLayer = ModalDialogLayer {};
public def layer: Node = modalDialogLayer;

public function addModalDialog(node: Node, center: Boolean, animated: Boolean, fullscreen: Boolean, xOffset: Integer): Void {
    modalDialogLayer.addModalDialogNode(node, center, animated, fullscreen, xOffset);
}

public function addModalDialog(node: Node, center: Boolean): Void {
    modalDialogLayer.addModalDialogNode(node, center, false, false, 0);
}

public function addModalDialog(node: Node): Void {
    modalDialogLayer.addModalDialogNode(node, false, false, false, 0);
}

public function removeModalDialog(node: Node): Void {
    removeModalDialog(node, false, false);
}

public function removeModalDialog(node: Node, animated: Boolean): Void {
    removeModalDialog(node, animated, false);
}

public function removeModalDialog(node: Node, animated: Boolean, reallyRemove: Boolean): Void {
    modalDialogLayer.removeModalDialogNode(node, animated, reallyRemove);
}

public function resize() : Void {
    modalDialogLayer.sceneSizeChanged();
}

public function addHiddenNode(node: Node): Void{
   insert node into modalDialogLayer.hiddenNodes;
   insert node into modalDialogLayer.modalDialogGroup.content
}


public class ModalDialogLayer extends CustomNode {

    def sceneWidth = bind scene.width;
    def sceneHeight = bind scene.height;
    def modalDialogGroup = Group {
                visible: true
            }
    def backgroundBlocker = Rectangle {
                blocksMouse: true
                x: 0, y: 0
                width: 100, height: 100
                fill: ArtSource.dialogBlockLayerColor
                visible: false
                onKeyPressed: function(e: KeyEvent): Void {
                }
                onKeyReleased: function(e: KeyEvent): Void {
                }
                onKeyTyped: function(e: KeyEvent): Void {
                }
            }
    var centeredNodes: Node[];

    var fullscreenNodes: Node[];

    var hiddenNodes: Node[];

    public function sceneSizeChanged() {
        backgroundBlocker.width = scene.width;
        backgroundBlocker.height = scene.height;
        if (backgroundBlocker.visible) {
            for (node in centeredNodes) {
                node.layoutX = -node.layoutBounds.minX + scene.width / 2 - node.layoutBounds.width / 2;
                node.layoutY = -node.layoutBounds.minY + scene.height / 2 - node.layoutBounds.height / 2;
            }
        } 
        for (node in hiddenNodes) {
            node.translateY = -node.layoutBounds.height + 26;
        }
    }

    public override function create(): Node {
        modalDialogGroup.content = [];
        modalDialogGroup
    }

    function addModalDialogNode(node: Node, center: Boolean, animated: Boolean, fullscreen: Boolean, xOffset: Integer): Void {
        if (center) {
            insert node into centeredNodes;
        }
        if (fullscreen) {
            insert node into fullscreenNodes;
        } else {
            backgroundBlocker.visible = true;
        }

        if (Sequences.indexOf(hiddenNodes, node) >= 0) {
            delete node from hiddenNodes;
            delete node from modalDialogGroup.content;
        }

        // reorder the background blocker, so that everything behin the new window is blocked
        delete backgroundBlocker from modalDialogGroup.content;
        insert backgroundBlocker into modalDialogGroup.content;
        if (Sequences.indexOf(modalDialogGroup.content, node) < 0) {
            insert node into modalDialogGroup.content;
        }
        node.visible = true;
        if (animated) {
            backgroundBlocker.opacity = 0.0;
            node.translateY = -node.layoutBounds.height + 26;
            node.translateX = xOffset;
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
                                    } else {
                                        insert node into hiddenNodes;
                                    }

                                    backgroundBlocker.visible = false;
                                    nodeTemp.cache = false;
                                }
                            }
                        ];
                    };
            removeDialogTimeline1.playFromStart();
        } else {
           // fix for deadlock, defer actions
           FX.deferAction(function():Void{
               delete node from modalDialogGroup.content;
               delete node from centeredNodes;
               node.visible = false;
               if (Sequences.indexOf(fullscreenNodes, node) == -1) {
                   backgroundBlocker.visible = false;
               } else {
                   delete node from fullscreenNodes;
               }
               if (sizeof modalDialogGroup.content > 1 and modalDialogGroup.content[sizeof modalDialogGroup.content - 1] == backgroundBlocker) {
                   delete backgroundBlocker from modalDialogGroup.content;
                   def lastNode = modalDialogGroup.content[sizeof modalDialogGroup.content - 1];
                   delete lastNode from modalDialogGroup.content;
                   insert backgroundBlocker into modalDialogGroup.content;
                   insert lastNode into modalDialogGroup.content;
               }
              });
        }
    }

}
