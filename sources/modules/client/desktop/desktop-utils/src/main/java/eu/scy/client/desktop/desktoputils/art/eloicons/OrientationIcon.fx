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
public class OrientationIcon extends AbstractEloIcon {

public override function clone(): OrientationIcon {
OrientationIcon {
selected: selected
size: size
windowColorScheme: windowColorScheme
}
}

public override function createNode(): Node {

return Group {

			content: [
				SVGPath {
					fill: bind windowColorScheme.secondColorLight
					stroke: null
					content: "M38.23,15.92 C34.73,4.78 24.88,1.90 14.46,2.59 C13.06,2.68 12.51,3.77 12.62,4.86 C12.57,4.89 12.51,4.91 12.46,4.95 C4.96,10.32 -0.46,17.71 2.97,27.27 C6.20,36.26 18.22,38.71 26.39,36.93 C35.27,35.01 40.96,24.60 38.23,15.92 Z M23.19,32.77 C17.06,33.64 9.94,31.15 7.16,25.33 C3.78,18.23 10.33,12.17 15.42,8.52 C15.97,8.12 16.24,7.63 16.30,7.13 C23.35,6.82 29.41,8.34 33.08,15.24 C37.28,23.15 31.09,31.66 23.19,32.77 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: null
					content: "M20.09,34.09 C15.64,34.09 11.49,31.89 8.98,28.20 C4.79,22.04 6.36,13.61 12.48,9.39 C14.73,7.85 17.36,7.03 20.07,7.03 C24.52,7.03 28.67,9.23 31.18,12.92 C35.37,19.08 33.80,27.51 27.67,31.72 C25.43,33.27 22.80,34.09 20.09,34.09 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M20.42,6.14 L20.42,7.95 C24.42,7.95 28.26,10.00 30.60,13.44 C34.50,19.18 32.95,27.04 27.24,30.97 C25.15,32.40 22.66,33.17 20.14,33.17 C15.99,33.17 12.10,31.12 9.76,27.68 C5.85,21.94 7.48,14.08 13.19,10.15 C15.29,8.71 17.42,7.95 20.42,7.95 Z M20.07,6.11 C17.27,6.11 14.44,6.93 11.96,8.64 C5.41,13.14 3.73,22.13 8.22,28.72 C11.00,32.81 15.51,35.01 20.09,35.01 C22.89,35.01 25.71,34.19 28.20,32.48 C34.75,27.98 36.43,18.99 31.94,12.40 C29.16,8.31 24.65,6.11 20.07,6.11 Z "
				},
				Polygon {
					points: [19.41,15.33,29.01,10.57,24.30,20.27,29.01,29.98,19.41,25.21,9.81,29.98,14.52,20.27,9.81,10.57]
					fill: bind windowColorScheme.secondColor
					stroke: null
				},
				Polygon {
					points: [20.37,20.14,37.56,20.14,23.14,22.83]
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: null
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M36.48,20.14 L23.18,22.62 L20.62,20.14 L36.42,20.14 M38.63,20.14 L20.13,20.14 L23.11,23.04 Z "
				},
				Polygon {
					points: [20.13,20.14,38.63,20.14,23.11,17.24]
					fill: bind windowColorScheme.mainColor
					stroke: null
				},
				Polygon {
					points: [3.10,20.14,17.56,17.44,20.28,20.14]
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: null
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M17.48,17.65 L20.03,20.14 L4.17,20.14 Z M17.54,17.24 L2.02,20.14 L20.53,20.14 Z "
				},
				Polygon {
					points: [20.53,20.14,2.02,20.14,17.54,23.04]
					fill: bind windowColorScheme.mainColor
					stroke: null
				},
				Polygon {
					points: [17.75,23.06,20.42,20.30,20.42,37.66]
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: null
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M20.42,20.14 L20.42,36.56 L17.96,23.14 L20.42,20.56 M20.42,20.06 L17.55,23.07 L20.42,38.76 Z "
				},
				Polygon {
					points: [20.42,20.06,20.42,38.76,23.29,23.07]
					fill: bind windowColorScheme.mainColor
					stroke: null
				},
				Polygon {
					points: [20.42,2.86,23.09,17.46,20.42,20.21]
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: null
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M20.42,3.96 L22.88,17.38 L20.42,19.96 L20.42,4.14 M20.42,1.76 L20.42,20.46 L23.29,17.44 Z "
				},
				Polygon {
					points: [20.42,20.46,20.42,1.76,17.55,17.44]
					fill: bind windowColorScheme.mainColor
					stroke: null
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
         OrientationIcon{
            windowColorScheme: windowColorScheme
           layoutX: 25
            layoutY: 25
         }
         OrientationIcon{
            windowColorScheme: windowColorScheme
            layoutX: 75
            layoutY: 25
            selected: true
         }
      ]
	}
}
}
