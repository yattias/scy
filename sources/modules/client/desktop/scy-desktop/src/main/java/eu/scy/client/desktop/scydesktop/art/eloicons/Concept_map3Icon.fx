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
public class Concept_map3Icon extends AbstractEloIcon {

public override function clone(): Concept_map3Icon {
Concept_map3Icon {
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
					stroke: bind windowColorScheme.mainColorLight
					strokeWidth: 0.98
					strokeLineCap: StrokeLineCap.BUTT
					content: "M38.63,20.53 C39.47,16.68 40.54,12.30 39.51,8.37 C38.10,2.98 33.29,4.11 29.14,3.82 C25.55,3.57 21.75,2.02 18.17,1.43 C13.98,0.74 9.74,0.54 5.51,0.51 C3.36,0.49 2.32,2.58 2.58,4.30 C2.53,4.45 2.48,4.61 2.46,4.78 C1.34,11.83 1.40,19.15 0.93,26.28 C0.59,31.59 3.37,35.47 8.16,36.66 C10.88,37.33 13.73,37.38 16.50,37.68 C19.75,38.03 22.92,38.92 26.15,39.46 C30.62,40.20 36.36,38.92 37.29,33.38 C37.69,31.02 37.15,28.71 37.42,26.36 C37.64,24.40 38.22,22.46 38.63,20.53 Z M33.57,17.30 C33.05,20.16 32.25,22.96 31.80,25.84 C31.65,26.82 31.72,27.84 31.79,28.82 C32.06,32.81 30.23,33.91 26.64,33.49 C21.81,32.93 17.12,31.68 12.24,31.27 C10.57,31.13 7.89,31.09 6.95,29.28 C5.84,27.12 6.89,23.44 7.13,21.14 C7.66,16.28 7.31,11.42 7.91,6.55 C11.71,6.68 15.48,7.03 19.25,7.65 C21.92,8.09 24.46,9.02 27.11,9.50 C27.95,9.66 33.66,9.34 34.03,9.73 C35.19,10.96 33.83,15.87 33.57,17.30 Z "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: null
					content: "M33.84,18.23 C33.30,21.21 32.48,24.12 32.02,27.11 C31.86,28.14 31.94,29.19 32.00,30.22 C32.28,34.37 30.41,35.51 26.73,35.08 C21.78,34.49 16.97,33.19 11.97,32.76 C10.26,32.62 7.51,32.58 6.55,30.69 C5.41,28.45 6.48,24.62 6.74,22.23 C7.28,17.17 6.92,12.12 7.54,7.04 C11.43,7.19 15.29,7.55 19.16,8.19 C21.89,8.65 24.50,9.61 27.21,10.12 C28.07,10.28 33.93,9.95 34.31,10.36 C35.50,11.64 34.10,16.74 33.84,18.23 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: bind windowColorScheme.mainColor
					strokeLineCap: StrokeLineCap.BUTT
					content: "M24.92,12.17 C23.92,12.17 21.26,12.14 19.42,11.86 C18.79,11.76 18.50,12.72 19.14,12.82 C21.06,13.12 23.92,13.17 24.92,13.17 C25.92,13.17 25.92,12.17 24.92,12.17 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: bind windowColorScheme.mainColor
					strokeLineCap: StrokeLineCap.BUTT
					content: "M27.23,12.18 C27.11,10.82 25.96,10.00 24.80,9.52 C24.20,9.28 23.94,10.25 24.53,10.49 C26.82,11.43 26.45,13.43 24.97,14.99 C24.53,15.45 25.24,16.16 25.68,15.69 C26.60,14.73 27.35,13.56 27.23,12.18 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: bind windowColorScheme.mainColor
					strokeLineCap: StrokeLineCap.BUTT
					content: "M13.00,31.84 C10.49,32.02 8.06,31.26 7.12,28.73 C6.30,26.52 7.39,24.17 8.87,22.53 C9.30,23.72 9.99,24.80 10.56,25.93 C10.86,26.50 11.72,25.99 11.43,25.42 C10.89,24.36 10.21,23.34 9.81,22.21 C9.70,21.89 9.38,21.81 9.13,21.90 C9.08,21.69 8.92,21.50 8.66,21.51 C8.18,21.51 7.73,21.53 7.28,21.60 C7.14,21.48 6.94,21.45 6.74,21.57 C6.59,21.68 6.42,21.76 6.25,21.84 C6.23,21.85 6.21,21.85 6.20,21.86 C6.12,21.89 6.05,21.93 6.00,21.98 C5.21,22.35 4.36,22.58 3.53,22.86 C2.92,23.06 3.18,24.02 3.80,23.82 C4.75,23.51 5.73,23.22 6.63,22.77 C6.93,22.68 7.24,22.61 7.54,22.57 C6.08,24.53 5.39,26.95 6.29,29.36 C7.32,32.12 10.31,33.03 13.00,32.84 C13.63,32.79 13.64,31.79 13.00,31.84 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: bind windowColorScheme.mainColor
					strokeLineCap: StrokeLineCap.BUTT
					content: "M18.14,4.78 C14.91,3.26 10.50,3.82 7.03,3.84 C6.85,3.80 6.66,3.86 6.56,4.09 C4.50,8.86 4.18,14.20 4.16,19.34 C4.16,19.54 4.31,19.79 4.53,19.82 C7.88,20.32 11.50,20.41 14.85,19.88 C15.93,19.71 17.45,19.14 18.08,18.20 C19.00,16.83 18.22,14.97 18.56,13.43 C19.13,10.91 21.38,6.31 18.14,4.78 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: bind windowColorScheme.mainColor
					strokeLineCap: StrokeLineCap.BUTT
					content: "M17.84,12.23 C17.38,13.93 17.77,15.59 17.46,17.26 C16.90,20.20 8.44,19.16 6.54,19.04 C5.51,18.97 5.16,19.14 5.17,17.98 C5.19,16.73 5.26,15.48 5.34,14.23 C5.56,11.03 6.09,7.80 7.33,4.83 C10.75,4.79 14.87,4.34 18.00,5.81 C19.86,6.69 18.23,10.78 17.84,12.23 Z "
				},
				Rectangle {
					transforms: [Transform.rotate( 4.90, 32.75, 11.21)]
					fill: bind windowColorScheme.secondColor
					stroke: null
					x: 29.53
					y: 6.29
					width: 6.45
					height: 9.85
				},
				SVGPath {
					fill: Color.rgb(0x6a,0xb3,0x30)
					stroke: bind windowColorScheme.mainColor
					strokeLineCap: StrokeLineCap.BUTT
					content: "M36.46,6.52 C33.91,6.11 31.45,5.29 28.86,5.86 C28.23,6.00 28.50,6.96 29.13,6.82 C30.65,6.49 32.04,6.68 33.55,6.97 C34.83,7.22 35.81,7.03 35.72,8.54 C35.57,10.97 35.71,13.40 35.25,15.80 C33.31,15.56 31.36,15.56 29.41,15.80 C28.93,13.01 29.45,10.15 29.50,7.34 C29.50,6.69 28.50,6.69 28.50,7.34 C28.45,10.39 27.88,13.45 28.51,16.47 C28.57,16.76 28.87,16.86 29.13,16.82 C29.98,16.70 30.83,16.64 31.69,16.61 C28.97,18.42 26.23,20.21 23.14,21.31 C23.10,20.88 22.99,20.47 22.76,20.09 C22.51,19.67 21.82,19.83 21.83,20.34 C21.84,21.02 21.92,21.50 22.18,22.14 C22.28,22.38 22.51,22.58 22.80,22.49 C23.04,22.41 23.29,22.32 23.53,22.23 C23.47,22.51 23.62,22.84 24.00,22.84 C25.08,22.84 26.13,22.90 27.20,23.16 C27.82,23.31 28.09,22.34 27.46,22.19 C26.48,21.96 25.51,21.87 24.51,21.85 C27.26,20.71 29.76,19.09 32.25,17.44 C32.59,17.21 32.51,16.78 32.27,16.59 C33.36,16.59 34.45,16.66 35.53,16.82 C35.79,16.86 36.08,16.76 36.14,16.47 C36.82,13.37 36.56,10.16 36.83,7.01 C36.85,6.80 36.66,6.56 36.46,6.52 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: null
					content: "M33.50,34.17 C33.50,35.28 32.60,36.17 31.50,36.17 L18.12,36.17 C17.02,36.17 16.12,35.28 16.12,34.17 L16.12,27.55 C16.12,26.44 17.02,25.55 18.12,25.55 L31.50,25.55 C32.60,25.55 33.50,26.44 33.50,27.55 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: bind windowColorScheme.mainColor
					strokeLineCap: StrokeLineCap.BUTT
					content: "M17.48,36.54 C16.32,32.61 16.96,28.47 15.81,24.54 C15.63,23.92 14.66,24.19 14.85,24.81 C16.00,28.73 15.36,32.88 16.51,36.81 C16.69,37.42 17.66,37.16 17.48,36.54 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: bind windowColorScheme.mainColor
					strokeLineCap: StrokeLineCap.BUTT
					content: "M30.52,24.49 C28.13,23.91 25.40,24.28 22.99,24.46 C20.83,24.63 18.67,24.85 16.53,25.19 C15.89,25.29 16.16,26.26 16.80,26.16 C19.03,25.80 21.30,25.59 23.55,25.42 C25.28,25.29 30.13,24.41 31.31,26.09 C32.87,28.30 32.47,32.43 32.49,35.01 C32.50,35.85 26.54,35.59 25.53,35.59 C23.24,35.59 21.05,35.83 18.86,36.52 C18.25,36.72 18.51,37.68 19.13,37.49 C23.70,36.05 28.47,37.01 33.13,36.16 C33.34,36.12 33.50,35.88 33.50,35.67 C33.49,32.83 34.26,25.40 30.52,24.49 Z "
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
         Concept_map3Icon{
            windowColorScheme: windowColorScheme
           layoutX: 25
            layoutY: 25
         }
         Concept_map3Icon{
            windowColorScheme: windowColorScheme
            layoutX: 75
            layoutY: 25
            selected: true
         }
      ]
	}
}
}
