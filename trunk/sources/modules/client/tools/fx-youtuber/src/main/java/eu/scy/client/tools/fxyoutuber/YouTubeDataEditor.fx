/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.fxyoutuber;
import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.control.TextBox;
import javafx.scene.layout.LayoutInfo;
import com.javafx.preview.layout.Grid;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import com.javafx.preview.layout.GridRow;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.geometry.HPos;
import javafx.scene.input.MouseEvent;

/**
 * @author pg
 */

public class YouTubeDataEditor extends CustomNode {
    var nodes:Node[];
    override var children = bind nodes;
    public-init var ytNode:YouTuberNode;
    var oldX = this.translateX;
    var oldY = this.translateY;

    var textLayout:LayoutInfo = LayoutInfo {
        hfill: false;
        width: 220;
        minWidth: 220;
        maxWidth: 220;
    }
    
    var youTubeField:TextBox = TextBox {
    }

    var titleField:TextBox = TextBox {
        
    }

    var textField:TextBox = TextBox {
        multiline: true;
        layoutInfo: textLayout;
    }
    var standardLayout:LayoutInfo = LayoutInfo {
                    hfill: false;
                    minWidth: 500
                    width: 500
                    maxWidth: 500
                }

    var cancelButton:Button = Button {
        text: "cancel"
        action: function():Void {
            ytNode.closePopup(this);
        }

    }

    var okButton:Button = Button {
        text: "ok"
        action:function():Void {
            ytNode.addDataSet(youTubeField.text, titleField.text, textField.text);
            ytNode.closePopup(this);
        }

    }




/*
    var content = HBox {
        content: [
            VBox {
                content: [
                    Text {content: "YouTube URL:", layoutInfo: textLayout},
                    Text { content: "Title:", layoutInfo: textLayout },
                    Text { content: "Description:", layoutInfo: textLayout }
                ]
            },
            VBox {
                content: [
                    youTubeField,
                    titleField,
                    textField
                ]
            }
        ]
        translateX: 10;
        translateY: 10;
    }
 }
*/

    var content = Grid {
        rows: [
            GridRow { cells: [Text { content: "YouTube URL:" }, youTubeField]},
            GridRow { cells: [Text { content: "Title:" }, titleField]},
            GridRow { cells: [Text { content: "Description:" }, textField]},
            GridRow { cells: [Text{}, HBox { content: [cancelButton, okButton] hpos: HPos.RIGHT }] }
        ]
        translateX: 10;
        translateY: 10;
    }

    var backgroundRectangle:Rectangle = Rectangle {
        height: bind content.layoutBounds.height+20;
        width: bind content.layoutBounds.width+20;
        fill: Color.WHITE;
        arcWidth: 8;
        arcHeight: 8;
        stroke: Color.BLACK;
    }


    postinit {
         insert [backgroundRectangle, content] into nodes;
         this.translateX = 15;
         this.translateY = 15;
         this.blocksMouse = true;
    }

    override var onMousePressed = function(e:MouseEvent):Void {
        oldX = this.translateX;
        oldY = this.translateY;
    }

    override var onMouseDragged = function(e:MouseEvent):Void {
        this.translateX = oldX + e.dragX;
        this.translateY = oldY + e.dragY;
    }

    /*
    override var onMouseReleased = function(e:MouseEvent):Void {
        oldX = this.translateX;
        oldY = this.translateY;
    }
    */


}
