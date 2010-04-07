/*
 * BulletPoint.fx
 *
 * Created on 17.08.2009, 14:18:21
 */

package eu.scy.client.tools.fxwebresourcer;

import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;

import javafx.scene.text.Text;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;

import javafx.scene.text.Font;

/**
 * @author pg
 */

public class BulletPoint extends CustomNode {

        public var text:String;
        public var y:Number;
        public var x:Number;
        var bullet:Circle = Circle {
                centerX: 20,
                centerY: bind y-4;
                radius: 3
                fill: Color.BLACK
        }
        var contentText:Text = Text {
                content:bind text;
                font: Font { size: 16; }
                wrappingWidth: 525;
                translateX: 30;
                translateY: bind y;
        }
        public var width:Number = bind 28 + contentText.layoutBounds.width;
        public var height:Number = bind 5 + contentText.layoutBounds.height;
        public override function create():Node {
            var g = Group {
                content: [bullet, contentText];
            }
            return g;

        }


}
