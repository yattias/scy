/*
 * ScyRelation.fx
 *
 * Created on 3-feb-2009, 12:43:45
 */

package eu.scy.scywindows.demos.stylebook;

import eu.scy.scywindows.ScyWindow;
import java.awt.Point;
import javafx.geometry.Point2D;
import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * @author JoolingenWR
 */

public class ScyRelation extends CustomNode {
    public var name = "";
    public var window1:ScyWindow;
    public var window2:ScyWindow;
    public var color = window1.color;
    public var strokeWidth = 2;

    function intersect(sx:Number, sy:Number, ex:Number, ey:Number, left:Number, top:Number, right:Number, bottom:Number): Point2D {
        // assert  left < sx < right
        // assert top < sy < bottom
        var px:Number;
        var py:Number;

        //need special cases for horizontal and vertical lines
        if (ex == sx) {
            //vertical
            px = sx;
            if (ey > sy) {
                py = top;
            } else {
                py = bottom;
            }
            return Point2D { x: px, y:py };
        }

        if (ey == sy) {
            py = sy;
            if (ex > sx) {
                px = right;
            } else {
                px = left;
            }
            return Point2D { x: px, y:py };
        }

        //diagonal lines
        var rc = (ey - sy) / (ex - sx);
        if (rc < 0) {
            if (ex > sx) {
                //up-right
                //compute y for right
                var y_right = sy + rc * (right - sx);
                if (y_right > top) {
                    px = right;
                    py = y_right;
                } else {
                    px = sx + (top - sy) / rc;
                    py = top;
                }
            } else {
                //dowm-left
                var y_left = sy + rc * (left - sx);
                if (y_left < bottom) {
                    px = left;
                    py = y_left;
                } else {
                    px = sx + (bottom - sy) / rc;
                    py = bottom;
                }
            }
        } else {
            if (ex > sx) {
                //down-right
                var y_right = sy + rc * (right - sx);
                if (y_right < bottom) {
                    px = right;
                    py = y_right;
                } else {
                    px = sx + (bottom - sy) / rc;
                    py = bottom;
                }
            } else {
                //up-left
                var y_left = sy + rc * (left - sx);
                if (y_left > top) {
                    px = left;
                    py = y_left;
                } else {
                    px = sx + (top - sy) / rc;
                    py = top;
                }
            }
        }
        return Point2D { x: px, y:py };
    }

    bound function startPoint():Point2D {
        return intersect(
            window1.translateX + window1.width / 2, window1.translateY + window1.height / 2,
            window2.translateX + window2.width / 2, window2.translateY + window2.height / 2,
            window1.translateX, window1.translateY,
            window1.translateX + window1.width, window1.translateY + window1.height)
    }

    bound function endPoint():Point2D {
        return intersect(
            window2.translateX + window2.width / 2, window2.translateY + window2.height / 2,
            window1.translateX + window1.width / 2, window1.translateY + window1.height / 2,
            window2.translateX, window2.translateY,
            window2.translateX + window2.width, window2.translateY + window2.height)
    }

    public override function create(): Node {
        return Group {
            content: [
                Line{
                    startX: bind window1.translateX + window1.width / 2,
                    startY: bind window1.translateY + window1.height / 2,
                    endX: bind window2.translateX + window2.width / 2,
                    endY: bind window2.translateY + window2.height / 2,
                    strokeWidth: strokeWidth,
                    stroke: color
                }
                Text {
                    font: Font {
                        size: 12

                    }
                    x: bind (startPoint().x + endPoint().x) / 2,
                    y: bind (startPoint().y + endPoint().y) / 2,
                    content: name,
                    fill: color
                }
            ]
        }
    }
}
