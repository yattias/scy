/*
 * Test.fx
 *
 * Created on 3-feb-2009, 10:39:40
 */

package eu.scy.scywindows.demos.stylebook;

import eu.scy.scywindows.ScyDesktop;
import eu.scy.scywindows.ScyWindow;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

/**
 * @author sikkenj
 */

function run(){

   var scyDesktop:ScyDesktop = ScyDesktop{};

   var closedScyWindow= ScyWindow{
      title:"Closed and very closed"
      color:Color.GRAY
      allowClose:true;
      allowResize:true;
      allowRotate:true;
      allowMinimize:true;
		translateX:20; // x pos of window
		translateY:20; // y pos of window
   };
   // uncomment next line to open the window, parameters are width and height
   //	closedScyWindow.openWindow(100, 150);
   scyDesktop.addScyWindow(closedScyWindow);

   var imageScyWindow= ScyWindow{
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
   imageScyWindow.openWindow(100, 150);
   scyDesktop.addScyWindow(imageScyWindow);

   // activate the window (only one window can be active)
   scyDesktop.activateScyWindow(imageScyWindow);

   Stage {
      title: "Scy window test"
      width: 300
      height: 400
      scene: Scene {
         content: [
				 scyDesktop.desktop,
         ]
      }

}
}