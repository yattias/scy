
package eu.scy.elobrowser.tool.webressource;

import eu.scy.elobrowser.tool.pictureviewer.EloPictureWrapper;
import eu.scy.scywindows.ScyWindow;
import java.net.URI;
import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;

import javafx.scene.Scene;
import javafx.stage.Stage;
import eu.scy.elobrowser.main.Roolo;

import javafx.scene.control.ScrollBar;
import javafx.scene.layout.ClipView;
import javafx.scene.layout.LayoutInfo;

import javafx.scene.layout.HBox;

import java.lang.System;

/**
 * @author pg
 */

public class WebRessourceNode extends CustomNode {
    //default toolwindow stuff
    public-init var eloPictureActionWrapper: EloPictureWrapper;
    public var scyWindow: ScyWindow on replace {
        setScyWindowTitle();
    };

    //content node:
  
    var myContent:ContentNode = ContentNode {};
  
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

    var scyWindowHeightWatch:Number = bind scyWindow.height on replace { updateScrollbars() }
    var scyWindowWidthtWatch:Number = bind scyWindow.width on replace { updateScrollbars() }

    public function addQuotation(text:String):Void {
        myContent.addQuotation(text);
    }

    function updateScrollbars():Void {
       System.out.println("content: {myContent.height}");
       System.out.println("window: {scyWindow.height}");
       if((myContent.height) <= scrollClipView.height) {
           System.out.println("i disable.");
           vScroll.value = 0;
           vScroll.max = 1;
           vScroll.disable = true;
       } else {
           System.out.println("i enable!");
           System.out.println(javafx.util.Math.abs(myContent.height - scrollClipView.height));
           vScroll.value = 0;
           vScroll.max = javafx.util.Math.abs(myContent.height - scrollClipView.height);
           vScroll.disable = false;
       }
    }


    public function loadElo(uri:URI) {
        eloPictureActionWrapper.loadElo(uri);
        setScyWindowTitle();
    }

    function setScyWindowTitle() {
        if(scyWindow == null)
        return
        scyWindow.title = "WebRessource";
        var eloUri = eloPictureActionWrapper.getEloUri();
        if(eloUri != null)
        scyWindow.id = eloUri.toString()
        else
        scyWindow.id = "";
    }



    public override function create():Node {
        //quotation.setContent("Your Web server thinks that the HTTP data stream sent by the client (e.g. your Web browser or our CheckUpDown robot) identifies a URL resource whose actual media type 1) does not agree with the media type specified on the request or 2) is incompatible with the current data for the resource or 3) is incompatible with the HTTP method specified on the request. Detecting exactly what is causing this problem can be difficult, because there a number of possible reasons. Often the request involves transfer of data from the client to the Web server (e.g. a file upload via the PUT method), in which case you need to confirm with your ISP which media types are acceptable for upload. ");
        var g = Group {
              //blocksMouse: true;
            content: bind hBox;
        };
        return g;
    }

    public function createWebRessourceNode(roolo:Roolo, eloUri: URI):WebRessourceNode {
        return WebRessourceNode {
        };
    }

    public function createWebRessourceNode(roolo:Roolo):WebRessourceNode {
        return createWebRessourceNode(roolo, null);
    }


    public function createWebRessourceWindow(roolo:Roolo):ScyWindow{
        return createWebRessourceWindow(WebRessourceNode.createWebRessourceNode(roolo));
    }

    public function createWebRessourceWindow(webRessNode:WebRessourceNode):ScyWindow {

        var webWindow = ScyWindow{
            color: Color.CORNFLOWERBLUE
            title: "WebRessource"
            scyContent: webRessNode;
            cache: true;
        }
        webWindow.openWindow(600,600);

        webRessNode.scyWindow =  webWindow;
        return webWindow;

    }

    function run(){ 

        var scyWind = createWebRessourceWindow(WebRessourceNode{});
        Stage {
            title: "PictureViewer"
            width: 600
            height: 600
            scene: Scene {
                content:
                    scyWind
            }
        }
    }
}
