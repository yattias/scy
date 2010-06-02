/*
 * CommentBox.fx
 *
 * Created on 29.04.2010, 13:12:50
 */

package eu.scy.client.tools.fxwebresourcer;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.Group;
import javafx.ext.swing.SwingComponent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javafx.scene.text.Text;
import javafx.scene.text.Font;

/**
 * @author pg
 */

public class CommentBox extends CustomNode {
    public-init var target:ContentNode;
    public-init var newComment:Boolean = false;
    public-init var content:String;
    public-init var commentID:Number = -1;
    public-init var point:CommentPoint;
    var editorPane = new JTextPane();
    var editorScroll = new JScrollPane(editorPane);
    var commentEditor: SwingComponent = SwingComponent.wrap(editorScroll) ;

    var commentUpdateButton:Button = Button {
        text: ##"update";
        translateX: 500;
        //translateY: bind commentClicker.translateY;
        visible: false;
        onMouseReleased:function(e:MouseEvent):Void {
            //target.boxReply(editorPane.getText(), newComment, commentID);
            //target.boxReply(editorPane.getText(), newComment, point);
            if(newComment) {
                target.addComment(editorPane.getText());
            }
            else {
                point.setText(editorPane.getText());
            }
            target.closeBox(this);
        }
    }

    function initCommentsEditor() {
        commentEditor.visible = false;
        //editorPane.setBackground(new java.awt.Color(0,0,0,1)); //0% opacity
        editorPane.setBackground(java.awt.Color.WHITE);
        editorPane.setForeground(java.awt.Color.RED);
        editorPane.setCaretColor(java.awt.Color.BLUE);
        editorScroll.setOpaque(false);
        //editorScroll.setBorder(setBorder(new EmptyBorder(new Insets(0,0,0,0))));
    }

    init {
        initCommentsEditor();
        if(newComment) {
            commentEditor.height = 75;
            commentEditor.width = 475;
        }
        else {
            def sizeTest:Text = Text {
                font: Font { size: 16; }
                content: this.content;
                wrappingWidth: 525;
            }
            commentEditor.translateX = 0;
            commentEditor.translateY = 0;
            commentEditor.height = sizeTest.layoutBounds.height + 15;
            commentEditor.width = 475;
            //textfeld befüllen!
            editorPane.setText(this.content);
        }
        commentEditor.visible = true;
        commentUpdateButton.visible = true;
    }

    override protected function create () : Node {
            var g:Group = Group {
                content: bind [
                        commentEditor,
                        commentUpdateButton
                        ]
            }
            return g;
    }
}
