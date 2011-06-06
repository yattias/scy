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
public class Concept_hypotheseIcon extends AbstractEloIcon {

public override function clone(): Concept_hypotheseIcon {
Concept_hypotheseIcon {
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
					content: "M39.93,16.00 C37.57,2.49 22.10,0.01 15.34,4.52 C14.38,5.16 14.07,6.45 14.18,7.68 C11.36,8.78 8.63,10.37 5.85,12.17 C1.77,14.81 0.15,19.06 0.68,25.93 C1.91,41.89 15.57,41.03 22.90,39.15 C29.52,37.45 42.47,30.49 39.93,16.00 Z M31.29,28.29 C28.57,30.83 25.27,31.85 22.21,32.75 C17.38,34.17 6.58,36.03 4.86,25.97 C3.85,20.07 6.36,18.87 9.27,16.94 C12.96,14.49 16.83,12.77 20.84,12.62 C22.25,12.57 23.00,10.68 23.00,8.95 C28.41,8.93 34.93,11.59 36.08,18.16 C36.83,22.44 33.20,26.50 31.29,28.29 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.3
					strokeLineCap: StrokeLineCap.BUTT
					content: "M36.03,24.59 C36.09,25.02 36.07,25.43 35.98,25.85 L35.84,26.37 L36.08,27.13 C36.32,28.14 36.02,29.07 35.20,29.71 C35.06,29.82 34.91,29.92 34.75,30.00 L34.72,30.25 C34.66,30.48 34.58,30.71 34.48,30.93 C34.23,31.37 33.84,31.72 33.36,31.92 L33.03,32.04 L32.69,32.12 L32.52,32.33 L32.33,32.50 C31.54,33.20 30.56,33.36 29.57,32.95 L29.33,32.85 L29.09,32.70 L29.02,32.81 C28.94,32.86 28.87,32.93 28.82,32.99 C27.67,33.84 26.29,33.96 24.97,33.39 C24.83,33.32 24.70,33.25 24.57,33.16 L24.43,33.20 L24.28,33.22 C23.38,33.41 22.47,33.25 21.70,32.77 L21.48,32.64 L21.29,32.77 C19.99,33.54 18.57,33.61 17.21,32.95 L16.92,33.06 C15.24,33.61 13.56,33.34 12.17,32.31 L11.85,32.02 L11.49,32.17 C9.94,32.67 8.52,32.30 7.46,31.12 L7.24,30.84 L7.09,30.58 L6.95,30.33 L6.85,30.27 L6.75,30.19 L6.68,30.08 C5.36,29.13 4.81,27.79 5.19,26.31 L5.36,25.96 L5.07,25.42 C4.42,24.06 4.58,22.68 5.56,21.56 L5.68,21.43 L5.51,20.85 C5.38,19.51 5.79,18.20 6.68,17.14 L6.63,16.71 C6.46,15.22 7.12,14.01 8.53,13.39 C9.14,11.89 10.39,11.16 12.05,11.40 L12.61,11.71 Q13.65,10.35 14.41,11.15 C14.89,11.67 14.82,10.54 16.95,10.54 Q18.27,10.54 19.24,11.49 L19.53,11.34 L19.80,11.23 C21.09,10.82 22.33,11.10 23.29,12.01 L23.45,12.17 L23.77,12.17 L24.07,12.19 C24.98,12.31 25.74,12.79 26.21,13.53 L26.31,13.67 L26.40,13.84 L26.82,13.92 L27.21,14.02 C28.49,14.46 29.33,15.38 29.60,16.65 L29.65,17.10 L29.92,17.20 C30.11,17.29 30.29,17.39 30.48,17.49 C31.04,17.85 31.47,18.36 31.70,18.96 L31.74,19.12 L31.77,19.31 L32.09,19.56 L32.37,19.82 C32.80,20.25 33.10,20.75 33.26,21.31 L33.33,21.62 L33.79,21.82 C34.98,22.40 35.76,23.36 36.03,24.59 L36.03,24.59 L36.03,24.59 L36.03,24.59 L36.03,24.59 L36.03,24.59 L36.03,24.59 L36.03,24.59 L36.03,24.59 L36.03,24.59 L36.03,24.59 L36.03,24.59 L36.03,24.59 L36.03,24.59 Q36.09,25.02 36.03,24.59 Q36.09,25.02 36.03,24.59 Q36.09,25.02 36.03,24.59 Q36.09,25.02 36.03,24.59 Q36.09,25.02 36.03,24.59 Q36.09,25.02 36.03,24.59 Q36.09,25.02 36.03,24.59 Q36.09,25.02 36.03,24.59 Q36.09,25.02 36.03,24.59 Q36.09,25.02 36.03,24.59 Q36.09,25.02 36.03,24.59 Q36.09,25.02 36.03,24.59 Q36.09,25.02 36.03,24.59 Q36.09,25.02 36.03,24.59 Q36.09,25.02 36.03,24.59 Q36.09,25.02 36.03,24.59 Q36.09,25.02 36.03,24.59 Q36.09,25.02 36.03,24.59 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.3
					strokeLineCap: StrokeLineCap.BUTT
					content: "M8.26,15.75 Q14.73,16.41 11.55,20.62 "
				},
				SVGPath {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.3
					strokeLineCap: StrokeLineCap.BUTT
					content: "M4.60,23.41 Q6.10,19.85 8.26,22.92 "
				},
				SVGPath {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.3
					strokeLineCap: StrokeLineCap.BUTT
					content: "M11.49,31.29 Q11.31,26.20 15.45,26.80 "
				},
				SVGPath {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.3
					strokeLineCap: StrokeLineCap.BUTT
					content: "M14.98,22.69 Q10.16,17.63 7.95,23.19 C6.13,27.79 10.23,28.28 11.19,28.23 "
				},
				SVGPath {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.3
					strokeLineCap: StrokeLineCap.BUTT
					content: "M13.71,11.53 Q18.21,17.23 13.05,18.48 "
				},
				SVGPath {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.3
					strokeLineCap: StrokeLineCap.BUTT
					content: "M15.66,16.04 Q18.50,12.71 19.40,16.19 "
				},
				SVGPath {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.3
					strokeLineCap: StrokeLineCap.BUTT
					content: "M18.44,17.77 Q22.22,13.12 24.26,15.75 "
				},
				SVGPath {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.3
					strokeLineCap: StrokeLineCap.BUTT
					content: "M23.36,17.23 Q30.12,14.30 28.75,21.17 "
				},
				Polyline {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.3
					strokeLineCap: StrokeLineCap.BUTT
					points: [16.47,22.75]
				},
				Polyline {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.3
					strokeLineCap: StrokeLineCap.BUTT
					points: [20.24,20.02]
				},
				SVGPath {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.3
					strokeLineCap: StrokeLineCap.BUTT
					content: "M14.07,23.30 Q18.15,21.77 20.42,23.90 "
				},
				SVGPath {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.3
					strokeLineCap: StrokeLineCap.BUTT
					content: "M21.16,28.33 Q21.50,31.80 25.88,31.08 "
				},
				SVGPath {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.3
					strokeLineCap: StrokeLineCap.BUTT
					content: "M15.99,24.89 Q15.15,29.49 21.08,29.43 "
				},
				SVGPath {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.3
					strokeLineCap: StrokeLineCap.BUTT
					content: "M26.06,20.95 Q22.94,21.28 24.50,23.96 "
				},
				SVGPath {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.3
					strokeLineCap: StrokeLineCap.BUTT
					content: "M34.21,24.72 Q30.07,21.11 27.62,25.98 "
				},
				SVGPath {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.3
					strokeLineCap: StrokeLineCap.BUTT
					content: "M33.79,27.24 Q30.43,25.71 27.73,30.63 "
				},
				SVGPath {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.3
					strokeLineCap: StrokeLineCap.BUTT
					content: "M18.38,32.44 Q15.75,31.73 18.44,28.72 "
				},
				SVGPath {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.3
					strokeLineCap: StrokeLineCap.BUTT
					content: "M19.47,24.38 Q27.79,18.89 28.63,28.96 "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: null
					content: "M17.06,25.07 C17.04,24.36 17.02,23.96 17.02,23.85 C17.02,21.33 18.08,19.06 20.24,16.91 C21.75,15.42 22.69,14.32 23.14,13.55 C23.59,12.74 23.82,12.04 23.82,11.39 C23.82,9.82 23.01,7.95 19.15,7.95 C17.08,7.95 15.18,8.40 13.48,9.27 L11.84,4.78 C14.50,3.55 17.61,2.93 21.09,2.93 C24.44,2.93 27.09,3.62 28.97,4.99 C30.82,6.34 31.71,8.04 31.71,10.19 C31.71,11.26 31.38,12.42 30.73,13.62 C30.07,14.85 28.78,16.37 26.91,18.14 C24.84,20.03 23.78,22.24 23.78,24.72 L23.78,25.07 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M21.09,3.43 C24.33,3.43 26.89,4.09 28.67,5.39 C30.38,6.64 31.21,8.21 31.21,10.19 C31.21,11.18 30.90,12.25 30.29,13.39 C29.65,14.57 28.40,16.04 26.58,17.76 C24.43,19.72 23.33,22.00 23.28,24.57 L17.55,24.57 C17.53,24.17 17.52,23.93 17.52,23.85 C17.52,21.47 18.53,19.32 20.60,17.27 C22.13,15.74 23.10,14.61 23.57,13.80 C24.07,12.91 24.32,12.12 24.32,11.39 C24.32,9.92 23.64,7.45 19.15,7.45 C17.20,7.45 15.40,7.83 13.76,8.58 L12.47,5.05 C14.97,3.98 17.87,3.43 21.09,3.43 M21.09,2.43 C17.34,2.43 14.05,3.13 11.22,4.52 L13.21,9.99 C14.96,8.97 16.94,8.45 19.15,8.45 C21.92,8.45 23.32,9.43 23.32,11.39 C23.32,11.95 23.11,12.59 22.70,13.30 C22.29,14.02 21.35,15.10 19.89,16.56 C17.64,18.78 16.52,21.21 16.52,23.85 C16.52,23.98 16.54,24.55 16.58,25.57 L24.28,25.57 L24.28,24.72 C24.28,22.38 25.27,20.31 27.25,18.50 C29.17,16.69 30.48,15.15 31.17,13.86 C31.87,12.58 32.21,11.35 32.21,10.19 C32.21,7.89 31.23,6.02 29.26,4.58 C27.29,3.15 24.57,2.43 21.09,2.43 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.secondColor
					stroke: null
					content: "M20.55,37.54 C19.14,37.54 18.02,37.14 17.14,36.31 C16.26,35.50 15.84,34.50 15.84,33.24 C15.84,31.97 16.26,30.96 17.14,30.16 C18.02,29.34 19.13,28.95 20.55,28.95 C21.96,28.95 23.08,29.34 23.96,30.16 C24.83,30.96 25.26,31.97 25.26,33.24 C25.26,34.51 24.84,35.52 23.98,36.33 C23.10,37.14 21.98,37.54 20.55,37.54 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.mainColor
					stroke: null
					content: "M20.55,29.45 C21.83,29.45 22.84,29.80 23.62,30.52 C24.39,31.23 24.76,32.11 24.76,33.24 C24.76,34.37 24.39,35.26 23.64,35.96 C22.86,36.68 21.85,37.04 20.55,37.04 C19.27,37.04 18.27,36.68 17.48,35.94 C16.71,35.23 16.34,34.35 16.34,33.24 C16.34,32.12 16.71,31.23 17.48,30.52 C18.26,29.80 19.27,29.45 20.55,29.45 M20.55,28.45 C19.02,28.45 17.77,28.89 16.80,29.79 C15.82,30.68 15.34,31.83 15.34,33.24 C15.34,34.63 15.82,35.78 16.80,36.68 C17.77,37.58 19.02,38.04 20.55,38.04 C22.10,38.04 23.35,37.59 24.32,36.69 C25.27,35.80 25.76,34.64 25.76,33.24 C25.76,31.83 25.27,30.68 24.30,29.79 C23.32,28.89 22.08,28.45 20.55,28.45 Z "
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
         Concept_hypotheseIcon{
            windowColorScheme: windowColorScheme
           layoutX: 25
            layoutY: 25
         }
         Concept_hypotheseIcon{
            windowColorScheme: windowColorScheme
            layoutX: 75
            layoutY: 25
            selected: true
         }
      ]
	}
}
}
