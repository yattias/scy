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
public class NewIcon extends AbstractEloIcon {

public override function clone(): NewIcon {
NewIcon {
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
					stroke: null
					content: "M38.87,36.42 C36.95,33.40 35.04,30.43 33.64,27.13 C33.07,25.81 29.65,20.72 30.23,19.38 C32.39,14.40 36.78,9.92 37.46,4.30 C37.50,3.95 37.14,3.75 36.83,3.75 C34.88,3.75 32.97,5.06 31.20,5.76 C27.81,7.12 24.34,8.87 21.30,10.92 C15.60,9.03 10.63,5.28 5.80,1.87 C5.36,1.56 3.82,1.91 4.08,2.65 C5.89,7.78 7.18,13.59 10.64,17.90 C12.12,19.74 8.05,25.90 7.22,27.72 C5.92,30.56 4.08,33.08 2.82,35.94 C2.46,36.76 3.71,36.68 4.14,36.52 C7.13,35.37 10.21,34.54 13.16,33.31 C13.84,33.03 20.90,28.99 21.41,29.55 C22.75,31.04 26.10,32.01 27.87,32.91 C31.00,34.49 34.38,35.22 37.23,37.35 C37.75,37.74 39.36,37.19 38.87,36.42 Z M21.64,27.79 C19.68,27.45 16.67,30.29 15.14,31.05 C11.97,32.62 8.60,33.63 5.28,34.80 C6.46,32.45 7.89,30.24 9.05,27.87 C10.24,25.42 12.77,21.76 13.20,19.11 C13.47,17.41 10.89,15.07 10.26,13.82 C8.83,11.00 7.87,7.82 6.88,4.75 C11.39,7.93 16.13,11.09 21.47,12.55 C22.14,12.74 23.44,11.88 22.88,11.47 C26.98,8.78 31.62,7.06 36.08,5.08 C34.59,5.74 33.97,8.93 33.35,10.31 C32.27,12.68 31.05,14.98 29.73,17.21 C28.01,20.13 27.57,20.41 29.55,23.30 C31.06,25.49 31.68,28.02 32.90,30.34 C33.60,31.67 34.38,32.97 35.19,34.25 C32.82,33.15 30.30,32.33 27.93,31.13 C25.98,30.14 23.77,28.17 21.64,27.79 Z "
				},
				Polygon {
					points: [33.35,7.53,26.48,19.96,33.35,32.38,20.92,25.52,8.49,32.38,15.36,19.96,8.49,7.53,20.92,14.40]
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 2.58
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
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
         NewIcon{
            windowColorScheme: windowColorScheme
           layoutX: 25
            layoutY: 25
         }
         NewIcon{
            windowColorScheme: windowColorScheme
            layoutX: 75
            layoutY: 25
            selected: true
         }
      ]
	}
}
}
