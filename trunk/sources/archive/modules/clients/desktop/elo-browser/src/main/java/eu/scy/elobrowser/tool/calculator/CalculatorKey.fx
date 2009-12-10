
package eu.scy.elobrowser.tool.calculator;

import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextOrigin;
import javafx.animation.Timeline;

/**
 * @author dean
 */
def keyFont = Font { size: 28 }

public class CalculatorKey extends CustomNode {
    public var keyColor = Color.BLACK;
    public var characterColor = Color.YELLOW;
    public-init var character: String;
    public-init var action: function();

    var background = bind LinearGradient {
        endX: 0.0
        endY: 1.0
        stops: [
            Stop { offset: 0.0 color: keyColor.ofTheWay( Color.WHITE, 0.75 ) as Color },
            Stop { offset: 1.0 color: keyColor },
        ]
    }

    var strokeColor = Color.DIMGRAY;
    var keyFade = Timeline {
        keyFrames: [
            at( 0.0s ) { strokeColor => Color.WHITE },
            at( 0.8s ) { strokeColor => Color.DIMGRAY }
        ]
    }

    override function create() {
        Group {
            def r:Rectangle = Rectangle {
                width: 60
                height: 40
                arcHeight: 10
                arcWidth: 10
                smooth: true
                stroke: bind strokeColor
                fill: bind background
                onMousePressed: function( me:MouseEvent ) {
                    keyFade.playFromStart();
                    action();
                }
            }
            def t:Text = Text {
                translateX: bind (r.width - t.layoutBounds.width) / 2 - t.layoutBounds.minX
                translateY: bind (r.height - t.layoutBounds.height) / 2 - t.layoutBounds.minY
                content: character
                fill: bind characterColor;
                font: keyFont
                textOrigin: TextOrigin.TOP
            }
            content: [ r, t ]
        }
    }
}
