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
public class Buddies_me_and_othersIcon extends AbstractEloIcon {

public override function clone(): Buddies_me_and_othersIcon {
Buddies_me_and_othersIcon {
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
					fill: bind windowColorScheme.mainColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 4.0
					strokeLineCap: StrokeLineCap.BUTT
					content: "M2.93,38.50 C2.93,34.80 10.84,31.80 20.59,31.80 C30.34,31.80 38.24,34.80 38.24,38.50 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 4.0
					strokeLineCap: StrokeLineCap.BUTT
					content: "M38.55,15.18 C38.55,21.79 33.19,27.14 26.59,27.14 C19.98,27.14 14.63,21.79 14.63,15.18 C14.63,8.58 19.98,3.22 26.59,3.22 C33.19,3.22 38.55,8.58 38.55,15.18 Z "
				},
				SVGPath {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 4.0
					strokeLineCap: StrokeLineCap.BUTT
					content: "M26.55,15.18 C26.55,21.79 21.19,27.14 14.59,27.14 C7.98,27.14 2.63,21.79 2.63,15.18 C2.63,8.58 7.98,3.22 14.59,3.22 C21.19,3.22 26.55,8.58 26.55,15.18 Z "
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
         Buddies_me_and_othersIcon{
            windowColorScheme: windowColorScheme
           layoutX: 25
            layoutY: 25
         }
         Buddies_me_and_othersIcon{
            windowColorScheme: windowColorScheme
            layoutX: 75
            layoutY: 25
            selected: true
         }
      ]
	}
}
}
