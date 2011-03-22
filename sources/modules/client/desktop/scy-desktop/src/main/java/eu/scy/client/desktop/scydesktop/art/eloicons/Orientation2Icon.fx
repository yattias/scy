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
public class Orientation2Icon extends AbstractEloIcon {

public override function clone(): Orientation2Icon {
Orientation2Icon {
selected: selected
size: size
windowColorScheme: windowColorScheme
}
}

public override function createNode(): Node {

return Group {

			content: [
				SVGPath {
					fill: bind windowColorScheme.mainColorLight
					stroke: bind windowColorScheme.mainColorLight
					strokeLineCap: StrokeLineCap.BUTT
					content: "M38.46,15.64 C37.25,7.28 29.19,4.89 23.40,4.69 C22.94,4.07 22.30,3.67 21.49,3.76 C12.83,4.68 4.89,8.88 3.05,21.34 C0.95,35.57 15.30,37.68 22.48,36.53 C30.25,35.29 40.22,27.86 38.46,15.64 Z M32.26,23.95 C30.30,27.34 26.91,29.13 23.88,29.97 C18.85,31.35 7.00,32.33 7.69,21.88 C8.24,13.71 14.44,11.14 19.75,10.20 C20.08,10.62 20.55,10.88 21.19,10.85 C25.06,10.68 30.25,11.07 33.12,15.01 C35.02,17.64 33.64,21.56 32.26,23.95 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M37.00,20.61 C37.00,27.93 29.61,33.88 20.48,33.88 C11.36,33.88 3.97,27.93 3.97,20.61 C3.97,13.27 11.36,7.34 20.48,7.34 C29.61,7.34 37.00,13.27 37.00,20.61 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.47
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					content: "M35.23,21.26 C35.23,27.01 28.39,31.67 19.94,31.67 C11.50,31.67 4.65,27.01 4.65,21.26 C4.65,15.51 11.50,10.85 19.94,10.85 C28.39,10.85 35.23,15.51 35.23,21.26 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M30.36,21.64 C30.36,25.38 27.49,28.42 23.94,28.42 C20.39,28.42 17.52,25.38 17.52,21.64 C17.52,17.89 20.39,14.86 23.94,14.86 C27.49,14.86 30.36,17.89 30.36,21.64 Z "
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
         Orientation2Icon{
            windowColorScheme: windowColorScheme
           layoutX: 25
            layoutY: 25
         }
         Orientation2Icon{
            windowColorScheme: windowColorScheme
            layoutX: 75
            layoutY: 25
            selected: true
         }
      ]
	}
}
}
