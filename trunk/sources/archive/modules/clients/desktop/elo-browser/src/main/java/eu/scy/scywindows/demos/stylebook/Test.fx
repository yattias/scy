/*
 * Test.fx
 *
 * Created on 3-feb-2009, 10:39:40
 */

package eu.scy.scywindows.demos.stylebook;

import eu.scy.elobrowser.main.Roolo;
import eu.scy.elobrowser.tool.textpad.TextpadNode;
import eu.scy.scywindows.demos.stylebook.ScyRelation;
import eu.scy.scywindows.ScyDesktop;
import eu.scy.scywindows.ScyWindow;
import java.util.Random;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 * @author sikkenj and joolingenwr
 */
var roolo=Roolo.getRoolo();

function createImageWindow(imageURL:String, color:Color, title: String, type: String, x: Number, y:Number, scale:Number):ScyWindow {
    var im = Image {
        url: imageURL
    }
    var win = ScyWindow{
        title: title;
        eloType: type;
        color:color;
        allowClose:true;
        allowResize:true;
        allowRotate:true;
        allowMinimize:true;
		translateX: x; // x pos of window
		translateY: y; // y pos of window

     }
    win.scyContent = ImageView {
        image: im;
        fitHeight: bind win.height - 40
        fitWidth: bind win.width - 10
    }
    win.openWindow(scale * im.width + 10, scale * im.height + 40);
    return win;
}

function createImageWindow(imageURL:String, color:Color, title: String, type: String):ScyWindow {
    return createImageWindow(imageURL, color, title, type, 20, 20)
}
function createImageWindow(imageURL:String, color:Color, title: String, type: String, x: Number, y:Number) :ScyWindow{
    return createImageWindow(imageURL, color, title, type, 20, 20, 1)
}

def buddyheight=80.0;
def buddywidth=60.0;

function buddy(imageName:String, x:Number, y:Number ):ImageView {
    return ImageView {
        var bx=x;
        var by=y;
        var startDragX = 0.0;
        var startDragY = 0.0;
        image: Image {
            url: "{__DIR__}{imageName}"
            width: buddywidth
            height: buddyheight
        },
        x:bind bx,
        y:bind by,
        onMousePressed: function( e: MouseEvent ):Void {
            startDragX = bx;
            startDragY = by;
        }
        onMouseDragged: function( e: MouseEvent ):Void {
            bx = startDragX + e.dragX;
            by = startDragY + e.dragY;
        }
    }
}

function run(){

var scyDesktop:ScyDesktop = ScyDesktop{};
var rnd = new Random();
var i=0;
var stackCenter_x = 100.0;
var stackCenter_y = 600.0;
var closedScyWindow = ScyWindow{
    title:"Temp Data"
    eloType: "DATA";
    color:Color.RED;
    allowClose:true;
    allowResize:true;
    allowRotate:true;
    allowMinimize:true;
    translateX: stackCenter_x + rnd.nextInt() mod 20; // x pos of window
    translateY: stackCenter_y + rnd.nextInt() mod 20; // y pos of window
    rotate: rnd.nextInt() mod 30; // the initial rotation angle
        };;

var tWindow:ScyWindow;
while (
i < 10) {
        i++;
    tWindow= ScyWindow{
        title:"Temp Data"
        eloType: "DATA";
        color:Color.RED;
        allowClose:true;
        allowResize:true;
        allowRotate:true;
        allowMinimize:true;
        translateX: bind closedScyWindow.translateX + rnd.nextInt() mod 20; // x pos of window
        translateY: bind closedScyWindow.translateY + rnd.nextInt() mod 20; // y pos of window
        rotate: rnd.nextInt() mod 30; // the initial rotation angle
    };
    // uncomment next line to open the window, parameters are width and height
    //	closedScyWindow.openWindow(100, 150);
    scyDesktop.addScyWindow(tWindow);
}
scyDesktop.addScyWindow(closedScyWindow);

var imageScyModelWindow=createImageWindow("{__DIR__}model.png", Color.BLUE, "Model of Greenhouse", "Model", 100,100);
scyDesktop.addScyWindow(imageScyModelWindow);
var imageScyWindow2= createImageWindow( "{__DIR__}graphsmall.png", Color.RED, "CO2 Data", "Graph", 200, 300);
scyDesktop.addScyWindow(imageScyWindow2);
var imageScyWindow3= createImageWindow("{__DIR__}co2sim.png", Color.DARKGREEN, "CO2 Simulation", "Simulation", 400, 400);
scyDesktop.addScyWindow(imageScyWindow3);
var imageScyBrowserWindow= createImageWindow("{__DIR__}browser.png", Color.MAGENTA, "Background information", "WebBrowser", 400, 400, 0.3);
scyDesktop.addScyWindow(imageScyBrowserWindow);
//    var textWindow = TextpadNode.createTextpadWindow(roolo);
//    textWindow.title = "Talking to Yuri";
//    scyDesktop.addScyWindow(textWindow);

// activate the window (only one window can be active)
scyDesktop.activateScyWindow(imageScyModelWindow);

Stage {
    title: "SCY-Lab mission CO2 House"
    width: 1100
    height: 700
    scene: Scene {
        content: [
            ImageView{
                image: Image {
                    url: "{__DIR__}scyLogo.png"
                }
                y:10
                x:750
            },
            ImageView{
                image: Image {
                    url: "{__DIR__}navigator.png"
                    width: 200
                    height: 150
                }
                y:550
                x:750
            },
            Rectangle {
                x:780
                y:580
                width: 60
                height: 50
                stroke: Color.RED
                fill: null
                strokeWidth: 1.5
            },
            ScyRelation {
                window1:imageScyWindow3,
                window2:imageScyModelWindow,
                name:"model_of"
            },
            ScyRelation {
                window1:imageScyWindow3,
                window2:closedScyWindow,
                name:"generated_by"
            },
            ScyRelation {
                window1:imageScyWindow2,
                window2:closedScyWindow,
                name:"displays"
            },
            ScyRelation {
                window1:imageScyWindow3,
                window2:closedScyWindow,
                name:"displays"
            },

            scyDesktop.desktop,
            buddy("adam.jpg",10,10),
            buddy("ard.jpg", 80, 10),
            buddy("cedric.jpg", 10, 100),
            buddy("yuri.jpg",80,100),
            CallOut{

                }
            
        ]
    }
}
}