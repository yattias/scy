/*
 * TestClass.fx
 *
 * Created on 06.05.2009, 21:34:56
 */

package jfxbrowser;

import java.lang.Object;
import javafx.ext.swing.SwingButton;
import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import jfxbrowser.BrowserComponent;

/**
 * @author Sven
 */

public class TestClass extends CustomNode{

    var browser:BrowserComponent = BrowserComponent{
        
    }

    var rect:Rectangle = Rectangle{
        width:800
        height:600
        fill:Color.BLACK;
    }

    var rotateButton:SwingButton = SwingButton{
        translateX: 100;
        text:"ROTATE";
        action:function(){
            browser.rotate=30;
        }
    }

     var translateButton:SwingButton = SwingButton{
        translateX: 200;
        text:"ROTATE";
        action:function(){
            browser.transforms = Translate{
                x:50;
            }

            browser.webBrowser.repaint();
        }
    }

    override public function create():Node{
        Group{
            content: [rect,browser,rotateButton,translateButton]
        }
    }
}


function run(__ARGS__ : String[]){
        Stage {

	title: "SCY Lab (FX)"
	width: 900;
	height: 700;
	scene: Scene {content: [TestClass{}]}
    }
    }

