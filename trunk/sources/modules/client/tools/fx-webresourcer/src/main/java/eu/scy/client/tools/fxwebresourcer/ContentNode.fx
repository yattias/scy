/*
 * ContentNode.fx
 *
 * Created on 19.08.2009, 14:32:42
 */

package eu.scy.client.tools.fxwebresourcer;

import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import javafx.scene.shape.Rectangle;


import javafx.scene.input.MouseEvent;
import java.lang.Void;


import eu.scy.client.tools.fxflyingsaucer.BareBonesBrowserLaunch;
import eu.scy.client.tools.fxwebresourcer.highlighter.XMLData;
import javafx.scene.control.Button;
import eu.scy.client.desktop.desktoputils.art.WindowColorScheme;

/**
 * @author pg
 * the content displayed in the scrollview
 */

public class ContentNode extends CustomNode {
    public var title:String;// = "Hi, i am the title!";// on replace {System.out.println("title var replaced")};
    public var comment:String;// = "Sup? This is comment speaking!";
    public var source:String;// =  "/dev/null";
    public var notes:String = "click here to take some notes";
    public var lineWidth:Number = 20;
    public-init var webNode:WebResourceNode;
    var quotes:BulletPoint[];
    var quotesHeight:Number = 0;
    var commentPoints:CommentPoint[];
    var commentsHeight:Number = 0;
    var boxes:CommentBox[];
    public var blockBoxes:Boolean = false;
    var sources:Node[];
    var sourceClickers:Node[];
    var sourcesHeight:Number = 5;
    public var titleText:Text = Text {
        content: bind title;
        font: Font { size: 32; name: "Times New Roman"; }
        translateX: 5;
        translateY: 30;
    }
    var titleLine:Line = Line {
                    startX: 5;
                    startY: titleText.translateY + 10;
                    endX: bind lineWidth;
                    endY: titleText.translateY + 10;
                    strokeWidth: 2
                    stroke: Color.GRAY
    }

    var subtitle:Text = Text {
        content: ##"Quotations";
        font: Font { size: 18; name: "Times New Roman"; }
        translateX: 5;
        translateY: titleLine.startY + 25;
    }

    var commentTitle:Text = Text {
        content: ##"Comments";
        font: Font { size: 18; name: "Times New Roman"; }
        translateX: 5;
        translateY: bind quotesHeight + subtitle.translateY + 30;
    }

    var commentTextAddNew:Text = Text {
        content: "[ + ]";
        font: Font { size: 18; name: "Times New Roman"; }
        translateX: 5 + commentTitle.layoutBounds.width + 25;
        translateY: bind commentTitle.translateY;
    }

    var commentTextAddNewClicker:Rectangle = Rectangle {
        translateX: commentTextAddNew.translateX;
        translateY: bind commentTitle.translateY -  commentTextAddNew.layoutBounds.height;
        height: commentTextAddNew.layoutBounds.height;
        width: commentTextAddNew.layoutBounds.width;
        fill: Color.TRANSPARENT;
        onMouseEntered: function (e:MouseEvent):Void {
            commentTextAddNew.underline = true;
        }
        onMouseExited: function (e:MouseEvent):Void {
            commentTextAddNew.underline = false;
        }
        onMouseReleased: function(e:MouseEvent):Void {
            var box:CommentBox = CommentBox {
                newComment: true;
                target: this;
                translateX: 5;
                translateY: commentTextAddNewClicker.translateY + 25;
            }
            blockBoxes = true;
            insert box into boxes;
        }
    }

    var sourcesTitle:Text = Text {
        content: ##"Sources";
        font: Font { size: 18; name: "Times New Roman"; }
        translateX: 5;
        translateY:  commentTitle.translateY + commentTitle.layoutBounds.height + commentsHeight + 20;
    }
    var sourcesStart:Node = Rectangle {
        translateX: 30;
        translateY: bind sourcesTitle.translateY + 20;
    }

    //this rectangle is used to get rid of some annoying jfx scrollview bugs
    public var testRectangle:Rectangle = Rectangle {
        height: 500;
        width: 50;
        translateY: sourcesTitle.translateY + sourcesTitle.layoutBounds.height;
        fill: bind webNode.scyWindow.windowColorScheme.backgroundColor;
    }

    public-read var height:Number = bind sourcesTitle.translateY + sourcesHeight;

    function updateCommentSizes():Void {
           commentsHeight = 0;
           for(comment in commentPoints) {
               comment.y = commentsHeight + commentTitle.translateY + commentTitle.layoutBounds.height + 5;
               commentsHeight += comment.height + 5;
           }
           sourcesTitle.translateY = commentTitle.translateY + commentTitle.layoutBounds.height + commentsHeight + 25;
           testRectangle.translateY =  sourcesTitle.translateY + sourcesTitle.layoutBounds.height + sourcesHeight;
           webNode.updateScrollbars();
    }

    public function addQuotation(text:String):Void {
        var newBullet:BulletPoint = BulletPoint {
            text: text;
            y: quotesHeight + subtitle.translateY + subtitle.layoutBounds.height + 5;
        }
        //check if we got an image :3
        text.toLowerCase();
        if(text.startsWith("http://") and (text.endsWith(".jpg") or text.endsWith(".png") or text.endsWith(".gif"))) {
            //display an image
            newBullet.setImage(text);
        }
        else {
            newBullet.setText(text);
        }
        insert newBullet into quotes;
        quotesHeight += newBullet.height+5;
    }


    public function addComment(text:String):Void {
            if(not(text == null) or not(text.equals(""))) {
                var commentPoint:CommentPoint = CommentPoint {
                    y: commentsHeight + commentTitle.translateY + commentTitle.layoutBounds.height + 5;
                    contentNode: this;
                }
                commentPoint.setText(text);
                insert commentPoint into commentPoints;
                commentsHeight += commentPoint.height + 5;
             }
    }

    public function showCommentBox(y:Number, point:CommentPoint):Void {
        blockBoxes = true;
        def box:CommentBox = CommentBox {
            content: point.getText();
            translateX: 5;
            translateY: point.y-15;
            newComment: false;
            target: this;
            point: point;
        }
        insert box into boxes;
    }


    public function deleteComment(point:CommentPoint):Void {
        delete point from commentPoints;
        updateCommentSizes();
    }


    public function loadXML(data:XMLData):Void {
        //reset vars to default values
       
        title = data.getTitle();
        for(quote in data.getQuotes()) {
            addQuotation(quote)
        }
        for(comment in data.getComments()) {
            addComment(comment);
            //println("DEBUG: comment: {comment}");
            //println("DEBUG: commentsHeight: {commentsHeight}");
        }
        for(source in data.getSources()) {
            //println("DEBUG: source: {source}");
            //println("DEBUG: sourcesHeight: {sourcesHeight}");
            var temp = sourcesHeight;
            var sourcePoint:BulletPoint = BulletPoint {
                y: bind temp + sourcesTitle.translateY + sourcesTitle.layoutBounds.height -2;
            }
            sourcePoint.setText(source);
            var sourcesClicker:Rectangle = Rectangle {
                translateX: 30;
                translateY: bind sourcePoint.y-sourcePoint.boundsInLocal.height;
                height: bind sourcePoint.boundsInLocal.height;
                width: bind sourcePoint.boundsInLocal.width;
                fill: Color.TRANSPARENT;
                onMouseEntered: function (e:MouseEvent):Void {
                    sourcePoint.contentText.underline = true;
                }
                onMouseExited: function (e:MouseEvent):Void {
                    sourcePoint.contentText.underline = false;
                }
                onMouseReleased: function (e:MouseEvent):Void {
                    BareBonesBrowserLaunch.openURL(source);
                }
            }
            insert sourcePoint into sources;
            insert sourcesClicker into sourceClickers;
            sourcesHeight += sourcePoint.height + 5;
        }
        updateCommentSizes();
    }

    public function closeBox(box:CommentBox):Void {
        delete box from boxes;
        blockBoxes = false;
        updateCommentSizes();
    }

    init {
     }

     public override function create():Node {
        var g = Group {
            content: bind [
                    testRectangle,
                    titleText,
                    titleLine,
                    subtitle,
                    quotes,
                    commentTitle,
                    commentTextAddNew,
                    commentTextAddNewClicker,
                    commentPoints,
                    sourcesTitle,
                    sources,
                    sourceClickers,
                    boxes
                    ];
        };
        return g;
    }

}