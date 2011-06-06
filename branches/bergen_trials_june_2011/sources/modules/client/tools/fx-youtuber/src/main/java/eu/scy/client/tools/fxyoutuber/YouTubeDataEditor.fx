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
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.DialogBox;

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
        tooltip: Tooltip {text: "cancel"}
        graphic: ImageView{ image: Image { url: "{__DIR__}resources/cancel.png" } }
        action: function():Void {
            ytNode.closePopup(this);
        }

    }

    var okButton:Button = Button {
        tooltip: Tooltip {text: "ok"}
        graphic: ImageView{ image: Image { url: "{__DIR__}resources/tick.png" } }
        action:function():Void {
            validateAndInsertData();
        }

    }

    var content = Grid {
        rows: [
            GridRow { cells: [Text { content: "YouTube URL:" font: ytNode.titleFont; }, youTubeField]},
            GridRow { cells: [Text { content: "Title:" font: ytNode.titleFont;  }, titleField]},
            GridRow { cells: [Text { content: "Description:" font: ytNode.titleFont;  }, textField]},
            GridRow { cells: [Text{}, HBox { content: [cancelButton, okButton] hpos: HPos.RIGHT }] }
        ]
        translateX: 10;
        translateY: 10;
    }

    var backgroundRectangle:Rectangle = Rectangle {
        height: bind content.layoutBounds.height+20;
        width: bind content.layoutBounds.width+20;
        fill: Color.WHITE;
        //arcWidth: 8;
        //arcHeight: 8;
        stroke: Color.BLACK;
    }



    postinit {
        if(dataSetID > -1) {
            dataSet = ytNode.getDataSet(dataSetID);
            this.ytID = dataSet.getYtid();
            this.title = dataSet.getTitle();
            this.text = dataSet.getText();
        }

        insert [backgroundRectangle, content] into nodes;
        this.blocksMouse = true;
    }

    /*
    override var onMousePressed = function(e:MouseEvent):Void {
        oldX = this.translateX;
        oldY = this.translateY;
    }

    override var onMouseDragged = function(e:MouseEvent):Void {
        this.translateX = oldX + e.dragX;
        this.translateY = oldY + e.dragY;
    }
    */

    function prepareDataSet(inputURL:String, title:String, text:String):YouTuberDataSet {
        var dataSet:YouTuberDataSet = new YouTuberDataSet();
        //var url = inputURL;
        /*
        if(url.equalsIgnoreCase("")) {
            url = "http://www.youtube.com/watch?v=spn-84Qe9i8";
        }
        var ytid = YouTubeSplitter.split(url);
        var dataSet:YouTuberDataSet = new YouTuberDataSet();
        if(ytid.equals("")) {
            dataSet.setYtid("spn-84Qe9i8");
        }
        else {
            dataSet.setYtid(ytid);
        }
        */
        dataSet.setYtid(inputURL);
        if(title.equals("")) {
            dataSet.setTitle("no title");
        }
        else {
            dataSet.setTitle(title);
        }
        if(text.equals("")) {
            dataSet.setText("no description");
        }
        else {
            dataSet.setText(text);
        }
        return dataSet;

    }

    function validateAndInsertData():Void {
            if(ytID.equalsIgnoreCase("")) {
                DialogBox.showMessageDialog("Please enter a valid Youtube URL or ID.","Error", ytNode.scyDesktop , function(){}, "youtuber");
                return;
            }
            var myYtID = YouTubeSplitter.split(ytID);
            /*
            //the code below is crap. i need to find a better way to validate input
            if(myYtID.equals("-1")) {
                JOptionPane.showMessageDialog(null, "Please enter a valid Youtube URL or ID.");
                return;
            }
            */
            this.dataSet = prepareDataSet(myYtID, title, text);
            ytNode.updateDataSet(dataSetID, dataSet);
            ytNode.closePopup(this);
    }


}
