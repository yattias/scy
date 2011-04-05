package eu.scy.client.desktop.scydesktop.art.eloicons;

import javafx.scene.Scene;
import javafx.stage.Stage;
import eu.scy.client.desktop.scydesktop.art.ScyColors;
import javafx.scene.Node;
import javafx.scene.Group;
import javafx.scene.shape.*;
import javafx.scene.paint.Color;
import javafx.scene.transform.Transform;
import eu.scy.client.desktop.scydesktop.art.WindowColorScheme;
/**
 * @author lars
 */
public class Buddies_othersIcon extends AbstractEloIcon {

public override function clone(): Buddies_othersIcon {
Buddies_othersIcon {
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
					fill: bind windowColorScheme.mainColorLight
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 4.0
					strokeLineCap: StrokeLineCap.BUTT
					content: "M26.22,15.18 C26.22,21.79 20.86,27.14 14.26,27.14 C7.65,27.14 2.30,21.79 2.30,15.18 C2.30,8.58 7.65,3.22 14.26,3.22 C20.86,3.22 26.22,8.58 26.22,15.18 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColorLight
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 4.0
					strokeLineCap: StrokeLineCap.BUTT
					content: "M2.93,38.50 C2.93,34.80 10.84,31.80 20.59,31.80 C30.34,31.80 38.24,34.80 38.24,38.50 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColorLight
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 4.0
					strokeLineCap: StrokeLineCap.BUTT
					content: "M38.79,15.18 C38.79,21.79 33.43,27.14 26.83,27.14 C20.22,27.14 14.87,21.79 14.87,15.18 C14.87,8.58 20.22,3.22 26.83,3.22 C33.43,3.22 38.79,8.58 38.79,15.18 Z "
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
         Buddies_othersIcon{
            windowColorScheme: windowColorScheme
           layoutX: 25
            layoutY: 25
         }
         Buddies_othersIcon{
            windowColorScheme: windowColorScheme
            layoutX: 75
            layoutY: 25
            selected: true
         }
      ]
	}
}
}
