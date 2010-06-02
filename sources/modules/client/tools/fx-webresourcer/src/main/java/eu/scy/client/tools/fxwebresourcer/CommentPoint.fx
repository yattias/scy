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
import javafx.scene.control.Button;
import javafx.scene.shape.Rectangle;
import javafx.scene.input.MouseEvent;

/**
 * @author pg
 */

public class CommentPoint extends CustomNode {

        public var text:String;
        public var y:Number;
        public var x:Number;
        public-init var contentNode:ContentNode;
        
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

        var commentClicker:Rectangle = Rectangle {
                translateX: 30;
                translateY: bind y-15;
                height: bind this.height;
                width: 575;
                fill: Color.TRANSPARENT;
                onMouseEntered: function(e:MouseEvent):Void {
                    if(not contentNode.blockBoxes) {
                        deleteButton.visible = true;
                        contentText.underline = true;
                    }
                }
                onMouseExited: function(e:MouseEvent):Void {
                    deleteButton.visible = false;
                    contentText.underline = false;
                }
                onMouseReleased: function(e:MouseEvent):Void {
                        if(not contentNode.blockBoxes) {
                            contentNode.showCommentBox(y, this);
                            deleteButton.visible = false;
                        }
                }
        }

        var deleteButton:Button = Button {
                text: ##"delete";
                visible: false;
                translateX: 510;
                translateY: bind y;
                action: function():Void {
                    contentNode.deleteComment(this);
                }
                onMouseEntered: function(e:MouseEvent):Void {
                    deleteButton.visible = true;
                }
                onMouseExited: function(e:MouseEvent):Void {
                    deleteButton.visible = false;
                }
        }



        public var width:Number;// = bind 28 + contentText.layoutBounds.width;
        public var height:Number;// = bind 5 + contentText.layoutBounds.height;

        public function setText(text:String) {
            this.text = text;
            height = 5 + contentText.layoutBounds.height;
            width = 28 + contentText.layoutBounds.width;
            /*
            if(not contentIsSet) {
                insert contentText into nodes;
                contentIsSet = true;
            }
            */
        }

        public function getText():String {
            return this.text;
        }


        public override function create():Node {

            var g = Group {
                content: [
                        bullet,
                        contentText,
                        commentClicker,
                        deleteButton
                        ]//bind nodes; //[bullet, contentText, contentImageView];
            }
            return g;

        }


}
