/*
 * BrowserComponent.fx
 *
 * Created on 21.04.2009, 13:42:05
 */

package jfxbrowser;

import chrriis.common.UIUtils;
import chrriis.dj.nativeswing.swtimpl.components.JFlashPlayer;
import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;
import chrriis.dj.nativeswing.swtimpl.NativeInterface;
import java.lang.Object;
import javafx.ext.swing.SwingButton;
import javafx.ext.swing.SwingComponent;
import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;


/**
 * @author Sven
 */

public class FlashComponent extends CustomNode {


    var flashPlayer:JFlashPlayer;
    var webBrowserComponent:SwingComponent;
    def transparentLayer:Rectangle = Rectangle {
        width:bind webBrowserComponent.width;
        height:bind webBrowserComponent.height;
        fill:Color.TRANSPARENT;
        blocksMouse:true;
    }
//    def highlightButton:SwingButton = SwingButton{
//        action: function():Void{
//        webBrowser.executeJavascript("theSelection = document.selection.createRange().htmlText; document.selection.createRange().pasteHTML(\"<span style='background-color: yellow;'>\" + theSelection + \"</span>\");");
//        }
//        text:"Highlight";
//    }


    init{
        UIUtils.setPreferredLookAndFeel();
        NativeInterface.open();
        flashPlayer = new JFlashPlayer();
        flashPlayer.load(getClass(), "test.swf");
        flashPlayer.setVisible(true);
        flashPlayer.setSize(640, 480);
//        flashPlayer.setLocation(0, 0);
        webBrowserComponent = SwingComponent.wrap(flashPlayer);
        webBrowserComponent.width = 640;
        webBrowserComponent.height = 480;
//        flashPlayer.navigate("http://www.google.de");
        NativeInterface.runEventPump();
    }

    postinit{ flashPlayer.load(getClass(), "WebBrowsingTool.flv");}

    override function create():Node{
        return Group{
            content: [webBrowserComponent,transparentLayer]
        }
    }
}
function run(__ARGS__ : String[]){
        Stage {

	title: "SCY Lab (FX)"
	width: 660;
	height: 520;
	scene: Scene {content: [FlashComponent{}]}
    }
    }

