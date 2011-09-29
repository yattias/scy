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
import javafx.scene.layout.Panel;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.Node;

/**
 * @author pg
 */

public class ChatLine extends CustomNode {

    public-init var name:String;
    public-init var text:String;
    public-init var color:Color;
    public var textLineWidth:Number;
    public-init var isSystemMessage: Boolean;

    var nameText:Text;
    var wrappingWidth:Number = bind textLineWidth - nameText.layoutBounds.width - 10 - 10 - 12 - 18; // scrollbar, insets, spacing, box
    
    public override function create() : Node {

       nameText = Text {
            content: bind "{name}";
            font: Font {
                embolden: true;
                size: 12.0;
            }
            fill: color
        }

        var textText:Text = Text {
            content: bind text;
            font: Font {
                size: 12.0;
            }
            fill: Color.BLACK;
            wrappingWidth: bind wrappingWidth
        }

        var line: HBox = HBox {
            spacing: 4.0;
            content: [//timeText,
                Panel {
                    content: [
                        Polyline {
                            points: bind [0, 8,
                                10, 3,
                                10, 0,
                                textText.layoutBounds.width + 15, 0,
                                textText.layoutBounds.width + 15, textText.layoutBounds.height + 5,
                                16, textText.layoutBounds.height + 5]
                            strokeWidth: 2.0
                            stroke: color
                        },
                        Arc {
                            centerX: 15, centerY: bind textText.layoutBounds.height,
                            radiusX: 5, radiusY: 5
                            startAngle: 180, length: 90
                            type: ArcType.OPEN,
                            fill: Color.WHITE,
                            stroke: color, strokeWidth: 2.0
                        },Polyline {
                            points: bind [
                                10, textText.layoutBounds.height,
                                10, 9,
                                0, 8]
                            strokeWidth: 2.0
                            stroke: color
                        },
                        VBox {
                            padding: Insets {
                                top: 2
                                right: 2
                                bottom: 2
                                left: 15
                            }
                            content: [textText]
                        }
                    ]
                }
            ]
        }
        if (not isSystemMessage) {
            insert nameText before line.content[0];
        }

        return line;
    }

}
