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
import java.net.URI;
import javafx.scene.CustomNode; 
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.lang.Math;
 
/**
 * @author pg
 */

public class PictureViewerNode extends CustomNode {

    var title: String;
    var description: String;
    var author: String;
    var dateCreatedString: String;

    public var img: Image = Image{
        url: "{__DIR__}image.jpg";
    }

    public-init var eloPictureActionWrapper:EloPictureWrapper;
    public var scyWindow: ScyWindow on replace {
        setScyWindowTitle();
    };

    public function loadElo(uri:URI) {
            eloPictureActionWrapper.loadElo(uri);
        setScyWindowTitle();
    }

    function setScyWindowTitle() {
        if(scyWindow == null)
        return
        scyWindow.title = "Pictureviewer";
        var eloUri = eloPictureActionWrapper.getEloUri();
        if(eloUri != null)
        scyWindow.id = eloUri.toString()
        else
        scyWindow.id = "";


    }

    var viewer: ImageView = ImageView {
        image: img;
        translateX: 5;
        translateY: 25;
        
    }
    var strokeRectangle: Rectangle = Rectangle {
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
        translateX: 85;
        translateY: strokeRectangle.height + strokeRectangle.translateY + 15;
    }

    var authorTextTitle: Text = Text {
        content: "Author: ";
        translateX: 5;
        translateY: descriptionText.layoutBounds.height + descriptionText.translateY + 15;;
        style: "font-weight: bold"
    };

    var authorText: Text = Text {
        content: bind author;
        translateX: 85;
        translateY: descriptionText.layoutBounds.height + descriptionText.translateY + 15;
    }

    var dateCreatedTextTitle: Text = Text {
        content: "Creation Date: ";
        translateX: 5;
        translateY: authorText.layoutBounds.height + authorText.translateY + 15;;
        style: "font-weight: bold"
    };

    var dateCreatedText: Text = Text {
        content: bind dateCreatedString;
        translateX: 85;
        translateY: authorText.layoutBounds.height + authorText.translateY + 15;
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
                descriptionText,
                authorTextTitle,
                authorText,
                dateCreatedTextTitle,
                dateCreatedText
            ]
        };
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
    //start picturewieverwrapper
    var eloPictureActionWrapper = new EloPictureWrapper();
    eloPictureActionWrapper.setRepository(roolo.repository);
    eloPictureActionWrapper.setMetadataTypeManager(roolo.metadataTypeManager);
    eloPictureActionWrapper.setEloFactory(roolo.eloFactory);
    eloPictureActionWrapper.loadPictureAction();
    return PictureViewerNode {
        eloPictureActionWrapper: eloPictureActionWrapper;
        img: eloPictureActionWrapper.getImage();
        title: eloPictureActionWrapper.getTitle();
        description: eloPictureActionWrapper.getDescription();
        author: eloPictureActionWrapper.getAuthor();
        dateCreatedString: eloPictureActionWrapper.getDateCreatedString();
    };
}


public function createPictureViewerWindow(roolo:Roolo):ScyWindow{
    return createPictureViewerWindow(PictureViewerNode.createPictureViewerNode(roolo));
}

public function createPictureViewerWindow(pictureViewerNode:PictureViewerNode):ScyWindow {
    var pictureWindow = ScyWindow{
        color: Color.PINK
        title: "PictureViewer"
        scyContent: pictureViewerNode;
        cache: true;
    }
    var height = pictureViewerNode.dateCreatedText.translateY + 50;
    var width = pictureViewerNode.img.width;
    width = Math.max(width, pictureViewerNode.titleText.boundsInParent.maxX);
    width = Math.max(width, pictureViewerNode.descriptionText.boundsInParent.maxX);
    width = Math.max(width, pictureViewerNode.authorText.boundsInParent.maxX);
    width = Math.max(width, pictureViewerNode.dateCreatedText.boundsInParent.maxX);
    width += 30;
    pictureWindow.openWindow(width,height);

    pictureViewerNode.scyWindow = pictureWindow;
    return pictureWindow;
    
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
