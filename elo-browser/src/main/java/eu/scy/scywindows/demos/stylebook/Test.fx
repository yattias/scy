/*
 * Test.fx
 *
 * Created on 3-feb-2009, 10:39:40
 */

package eu.scy.scywindows.demos.stylebook;

import eu.scy.scywindows.ScyDesktop;
import eu.scy.scywindows.ScyWindow;
import java.util.Random;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Line;

/**
 * @author sikkenj
 */

function run(){

    var scyDesktop:ScyDesktop = ScyDesktop{};
    var rnd = new Random();
    var i=0;
    var stackCenter_x = 300.0;
    var stackCenter_y = 600.0;
    var closedScyWindow:ScyWindow;

    while (i<10)  {
        i++;
        closedScyWindow= ScyWindow{
            title:"Temp Data"
            color:Color.RED;
            allowClose:true;
            allowResize:true;
            allowRotate:true;
            allowMinimize:true;
            translateX: bind stackCenter_x + rnd.nextInt() mod 20; // x pos of window
            translateY: bind stackCenter_y + rnd.nextInt() mod 20; // y pos of window
            rotate:rnd.nextInt() mod 30; // the initial rotation angle
            onMouseClicked: function( e: MouseEvent ):Void {
                stackCenter_x = e.screenX;
                stackCenter_y = e.screenY;
            }
       };
        // uncomment next line to open the window, parameters are width and height
        //	closedScyWindow.openWindow(100, 150);
        scyDesktop.addScyWindow(closedScyWindow);
    }
    var imageScyWindow1= ScyWindow{
        title:"An image"
        color:Color.GREEN
        allowClose:true;
        allowResize:true;
        allowRotate:true;
        allowMinimize:true;
		translateX:200; // x pos of window
		translateY:20; // y pos of window
        scyContent: ImageView {
            image: Image {
                url: "{__DIR__}Water lilies.jpg"
            }
        }
   };
    // open the window, parameters are width and height
    imageScyWindow1.openWindow(100, 150);
    scyDesktop.addScyWindow(imageScyWindow1);
    var imageScyWindow2= ScyWindow{
        title:"An image"
        color:Color.MAGENTA;
        allowClose:true;
        allowResize:true;
        allowRotate:true;
        allowMinimize:true;
		translateX:400; // x pos of window
		translateY:200; // y pos of window
        scyContent: ImageView {
            image: Image {
                url: "{__DIR__}Water lilies.jpg"
            }
        }
   };
    // open the window, parameters are width and height
    imageScyWindow2.openWindow(500, 200);
    scyDesktop.addScyWindow(imageScyWindow2);

    // activate the window (only one window can be active)
    scyDesktop.activateScyWindow(imageScyWindow1);

    Stage {
        title: "SCY-Lab mission CO2 House"
        width: 300
        height: 400
        scene: Scene {
            content: [
				 scyDesktop.desktop,
                 ScyRelation {
                     window1:imageScyWindow1,
                     window2:imageScyWindow2,
                     color:imageScyWindow1.color,
                     name:"Test"
                }
            ]
        }
    }
}