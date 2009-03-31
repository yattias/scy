/*
 * TestAttribute.fx
 *
 * Created on 26-mrt-2009, 15:14:09
 */

package eu.scy.scywindows;

import eu.scy.scywindows.ScyDesktop;
import eu.scy.scywindows.ScyWindow;
import eu.scy.scywindows.ScyWindowAttribute;
import java.lang.Object;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

/**
 * @author sikkenj
 */

public class TestAttribute extends ScyWindowAttribute {
   //public-read var priority = bind radius;

    public var radius = 20;

	 postinit {
		 priority = radius;
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

}

function run(){

    var scyDesktop: ScyDesktop = ScyDesktop{
    };

    var att1 = TestAttribute{
        radius:5
    }

    var att2 = TestAttribute{
        radius:10
    }
    var att3 = TestAttribute{
        radius:15
    }



var window = ScyWindow{
        title:"test"
        translateX:20
        translateY:50
        scyWindowAttributes: [att1,att3,att2]
    }
   scyDesktop.addScyWindow(window);

Stage {
    title: "test att"
    width: 250
    height: 200
    scene: Scene {
        content: [
            scyDesktop.desktop
        ]
    }
}
}
