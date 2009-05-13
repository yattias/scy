/*
 * BrowserComponent.fx
 *
 * Created on 21.04.2009, 13:42:05
 */

package jfxbrowser;

import chrriis.common.UIUtils;
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
import jfxbrowser.BrowserComponent;


/**
 * @author Sven
 */

public class BrowserComponent extends CustomNode {


    public var webBrowser:JWebBrowser;
    public var webBrowserComponent:SwingComponent;
    def transparentLayer:Rectangle = Rectangle {
        width:bind webBrowserComponent.width;
        height:bind webBrowserComponent.height;
        fill:Color.TRANSPARENT;
        blocksMouse:true;
    }
    def highlightButton:SwingButton = SwingButton{
        action: function():Void{
        webBrowser.executeJavascript("theSelection = document.selection.createRange().htmlText; document.selection.createRange().pasteHTML(\"<span style='background-color: yellow;'>\" + theSelection + \"</span>\");");
        }
        text:"Highlight";
    }


    init{
        UIUtils.setPreferredLookAndFeel();
        NativeInterface.open();
        webBrowser = new JWebBrowser(JWebBrowser.proxyComponentHierarchy());
//        webBrowser.set
        webBrowser.setSize(640, 480);
        webBrowser.setLocation(0, 0);
        webBrowserComponent = SwingComponent.wrap(webBrowser);
        webBrowserComponent.width = 640;
        webBrowserComponent.height = 480;
        webBrowser.navigate("http://www.google.de");
        NativeInterface.runEventPump();
    }

    override function create():Node{
        return Group{
            content: [webBrowserComponent,transparentLayer,highlightButton]
        }
    }
}
function run(__ARGS__ : String[]){
        Stage {

	title: "SCY Lab (FX)"
	width: 660;
	height: 520;
	scene: Scene {content: [BrowserComponent{}]}
    }
    }

