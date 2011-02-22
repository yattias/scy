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
public class Designed_artifactIcon extends AbstractEloIcon {

public override function clone(): Designed_artifactIcon {
Designed_artifactIcon {
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
					content: "M30.61,1.00 C26.74,4.93 22.32,6.33 17.88,7.47 C13.19,8.67 7.98,7.24 3.48,9.52 C-3.11,12.88 2.23,43.27 8.86,39.90 C13.35,37.62 18.55,38.99 23.26,37.85 C28.63,36.55 33.82,34.23 38.49,29.50 C44.51,23.40 36.58,-5.04 30.61,1.00 Z "
				},
				Polygon {
					points: [1.05,2.83,0.90,12.91,39.71,34.69,39.93,25.22]
					fill: bind windowColorScheme.secondColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.94
					strokeLineCap: StrokeLineCap.BUTT
				},
				Polygon {
					points: [1.89,30.10,1.98,35.76,5.62,38.69,25.05,26.70,25.05,16.85]
					fill: bind windowColorScheme.secondColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.94
					strokeLineCap: StrokeLineCap.BUTT
				},
				Polygon {
					points: [16.33,23.46,16.45,31.42,25.25,26.12,33.92,31.42,33.75,23.30,25.23,18.30]
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.92
					strokeLineCap: StrokeLineCap.BUTT
				},
				Polygon {
					points: [11.55,18.84,11.82,10.91,18.64,14.83]
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.94
					strokeLineCap: StrokeLineCap.BUTT
				},
				Polyline {
					fill: bind windowColorScheme.thirdColorLight
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.19
					strokeLineCap: StrokeLineCap.BUTT
					points: [39.93,25.22]
				},
				Polygon {
					points: [39.93,25.22,39.95,18.73,7.59,0.83,1.02,0.75,1.05,3.14]
					fill: bind windowColorScheme.secondColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.94
					strokeLineCap: StrokeLineCap.BUTT
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M32.88,23.30 C32.84,23.26 32.80,23.24 32.75,23.22 C32.58,22.83 32.22,22.65 31.79,22.72 C31.47,22.77 31.24,23.06 31.25,23.39 C31.26,23.58 31.32,23.79 31.41,23.99 C31.48,24.35 31.66,24.64 32.09,24.67 C32.16,24.70 32.24,24.71 32.32,24.71 C32.71,24.70 32.93,24.37 33.01,24.00 C33.06,23.74 33.09,23.49 32.88,23.30 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M32.87,28.04 C32.83,28.00 32.78,27.98 32.73,27.95 C32.54,27.53 32.16,27.34 31.69,27.41 C31.34,27.46 31.10,27.78 31.11,28.14 C31.12,28.33 31.18,28.56 31.29,28.78 C31.36,29.17 31.56,29.48 32.01,29.51 C32.09,29.54 32.18,29.56 32.27,29.56 C32.69,29.55 32.92,29.19 33.00,28.79 C33.06,28.52 33.09,28.24 32.87,28.04 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M19.17,23.61 C19.13,23.57 19.09,23.55 19.04,23.53 C18.86,23.14 18.51,22.96 18.07,23.02 C17.75,23.08 17.53,23.37 17.54,23.70 C17.54,23.88 17.60,24.10 17.70,24.30 C17.76,24.66 17.95,24.95 18.37,24.98 C18.45,25.01 18.53,25.02 18.61,25.02 C19.00,25.01 19.21,24.68 19.29,24.31 C19.35,24.05 19.38,23.80 19.17,23.61 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M19.31,28.04 C19.27,28.00 19.22,27.98 19.17,27.95 C18.98,27.53 18.60,27.34 18.13,27.41 C17.79,27.46 17.55,27.78 17.55,28.14 C17.56,28.33 17.63,28.56 17.73,28.78 C17.80,29.17 18.00,29.48 18.45,29.51 C18.54,29.54 18.62,29.56 18.71,29.56 C19.13,29.55 19.36,29.19 19.45,28.79 C19.51,28.52 19.54,28.24 19.31,28.04 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M24.11,25.43 C24.10,25.26 23.96,25.10 23.77,25.17 C23.59,25.15 23.43,25.32 23.45,25.48 C23.43,25.51 23.42,25.54 23.41,25.57 C23.41,25.57 23.41,25.58 23.41,25.58 C23.40,25.61 23.39,25.64 23.39,25.68 C23.39,25.68 23.39,25.69 23.38,25.69 C23.37,25.85 23.40,26.02 23.48,26.18 C23.54,26.31 23.71,26.39 23.84,26.32 C23.99,26.23 24.09,26.14 24.15,25.99 C24.24,25.81 24.21,25.60 24.11,25.43 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M24.23,20.02 C24.23,19.84 24.09,19.68 23.90,19.76 C23.72,19.74 23.56,19.91 23.58,20.07 C23.56,20.10 23.55,20.13 23.54,20.16 C23.54,20.16 23.54,20.17 23.54,20.17 C23.53,20.20 23.52,20.23 23.52,20.27 C23.52,20.27 23.51,20.28 23.51,20.28 C23.50,20.44 23.53,20.61 23.61,20.77 C23.66,20.90 23.84,20.98 23.97,20.91 C24.12,20.82 24.22,20.72 24.28,20.58 C24.37,20.40 24.34,20.19 24.23,20.02 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M30.07,24.52 C30.06,24.27 29.82,24.04 29.51,24.15 C29.20,24.11 28.93,24.36 28.96,24.59 C28.93,24.64 28.91,24.68 28.90,24.72 C28.89,24.73 28.89,24.74 28.89,24.74 C28.88,24.79 28.87,24.83 28.86,24.88 C28.86,24.89 28.85,24.89 28.85,24.90 C28.82,25.13 28.88,25.37 29.01,25.62 C29.11,25.79 29.40,25.92 29.62,25.81 C29.87,25.69 30.04,25.54 30.14,25.34 C30.30,25.07 30.25,24.76 30.07,24.52 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M21.76,24.32 C21.76,24.08 21.52,23.86 21.22,23.97 C20.92,23.93 20.65,24.17 20.68,24.40 C20.66,24.44 20.64,24.48 20.62,24.53 C20.62,24.53 20.62,24.54 20.62,24.54 C20.60,24.59 20.59,24.63 20.58,24.68 C20.58,24.68 20.58,24.69 20.58,24.70 C20.55,24.92 20.60,25.15 20.73,25.39 C20.83,25.56 21.11,25.68 21.32,25.58 C21.56,25.46 21.73,25.32 21.83,25.12 C21.99,24.86 21.94,24.56 21.76,24.32 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M13.90,16.56 C13.90,16.38 13.63,16.21 13.29,16.29 C12.95,16.26 12.65,16.45 12.68,16.61 C12.66,16.65 12.64,16.68 12.62,16.71 C12.62,16.72 12.61,16.72 12.61,16.72 C12.59,16.76 12.58,16.79 12.57,16.83 C12.57,16.83 12.57,16.84 12.57,16.84 C12.54,17.01 12.59,17.19 12.74,17.37 C12.85,17.50 13.16,17.59 13.40,17.51 C13.67,17.42 13.87,17.31 13.98,17.16 C14.15,16.97 14.10,16.74 13.90,16.56 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M13.77,12.86 C13.76,12.67 13.58,12.49 13.33,12.58 C13.09,12.55 12.88,12.74 12.90,12.92 C12.88,12.96 12.87,12.99 12.86,13.03 C12.86,13.03 12.85,13.03 12.85,13.04 C12.84,13.07 12.83,13.11 12.82,13.15 C12.82,13.15 12.82,13.16 12.82,13.17 C12.80,13.34 12.84,13.53 12.94,13.72 C13.02,13.86 13.25,13.95 13.42,13.87 C13.61,13.77 13.75,13.66 13.82,13.50 C13.95,13.30 13.91,13.06 13.77,12.86 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M26.81,20.20 C26.80,20.03 26.66,19.87 26.47,19.95 C26.29,19.92 26.13,20.10 26.15,20.25 C26.13,20.28 26.12,20.31 26.11,20.35 C26.11,20.35 26.11,20.35 26.11,20.36 C26.10,20.39 26.09,20.42 26.09,20.45 C26.09,20.46 26.09,20.46 26.09,20.47 C26.07,20.62 26.10,20.79 26.18,20.96 C26.24,21.09 26.41,21.17 26.54,21.09 C26.69,21.01 26.79,20.91 26.85,20.77 C26.95,20.58 26.92,20.37 26.81,20.20 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M26.69,25.30 C26.69,25.13 26.55,24.97 26.36,25.04 C26.18,25.02 26.02,25.19 26.04,25.35 C26.02,25.38 26.01,25.41 26.00,25.44 C26.00,25.45 26.00,25.45 26.00,25.45 C25.99,25.48 25.98,25.52 25.97,25.55 C25.97,25.55 25.97,25.56 25.97,25.56 C25.95,25.72 25.98,25.89 26.06,26.06 C26.12,26.18 26.30,26.26 26.42,26.19 C26.57,26.11 26.68,26.01 26.73,25.86 C26.83,25.68 26.80,25.47 26.69,25.30 Z "
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.19
					strokeLineCap: StrokeLineCap.BUTT
					startX: 25.18
					startY: 17.81
					endX: 25.24
					endY: 26.61
				},
				Polyline {
					fill: Color.rgb(0x9a,0x99,0x99)
					stroke: Color.rgb(0x47,0x46,0x46)
					strokeWidth: 0.39
					strokeLineCap: StrokeLineCap.BUTT
					points: [25.21,23.12]
				},
				Polygon {
					points: [26.55,24.54,25.16,24.06,23.77,24.51,23.72,23.24,25.16,22.71,26.52,23.18]
					fill: Color.rgb(0xf1,0xf1,0xef)
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.39
					strokeLineCap: StrokeLineCap.BUTT
				},
				Polygon {
					points: [25.18,20.67,23.64,22.49,26.56,22.51]
					fill: bind windowColorScheme.mainColor
					stroke: null
				},
				Polygon {
					points: [1.90,24.38,20.27,14.14,25.03,16.71,1.89,30.10]
					fill: bind windowColorScheme.secondColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.94
					strokeLineCap: StrokeLineCap.BUTT
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
         Designed_artifactIcon{
            windowColorScheme: windowColorScheme
           layoutX: 25
            layoutY: 25
         }
         Designed_artifactIcon{
            windowColorScheme: windowColorScheme
            layoutX: 75
            layoutY: 25
            selected: true
         }
      ]
	}
}
}
