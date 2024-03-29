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
public class Research_questionIcon extends AbstractEloIcon {

public override function clone(): Research_questionIcon {
Research_questionIcon {
selected: selected
size: size
windowColorScheme: windowColorScheme
}
}

public override function createNode(): Node {

return Group {

			content: [
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M7.21,38.46 C5.27,38.46 4.03,36.54 4.68,34.41 C4.89,33.73 5.55,33.13 5.95,32.51 Q6.68,31.36 7.21,30.37 Q7.86,31.46 8.55,32.51 C9.00,33.18 9.51,33.67 9.73,34.41 C10.37,36.54 9.57,38.46 7.21,38.46 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: bind windowColorScheme.mainColor
					strokeLineCap: StrokeLineCap.BUTT
					content: "M37.75,14.94 C37.75,21.80 33.01,27.36 27.17,27.36 C21.32,27.36 16.59,21.80 16.59,14.94 Z "
				},
				SVGPath {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 2.0
					strokeLineCap: StrokeLineCap.BUTT
					content: "M7.10,29.25 L18.77,22.52 C20.71,25.03 23.74,26.64 27.15,26.64 C33.00,26.64 37.73,21.90 37.73,16.06 C37.73,10.22 33.00,5.48 27.15,5.48 C21.31,5.48 16.57,10.22 16.57,16.06 C16.57,17.02 16.70,17.95 16.94,18.83 L5.11,26.39 "
				},
				Polyline {
					fill: Color.rgb(0x6a,0xb3,0x30)
					stroke: null
					points: [6.19,25.43]
				},
				Polygon {
					points: [18.86,22.13,7.36,29.58,5.61,27.08,17.26,19.63]
					fill: bind windowColorScheme.mainColor
					stroke: null
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: null
					content: "M21.73,25.45 C21.71,24.71 21.70,24.29 21.70,24.18 C21.70,21.60 22.75,19.18 24.84,16.99 C26.30,15.44 27.22,14.30 27.65,13.52 C28.08,12.71 28.30,11.97 28.30,11.31 C28.30,9.69 27.51,7.77 23.75,7.77 C21.74,7.77 19.89,8.23 18.25,9.13 L16.65,4.47 C19.23,3.20 22.25,2.56 25.64,2.56 C28.90,2.56 31.47,3.27 33.29,4.68 C35.09,6.07 35.97,7.83 35.97,10.07 C35.97,11.19 35.64,12.39 35.01,13.63 C34.36,14.91 33.11,16.48 31.28,18.30 C29.27,20.24 28.25,22.53 28.25,25.08 L28.25,25.45 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M25.64,3.06 C28.78,3.06 31.25,3.74 32.98,5.07 C34.65,6.36 35.47,8.00 35.47,10.07 C35.47,11.11 35.16,12.23 34.56,13.41 C33.94,14.64 32.71,16.16 30.94,17.93 C28.86,19.95 27.78,22.31 27.75,24.95 L22.22,24.95 C22.20,24.52 22.20,24.26 22.20,24.18 C22.20,21.70 23.18,19.46 25.20,17.34 C26.70,15.75 27.64,14.58 28.09,13.76 C28.57,12.86 28.80,12.06 28.80,11.31 C28.80,9.80 28.15,7.27 23.75,7.27 C21.87,7.27 20.12,7.66 18.53,8.42 L17.27,4.73 C19.69,3.62 22.50,3.06 25.64,3.06 M25.64,2.06 C21.99,2.06 18.79,2.78 16.03,4.22 L17.97,9.87 C19.68,8.81 21.60,8.27 23.75,8.27 C26.45,8.27 27.80,9.28 27.80,11.31 C27.80,11.89 27.60,12.55 27.21,13.29 C26.81,14.02 25.90,15.14 24.47,16.65 C22.29,18.95 21.20,21.45 21.20,24.18 C21.20,24.31 21.21,24.90 21.25,25.95 L28.75,25.95 L28.75,25.08 C28.75,22.66 29.71,20.52 31.64,18.65 C33.51,16.79 34.78,15.19 35.45,13.86 C36.13,12.53 36.47,11.27 36.47,10.07 C36.47,7.69 35.51,5.76 33.59,4.28 C31.68,2.80 29.02,2.06 25.64,2.06 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: null
					content: "M21.81,25.52 C21.79,24.79 21.78,24.36 21.78,24.25 C21.78,21.67 22.83,19.26 24.92,17.07 C26.38,15.51 27.30,14.38 27.73,13.60 C28.16,12.78 28.38,12.04 28.38,11.38 C28.38,9.77 27.59,7.85 23.83,7.85 C21.82,7.85 19.97,8.30 18.33,9.20 L16.73,4.54 C19.31,3.27 22.33,2.63 25.72,2.63 C28.98,2.63 31.55,3.34 33.37,4.75 C35.17,6.14 36.05,7.91 36.05,10.14 C36.05,11.26 35.72,12.46 35.09,13.71 C34.44,14.99 33.19,16.55 31.36,18.37 C29.35,20.32 28.33,22.60 28.33,25.15 L28.33,25.52 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M25.72,3.13 C28.86,3.13 31.33,3.81 33.06,5.14 C34.73,6.44 35.55,8.07 35.55,10.14 C35.55,11.18 35.24,12.30 34.64,13.48 C34.02,14.71 32.80,16.24 31.02,18.01 C28.94,20.02 27.86,22.38 27.83,25.02 L22.30,25.02 C22.28,24.59 22.28,24.33 22.28,24.25 C22.28,21.77 23.26,19.53 25.28,17.41 C26.78,15.82 27.72,14.65 28.17,13.83 C28.65,12.94 28.88,12.13 28.88,11.38 C28.88,9.87 28.23,7.35 23.83,7.35 C21.95,7.35 20.20,7.73 18.61,8.49 L17.35,4.80 C19.77,3.69 22.58,3.13 25.72,3.13 M25.72,2.13 C22.07,2.13 18.87,2.85 16.11,4.29 L18.05,9.94 C19.76,8.88 21.68,8.35 23.83,8.35 C26.53,8.35 27.88,9.36 27.88,11.38 C27.88,11.96 27.68,12.62 27.29,13.36 C26.89,14.10 25.98,15.22 24.55,16.72 C22.37,19.02 21.28,21.53 21.28,24.25 C21.28,24.39 21.30,24.98 21.33,26.02 L28.83,26.02 L28.83,25.15 C28.83,22.74 29.79,20.59 31.72,18.73 C33.59,16.86 34.86,15.26 35.53,13.93 C36.21,12.61 36.55,11.34 36.55,10.14 C36.55,7.77 35.59,5.83 33.67,4.35 C31.76,2.87 29.10,2.13 25.72,2.13 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: null
					content: "M25.41,37.76 C24.05,37.76 22.97,37.37 22.12,36.56 C21.27,35.76 20.86,34.77 20.86,33.53 C20.86,32.28 21.27,31.28 22.11,30.49 C22.97,29.70 24.05,29.31 25.41,29.31 C26.77,29.31 27.85,29.70 28.71,30.49 C29.55,31.28 29.96,32.28 29.96,33.53 C29.96,34.78 29.56,35.78 28.72,36.57 C27.87,37.37 26.79,37.76 25.41,37.76 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M25.41,29.81 C26.64,29.81 27.61,30.15 28.36,30.86 C29.10,31.55 29.46,32.42 29.46,33.53 C29.46,34.64 29.11,35.52 28.38,36.21 C27.63,36.92 26.66,37.26 25.41,37.26 C24.18,37.26 23.22,36.91 22.46,36.19 C21.72,35.49 21.36,34.62 21.36,33.53 C21.36,32.42 21.71,31.55 22.46,30.86 C23.21,30.15 24.18,29.81 25.41,29.81 M25.41,28.81 C23.93,28.81 22.72,29.25 21.77,30.13 C20.83,31.01 20.36,32.15 20.36,33.53 C20.36,34.90 20.83,36.03 21.77,36.92 C22.72,37.81 23.93,38.26 25.41,38.26 C26.91,38.26 28.13,37.82 29.06,36.93 C29.99,36.05 30.46,34.92 30.46,33.53 C30.46,32.15 29.99,31.01 29.05,30.13 C28.10,29.25 26.89,28.81 25.41,28.81 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: null
					content: "M25.43,37.64 C24.07,37.64 22.99,37.25 22.13,36.44 C21.29,35.64 20.87,34.65 20.87,33.42 C20.87,32.16 21.28,31.17 22.13,30.38 C22.98,29.58 24.06,29.19 25.43,29.19 C26.79,29.19 27.87,29.58 28.72,30.38 C29.57,31.17 29.98,32.16 29.98,33.42 C29.98,34.67 29.57,35.66 28.74,36.46 C27.89,37.26 26.81,37.64 25.43,37.64 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M25.43,29.69 C26.66,29.69 27.63,30.04 28.38,30.74 C29.12,31.43 29.48,32.31 29.48,33.42 C29.48,34.53 29.12,35.40 28.39,36.09 C27.64,36.80 26.67,37.14 25.43,37.14 C24.20,37.14 23.24,36.80 22.48,36.08 C21.73,35.38 21.37,34.51 21.37,33.42 C21.37,32.31 21.73,31.43 22.47,30.74 C23.23,30.04 24.19,29.69 25.43,29.69 M25.43,28.69 C23.95,28.69 22.73,29.13 21.79,30.01 C20.85,30.89 20.37,32.03 20.37,33.42 C20.37,34.78 20.85,35.92 21.79,36.81 C22.73,37.70 23.95,38.14 25.43,38.14 C26.93,38.14 28.14,37.70 29.08,36.82 C30.01,35.94 30.48,34.80 30.48,33.42 C30.48,32.03 30.01,30.89 29.06,30.01 C28.12,29.13 26.91,28.69 25.43,28.69 Z "
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
         Research_questionIcon{
            windowColorScheme: windowColorScheme
           layoutX: 25
            layoutY: 25
         }
         Research_questionIcon{
            windowColorScheme: windowColorScheme
            layoutX: 75
            layoutY: 25
            selected: true
         }
      ]
	}
}
}
