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
import eu.scy.client.tools.fxflyingsaucer.BareBonesBrowserLaunch;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

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
            println("dataset == null. cannot work w/o data.");

        }
        if(dataSetID == -1) {
            println("dataSetID == null. cannot work w/o my dataSetId! I miss it soo much :o(");
        }
        loadData();
        insert backgroundRectangle into nodes;
        insert content into nodes;
    }


    var titleText:Text = Text {
        content: bind title;
    }

    var textText:Text = Text {
        content: bind text;
    }

    var launchButton:Button = Button {
        text: "watch on YouTube.com";
        action:function():Void {
            def url = "http://www.youtube.com/watch?v={ytID}";
            BareBonesBrowserLaunch.openURL(url);
        }
    }

    var backgroundRectangle:Rectangle = Rectangle {
        height: bind content.layoutBounds.height+20;
        width: bind content.layoutBounds.width+20;
        fill: Color.WHITE;
        arcWidth: 8;
        arcHeight: 8;
        stroke: Color.BLACK;
    }


    var topLine:HBox = HBox {
        content: [titleText, launchButton]
    }

    var bottomLine:HBox = HBox {
        content: [textText]
    }

    var content:VBox = VBox {
        content: [ topLine, bottomLine]
    }


    function loadData() {
        ytID = dataSet.getYtid();
        title = dataSet.getTitle();
        text = dataSet.getText();
        
    }





}