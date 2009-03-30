/*
 * PictureViewerNode.fx
 *
 * Created on 30.03.2009, 13:12:38
 */

package eu.scy.elobrowser.tool.pictureviewer;

import eu.scy.elobrowser.main.Roolo;
import eu.scy.elobrowser.tool.pictureviewer.PictureViewerNode;
import eu.scy.scywindows.ScyWindow;
import java.lang.Object;
import java.lang.System;
import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * @author pg
 */

public class PictureViewerNode extends CustomNode {

    var title: String = "a great picture.";
    var description: String = "bla bla bla\nbla blala bla";
    public var img: Image = Image{
        url: "{__DIR__}image.jpg";
    }
    var viewer: ImageView = ImageView {
        image: img;
        translateX: 5;
        translateY: 25;
        
    }
    public var strokeRectangle: Rectangle = Rectangle {
        height: bind img.height + 2;
        width: bind img.width + 2;
        translateX: bind viewer.translateX - 1;
        translateY: bind viewer.translateY - 1;
        stroke: Color.BLACK;
        fill: Color.TRANSPARENT;

    }

    var titleTextTitle: Text = Text {
        content: "Title: ";
        translateX: 5;
        translateY: 15;
        style: "font-weight: bold"
    };


    var titleText: Text = Text {
        content: bind title;
        translateX: 32;
        translateY: 15;
    }

    var descriptionTextTitle: Text = Text {
        content: "Description: ";
        translateX: 5;
        translateY: strokeRectangle.height + strokeRectangle.translateY + 15;;
        style: "font-weight: bold"
    };

    var descriptionText: Text = Text {
        content: bind description;
        translateX: 77;
        translateY: strokeRectangle.height + strokeRectangle.translateY + 15;
    }



    public override function create():Node {
        var g = Group {
            //blocksMouse: true;
            content: [
                strokeRectangle,
                titleTextTitle,
                titleText,
                viewer,
                descriptionTextTitle,
                descriptionText
            ]
        };
        System.out.println("..");
        System.out.println(img);
        System.out.println(img.url);
        return g;
    }

    public function setTitle(title:String):Void {
        this.title = title;
    }

    public function setDescription(description:String):Void {
        this.description = description;
    }

    public function setImage(url:String):Void {
        var newImage = Image {
            url: url;
        }
        viewer.image = newImage;
    }

}

public function createPictureViewerNode(roolo:Roolo):PictureViewerNode {
    return PictureViewerNode {
    };
}


public function createPictureViewerWindow(roolo:Roolo):ScyWindow{
    return createPictureViewerWindow(PictureViewerNode.createPictureViewerNode(roolo));
}

public function createPictureViewerWindow(pictureViewerNode:PictureViewerNode):ScyWindow {
    var textpadWindow = ScyWindow{
        color: Color.PINK
        title: "PictureViewer"
        scyContent: pictureViewerNode;
        cache: true;
    }
    
}

function run(){
    Stage {
        title: "PictureViewer"
        width: 600
        height: 600
        scene: Scene {
            content: PictureViewerNode{
            }
        }
    }
}
