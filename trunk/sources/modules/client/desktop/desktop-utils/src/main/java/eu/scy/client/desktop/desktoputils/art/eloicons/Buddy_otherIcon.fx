package eu.scy.client.desktop.desktoputils.art.eloicons;

import javafx.scene.Scene;
import javafx.stage.Stage;
import eu.scy.client.desktop.desktoputils.art.ScyColors;
import javafx.scene.Node;
import javafx.scene.Group;
import javafx.scene.shape.*;
import javafx.scene.paint.Color;
import javafx.scene.transform.Transform;
import eu.scy.client.desktop.desktoputils.art.WindowColorScheme;
/**
 * @author lars
 */
public class Buddy_otherIcon extends AbstractEloIcon {

public override function clone(): Buddy_otherIcon {
Buddy_otherIcon {
selected: selected
size: size
windowColorScheme: windowColorScheme
}
}

public override function createNode(): Node {

return Group {

			content: [
				Rectangle {
					fill: bind windowColorScheme.mainColorLight
					stroke: null
					x: 0.59
					y: 0.36
					width: 40.0
					height: 39.91
				},
				SVGPath {
					fill: bind windowColorScheme.secondColorLight
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 4.0
					strokeLineCap: StrokeLineCap.BUTT
					content: "M2.93,38.50 C2.93,34.80 10.84,31.80 20.59,31.80 C30.34,31.80 38.24,34.80 38.24,38.50 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColorLight
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 4.0
					strokeLineCap: StrokeLineCap.BUTT
					content: "M32.55,15.18 C32.55,21.79 27.19,27.14 20.59,27.14 C13.98,27.14 8.63,21.79 8.63,15.18 C8.63,8.58 13.98,3.22 20.59,3.22 C27.19,3.22 32.55,8.58 32.55,15.18 Z "
},
]
}
}
}
function run(){
   def windowColorScheme = WindowColorScheme.getWindowColorScheme(ScyColors.darkGray);
   Stage {
	title: 'MyApp'
	onClose: function () {  }
	scene: Scene {
		width: 200
		height: 200
      fill: Color.YELLOW
		content: [
         Buddy_otherIcon{
            windowColorScheme: windowColorScheme
           layoutX: 25
            layoutY: 25
         }
         Buddy_otherIcon{
            windowColorScheme: windowColorScheme
            layoutX: 75
            layoutY: 25
            selected: true
         }
      ]
	}
}
}
