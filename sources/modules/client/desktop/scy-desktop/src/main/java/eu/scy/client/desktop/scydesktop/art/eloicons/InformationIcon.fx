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
public class InformationIcon extends AbstractEloIcon {

public override function clone(): InformationIcon {
InformationIcon {
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
					stroke: bind windowColorScheme.secondColorLight
					strokeWidth: 1.0
					strokeLineCap: StrokeLineCap.BUTT
					content: "M23.88,1.89 C14.98,-0.88 4.18,3.58 1.59,13.06 C0.39,17.44 0.83,22.62 2.67,26.72 C4.67,31.18 9.71,32.91 13.91,34.26 C16.56,35.11 18.03,30.97 15.37,30.11 C12.06,29.05 8.57,28.09 6.59,24.89 C4.93,22.19 5.23,18.44 5.52,15.44 C6.39,6.48 16.87,4.31 23.83,6.47 C31.18,8.76 35.43,13.93 35.62,21.75 C35.70,25.20 35.22,28.66 34.69,32.06 C34.50,32.82 34.31,33.58 34.12,34.35 C33.82,35.55 33.72,35.54 33.82,34.32 C32.99,33.49 32.16,32.78 31.25,32.05 C29.07,30.28 26.63,33.92 28.80,35.68 C31.03,37.47 34.00,41.64 36.84,38.52 C39.87,35.17 39.69,27.94 39.89,23.78 C40.43,12.37 34.39,5.15 23.88,1.89 Z "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 2.06
					strokeLineCap: StrokeLineCap.BUTT
					content: "M28.33,30.33 L34.72,34.46 L34.04,24.92 C35.87,23.14 37.06,20.92 37.35,18.44 C38.17,11.47 31.52,5.04 22.50,4.09 C13.49,3.13 5.51,8.01 4.70,14.99 C3.92,21.56 9.80,27.66 18.04,29.12 "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: null
					content: "M18.86,25.33 C18.83,24.61 18.82,24.19 18.82,24.08 C18.82,21.54 19.86,19.17 21.91,17.02 C23.35,15.49 24.26,14.37 24.67,13.60 C25.10,12.80 25.32,12.07 25.32,11.42 C25.32,9.84 24.55,7.95 20.84,7.95 C18.86,7.95 17.05,8.39 15.43,9.28 L13.85,4.69 C16.40,3.45 19.37,2.82 22.70,2.82 C25.90,2.82 28.43,3.52 30.22,4.90 C32.00,6.27 32.86,8.00 32.86,10.20 C32.86,11.30 32.54,12.48 31.91,13.71 C31.28,14.97 30.05,16.51 28.25,18.30 C26.27,20.22 25.27,22.46 25.27,24.97 L25.27,25.33 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M22.70,3.31 C25.79,3.31 28.22,3.97 29.92,5.29 C31.57,6.56 32.37,8.17 32.37,10.20 C32.37,11.22 32.07,12.33 31.47,13.49 C30.86,14.70 29.66,16.20 27.91,17.94 C25.86,19.93 24.81,22.25 24.78,24.84 L19.33,24.84 C19.32,24.42 19.31,24.16 19.31,24.08 C19.31,21.64 20.28,19.44 22.27,17.35 C23.74,15.80 24.67,14.64 25.11,13.83 C25.58,12.95 25.81,12.16 25.81,11.42 C25.81,9.94 25.17,7.45 20.84,7.45 C18.99,7.45 17.27,7.83 15.71,8.58 L14.46,4.95 C16.85,3.86 19.62,3.31 22.70,3.31 M22.70,2.33 C19.11,2.33 15.96,3.03 13.25,4.45 L15.16,10.00 C16.83,8.96 18.73,8.44 20.84,8.44 C23.50,8.44 24.83,9.43 24.83,11.42 C24.83,11.99 24.63,12.64 24.24,13.37 C23.85,14.09 22.95,15.20 21.55,16.68 C19.40,18.94 18.33,21.40 18.33,24.08 C18.33,24.22 18.35,24.80 18.38,25.82 L25.76,25.82 L25.76,24.97 C25.76,22.59 26.70,20.48 28.60,18.65 C30.44,16.81 31.69,15.24 32.35,13.93 C33.02,12.63 33.35,11.38 33.35,10.20 C33.35,7.87 32.41,5.97 30.52,4.51 C28.64,3.05 26.03,2.33 22.70,2.33 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: null
					content: "M18.94,25.40 C18.91,24.68 18.90,24.26 18.90,24.15 C18.90,21.62 19.94,19.24 21.99,17.09 C23.44,15.56 24.34,14.44 24.75,13.67 C25.18,12.87 25.40,12.14 25.40,11.49 C25.40,9.91 24.62,8.02 20.92,8.02 C18.94,8.02 17.13,8.47 15.51,9.35 L13.93,4.76 C16.48,3.52 19.45,2.89 22.78,2.89 C25.98,2.89 28.51,3.59 30.30,4.97 C32.08,6.34 32.94,8.08 32.94,10.28 C32.94,11.37 32.62,12.56 31.99,13.78 C31.36,15.04 30.12,16.58 28.33,18.37 C26.35,20.28 25.35,22.53 25.35,25.04 L25.35,25.40 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M22.78,3.38 C25.87,3.38 28.30,4.05 30.00,5.36 C31.65,6.63 32.45,8.24 32.45,10.28 C32.45,11.30 32.15,12.40 31.55,13.56 C30.94,14.77 29.74,16.27 27.99,18.01 C25.94,19.99 24.89,22.32 24.86,24.91 L19.41,24.91 C19.40,24.49 19.39,24.23 19.39,24.15 C19.39,21.71 20.36,19.51 22.35,17.42 C23.82,15.86 24.75,14.71 25.19,13.91 C25.66,13.02 25.89,12.23 25.89,11.49 C25.89,10.01 25.25,7.53 20.92,7.53 C19.07,7.53 17.35,7.90 15.79,8.65 L14.54,5.02 C16.93,3.93 19.69,3.38 22.78,3.38 M22.78,2.40 C19.19,2.40 16.04,3.10 13.33,4.52 L15.24,10.08 C16.91,9.03 18.81,8.51 20.92,8.51 C23.58,8.51 24.91,9.50 24.91,11.49 C24.91,12.07 24.71,12.71 24.32,13.44 C23.93,14.17 23.03,15.27 21.63,16.75 C19.48,19.01 18.41,21.47 18.41,24.15 C18.41,24.29 18.43,24.87 18.46,25.90 L25.84,25.90 L25.84,25.04 C25.84,22.66 26.78,20.55 28.68,18.72 C30.52,16.89 31.77,15.31 32.43,14.01 C33.10,12.70 33.43,11.46 33.43,10.28 C33.43,7.94 32.49,6.04 30.60,4.58 C28.72,3.13 26.11,2.40 22.78,2.40 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: null
					content: "M22.47,37.44 C21.14,37.44 20.08,37.06 19.23,36.26 C18.40,35.47 17.99,34.50 17.99,33.28 C17.99,32.05 18.40,31.07 19.23,30.29 C20.07,29.51 21.13,29.13 22.47,29.13 C23.82,29.13 24.88,29.51 25.72,30.29 C26.55,31.07 26.95,32.05 26.95,33.28 C26.95,34.52 26.55,35.49 25.73,36.27 C24.90,37.06 23.84,37.44 22.47,37.44 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M22.47,29.62 C23.69,29.62 24.64,29.96 25.38,30.65 C26.11,31.33 26.46,32.19 26.46,33.28 C26.46,34.37 26.11,35.23 25.39,35.92 C24.66,36.61 23.70,36.95 22.47,36.95 C21.27,36.95 20.32,36.61 19.57,35.90 C18.84,35.21 18.49,34.36 18.49,33.28 C18.49,32.19 18.84,31.33 19.57,30.65 C20.31,29.96 21.26,29.62 22.47,29.62 M22.47,28.64 C21.02,28.64 19.82,29.07 18.90,29.93 C17.97,30.80 17.50,31.92 17.50,33.28 C17.50,34.63 17.97,35.74 18.90,36.62 C19.82,37.49 21.02,37.93 22.47,37.93 C23.95,37.93 25.15,37.50 26.07,36.63 C26.98,35.76 27.45,34.65 27.45,33.28 C27.45,31.92 26.98,30.80 26.05,29.93 C25.12,29.07 23.93,28.64 22.47,28.64 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: null
					content: "M22.49,37.33 C21.16,37.33 20.10,36.94 19.25,36.15 C18.42,35.36 18.01,34.38 18.01,33.17 C18.01,31.94 18.42,30.96 19.25,30.18 C20.09,29.40 21.15,29.02 22.49,29.02 C23.84,29.02 24.90,29.40 25.73,30.18 C26.57,30.96 26.97,31.94 26.97,33.17 C26.97,34.40 26.57,35.38 25.75,36.16 C24.91,36.95 23.85,37.33 22.49,37.33 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M22.49,29.51 C23.71,29.51 24.66,29.85 25.40,30.54 C26.13,31.22 26.48,32.08 26.48,33.17 C26.48,34.26 26.13,35.12 25.41,35.80 C24.67,36.50 23.72,36.84 22.49,36.84 C21.29,36.84 20.34,36.50 19.59,35.79 C18.86,35.10 18.50,34.24 18.50,33.17 C18.50,32.08 18.86,31.22 19.59,30.54 C20.33,29.85 21.28,29.51 22.49,29.51 M22.49,28.52 C21.03,28.52 19.84,28.95 18.91,29.82 C17.98,30.69 17.52,31.81 17.52,33.17 C17.52,34.52 17.98,35.63 18.91,36.51 C19.84,37.38 21.03,37.82 22.49,37.82 C23.97,37.82 25.16,37.39 26.08,36.52 C27.00,35.65 27.46,34.53 27.46,33.17 C27.46,31.81 27.00,30.69 26.07,29.82 C25.14,28.95 23.95,28.52 22.49,28.52 Z "
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
         InformationIcon{
            windowColorScheme: windowColorScheme
           layoutX: 25
            layoutY: 25
         }
         InformationIcon{
            windowColorScheme: windowColorScheme
            layoutX: 75
            layoutY: 25
            selected: true
         }
      ]
	}
}
}
