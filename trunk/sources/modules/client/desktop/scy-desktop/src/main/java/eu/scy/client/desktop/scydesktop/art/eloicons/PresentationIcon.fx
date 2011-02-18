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
public class PresentationIcon extends AbstractEloIcon {

public override function clone(): PresentationIcon {
PresentationIcon {
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
					content: "M39.84,9.72 C38.84,7.72 37.10,6.33 34.54,5.54 C33.00,5.07 31.58,4.81 29.96,4.54 C28.77,4.34 27.69,3.26 26.60,2.64 C21.04,-0.56 14.64,0.14 8.79,2.00 C2.19,4.09 1.86,8.70 1.61,13.82 C1.45,16.89 2.06,19.96 2.19,23.02 C2.32,25.92 0.92,28.66 0.90,31.45 C0.84,37.23 6.32,38.20 12.50,38.82 C17.28,39.30 27.64,41.80 31.43,38.51 C33.49,36.73 33.42,34.53 34.14,32.28 C35.09,29.33 35.16,26.48 34.81,23.46 C34.69,22.39 34.18,21.60 33.45,21.05 C37.49,18.55 41.91,13.83 39.84,9.72 Z M13.41,30.56 C12.89,30.51 12.37,30.45 11.85,30.40 C11.83,30.39 11.82,30.39 11.80,30.39 C12.99,25.10 12.03,19.94 11.98,14.54 C11.94,13.59 12.06,12.65 12.32,11.72 C12.42,11.29 12.52,10.86 12.62,10.43 C12.72,10.08 12.79,9.81 12.83,9.59 C15.44,8.66 17.91,8.24 20.69,9.47 C23.23,10.59 24.24,12.24 27.46,12.62 C27.99,12.68 28.52,12.74 29.05,12.80 C29.08,12.81 29.09,12.82 29.12,12.83 C28.29,13.87 27.07,14.57 25.80,15.25 C22.52,17.03 23.54,19.97 26.06,21.46 C25.03,22.27 24.39,23.42 24.55,24.86 C24.81,27.11 24.37,29.30 23.81,31.48 C22.10,31.33 20.39,31.17 18.68,31.03 C16.93,30.88 15.17,30.73 13.41,30.56 Z "
				},
				Polyline {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.12
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					points: [27.89,22.70,27.89,31.46,3.97,31.46,3.97,6.29,27.89,6.29,27.89,16.92]
				},
				Polyline {
					fill: null
					stroke: bind windowColorScheme.thirdColor
					strokeWidth: 1.0
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					points: [28.33,23.19]
				},
				Polyline {
					fill: null
					stroke: bind windowColorScheme.thirdColor
					strokeWidth: 1.0
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					points: [28.33,13.47]
				},
				Polygon {
					points: [30.26,13.04,30.50,13.55,30.86,14.04,31.21,14.38,31.60,14.68,32.09,14.87,32.63,15.02,33.14,15.02,33.68,14.92,34.12,14.72,34.58,14.43,35.02,14.09,35.32,13.70,35.56,13.19,35.66,12.65,35.76,12.11,35.71,11.57,35.56,11.08,35.32,10.57,35.02,10.13,34.68,9.73,34.22,9.44,33.73,9.23,33.19,9.19,32.70,9.13,32.19,9.23,31.70,9.44,31.26,9.73,30.86,10.08,30.57,10.52,30.31,11.01,30.21,11.51,30.11,12.06,30.16,12.55]
					fill: bind windowColorScheme.mainColor
					stroke: null
				},
				Rectangle {
					fill: bind windowColorScheme.secondColor
					stroke: null
					x: 6.6
					y: 9.84
					width: 3.48
					height: 16.93
				},
				Rectangle {
					fill: bind windowColorScheme.mainColor
					stroke: null
					x: 11.81
					y: 12.35
					width: 2.9
					height: 14.42
				},
				Rectangle {
					fill: bind windowColorScheme.mainColor
					stroke: null
					x: 16.22
					y: 17.26
					width: 2.66
					height: 9.51
				},
				Rectangle {
					fill: bind windowColorScheme.secondColor
					stroke: null
					x: 20.67
					y: 12.24
					width: 2.66
					height: 14.53
				},
				Polyline {
					fill: null
					stroke: bind windowColorScheme.secondColor
					strokeWidth: 3.0
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					points: [22.42,37.64]
				},
				Polygon {
					points: [37.70,35.00,36.12,26.46,37.70,24.87,37.70,20.59,35.46,15.62,30.57,15.95,27.25,17.39,25.73,18.05,22.41,16.85,21.41,18.29,24.97,21.41,27.25,21.26,29.57,21.01,28.75,37.97,31.83,37.97,32.80,29.77,35.66,37.97,37.70,37.97]
					fill: bind windowColorScheme.mainColor
					stroke: null
				},
				Polygon {
					points: [20.80,15.17,18.54,13.48,18.03,14.13,19.13,14.97,18.82,15.95,21.50,17.54,22.26,16.26,21.41,14.13]
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
         PresentationIcon{
            windowColorScheme: windowColorScheme
           layoutX: 25
            layoutY: 25
         }
         PresentationIcon{
            windowColorScheme: windowColorScheme
            layoutX: 75
            layoutY: 25
            selected: true
         }
      ]
	}
}
}
