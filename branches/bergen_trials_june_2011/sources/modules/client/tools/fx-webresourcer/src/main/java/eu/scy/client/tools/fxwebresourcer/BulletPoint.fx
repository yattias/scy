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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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
        var nodes:Node[] = { bullet };
        var contentIsSet:Boolean = false;
        public var contentText:Text = Text {
                content:bind text;
                font: Font { size: 16; }
                wrappingWidth: 525;
                translateX: 30;
                translateY: bind y;
        }

        public var contentImage:Image = Image {

        }

        var contentImageView:ImageView = ImageView {
                preserveRatio: true;
                translateX: 30;
                translateY: bind y;
        }

        public var width:Number;// = bind 28 + contentText.layoutBounds.width;
        public var height:Number;// = bind 5 + contentText.layoutBounds.height;

        public function setText(text:String) {
            this.text = text;
            height = 5 + contentText.layoutBounds.height;
            width = 28 + contentText.layoutBounds.width;
            if(not contentIsSet) {
                insert contentText into nodes;
                contentIsSet = true;
            }
        }

        public function setImage(url:String) {
            contentImage = Image {
                url: url;
            }
            if(contentImage.width > 500) {
                contentImageView.fitWidth = 500;
            }
            else if(contentImage.height > 500) {
                contentImageView.fitHeight = 500;
            }


            contentImageView.image = contentImage;
            height = 5 + contentImageView.layoutBounds.height+15;
            width = 5 + contentImageView.layoutBounds.width;
            insert contentImageView into nodes;

        }

        public function getText():String {
            return this.text;
        }


        public override function create():Node {
            var g = Group {
                content: bind nodes; //[bullet, contentText, contentImageView];
            }
            return g;

        }


}
