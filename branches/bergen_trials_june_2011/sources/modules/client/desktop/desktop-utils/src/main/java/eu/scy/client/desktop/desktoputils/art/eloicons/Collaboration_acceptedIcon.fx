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
					content: "M40.01,17.85 C39.87,14.09 41.69,4.66 37.91,2.04 C36.50,1.07 34.68,1.06 33.11,1.56 C29.80,2.60 26.75,3.15 23.35,3.64 C21.03,3.97 19.01,5.46 17.12,6.76 C12.29,10.10 8.74,5.86 5.24,3.41 C3.25,2.03 0.78,4.27 1.46,6.11 C1.34,6.40 1.28,6.72 1.28,7.08 C1.48,16.82 -3.43,32.29 8.21,36.81 C16.74,40.12 32.31,39.52 39.16,32.55 C41.43,30.23 40.14,21.38 40.01,17.85 Z M30.02,32.29 C24.97,34.01 19.45,34.18 14.22,33.49 C11.64,33.16 8.75,32.50 6.90,30.48 C5.18,28.61 5.45,24.64 5.46,22.34 C5.48,18.17 5.92,14.04 6.06,9.88 C7.77,11.08 9.54,12.16 11.58,12.77 C14.60,13.67 17.67,11.85 20.13,10.31 C24.14,7.81 27.90,8.27 32.17,6.71 C35.05,5.67 34.90,6.30 35.11,9.14 C35.35,12.27 35.10,15.46 35.19,18.60 C35.27,21.18 35.41,23.75 35.54,26.33 C35.73,30.17 33.05,31.26 30.02,32.29 Z "
				},
				Polyline {
					fill: bind windowColorScheme.mainColor
					stroke: null
					points: [25.51,14.05]
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M9.46,10.20 L5.23,24.11 L8.03,24.85 L8.93,21.44 Q9.49,20.03 10.93,20.42 Q12.36,20.81 12.35,22.63 Q13.39,21.03 14.66,21.45 C15.92,21.86 15.94,23.72 15.96,23.68 C15.98,23.63 16.44,22.12 18.13,22.92 Q19.83,23.71 19.23,25.87 Q19.84,24.64 21.47,25.39 Q22.76,25.98 22.50,28.32 L25.70,31.98 C26.40,32.84 27.34,33.04 28.00,32.18 C28.67,31.32 28.64,29.93 27.94,29.07 Q23.59,24.43 23.57,24.05 Q23.55,23.66 24.00,23.84 L29.21,29.88 Q30.15,31.46 31.36,29.86 Q32.57,28.25 31.29,26.96 Q26.81,22.15 26.79,21.85 Q26.77,21.56 27.15,21.44 Q30.51,25.22 31.67,26.71 Q32.82,28.21 33.93,26.71 Q34.97,25.52 33.80,23.88 L25.17,14.03 L22.89,13.70 Q20.50,23.81 16.33,18.73 L19.36,9.83 L10.21,10.20 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: null
					content: "M31.85,8.28 L22.74,7.39 Q20.79,7.65 20.49,8.56 Q20.20,9.48 17.40,18.07 Q19.95,19.62 21.03,16.15 Q22.16,12.53 23.42,11.72 L25.09,12.02 L34.21,22.51 L35.89,21.60 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M23.40,34.76 C23.24,34.73 22.20,33.94 21.27,33.18 L22.51,29.17 L24.72,32.00 C24.72,32.00 25.29,33.49 24.75,34.28 C24.53,34.60 24.15,34.76 23.61,34.76 Q23.47,34.76 23.40,34.76 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: null
					content: "M22.55,29.38 L24.63,32.03 C24.79,32.46 25.08,33.61 24.67,34.22 C24.47,34.51 24.11,34.66 23.61,34.66 C23.54,34.66 23.48,34.66 23.42,34.66 C23.25,34.60 22.27,33.86 21.39,33.14 Z M22.47,28.95 L21.15,33.21 Q23.14,34.84 23.39,34.85 C23.47,34.86 23.54,34.86 23.61,34.86 Q25.95,34.86 24.80,31.93 Z "
				},
				Polygon {
					points: [1.20,29.61,1.20,4.01,8.48,7.29,2.44,29.66]
					fill: bind windowColorScheme.mainColor
					stroke: null
				},
				SVGPath {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: null
					content: "M1.70,4.79 L7.89,7.57 L2.06,29.14 L1.70,29.13 Z M0.70,3.24 L0.70,30.10 L2.82,30.17 L9.08,7.01 Z "
				},
				Polygon {
					points: [32.80,6.55,39.92,3.40,39.92,25.18,38.85,26.23]
					fill: bind windowColorScheme.secondColor
					stroke: null
				},
				SVGPath {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: null
					content: "M39.42,4.17 L39.42,24.97 L39.08,25.30 L33.41,6.83 Z M40.42,2.64 L32.19,6.27 L38.61,27.16 L40.42,25.39 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M15.17,25.07 C15.54,23.99 15.27,22.82 14.56,22.46 C13.85,22.11 12.97,22.69 12.60,23.77 L11.34,27.49 C10.97,28.56 11.25,29.73 11.96,30.09 C12.66,30.45 13.54,29.86 13.91,28.78 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M18.53,26.61 C18.86,25.53 18.54,24.36 17.82,24.02 C17.10,23.68 16.25,24.29 15.92,25.38 L14.81,29.12 C14.48,30.21 14.80,31.37 15.52,31.71 C16.24,32.05 17.09,31.45 17.42,30.36 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M21.75,28.98 C22.09,27.90 21.80,26.74 21.10,26.40 C20.40,26.05 19.56,26.65 19.22,27.73 L18.20,30.91 C17.91,31.97 18.20,33.08 18.88,33.41 C19.58,33.75 20.43,33.16 20.77,32.07 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M8.95,23.23 L8.14,25.52 C7.75,26.59 8.00,27.77 8.71,28.14 C9.41,28.52 10.30,27.95 10.70,26.88 L11.51,24.55 C11.91,23.48 11.65,22.31 10.95,21.93 C10.24,21.56 9.35,22.12 8.96,23.19 C8.95,23.21 8.95,23.23 8.94,23.25 "
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
