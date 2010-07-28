/*
 * Arrow.fx
 *
 * Created on 24.03.2010, 15:13:28
 */

package eu.scy.client.desktop.scydesktop.edges;
import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Polygon;

/**
 * @author pg
 */

public class Arrow extends CustomNode {

    public-init var color:Color = Color.BLACK;
    public-init var strokeWidth = 3.0;


    //i hope noone will ever find out the truth about these polygons
    def arrowPolygonTop:Polygon = Polygon {
        points: [
                    -15.0, 7.0,
                    0.0, 2.0,
                    -15.0, 2.0
                ]
        fill: bind color;
        stroke: bind color;
    }

    def arrowPolygonBottom:Polygon = Polygon {
        points: [
                    -15.0, -7.0,
                    0.0, -2.0,
                    -15.0, -2.0
                ]
        fill: bind color;
        stroke: bind color;
    }


    def arrowPolygon:Polygon = Polygon {
        points: [
                    0, -7.0,
                    15.0, 0.0,
                    0.0, 7.0
                ]
        fill: bind color;
    }


    def line_a:Line = Line {
        startX: -25;
        startY: -10;
        endX: 0;
        endY: -1;
        strokeWidth: bind strokeWidth;
        stroke: bind color;
    }

    def line_b:Line = Line {
        startX: 0;
        startY: 0;
        endX: 0;
        endY: 10;
        stroke: bind color;
    }

    def line_c:Line = Line {
        startX: -25;
        startY: 10;
        endX: 0;
        endY: 1;
        strokeWidth: bind strokeWidth;
        stroke: bind color;
    }

    override protected function create () : Node {
            var g:Group = Group {
                content: [
                        //line_a,
                        //line_c,
                        //arrowPolygonTop,
                        //arrowPolygonBottom,
                        arrowPolygon,
                        Rectangle {
                            x: -15;
                            y: -7;
                            width: 15;
                            height: 14;
                            fill: Color.TRANSPARENT;
                        }
                        ]
            }
            return g;
    }

}