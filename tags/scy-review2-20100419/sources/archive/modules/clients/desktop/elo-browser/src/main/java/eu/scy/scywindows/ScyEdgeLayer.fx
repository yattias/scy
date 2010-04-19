/*
 * ScyEdgeLayer.fx
 *
 * Created on 27.03.2009, 13:58:04
 */

package eu.scy.scywindows;

import eu.scy.scywindows.ScyEdge;
import eu.scy.scywindows.ScyWindow;
import java.lang.Math;
import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.text.Text;

/**
 * @author pg
 */

public class ScyEdgeLayer extends CustomNode {
    public var node1: ScyWindow;
    public var node2: ScyWindow;
    public var caption: String;
    var edge: ScyEdge;

    var displaytext: Text =
    Text {
        visible: bind edge.visible;
        content: bind caption;
        x: bind (edge.startX+edge.endX) /2;
        y: bind (edge.startY+edge.endY) /2;
        fill:bind node1.color;
        /* total idiotischer ansatz :) */
        //mal was anderes:
        // var angle = bind toDegrees(java.lang.Math.atan((node1.y  -  node2.y) / (node1.x - node2.x)));
        // transforms: bind javafx.scene.transform.Transform.rotate(angle, displaytext.x, displaytext.y);
        //oh gott, das wird ein krampf.
        /*
         */
         //(abs(node1.x - node2.x) < 100) or
        var foobar = bind  if( Math.abs(node1.translateX - node2.translateX) < 100)
              then 0
              else
        Math.toDegrees(edge.gradientAngle);
        transforms: bind javafx.scene.transform.Transform.rotate(foobar, displaytext.x, displaytext.y);
           /*java.lang.Math.toDegrees((java.lang.Math.abs(node1.x  -  node2.x) / 2) /
            (java.lang.Math.sqrt(
                    ((java.lang.Math.abs(node1.x       -       node2.x)   /   2)   *   (java.lang.Math.abs(node1.x           -           node2.x /   2))) +
                    ((java.lang.Math.abs(node1.y       -       node2.y)   /   2)   *   (java.lang.Math.abs(node1.y           -           node2.y /   2)))
            )));
*/
    };
    init {
        edge = ScyEdge{
            node1: bind this.node1;
            node2: bind this.node2;
            stroke: bind node1.color;
        };
        edge.repaint();
    }

    override function create():Node {
        return Group {
            content: [edge, displaytext];
        }

    }

    public function repaint() {
        edge.repaint();
    }


}