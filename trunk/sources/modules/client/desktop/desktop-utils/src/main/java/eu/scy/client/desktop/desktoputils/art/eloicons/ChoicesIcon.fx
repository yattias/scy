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
public class ChoicesIcon extends AbstractEloIcon {

public override function clone(): ChoicesIcon {
ChoicesIcon {
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
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.22
					content: "M32.37,1.10 C24.74,1.58 17.06,1.24 9.42,1.30 C-1.06,1.40 -1.26,19.51 9.21,19.41 C16.85,19.35 24.53,19.69 32.16,19.21 C36.58,18.93 40.32,15.57 40.38,10.35 C40.44,5.71 36.78,0.82 32.37,1.10 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColorLight
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.22
					content: "M35.59,22.28 C35.56,22.25 35.54,22.23 35.51,22.21 C33.54,20.72 31.52,20.42 29.67,20.92 C29.69,20.87 29.70,20.82 29.71,20.77 C28.54,20.98 24.24,20.30 22.08,20.15 C17.79,19.85 13.51,19.24 9.22,18.90 C-0.96,18.10 -2.92,37.01 7.25,37.81 C11.55,38.14 15.82,38.56 20.11,39.06 C23.91,39.50 28.16,40.13 31.99,39.42 C38.71,38.16 41.33,26.63 35.59,22.28 Z "
				},
				Polyline {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.76
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					points: [27.14,17.40]
				},
				Polyline {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.35
					strokeLineCap: StrokeLineCap.BUTT
					strokeMiterLimit: 4.0
					points: [15.21,21.68]
				},
				Polyline {
					fill: bind windowColorScheme.mainColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.22
					points: [36.60,36.26,36.60,22.25,9.77,22.25,5.16,29.46,9.77,36.66,21.71,36.66,33.15,36.55,36.60,36.55]
				},
				Polygon {
					points: [31.55,3.78,36.16,10.99,31.55,18.19,4.72,18.19,4.72,3.78]
					fill: bind windowColorScheme.mainColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 0.22
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: bind windowColorScheme.emptyBackgroundColor
					strokeWidth: 0.35
					strokeLineCap: StrokeLineCap.BUTT
					content: "M21.54,13.33 L18.89,13.33 C18.88,12.96 18.87,12.75 18.87,12.70 C18.87,11.74 19.25,10.85 20.03,10.04 C20.53,9.50 20.85,9.11 21.00,8.85 C21.14,8.59 21.21,8.35 21.21,8.15 C21.21,7.43 20.73,7.07 19.77,7.07 C19.01,7.07 18.33,7.26 17.73,7.64 L17.04,5.64 C18.02,5.13 19.15,4.88 20.44,4.88 C21.64,4.88 22.58,5.14 23.25,5.66 C23.93,6.19 24.27,6.87 24.27,7.71 C24.27,8.13 24.15,8.58 23.91,9.05 C23.67,9.52 23.22,10.09 22.56,10.75 C21.88,11.41 21.54,12.16 21.54,13.02 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: bind windowColorScheme.emptyBackgroundColor
					strokeWidth: 0.44
					strokeLineCap: StrokeLineCap.BUTT
					content: "M21.57,13.35 L18.92,13.35 C18.90,12.98 18.90,12.77 18.90,12.73 C18.90,11.76 19.28,10.88 20.06,10.06 C20.56,9.53 20.88,9.13 21.02,8.87 C21.16,8.61 21.23,8.38 21.23,8.17 C21.23,7.46 20.76,7.10 19.80,7.10 C19.04,7.10 18.36,7.29 17.76,7.66 L17.07,5.66 C18.05,5.16 19.18,4.90 20.47,4.90 C21.67,4.90 22.61,5.16 23.28,5.69 C23.96,6.21 24.30,6.90 24.30,7.74 C24.30,8.16 24.18,8.61 23.94,9.08 C23.70,9.55 23.25,10.11 22.59,10.77 C21.91,11.43 21.57,12.19 21.57,13.05 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: bind windowColorScheme.emptyBackgroundColor
					strokeWidth: 0.35
					strokeLineCap: StrokeLineCap.BUTT
					content: "M22.15,16.01 C22.15,16.50 21.98,16.90 21.65,17.22 C21.32,17.53 20.89,17.68 20.36,17.68 C19.84,17.68 19.41,17.53 19.07,17.21 C18.74,16.89 18.57,16.49 18.57,16.01 C18.57,15.52 18.74,15.12 19.07,14.81 C19.41,14.49 19.84,14.34 20.36,14.34 C20.88,14.34 21.31,14.49 21.65,14.81 C21.98,15.12 22.15,15.52 22.15,16.01 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: bind windowColorScheme.emptyBackgroundColor
					strokeWidth: 0.35
					strokeLineCap: StrokeLineCap.BUTT
					content: "M22.15,15.97 C22.15,16.46 21.99,16.86 21.66,17.17 C21.33,17.49 20.90,17.64 20.37,17.64 C19.84,17.64 19.41,17.48 19.08,17.17 C18.74,16.85 18.58,16.45 18.58,15.97 C18.58,15.48 18.74,15.08 19.08,14.77 C19.41,14.45 19.84,14.30 20.37,14.30 C20.89,14.30 21.32,14.45 21.65,14.77 C21.99,15.08 22.15,15.48 22.15,15.97 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: bind windowColorScheme.emptyBackgroundColor
					strokeWidth: 0.35
					strokeLineCap: StrokeLineCap.BUTT
					content: "M19.80,31.49 L22.45,31.49 C22.47,31.12 22.47,30.91 22.47,30.86 C22.47,29.90 22.09,29.01 21.31,28.20 C20.81,27.67 20.49,27.27 20.35,27.01 C20.21,26.75 20.14,26.52 20.14,26.31 C20.14,25.60 20.61,25.24 21.57,25.24 C22.33,25.24 23.01,25.43 23.61,25.80 L24.30,23.80 C23.33,23.29 22.19,23.04 20.90,23.04 C19.70,23.04 18.77,23.30 18.09,23.82 C17.41,24.35 17.07,25.03 17.07,25.87 C17.07,26.30 17.19,26.75 17.43,27.22 C17.67,27.68 18.12,28.25 18.78,28.91 C19.46,29.57 19.80,30.33 19.80,31.18 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: bind windowColorScheme.emptyBackgroundColor
					strokeWidth: 0.44
					strokeLineCap: StrokeLineCap.BUTT
					content: "M19.77,31.52 L22.43,31.52 C22.44,31.15 22.45,30.94 22.45,30.89 C22.45,29.93 22.06,29.04 21.29,28.23 C20.78,27.69 20.46,27.30 20.32,27.04 C20.18,26.78 20.11,26.54 20.11,26.34 C20.11,25.62 20.59,25.26 21.54,25.26 C22.30,25.26 22.98,25.45 23.59,25.83 L24.27,23.83 C23.30,23.32 22.16,23.07 20.87,23.07 C19.68,23.07 18.74,23.33 18.06,23.85 C17.38,24.37 17.04,25.06 17.04,25.90 C17.04,26.32 17.16,26.77 17.40,27.24 C17.64,27.71 18.09,28.28 18.75,28.94 C19.43,29.60 19.77,30.35 19.77,31.21 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: bind windowColorScheme.emptyBackgroundColor
					strokeWidth: 0.35
					strokeLineCap: StrokeLineCap.BUTT
					content: "M19.20,34.18 C19.20,34.67 19.36,35.07 19.69,35.38 C20.02,35.69 20.45,35.85 20.98,35.85 C21.51,35.85 21.93,35.69 22.27,35.37 C22.60,35.06 22.77,34.66 22.77,34.18 C22.77,33.68 22.60,33.28 22.27,32.97 C21.93,32.66 21.51,32.50 20.98,32.50 C20.46,32.50 20.03,32.66 19.70,32.97 C19.36,33.28 19.20,33.68 19.20,34.18 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: bind windowColorScheme.emptyBackgroundColor
					strokeWidth: 0.35
					strokeLineCap: StrokeLineCap.BUTT
					content: "M19.19,34.13 C19.19,34.62 19.35,35.03 19.68,35.34 C20.02,35.65 20.45,35.81 20.98,35.81 C21.50,35.81 21.93,35.65 22.26,35.33 C22.60,35.02 22.76,34.62 22.76,34.13 C22.76,33.64 22.60,33.24 22.26,32.93 C21.93,32.62 21.50,32.46 20.98,32.46 C20.45,32.46 20.02,32.62 19.69,32.93 C19.36,33.24 19.19,33.64 19.19,34.13 Z "
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
         ChoicesIcon{
            windowColorScheme: windowColorScheme
           layoutX: 25
            layoutY: 25
         }
         ChoicesIcon{
            windowColorScheme: windowColorScheme
            layoutX: 75
            layoutY: 25
            selected: true
         }
      ]
	}
}
}
