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
import javafx.stage.Stage;
import jfxbrowser.BrowserComponent;

/**
 * @author Sven
 */

public class TwoStageTest extends CustomNode{

    var browser:BrowserComponent = BrowserComponent{
        
    }

    var htmlStore:String;
    var urlStore:String;

    var content:Node[];
    
    var rect:Rectangle = Rectangle{
        width:800
        height:600
        fill:Color.BLACK;
    }

    init{
        content = [newBrowser,reloadPageButton];
    }

    var reloadPageButton:SwingButton = SwingButton{
        text:"reload saved Page";
        translateX:150;
        
        action: function():Void{
            var storedPageBrowser = BrowserComponent{};
            storedPageBrowser.webBrowser.setHTMLContent(htmlStore);
            Stage{
                scene:Scene{
                    content: [storedPageBrowser]
                }
            }
        }
    }


     var newBrowser:SwingButton = SwingButton{
//        translateX: 200;
        text:"new Browser Window";
        action:function(){
            Stage{
                scene:Scene{
                    content: [browser]
                }
                onClose:function():Void{
                    parseHTML(htmlStore);
                    htmlStore = browser.webBrowser.getHTMLContent();
                    println("{browser.webBrowser.getHTMLContent()}");
//                    insert Text{content:"{browser.webBrowser.getHTMLContent()}"} into content;
                }
            }
        }
    }

    public function parseHTML(html:String):Void{
//      replace  src="/ with src="{url}/
        var temp:String = html;
//        this.html = temp.replace("src=\"/", "src=\"/");
    }


override public function create():Node{
        Group{
            content: bind this.content;
        }
    }
}


function run(__ARGS__ : String[]){
        Stage {

	title: "SCY Lab (FX)"
	width: 900;
	height: 700;
	scene: Scene {content: [TwoStageTest{}]}
    }
    }

