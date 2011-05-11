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
public class ReflectionIcon extends AbstractEloIcon {

public override function clone(): ReflectionIcon {
ReflectionIcon {
selected: selected
size: size
windowColorScheme: windowColorScheme
}
}

public override function createNode(): Node {

return Group {

			content: [
				Polygon {
					points: [37.98,16.14,2.95,16.14,32.33,2.55]
					fill: bind windowColorScheme.mainColor
					stroke: null
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.96
					strokeLineCap: StrokeLineCap.BUTT
					startX: 1.53
					startY: 18.08
					endX: 39.31
					endY: 18.35
				},
				SVGPath {
					fill: bind windowColorScheme.mainColorLight
					stroke: null
					content: "M35.71,19.58 C35.13,19.58 34.63,19.76 34.21,20.04 C33.94,19.94 33.63,19.88 33.28,19.89 C28.88,20.00 24.63,20.64 20.24,20.26 C15.65,19.87 11.03,19.89 6.42,19.89 C2.79,19.89 2.26,26.23 5.93,26.23 C7.19,26.23 8.45,26.23 9.71,26.25 C9.22,28.01 9.92,30.18 12.03,30.30 C13.01,30.36 14.00,30.52 14.99,30.69 C14.92,32.05 15.58,33.59 16.82,33.90 C19.51,34.58 21.69,36.25 24.11,37.64 C25.47,38.42 27.07,38.39 28.03,36.83 C28.56,35.97 28.66,34.64 28.25,33.61 C29.61,33.57 30.87,33.26 31.92,32.16 C32.64,31.41 33.01,30.38 32.98,29.36 C33.88,28.50 34.35,27.12 33.93,25.95 C34.08,25.88 34.22,25.79 34.35,25.69 C34.62,25.78 34.90,25.85 35.22,25.85 C38.82,25.85 39.34,19.58 35.71,19.58 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: bind windowColorScheme.emptyBackgroundColor
					strokeLineCap: StrokeLineCap.BUTT
					content: "M19.77,27.31 C20.52,27.02 21.28,26.83 22.08,26.75 C23.15,26.59 24.35,26.70 25.45,26.64 C26.61,26.57 28.01,26.18 28.85,25.34 C29.90,24.30 26.66,23.71 26.36,23.65 C22.13,22.72 18.00,23.21 13.72,23.09 C13.54,23.09 13.51,23.37 13.69,23.37 C18.32,23.50 22.78,23.01 27.32,24.16 C28.62,24.49 28.88,25.27 27.48,25.87 C26.30,26.38 25.02,26.39 23.76,26.42 C21.67,26.47 20.01,26.80 18.15,27.77 C18.06,27.81 18.04,27.91 18.10,27.99 C18.75,28.87 20.01,29.03 21.03,29.21 C22.01,29.38 23.05,29.70 24.05,29.71 C24.82,29.59 24.82,29.95 24.03,30.79 C23.40,30.98 22.67,30.99 22.02,31.09 C21.94,31.11 20.65,31.51 20.90,31.96 C21.40,32.86 24.58,33.41 23.76,34.49 C23.64,34.63 23.88,34.79 23.99,34.65 C24.50,33.99 24.46,33.45 23.59,33.12 C22.63,32.75 19.99,31.70 22.71,31.27 C23.74,31.11 24.97,31.08 25.74,30.29 C25.78,30.24 25.81,30.18 25.77,30.12 C25.33,29.39 24.96,29.46 24.07,29.43 C22.53,29.37 20.84,28.94 19.35,28.52 C18.74,27.80 18.88,27.40 19.77,27.31 Z "
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
         ReflectionIcon{
            windowColorScheme: windowColorScheme
           layoutX: 25
            layoutY: 25
         }
         ReflectionIcon{
            windowColorScheme: windowColorScheme
            layoutX: 75
            layoutY: 25
            selected: true
         }
      ]
	}
}
}
