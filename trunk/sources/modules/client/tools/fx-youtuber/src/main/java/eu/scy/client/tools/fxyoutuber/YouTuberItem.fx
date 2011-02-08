/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.fxyoutuber;
import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import eu.scy.client.tools.fxflyingsaucer.BareBonesBrowserLaunch;

/**
 * @author frogbox-desktop
 */

public class YouTuberItem extends CustomNode {
    public-init var dataSetID:Integer = -1;
    public-init var dataSet:YouTuberDataSet;
    public-init var ytNode:YouTuberNode;

    var title:String;
    var text:String;
    var ytID:String;

    var nodes:Node[];

    override var children = bind nodes;


    postinit {
        if(dataSet == null) {
            //println("dataset == null. cannot work w/o data.");

        }
        if(dataSetID == -1) {
            //println("dataSetID == null. cannot work w/o my dataSetId! I miss it soo much :o(");
        }
        loadData();
        insert backgroundRectangle into nodes;
        insert content into nodes;
    }


    var titleText:Text = Text {
        content: bind title;
        font: ytNode.titleFont;
        wrappingWidth: bind ytNode.width - 265;
        /*
        layoutInfo: LayoutInfo {
            maxWidth: bind ytNode.scyWindow.width - 200;
        }
        */
    }

    var textText:Text = Text {
        content: bind text;
        wrappingWidth: bind ytNode.width - 55;
        font: ytNode.textFont;
    }

    var launchButton:Button = Button {
        tooltip: Tooltip {text: "watch on YouTube.com"}
        graphic: ImageView{ image: Image { url: "{__DIR__}resources/watchonYT.png"  } fitHeight: 16; preserveRatio: true;}
        action:function():Void {
            def url = "http://www.youtube.com/watch?v={ytID}";
            BareBonesBrowserLaunch.openURL(url);
        }
    }

    var editButton:Button = Button {
        tooltip: Tooltip {text: "edit"}
        graphic: ImageView{ image: Image { url: "{__DIR__}resources/cog_edit.png"  } fitHeight: 16; preserveRatio: true;}
        action:function():Void {
            ytNode.showPopup(YouTubeDataEditor {
                ytNode: this.ytNode;
                dataSetID: this.dataSetID;
            });
        }

    }


    var deleteButton:Button = Button {
        tooltip: Tooltip {text: "delete" }
        graphic: ImageView{ image: Image { url: "{__DIR__}resources/delete.png"  } fitHeight: 16; preserveRatio: true;}
        action:function():Void {
            ytNode.deleteItem(this.dataSetID);
        }
    }



    var backgroundRectangle:Rectangle = Rectangle {
        height: bind content.layoutBounds.height+20;
        width: bind ytNode.width - 30;
        fill: Color.WHITE;
        //arcWidth: 8;
        //arcHeight: 8;
        stroke: Color.BLACK;
    }


    var topLine:HBox = HBox {
        content: [titleText, launchButton, editButton, deleteButton]
        spacing: 10.0;
    }

    var bottomLine:HBox = HBox {
        content: [textText]
    }

    var content:VBox = VBox {
        content: [ topLine, bottomLine]
        translateX: 10;
        translateY: 10;
    }


    function loadData() {
        ytID = dataSet.getYtid();
        title = dataSet.getTitle();
        text = dataSet.getText();

    }

}
