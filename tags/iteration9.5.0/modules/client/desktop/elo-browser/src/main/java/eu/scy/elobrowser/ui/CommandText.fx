/*
 * CommandText.fx
 *
 * Created on 3-apr-2009, 10:19:28
 */

package eu.scy.elobrowser.ui;import java.lang.Math;
import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * @author sikkenj
 */

public class CommandText extends CustomNode {
		public var label="label";
		public var clickAction:function(e: MouseEvent):Void;

        def height = 17;
        def horizontalSpace = 8;
        def minimumWidth = 15;
        def acrSize = 5;

        var color = Color.color(0.34,0.34,0.34);
		var hoverColor = Color.BLACK;
		var textFont =  Font {
			size: 11}
		var text:Text = Text{
						translateX:horizontalSpace;
						translateY:12;
						font:textFont
						content: bind label
						fill:color
					}

		public override function create(): Node {
			return Group {
				content: [
					Rectangle {
						x: 0,
						y: 0
						width: Math.max(minimumWidth,text.boundsInLocal.width+2*horizontalSpace),
						height: height
						arcHeight:acrSize
						arcWidth:acrSize
						fill: Color.color(0.9,0.9,0.9)
					}
					text
				]
				onMouseEntered: function( e: MouseEvent ):Void {
					text.fill = hoverColor;
				}
				onMouseExited: function( e: MouseEvent ):Void {
					text.fill = color;
				}
				onMouseClicked: function( e: MouseEvent ):Void {
					if (clickAction != null) clickAction(e);
				}
      };
		}
	}
