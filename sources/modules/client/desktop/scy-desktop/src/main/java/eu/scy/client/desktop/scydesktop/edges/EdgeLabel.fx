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
    public var x:Number = 0.0;
    public var y:Number = 0.0;
    public-read var width:Number = bind border.width;
    public-read var height:Number = bind border.height;

    var label:Text = Text {
            content: bind labelText;
            translateX: bind border.translateX + 2;
            translateY: bind border.translateY + 6 + (label.layoutBounds.height / 2);
    }

    var border:Rectangle = Rectangle {
        translateX: bind x - (label.layoutBounds.width / 2) - 2;
        translateY: bind y - (label.layoutBounds.height / 2) - 2;
        height: bind label.layoutBounds.height+4;
        width: bind label.layoutBounds.width+4;
        fill: Color.WHITE;
        stroke: Color.BLACK;
    }

    var transparentBorder:Rectangle = Rectangle {
        //This is a transparent Rectangle for smoother fading (beyond the label)
        translateX: bind x-15;
        translateY: bind y-15;
        height: bind label.boundsInParent.height+35;
        width: bind label.boundsInParent.width+35;
        fill: Color.TRANSPARENT;
    }



    override function create():Node {
        var g:Group = Group {
            content: [
                    transparentBorder,
                    border,
                    label,
                    ]
        }
    }


}
