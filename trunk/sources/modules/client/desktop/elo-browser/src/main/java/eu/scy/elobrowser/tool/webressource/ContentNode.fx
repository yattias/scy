/*
 * ContentNode.fx
 *
 * Created on 19.08.2009, 14:32:42
 */

package eu.scy.elobrowser.tool.webressource;

import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import javafx.scene.shape.Rectangle;

/**
 * @author pg
 */

public class ContentNode extends CustomNode {
    public var title:String = "Hi, i am the title!";
    public var comment:String = "Sup? This is comment speaking!";
    public var source:String =  "/dev/null";
    var titleText:Text = Text {
        content: bind title;
        font: Font { size: 32; name: "Times New Roman"; }
        translateX: 5;
        translateY: 30;
    }
    var titleLine:Line = Line {
                    startX: 5;
                    startY: titleText.translateY + 10;
                    endX: 1000;
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

    var points:BulletPoint[];
    var pointsHeight:Number;
    var commentTitle:Text = Text {
        content: "Comments";
        font: Font { size: 18; name: "Times New Roman"; }
        translateX: 5;
        translateY: bind pointsHeight + subtitle.translateY + 30;
    }

    var commentText:Text = Text {
        content: bind comment;
        font: Font { size: 14; }
        wrappingWidth: 525;
        translateX: 30;
        translateY: bind commentTitle.translateY + 20;
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

    public function setTtitle(text:String):Void {
        this.title = text;
    }

    public function setComment(text:String):Void {
        this.comment = text;
    }

    public function setSource(text:String):Void {
        this.source = text;
    }

     public override function create():Node {
        //quotation.setContent("Your Web server thinks that the HTTP data stream sent by the client (e.g. your Web browser or our CheckUpDown robot) identifies a URL resource whose actual media type 1) does not agree with the media type specified on the request or 2) is incompatible with the current data for the resource or 3) is incompatible with the HTTP method specified on the request. Detecting exactly what is causing this problem can be difficult, because there a number of possible reasons. Often the request involves transfer of data from the client to the Web server (e.g. a file upload via the PUT method), in which case you need to confirm with your ISP which media types are acceptable for upload. ");
        var g = Group {
              //blocksMouse: true;
            content: bind [
                    titleText,
                    titleLine,
                    subtitle,
                    points,
                    commentTitle,
                    commentText,
                    sourcesTitle,
                    sourcesText,
                    testRectangle];
        };
        return g;
    }

}
