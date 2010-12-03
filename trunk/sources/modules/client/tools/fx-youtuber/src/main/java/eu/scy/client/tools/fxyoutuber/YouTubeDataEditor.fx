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

    public-init var ytNode:YouTuberNode;
    public-init var dataSetID:Integer = -1;
    public-init var ytID:String;
    public-init var title:String;
    public-init var text:String;
    public-init var dataSet:YouTuberDataSet = null;

    var nodes:Node[];
    var oldX = this.translateX;
    var oldY = this.translateY;
    override var children = bind nodes;


    var textLayout:LayoutInfo = LayoutInfo {
        hfill: false;
        width: 220;
        minWidth: 220;
        maxWidth: 220;
    }
    
    var youTubeField:TextBox = TextBox {
        text: bind ytID with inverse;
    }

    var titleField:TextBox = TextBox {
        text: bind title with inverse;
    }

    var textField:TextBox = TextBox {
        multiline: true;
        layoutInfo: textLayout;
        text: bind text with inverse;
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
            this.dataSet = prepareDataSet(ytID, title, text);
            ytNode.updateDataSet(dataSetID, dataSet);
            ytNode.closePopup(this);
        }

    }

    var content = Grid {
        rows: [
            GridRow { cells: [Text { content: "YouTube URL:" }, youTubeField]},
            GridRow { cells: [Text { content: "Title:" }, titleField]},
            GridRow { cells: [Text { content: "Description:" }, textField]},
            GridRow { cells: [null, HBox { content: [cancelButton, okButton] hpos: HPos.RIGHT }] }
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
        if(not (dataSet == null)) {
            this.ytID = dataSet.getYtid();
            this.title = dataSet.getTitle();
            this.text = dataSet.getText();
        }
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


    function prepareDataSet(inputURL:String, title:String, text:String):YouTuberDataSet {
        var url = inputURL;
        if(url.equalsIgnoreCase("")) {
            url = "http://www.youtube.com/watch?v=spn-84Qe9i8";
        }
        var ytid = YouTubeSplitter.split(url);
        var dataSet:YouTuberDataSet = new YouTuberDataSet();

        dataSet.setYtid(ytid);
        dataSet.setText(title);
        dataSet.setText(text);

        return dataSet;

    }

}
