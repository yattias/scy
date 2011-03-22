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
public class HypotheseIcon extends AbstractEloIcon {

public override function clone(): HypotheseIcon {
HypotheseIcon {
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
					content: "M39.92,18.26 C39.44,15.95 38.35,13.88 36.85,12.16 C36.61,10.63 36.15,9.13 35.42,7.73 C33.68,4.40 30.57,1.79 26.74,0.71 C22.60,-0.44 19.09,0.46 15.34,2.11 C14.55,2.46 13.82,2.90 13.11,3.37 C11.98,3.72 10.86,4.10 9.70,4.67 C5.74,6.62 1.98,11.38 1.13,15.48 C0.44,18.78 0.37,20.55 0.80,23.88 C0.99,25.30 2.24,28.42 2.56,29.06 C4.72,33.39 8.32,35.70 12.85,37.49 C14.15,38.00 15.51,38.57 16.87,38.90 C20.69,39.84 23.04,39.70 26.82,39.17 C31.22,38.55 35.29,35.21 37.55,31.81 C40.28,27.73 40.88,22.91 39.92,18.26 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M36.96,20.26 C36.96,29.42 29.53,36.84 20.38,36.84 C11.22,36.84 3.80,29.42 3.80,20.26 C3.80,11.11 11.22,3.68 20.38,3.68 C29.53,3.68 36.96,11.11 36.96,20.26 Z "
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.emptyBackgroundColor
					strokeWidth: 4.99
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					startX: 36.98
					startY: 20.23
					endX: 3.73
					endY: 20.23
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.emptyBackgroundColor
					strokeWidth: 4.55
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					startX: 20.45
					startY: 3.58
					endX: 20.45
					endY: 36.88
				},
				SVGPath {
					fill: bind windowColorScheme.thirdColor
					stroke: null
					content: "M23.94,20.20 C23.94,22.13 22.37,23.70 20.44,23.70 C18.50,23.70 16.94,22.13 16.94,20.20 C16.94,18.27 18.50,16.70 20.44,16.70 C22.37,16.70 23.94,18.27 23.94,20.20 Z "
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
         HypotheseIcon{
            windowColorScheme: windowColorScheme
           layoutX: 25
            layoutY: 25
         }
         HypotheseIcon{
            windowColorScheme: windowColorScheme
            layoutX: 75
            layoutY: 25
            selected: true
         }
      ]
	}
}
}
