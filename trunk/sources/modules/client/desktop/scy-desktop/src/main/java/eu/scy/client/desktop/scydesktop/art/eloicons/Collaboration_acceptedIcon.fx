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
public class Collaboration_acceptedIcon extends AbstractEloIcon {

public override function clone(): Collaboration_acceptedIcon {
Collaboration_acceptedIcon {
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
					content: "M39.78,18.10 C39.64,14.34 41.46,4.91 37.69,2.29 C36.28,1.31 34.46,1.30 32.89,1.80 C29.58,2.85 26.53,3.39 23.12,3.88 C20.81,4.21 18.78,5.70 16.90,7.00 C12.06,10.34 8.51,6.10 5.01,3.66 C3.03,2.27 0.55,4.52 1.23,6.35 C1.12,6.64 1.05,6.96 1.06,7.32 C1.26,17.07 -3.66,32.54 7.98,37.06 C16.52,40.36 32.09,39.76 38.93,32.79 C41.21,30.47 39.92,21.63 39.78,18.10 Z M29.80,32.54 C24.75,34.25 19.22,34.42 14.00,33.74 C11.42,33.40 8.53,32.74 6.68,30.73 C4.95,28.85 5.23,24.88 5.24,22.58 C5.26,18.42 5.70,14.28 5.84,10.12 C7.55,11.33 9.32,12.41 11.36,13.01 C14.37,13.91 17.45,12.09 19.91,10.56 C23.92,8.05 27.67,8.51 31.95,6.96 C34.82,5.91 34.68,6.54 34.89,9.39 C35.12,12.52 34.87,15.70 34.97,18.84 C35.05,21.42 35.19,24.00 35.32,26.57 C35.51,30.41 32.83,31.51 29.80,32.54 Z "
				},
				Polyline {
					fill: bind windowColorScheme.mainColor
					stroke: null
					points: [25.29,14.29]
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M9.24,10.45 L5.01,24.35 L7.80,25.09 L8.70,21.68 Q9.26,20.28 10.70,20.67 Q12.14,21.06 12.13,22.88 Q13.17,21.28 14.43,21.69 C15.70,22.10 15.72,23.96 15.74,23.92 C15.75,23.88 16.21,22.36 17.91,23.16 Q19.60,23.96 19.01,26.11 Q19.62,24.89 21.25,25.63 Q22.53,26.22 22.27,28.57 L25.47,32.23 C26.18,33.08 27.11,33.28 27.78,32.42 C28.45,31.56 28.42,30.17 27.71,29.32 Q23.36,24.68 23.35,24.29 Q23.33,23.90 23.78,24.09 L28.98,30.13 Q29.92,31.71 31.14,30.10 Q32.35,28.50 31.06,27.21 Q26.58,22.39 26.56,22.10 Q26.54,21.80 26.92,21.69 Q30.29,25.47 31.44,26.96 Q32.60,28.45 33.70,26.96 Q34.74,25.76 33.58,24.12 L24.95,14.28 L22.66,13.95 Q20.28,24.06 16.10,18.97 L19.14,10.07 L9.99,10.45 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: null
					content: "M31.63,8.52 L22.51,7.63 Q20.56,7.89 20.27,8.81 Q19.98,9.72 17.17,18.32 Q19.72,19.86 20.80,16.39 Q21.93,12.78 23.20,11.97 L24.87,12.26 L33.99,22.75 L35.66,21.84 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M23.17,35.00 C23.02,34.97 21.98,34.18 21.05,33.42 L22.28,29.41 L24.50,32.24 C24.49,32.24 25.06,33.73 24.53,34.52 C24.31,34.84 23.92,35.01 23.38,35.01 Q23.25,35.00 23.17,35.00 Z "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: null
					content: "M22.32,29.62 L24.40,32.28 C24.56,32.71 24.86,33.86 24.44,34.47 C24.24,34.76 23.89,34.91 23.38,34.91 C23.32,34.91 23.25,34.90 23.20,34.90 C23.02,34.85 22.04,34.10 21.16,33.39 Z M22.24,29.20 L20.93,33.46 Q22.92,35.08 23.17,35.10 C23.24,35.10 23.31,35.11 23.38,35.11 Q25.72,35.11 24.58,32.18 Z "
				},
				Polygon {
					points: [0.97,29.86,0.97,4.26,8.26,7.54,2.21,29.90]
					fill: bind windowColorScheme.mainColor
					stroke: null
				},
				SVGPath {
					fill: Color.WHITE
					stroke: null
					content: "M1.47,5.03 L7.66,7.82 L1.83,29.39 L1.47,29.38 Z M0.47,3.49 L0.47,30.34 L2.59,30.41 L8.85,7.25 Z "
				},
				Polygon {
					points: [32.58,6.79,39.70,3.65,39.70,25.42,38.62,26.47]
					fill: bind windowColorScheme.secondColor
					stroke: null
				},
				SVGPath {
					fill: Color.WHITE
					stroke: null
					content: "M39.20,4.41 L39.20,25.21 L38.86,25.54 L33.18,7.07 Z M40.20,2.88 L31.97,6.51 L38.38,27.41 L40.20,25.63 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M14.94,25.31 C15.31,24.23 15.04,23.07 14.33,22.71 C13.62,22.35 12.75,22.94 12.38,24.02 L11.12,27.73 C10.75,28.81 11.02,29.98 11.73,30.33 C12.44,30.69 13.31,30.10 13.69,29.03 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M18.31,26.86 C18.64,25.77 18.32,24.61 17.60,24.27 C16.88,23.93 16.02,24.53 15.69,25.62 L14.58,29.37 C14.25,30.45 14.57,31.62 15.30,31.95 C16.02,32.30 16.87,31.69 17.20,30.60 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M21.53,29.23 C21.87,28.14 21.58,26.99 20.88,26.64 C20.18,26.30 19.33,26.89 18.99,27.98 L17.98,31.16 C17.68,32.22 17.98,33.32 18.66,33.65 C19.36,34.00 20.20,33.40 20.54,32.32 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M8.72,23.47 L7.91,25.77 C7.52,26.84 7.78,28.01 8.48,28.39 C9.19,28.76 10.08,28.19 10.47,27.12 L11.29,24.80 C11.68,23.72 11.43,22.55 10.72,22.17 C10.02,21.80 9.13,22.37 8.73,23.44 C8.73,23.46 8.72,23.47 8.71,23.49 "
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
         Collaboration_acceptedIcon{
            windowColorScheme: windowColorScheme
           layoutX: 25
            layoutY: 25
         }
         Collaboration_acceptedIcon{
            windowColorScheme: windowColorScheme
            layoutX: 75
            layoutY: 25
            selected: true
         }
      ]
	}
}
}
