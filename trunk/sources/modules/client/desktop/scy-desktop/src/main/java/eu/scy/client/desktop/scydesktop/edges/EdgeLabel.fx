/*
 * EdgeLabel.fx
 *
 * Created on 12.01.2010, 10:49:52
 */

package eu.scy.client.desktop.scydesktop.edges;
import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.text.Text;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.input.MouseEvent;

/**
 * @author pg
 */

public class EdgeLabel extends CustomNode {

    public-init var edge:Edge;

    public var labelText:String;
    //todo: label is not realy in the center.
    var x:Number = bind (edge.line.startX+edge.line.endX) / 2;
    var y:Number= bind (edge.line.startY+edge.line.endY) / 2;
    public-read var width:Number = bind border.width;
    public-read var height:Number = bind border.height;
    
    var label:Text = Text {
            content: bind labelText;
            translateX: bind x+2;
            translateY: bind y+14;
    }

    var border:Rectangle = Rectangle {
        translateX: bind x;
        translateY: bind y;
        height: bind label.boundsInParent.height+5;
        width: bind label.boundsInParent.width+5;
        fill: Color.WHITE;
        stroke: Color.BLACK;
    }



    override function create():Node {
        var g:Group = Group {
            content: [
                    border,
                    label,
                    ]
        }
    }


}
