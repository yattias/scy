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

/**
 * @author pg
 */

public class YouTubeDataEditor extends CustomNode {
    var nodes:Node[];
    override var children = bind nodes;

    var youTubeField:TextBox = TextBox {
    }

    var titleField:TextBox = TextBox {
        
    }

    var textField:TextBox = TextBox {
        multiline: true;
    }
    var standardLayout:LayoutInfo = LayoutInfo {
                    hfill: false;
                    minWidth: 500
                    width: 500
                    maxWidth: 500
                }
    var textLayout:LayoutInfo = LayoutInfo {
        hfill: false;
        width: 220;
        minWidth: 220;
        maxWidth: 220;
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
            GridRow { cells: [Text { content: "YouTube URL:" }, TextBox {}]},
            GridRow { cells: [Text { content: "Title:" }, TextBox {}]},
            GridRow { cells: [Text { content: "Description:" }, TextBox {}]}
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
    }


    postinit {
         insert [backgroundRectangle, content] into nodes;
         this.translateX = 15;
         this.translateY = 15;
    }

 



}
