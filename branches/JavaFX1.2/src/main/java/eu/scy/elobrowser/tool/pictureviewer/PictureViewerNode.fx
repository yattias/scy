/*
 * PictureViewerNode.fx
 *
 * Created on 30.03.2009, 13:12:38
 */

package eu.scy.elobrowser.tool.pictureviewer;

import eu.scy.elobrowser.tool.pictureviewer.EloPictureWrapper;
import eu.scy.elobrowser.tool.pictureviewer.PictureViewerNode;
import eu.scy.scywindows.ScyWindow;
import java.lang.Object;
import java.net.URI;
import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;


import javafx.ext.swing.SwingButton;

import javafx.scene.Scene;
import javafx.stage.Stage;
import eu.scy.elobrowser.main.Roolo;
import java.lang.Math;

import eu.scy.elobrowser.tool.pictureviewer.map.MapWrapper;

import javafx.scene.input.MouseEvent;
 
/**
 * @author pg
 */

public class PictureViewerNode extends CustomNode {

    public var title: String;
    public var description: String;
    public var author: String;
    public var dateCreatedString: String;

    public var img: Image = Image{
        url: "{__DIR__}image.jpg";
    }

    public-init var eloPictureActionWrapper: EloPictureWrapper;
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
        image: bind img;
      //  fitWidth: bind if(img.width > img.height) { 500 } else { 500 * img.height / img.width };
      //  fitHeight: bind if(img.width < img.height) { 500 * img.width / img.height} else { 500 };
        preserveRatio: true;
        translateX: 5;
        translateY: 25;
    }

    var strokeRectangle: Rectangle = Rectangle {
        stroke: Color.BLACK;
        strokeWidth: 2;
        height: bind viewer.image.height;
        width: bind viewer.image.width;
        translateX: bind viewer.translateX;
        translateY: bind viewer.translateY;
        fill: Color.TRANSPARENT;
        

    }

    var titleTextTitle: Text = Text {
        content: "Title: ";
        translateX: 5;
        translateY: 15;
        font: Font.font("", FontWeight.BOLD, 12);
    };


    var titleText: Text = Text {
        content: bind title;
        translateX: 5;
        translateY: 15;
        font: Font.font("", FontWeight.BOLD, 18);
    }

    var descriptionTextTitle: Text = Text {
        content: "Description: ";
        translateX: 5;
        translateY: bind strokeRectangle.height + strokeRectangle.translateY + 15;
        font: Font.font("", FontWeight.BOLD, 12);
    };

    var descriptionText: Text = Text {
        content: bind description;
        translateX: 85;
        translateY: bind descriptionTextTitle.translateY;
    }

    var authorTextTitle: Text = Text {
        content: "Author: ";
        translateX: 5;
        translateY: bind descriptionText.translateY + 13;
        font: Font.font("", FontWeight.BOLD, 12);
    };

    var authorText: Text = Text {
        content: bind author;
        translateX: 85;
        translateY: bind authorTextTitle.translateY;
    }

    var dateCreatedTextTitle: Text = Text {
        content: "Creation Date: ";
        font: Font.font("", FontWeight.BOLD, 12);
        translateX: 5;
        translateY: bind authorText.translateY + 13;
    };
    var dateCreatedText: Text = Text {
        content: bind dateCreatedString;
        translateX: 93;
        translateY: bind dateCreatedTextTitle.translateY;
    };

//    var openMap:SwingButton = SwingButton {
//        text: "show location on map";
//        translateX: 5;
//        translateY: bind dateCreatedTextTitle.translateY;
//        onMousePressed: function(e:MouseEvent):Void {
//            mapWrapper.showMap();
//        }
//
//    };

    var mapWrapper:MapWrapper = MapWrapper{};
    var worldMap: ImageView = ImageView {
        image: Image{
            url: "{__DIR__}worldmap.png";
        }
        translateX: 5;
        translateY: bind dateCreatedTextTitle.translateY+13;
        onMousePressed: function(e:MouseEvent):Void {
            mapWrapper.showMap();
        }
    }
 

    init {
        mapWrapper.addPosition(51.427783,6.800172, "UDE SCY Headquarters");
        mapWrapper.centerView(51.427783,6.800172);
//        if((img.width < 500) and (img.height < 500)) {
//            if(img.width > img.height) {
//                viewer.fitWidth = 500;
//                viewer.fitHeight = 500 * img.height / img.width;
//            }
//            else {
//                viewer.fitHeight = 500;
//                viewer.fitWidth = 500 * img.width / img.height;
//            }
//        }

    }


    public override function create():Node {
//       if((img.width < 500) and (img.height < 500)) {
//            if(img.width > img.height) {
//                viewer.fitWidth = 500;
//                viewer.fitHeight = 500 * img.height / img.width;
//            }
//            else {
//                viewer.fitHeight = 500;
//                viewer.fitWidth = 500 * img.width / img.height;
//            }
//        }

        var g = Group {
              //blocksMouse: true;
            content: [
                  //FIXME doesnt work -> outcommented
                //strokeRectangle,
//                titleTextTitle,
                titleText,
                viewer,
                descriptionTextTitle,
                descriptionText,
                authorTextTitle,
                authorText,
                dateCreatedTextTitle,
                dateCreatedText,
                strokeRectangle,
                worldMap,
                mapWrapper
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
        if((img.width < 500) and (img.height < 500)) {
            if(img.width > img.height) {
                viewer.fitWidth = 500;
                viewer.fitHeight = 500 * img.height / img.width;
            }
            else {
                viewer.fitHeight = 500;
                viewer.fitWidth = 500 * img.width / img.height;
            }
        }

    }

}


public function createPictureViewerNode(roolo:Roolo, eloUri: URI):PictureViewerNode {
    //start picturewieverwrapper
    var eloPictureActionWrapper = new EloPictureWrapper();
    eloPictureActionWrapper.setRepository(roolo.repository);
    eloPictureActionWrapper.setMetadataTypeManager(roolo.metadataTypeManager);
    eloPictureActionWrapper.setEloFactory(roolo.eloFactory);
    if (eloUri == null) {
        eloPictureActionWrapper.loadPictureAction();
    } else {
        eloPictureActionWrapper.loadElo(eloUri);
    }
    return PictureViewerNode {
        eloPictureActionWrapper: eloPictureActionWrapper;
        img: eloPictureActionWrapper.getImage();
        title: eloPictureActionWrapper.getTitle();
        description: eloPictureActionWrapper.getDescription();
        author: eloPictureActionWrapper.getAuthor();
        dateCreatedString: eloPictureActionWrapper.getDateCreatedString();
    };
}

public function createPictureViewerNode(roolo:Roolo):PictureViewerNode {
    return createPictureViewerNode(roolo, null);
}


public function createPictureViewerWindow(roolo:Roolo):ScyWindow{
    return createPictureViewerWindow(PictureViewerNode.createPictureViewerNode(roolo));
}

public function createPictureViewerWindow(picViewNode:PictureViewerNode):ScyWindow {
    var pictureWindow = ScyWindow{
        color: Color.PINK
        title: "PictureViewer"
        scyContent: picViewNode;
        cache: true;
    }
    var height = picViewNode.dateCreatedText.translateY + 85;
    var width = picViewNode.viewer.fitWidth;
    width = Math.max(width, picViewNode.titleText.boundsInParent.maxX);
    width = Math.max(width, picViewNode.descriptionText.boundsInParent.maxX);
    width = Math.max(width, picViewNode.authorText.boundsInParent.maxX);
    width = Math.max(width, picViewNode.dateCreatedText.boundsInParent.maxX);
    width += 70;
    pictureWindow.openWindow(width,height);

    picViewNode.scyWindow = pictureWindow;
    return pictureWindow;
    
}

function run(){

    var scyWind = createPictureViewerWindow(PictureViewerNode{});
    Stage {
        title: "PictureViewer"
        width: 600
        height: 600
        scene: Scene {
            content:// PictureViewerNode{
            // }
            scyWind
        }
    }
}
