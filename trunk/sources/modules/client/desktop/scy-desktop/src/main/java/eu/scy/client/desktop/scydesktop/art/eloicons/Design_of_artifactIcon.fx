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
public class Design_of_artifactIcon extends AbstractEloIcon {

public override function clone(): Design_of_artifactIcon {
Design_of_artifactIcon {
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
					content: "M39.80,1.53 C39.63,0.21 37.96,-0.42 37.08,0.77 C33.95,5.04 31.26,9.56 27.93,13.71 C26.54,15.44 24.92,16.97 23.23,18.45 C20.19,21.14 15.29,24.49 14.74,18.03 C14.52,15.47 16.19,12.24 13.90,10.10 C11.71,8.06 7.77,10.19 5.84,11.37 C5.09,11.84 5.09,12.66 5.47,13.27 C5.25,13.83 5.09,14.43 5.03,15.10 C4.96,16.31 4.83,17.51 4.63,18.70 C3.79,19.72 2.53,20.49 1.66,21.54 C0.87,22.50 0.23,24.06 0.47,25.27 C0.66,26.23 0.93,27.41 1.98,27.93 C4.69,29.28 6.43,29.27 6.68,32.99 C6.88,36.01 8.61,38.21 11.99,38.69 C13.91,38.96 15.83,38.95 17.77,39.00 C21.18,39.09 24.33,40.21 27.79,40.14 C31.21,40.07 35.98,40.35 38.02,37.40 C40.60,33.68 39.80,26.78 40.05,22.55 C40.48,15.53 40.69,8.53 39.80,1.53 Z M37.01,25.81 C36.92,27.93 36.74,30.03 36.45,32.12 C35.95,35.72 35.12,36.68 31.06,36.81 C29.33,36.87 27.75,37.38 25.98,37.41 C23.72,37.46 21.50,36.72 19.28,36.44 C15.60,35.99 9.81,37.10 9.47,31.93 C9.34,30.03 8.91,28.49 7.42,27.14 C7.02,26.76 6.26,26.56 5.79,26.38 C2.35,25.02 4.96,22.13 6.76,20.69 C9.21,18.73 6.96,14.71 9.46,12.96 C10.31,12.99 11.20,13.44 12.15,14.33 C12.01,15.66 11.92,16.60 11.87,17.95 C11.81,19.63 12.34,21.26 13.06,22.78 C14.02,24.78 16.66,25.56 18.76,24.68 C27.49,21.05 32.36,12.85 37.31,5.54 C37.76,12.30 37.30,19.02 37.01,25.81 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColorLight
					stroke: null
					content: "M22.76,22.24 C21.46,22.16 19.49,23.05 18.23,24.31 C19.01,22.82 16.92,21.40 15.82,22.84 C15.61,23.12 14.96,23.98 14.21,25.09 C14.16,25.06 14.10,25.02 14.05,24.99 C13.09,24.50 12.14,24.26 11.08,24.54 C10.24,24.76 8.87,24.31 7.06,23.97 C5.69,23.71 4.63,25.69 5.80,26.59 C8.71,28.83 9.38,33.54 11.70,36.42 C12.55,37.48 14.06,36.88 14.39,35.69 C14.45,35.48 14.48,35.28 14.50,35.08 C15.21,34.80 15.74,34.38 16.21,33.74 C16.50,33.36 16.59,32.96 16.53,32.61 C18.90,31.57 21.07,29.63 22.79,27.99 C24.30,26.54 26.57,22.45 22.76,22.24 Z M11.75,27.34 C12.33,26.84 12.21,28.01 12.15,28.58 C11.92,29.04 11.72,29.49 11.57,29.93 C11.22,29.17 10.88,28.43 10.49,27.71 C10.99,27.70 11.44,27.61 11.75,27.34 Z "
				},
				Polygon {
					points: [15.02,36.41,38.15,38.34,38.29,5.98]
					fill: bind windowColorScheme.secondColor
					stroke: bind windowColorScheme.secondColor
					strokeWidth: 2.52
					strokeLineCap: StrokeLineCap.BUTT
				},
				Polygon {
					points: [33.14,21.13,33.09,31.95,25.37,31.30]
					fill: Color.WHITE
					stroke: Color.WHITE
					strokeWidth: 2.35
					strokeLineCap: StrokeLineCap.BUTT
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 4.0
					strokeLineCap: StrokeLineCap.BUTT
					startX: 3.42
					startY: 23.14
					endX: 32.42
					endY: 23.14
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 5.1
					strokeLineCap: StrokeLineCap.BUTT
					startX: 10.78
					startY: 4.8
					endX: 10.65
					endY: 32.0
				},
				SVGPath {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.09
					strokeLineCap: StrokeLineCap.BUTT
					content: "M8.82,16.80 Q9.57,21.67 6.40,21.75 "
				},
				SVGPath {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.09
					strokeLineCap: StrokeLineCap.BUTT
					content: "M8.74,28.90 Q9.19,24.33 6.02,24.24 "
				},
				Line {
					fill: null
					stroke: Color.WHITE
					strokeLineCap: StrokeLineCap.BUTT
					startX: 14.92
					startY: 22.14
					endX: 14.92
					endY: 24.14
				},
				Line {
					fill: null
					stroke: Color.WHITE
					strokeLineCap: StrokeLineCap.BUTT
					startX: 18.92
					startY: 22.14
					endX: 18.92
					endY: 24.14
				},
				Line {
					fill: null
					stroke: Color.WHITE
					strokeLineCap: StrokeLineCap.BUTT
					startX: 21.92
					startY: 22.14
					endX: 21.92
					endY: 24.14
				},
				Line {
					fill: null
					stroke: Color.WHITE
					strokeLineCap: StrokeLineCap.BUTT
					startX: 25.92
					startY: 22.14
					endX: 25.92
					endY: 24.14
				},
				Line {
					fill: null
					stroke: Color.WHITE
					strokeLineCap: StrokeLineCap.BUTT
					startX: 29.92
					startY: 22.14
					endX: 29.92
					endY: 24.14
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
         Design_of_artifactIcon{
            windowColorScheme: windowColorScheme
           layoutX: 25
            layoutY: 25
         }
         Design_of_artifactIcon{
            windowColorScheme: windowColorScheme
            layoutX: 75
            layoutY: 25
            selected: true
         }
      ]
	}
}
}
