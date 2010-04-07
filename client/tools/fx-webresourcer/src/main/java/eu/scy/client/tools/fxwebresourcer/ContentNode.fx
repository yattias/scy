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

import javafx.ext.swing.SwingComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import javafx.scene.control.Button;


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
    var points:BulletPoint[];
    var pointsHeight:Number;
    var isEditorDisplayed:Boolean = false;
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
        content: "Quotations";
        font: Font { size: 18; name: "Times New Roman"; }
        translateX: 5;
        translateY: titleLine.startY + 25;
    }

    var commentTitle:Text = Text {
        content: "Comments";
        font: Font { size: 18; name: "Times New Roman"; }
        translateX: 5;
        translateY: bind pointsHeight + subtitle.translateY + 20;
    }

    var commentText:Text = Text {
        content: bind comment;
        font: Font { size: 14; }
        wrappingWidth: 525;
        translateX: 30;
        translateY: bind commentTitle.translateY + 20;
    }


    var commentClicker:Rectangle = Rectangle {
        translateX: 5;
        translateY: bind commentText.translateY-11;
        height: bind commentText.layoutBounds.height;
        width: 555;
        fill: Color.TRANSPARENT;
        //stroke: Color.BLACK;
        onMouseReleased:function(e:MouseEvent):Void {
            //show textArea
            if(not isEditorDisplayed) {
                showNotesEditor();
            }
        }
    }

    // urg... rather much stuff for the textarea goes here
    var editorPane = new JTextPane();
    var editorScroll = new JScrollPane(editorPane);
    var commentEditor: SwingComponent = SwingComponent.wrap(editorScroll) ;

    var commentUpdateButton:Button = Button {
        text: "update";
        translateX: 500;
        translateY: bind commentClicker.translateY;
        visible: false;
        onMouseReleased:function(e:MouseEvent):Void {
            updateComments();
        }
    }

    var sourcesTitle:Text = Text {
        content: "Sources";
        font: Font { size: 18; name: "Times New Roman"; }
        translateX: 5;
        translateY: bind commentText.translateY + commentText.layoutBounds.height + 20;
    }

    var sourcesText:Text = Text {
        content: bind source;
        font: Font { size: 14; }
        wrappingWidth: 525;
        translateX: 30;
        translateY: bind sourcesTitle.translateY + 20;
    }

    public var testRectangle:Rectangle = Rectangle {
        height: 50;
        width: 50;
        translateY: bind sourcesText.translateY + sourcesText.layoutBounds.height;
        fill: Color.TRANSPARENT;
    }

    public-read var height:Number = bind sourcesText.layoutBounds.height + sourcesText.translateY;
    
    public function addQuotation(text:String):Void {
        var newBullet:BulletPoint = BulletPoint {
            text: text;
            y: pointsHeight + subtitle.translateY + subtitle.layoutBounds.height + 5;
        }
        insert newBullet into points;
        pointsHeight += newBullet.height+5;
    }

    public function clearQuotations():Void {
        pointsHeight = 0;
        delete points;
    }

    public function setTitle(text:String):Void {
        this.title = text;
    }

    public function setComment(text:String):Void {
        this.comment = text;

    }

    public function setSource(text:String):Void {
        this.source = text;
    }

    function showNotesEditor():Void {
            commentEditor.translateX = commentClicker.translateX;
            commentEditor.translateY = commentClicker.translateY;
            commentEditor.height = commentClicker.height + 10;
            commentEditor.width = commentClicker.width - 75;
            editorPane.setText(comment);
            commentText.visible = false;
            commentEditor.visible = true;
            commentUpdateButton.visible = true;
            isEditorDisplayed = true;
    }

    function updateComments():Void {
            comment = editorPane.getText();
            commentText.visible = true;
            commentUpdateButton.visible = false;
            commentEditor.visible = false;
            isEditorDisplayed = false;
            webNode.updateScrollbars();
    }


    function initCommentsEditor() {
        commentEditor.visible = false;
        //editorPane.setBackground(new java.awt.Color(0,0,0,1)); //0% opacity
        editorPane.setBackground(java.awt.Color.WHITE);
        editorPane.setForeground(java.awt.Color.RED);
        editorPane.setCaretColor(java.awt.Color.BLUE);
        editorScroll.setOpaque(false);
//        editorScroll.setBorder(setBorder(new EmptyBorder(new Insets(0,0,0,0))));
    }

    init {
        initCommentsEditor();
     }

     public override function create():Node {
        var g = Group {
            content: bind [ 
                    titleText,
                    titleLine,
                    subtitle,
                    points,
                    commentTitle,
                    commentText,
                    commentClicker,
                    commentEditor,
                    commentUpdateButton,
                    sourcesTitle,
                    sourcesText,
                    testRectangle];
        };
        return g;
    }

}
