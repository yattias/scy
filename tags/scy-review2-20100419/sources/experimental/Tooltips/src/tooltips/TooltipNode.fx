/*
 * TooltipNode.fx
 *
 * Created on 26-nov-2009, 17:22:17
 */

package tooltips;

import javafx.animation.Interpolator;
import javafx.animation.Timeline;
import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * @author Raghu Nair
 */

public class TooltipNode extends CustomNode{


    /* Source node where you want to show the tool tip */
    public-init var sourceNode:Node;

    /* Tool tip text */
    public-init var tooltip:String;

    /* Width */
    public-init var width :Integer = 80;



    var glowColor = Color.TRANSPARENT;
    override var opacity = 0.0;
    def showTip:Timeline = Timeline {
        keyFrames: [
            at (0s) {
                glowColor => Color.TRANSPARENT;
            opacity => 0.0;
            },
            at (1s) {
                glowColor =>Color.color(0.5, 0.5, 0.5, 0.5);
                opacity => 1.0 tween Interpolator.EASEBOTH;
            },
        ]
    }

    var rectangle:Rectangle = Rectangle{
        translateX: bind ( sourceNode.translateX+(sourceNode.layoutBounds.width - rectangle.layoutBounds.width )/2)
        translateY: bind (( sourceNode.layoutBounds.height/2) + 45);
        fill:Color.YELLOW
        width:width
        height:25
    }

    var text:Text = Text {
        translateX: bind (rectangle.translateX )
        translateY: bind rectangle.translateY+(rectangle.layoutBounds.height / 2) + 3
        font: Font {
            size: 11
        }
        fill: Color.BLUEVIOLET
        content: bind tooltip
    }

    override function create():Node {
        sourceNode.onMouseEntered = function(e:MouseEvent){
            sourceNode.opacity = 1.0;
            showTip.rate = 1.0;
            showTip.playFromStart();
        }
        sourceNode.onMouseExited =  function(e:MouseEvent){
            sourceNode.opacity = 0.7;
            showTip.rate = -1.0;
            opacity = 0.0;
        }
        Group{
            content: [rectangle,text]
       }

    }
}