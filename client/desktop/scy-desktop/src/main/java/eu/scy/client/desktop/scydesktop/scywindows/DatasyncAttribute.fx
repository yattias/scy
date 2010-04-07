/*
 * DatasyncAttribute.fx
 *
 * Created on 09.02.2010, 11:29:49
 */

package eu.scy.client.desktop.scydesktop.scywindows;

/**
 * @author lars
 */

import eu.scy.client.desktop.scydesktop.scywindows.ScyWindowAttribute;
import java.lang.Object;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import eu.scy.client.desktop.scydesktop.tools.corner.missionmap.EloContour;
import eu.scy.client.desktop.scydesktop.draganddrop.DragAndDropManager;
import eu.scy.client.desktop.scydesktop.utils.log4j.Logger;

/**
 * @author lars
 */
public class DatasyncAttribute extends ScyWindowAttribute {

    public-init var dragAndDropManager:DragAndDropManager;
    public-init var dragObject:Object;

    def logger = Logger.getLogger(this.getClass());
    def size = 16.0;
    def strokeWidth = 2;
    def defaultTitleColor = Color.WHITE;
    def defaultContentColor = Color.GRAY;
    var titleColor = defaultTitleColor;
    var contentColor = defaultContentColor;

    function mousePressed(e: MouseEvent) {
        dragAndDropManager.startDrag(dragIcon(), dragObject, this, e);
    };

    function mouseDragged(e: MouseEvent) {};

    function mouseReleased(e: MouseEvent) {};

    function dragIcon(): Node {
        return Group {
                    cursor: Cursor.HAND;
                    translateY: -size - 2;
                    content: [
                        EloContour {
                            width: size;
                            height: size;
                            controlLength: 5;
                            borderWidth: 2;
                            fillColor: bind contentColor;
                        }
                        Circle {
                            centerX: size / 2 - size/6,
                            centerY: size / 2 - size/6,
                            radius: size / 4,
                            fill: contentColor,
                            stroke: bind titleColor,
                            strokeWidth: strokeWidth
                        }
                        Circle {
                            centerX: size / 2 + size/6,
                            centerY: size / 2 + size/6,
                            radius: size / 4,
                            fill: contentColor,
                            stroke: bind titleColor,
                            strokeWidth: strokeWidth
                        }

                    ]
            }
    }

    public override function create(): Node {
        return Group {
                    cursor: Cursor.HAND;
                    translateY: -size - 2;
                    blocksMouse: true;
                    content: [
                        EloContour {
                            width: size;
                            height: size;
                            controlLength: 5;
                            borderWidth: 2;
                            fillColor: bind contentColor;
                        }
                        Circle {
                            centerX: size / 2,
                            centerY: size / 2,
                            radius: size / 4,
                            fill: contentColor,
                            stroke: bind titleColor,
                            strokeWidth: strokeWidth
                        }
                        Circle {
                            centerX: size / 2 + size/6,
                            centerY: size / 2 + size/6,
                            radius: size / 4,
                            fill: contentColor,
                            stroke: bind titleColor,
                            strokeWidth: strokeWidth
                        }

                    ]
                    onMousePressed:mousePressed;
                    onMouseDragged:mouseDragged;
                    onMouseReleased:mouseReleased;
                /*
                onMouseClicked: function( e: MouseEvent ):Void {
                if (anchorDisplay.selectionAction != null){
                anchorDisplay.selectionAction(anchorDisplay);
                }
                else {
                selected = not selected;
                }
                },
                };*/
                }
    }
}