/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.fxchattool.registration;
import javafx.scene.CustomNode;
import javafx.scene.text.Text;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;
import javafx.geometry.VPos;

/**
 * @author pg
 */

public class SystemLine extends CustomNode {

    public-init var text:String;
    public-init var color:Color;
    public var layoutWidth:Number;

    var timeText:Text = Text {
        content: "[{text}]"
        font: Font {
            size: 11.0;
        }
        fill: Color.GREY;
    }

    var line: HBox = HBox {
        spacing: 4.0;
        nodeVPos: VPos.CENTER
        content: [
            Polyline {
                points: bind [2, 0, ((layoutWidth - 24) / 2) - (timeText.boundsInParent.width / 2), 0]
                strokeWidth: 1.0
                stroke: Color.LIGHTGREY
            }, timeText,
            Polyline {
                points: bind [(layoutWidth / 2) + (timeText.boundsInParent.width / 2), 0, layoutWidth - 20, 0]
                strokeWidth: 1.0
                stroke: Color.LIGHTGREY
            }
        ]
    }

    override var children = bind line;
    postinit {
        //check for replace -> smileys
    }
}
