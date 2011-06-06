/*
 * TestAttribute.fx
 *
 * Created on 26-mrt-2009, 15:14:09
 */

package eu.scy.client.desktop.scydesktop.scywindows;

//import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.WindowManagerImpl;
//import eu.scy.client.desktop.scydesktop.scywindows.window.StandardScyWindow;
import java.lang.Object;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Math;

/**
 * @author sikkenj
 */

public class TestAttribute extends ScyWindowAttribute {
   //public-read var priority = bind radius;

    public var radius = 10.0;

	 postinit {
		 priority = Math.round(radius);
	 }

    
//	 public override function compareTo(scyWindowAttribute: ScyWindowAttribute):Integer{
//		 return priority - scyWindowAttribute.priority;
//	 }

   public override function create(): Node {
        return Group {
            content: [
                Circle {
                    centerX: radius,
                    centerY: -radius
                    radius: radius
                    fill: Color.TRANSPARENT
                    stroke:Color.BLACK
                }
            ]
        };
    }

	 public override function toString():String{
		 return "TestAttribute[priority:{priority}, radius:{radius}]";
	 }

    public override function clone():ScyWindowAttribute{
       TestAttribute{
          radius: radius
       }
    }

}

function run(){

//    var scyDesktop: WindowManager = WindowManagerImpl{
//    };
//
//    var att1 = TestAttribute{
//        radius:5
//    }
//
//    var att2 = TestAttribute{
//        radius:10
//    }
//    var att3 = TestAttribute{
//        radius:15
//    }
//
//
//
//var window = StandardScyWindow{
//        title:"test"
//        translateX:20
//        translateY:50
//        scyWindowAttributes: [att1,att3,att2]
//    }
//   scyDesktop.addScyWindow(window);
//
//Stage {
//    title: "test att"
//    width: 250
//    height: 200
//    scene: Scene {
//        content: [
//            scyDesktop.scyWindows
//        ]
//    }
//}
}
