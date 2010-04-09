/*
 * PictureViewerNode.fx
 *
 * Created on 09.09.2009, 18:44:30
 */

package eu.scy.client.tools.fxpictureviewer;

import javafx.scene.Group;
import javafx.scene.Node;
import java.net.URI;

import javafx.scene.CustomNode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import eu.scy.client.tools.fxpictureviewer.map.MapWrapper;

import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;

import javafx.scene.control.ScrollBar;
import javafx.scene.layout.ClipView;

import javafx.scene.layout.HBox;

import javafx.scene.layout.LayoutInfo;

import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.awt.image.BufferedImage;
import javafx.ext.swing.SwingUtils;

import eu.scy.client.desktop.scydesktop.tools.ScyToolFX;

import eu.scy.client.desktop.scydesktop.tools.EloSaver;

import eu.scy.client.desktop.scydesktop.tools.MyEloChanged;

import roolo.api.IRepository;

import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataTypeManager;
/**
 * @author phil
 */

public class PictureViewerNode extends CustomNode, ILoadPicture, ScyToolFX {
  //default toolwindow stuff
    public var scyWindow: ScyWindow on replace {
        setScyWindowTitle();
    };
    public var repository:IRepository;
    public var eloFactory:IELOFactory;
    public var metadataTypeManager:IMetadataTypeManager;
    public var eloPictureActionWrapper:EloPictureActionWrapper;
    //content node:
    //PICTUREVIEWER VARS########################################################
    public var title: String;
    public var description: String;
    public var author: String;
    public var dateCreatedString: String;
    public var startHeight:Number;
    public var startWidth:Number;
    public var textHeight:Number;


    // images minimum sizes:
    var minWidth:Number = 400;
    var minHeight:Number = 300;

    var mapWrapper:MapWrapper = MapWrapper{};
    var height : Number = bind scyWindow.height on replace {
        mapWrapper.setSize(width, height);
        updateScrollbars();
        updateViewer();

//        if(width >= startWidth) {
//            viewer.fitWidth = width - 20;
//        }

    };
    var width : Number = bind scyWindow.width on replace {
        mapWrapper.setSize(width, height);
        updateScrollbars();
        updateViewer();
//        if((height >= startHeight) and (width >= startWidth)) {
//            viewer.fitHeight = height - textHeight;
//            viewer.fitWidth = width - 20;
//        } else if(height >= startHeight) {
//            viewer.fitHeight = height - textHeight;
//        }

    };

    var viewer: ImageView = ImageView {
        fitWidth: scyWindow.height-100;
        fitHeight: scyWindow.width;
        preserveRatio: true;
        translateX: 5;
        translateY: 5;
    }

    var strokeRectangle: Rectangle = Rectangle {
        //stroke: Color.BLACK;
        stroke: Color.TRANSPARENT;
        strokeWidth: 2;
        height: bind viewer.fitHeight;
        width: bind viewer.fitWidth;
        translateX: bind viewer.translateX;
        translateY: bind viewer.translateY;
        fill: Color.TRANSPARENT;


    }

    var titleTextTitle: Text = Text {
        content: "Title: ";
        translateX: 5;
        translateY: 15;
        font: Font {size: 14; name: "Times New Roman" }
    };


    var titleText: Text = Text {
        content: bind title;
        translateX: 5;
        translateY: 15;
        font: Font {size: 18; name: "Times New Roman" }
    }

    var descriptionTextTitle: Text = Text {
        content: "Description: ";
        translateX: 5;
        translateY: bind strokeRectangle.height + strokeRectangle.translateY + 20;
        font: Font {size: 18; name: "Times New Roman" }
    };

    var descriptionText: Text = Text {
        content: bind description;
        font: Font { size: 14; name: "Times New Roman"; }
        translateX: 30;
        translateY: bind descriptionTextTitle.translateY + 20;
        wrappingWidth: 370;
    }

    var authorTextTitle: Text = Text {
        content: "Author: ";
        translateX: 5;
        translateY: bind descriptionText.translateY + descriptionText.layoutBounds.height + 20;
        font: Font {size: 18; name: "Times New Roman" }
    };

    var authorText: Text = Text {
        content: bind author;
        font: Font { size: 14; name: "Times New Roman"; }
        translateX: 30;
        translateY: bind authorTextTitle.translateY + 20;
    }

    var dateCreatedTextTitle: Text = Text {
        content: "Creation Date: ";
        font: Font {size: 18; name: "Times New Roman" }
        translateX: 5;
        translateY: bind authorText.translateY + authorText.layoutBounds.height + 20;
    };
    var dateCreatedText: Text = Text {
        content: bind dateCreatedString;
        font: Font { size: 14; name: "Times New Roman"; }
        translateX: 30;
        translateY: bind dateCreatedTextTitle.translateY;
    };

    var worldMap: ImageView = ImageView {
        image: Image{
            url: "{__DIR__}worldmap.png";
        }
        translateX: bind viewer.fitWidth - 59;
        translateY: bind dateCreatedText.translateY - worldMap.image.height;
        onMousePressed: function(e:MouseEvent):Void {
            mapWrapper.showMap();
        }
    }

    var debugRectangle:Rectangle = Rectangle {
            height: 350;
            fill: Color.TRANSPARENT;
            translateY: dateCreatedText.translateY;
    }



    init {
        setImage("{__DIR__}image.jpg");
        mapWrapper.addPosition(51.427783,6.800172, "UDE SCY Headquarters");
        mapWrapper.centerView(51.427783,6.800172);
        setTitle("a nice Smile");
        scyWindow.title = "PictureViewer - {title}";
        setDescription("Phew! This is a nice smile.. it loks sooooooo happy! Like it never heard about the HackLab before!!");
    }



    function setScyWindowTitle() {
        if(scyWindow == null)
        return
        scyWindow.title = "PictureViewer";
        var eloUri = eloPictureActionWrapper.getEloUri();
        if(eloUri != null)
        scyWindow.id = eloUri.toString()
        else
        scyWindow.id = "";
    }

    public function setTitle(title:String):Void {
        this.title = title;
        scyWindow.title = "PictureViewer - {title}";
    }

    public function setDescription(description:String):Void {
        this.description = description;
    }

    override function loadPicture(img:BufferedImage):Void {
        viewer.image = SwingUtils.toFXImage(img);
        println("loading image {img}")
    }

    public function setImage(url:String):Void {
        var newImage = Image {
            url: url;
        }
        viewer.image = newImage;
        /*
        viewer.fitHeight = maxStartHeight;
        viewer.fitWidth = maxStartWidth;

        startWidth = maxStartWidth + 25;
        maxStartHeight = startHeight + textHeight;
        */
    }

    function updateViewer():Void {
        if((scyWindow.height > minHeight) and(scyWindow.width > minWidth)) {
            viewer.fitWidth = scyWindow.width-25;
            viewer.fitHeight = scyWindow.height -150;
        }
    }


    //scrollbar:
    function updateScrollbars():Void {
       //disables the scrollbars if the content is smaller than the scywindow to prevent some errors
       if((scrollHeight) <= scrollClipView.height) {
           vScroll.value = 0;
           vScroll.max = 1;
           vScroll.disable = true;
       } else {
           vScroll.value = 0;
           vScroll.max = javafx.util.Math.abs(scrollHeight - scrollClipView.height);
           vScroll.disable = false;
       }
    }

    //height of the content:
    var scrollHeight = bind dateCreatedText.translateY + dateCreatedText.layoutBounds.height+10;
    var myContent:Group = Group {
        content: [
                viewer,
                descriptionTextTitle,
                descriptionText,
                authorTextTitle,
                authorText,
                dateCreatedTextTitle,
                dateCreatedText,
                strokeRectangle,
                debugRectangle,
                worldMap,
                mapWrapper
        ]
    }
    var vScroll:ScrollBar = ScrollBar {
        min: 0;
        value: 0;
        max: 1;
        vertical: true;
        disable: true;
        layoutInfo:LayoutInfo {
            height: bind scyWindow.height-50;
        }
    }
    
    var scrollClipView:ClipView = ClipView {
        clipY: bind vScroll.value;
        node: myContent;
        pannable: false;
        layoutInfo:LayoutInfo {
            height: bind scyWindow.height-50;
            width: bind scyWindow.width - 20;
        }
    }

    var hBox:HBox = HBox {
        content: [scrollClipView, vScroll]
    }

    /**
    * ScyTool methods
    */
    override function initialize(windowContent:Boolean):Void {

    }

    /**
    * step 6 in sequence of calls -> wiki ScyTool
    * ScyWindow + repository should be fine now -> create eloactionwrapper and be happy!
    */
    override function postInitialize():Void {

            this.eloPictureActionWrapper = new EloPictureActionWrapper(this);
            eloPictureActionWrapper.setRepository(repository);
            eloPictureActionWrapper.setMetadataTypeManager(metadataTypeManager);
            eloPictureActionWrapper.setEloFactory(eloFactory);
            eloPictureActionWrapper.setDocName(scyWindow.title);
    }

    override function newElo():Void {

    }

    override function loadElo(uri:URI):Void {
        eloPictureActionWrapper.loadElo(uri);
        setScyWindowTitle();
    }

    override function onGotFocus():Void {

    }

    override function onLostFocus():Void {
        
    }

    override function onMinimized():Void {

    }

    override function onUnMinimized():Void {

    }

    override function onClosed():Void {

    }

    override function aboutToClose():Boolean {
        return true
    }

    override function setEloSaver(eloSaver:EloSaver):Void {

    }

    override function setMyEloChanged(myEloChanged:MyEloChanged) {

    }









    public override function create():Node {
    var g = Group {
            content: [
                VBox {
                    content: [
                            Button {
                               text: "Open"
                               action: function() {
                                    eloPictureActionWrapper.loadPictureAction();
                                    //setScyWindowTitle();
                               }
                             },
                            hBox]
                }
            ]
        };
        return g;
    }


}
