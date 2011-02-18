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
public class AnalyseIcon extends AbstractEloIcon {

public override function clone(): AnalyseIcon {
AnalyseIcon {
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
					content: "M35.93,22.95 C33.94,20.45 33.28,19.53 33.73,16.13 C34.10,13.32 34.12,10.54 33.76,7.74 C33.23,3.55 31.28,2.43 27.41,2.85 C19.81,3.68 11.71,3.13 4.06,3.15 C3.39,3.15 3.06,3.76 3.10,4.30 C2.70,4.43 2.35,4.79 2.39,5.36 C2.56,8.17 2.96,10.70 2.55,13.54 C2.27,15.53 1.54,18.29 2.15,20.24 C2.94,22.75 3.82,23.38 5.39,23.51 C3.94,24.74 2.84,26.33 3.41,28.34 C4.66,32.73 8.13,35.24 12.26,36.88 C16.92,38.74 22.26,38.24 27.13,37.82 C31.80,37.41 36.26,36.51 39.12,32.53 C41.53,29.16 37.84,25.33 35.93,22.95 Z M26.80,35.85 C23.49,36.17 20.22,36.31 16.91,35.96 C15.04,35.76 13.26,35.18 11.53,34.49 C7.52,32.90 3.62,26.59 9.01,23.43 C9.72,23.01 9.70,21.52 8.69,21.52 C4.54,21.55 3.41,21.43 4.09,16.57 C4.65,12.52 4.44,9.10 4.21,5.13 C11.35,5.11 18.50,5.04 25.64,4.93 C37.40,4.74 29.18,15.80 31.96,21.36 C32.92,23.27 35.25,25.10 36.61,26.84 C42.20,34.01 29.74,35.56 26.80,35.85 Z "
				},
				Polyline {
					fill: Color.WHITE
					stroke: null
					points: [4.94,15.64]
				},
				Polyline {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.04
					points: [3.64,7.27]
				},
				Polyline {
					fill: Color.WHITE
					stroke: bind windowColorScheme.secondColor
					strokeWidth: 0.95
					points: [3.64,7.92,3.64,4.91,31.51,4.91,31.51,7.92]
				},
				Rectangle {
					fill: Color.WHITE
					stroke: bind windowColorScheme.secondColor
					strokeWidth: 1.04
					x: 3.68
					y: 8.39
					width: 27.8
					height: 13.07
				},
				Line {
					fill: Color.WHITE
					stroke: bind windowColorScheme.secondColor
					strokeWidth: 0.67
					startX: 8.76
					startY: 5.23
					endX: 8.76
					endY: 21.18
				},
				Line {
					fill: Color.WHITE
					stroke: bind windowColorScheme.secondColor
					strokeWidth: 0.69
					startX: 25.08
					startY: 15.72
					endX: 25.08
					endY: 20.8
				},
				Line {
					fill: Color.WHITE
					stroke: bind windowColorScheme.secondColor
					strokeWidth: 0.7
					startX: 4.12
					startY: 15.48
					endX: 30.36
					endY: 15.48
				},
				Line {
					fill: Color.WHITE
					stroke: bind windowColorScheme.secondColor
					strokeWidth: 0.69
					startX: 4.43
					startY: 18.54
					endX: 30.37
					endY: 18.54
				},
				Line {
					fill: Color.WHITE
					stroke: bind windowColorScheme.secondColor
					strokeLineCap: StrokeLineCap.BUTT
					startX: 5.42
					startY: 6.64
					endX: 7.42
					endY: 6.64
				},
				Line {
					fill: Color.WHITE
					stroke: bind windowColorScheme.secondColor
					strokeLineCap: StrokeLineCap.BUTT
					startX: 5.42
					startY: 10.64
					endX: 7.42
					endY: 10.64
				},
				Line {
					fill: Color.WHITE
					stroke: bind windowColorScheme.secondColor
					strokeLineCap: StrokeLineCap.BUTT
					startX: 5.42
					startY: 13.64
					endX: 7.42
					endY: 13.64
				},
				Line {
					fill: Color.WHITE
					stroke: bind windowColorScheme.secondColor
					strokeLineCap: StrokeLineCap.BUTT
					startX: 5.42
					startY: 16.64
					endX: 7.42
					endY: 16.64
				},
				Line {
					fill: Color.WHITE
					stroke: bind windowColorScheme.secondColor
					strokeLineCap: StrokeLineCap.BUTT
					startX: 5.42
					startY: 19.64
					endX: 7.42
					endY: 19.64
				},
				Line {
					fill: Color.WHITE
					stroke: bind windowColorScheme.secondColor
					strokeLineCap: StrokeLineCap.BUTT
					startX: 9.42
					startY: 6.64
					endX: 24.42
					endY: 6.64
				},
				Line {
					fill: Color.WHITE
					stroke: bind windowColorScheme.secondColor
					strokeLineCap: StrokeLineCap.BUTT
					startX: 9.42
					startY: 19.64
					endX: 24.42
					endY: 19.64
				},
				Line {
					fill: Color.WHITE
					stroke: bind windowColorScheme.secondColor
					strokeLineCap: StrokeLineCap.BUTT
					startX: 9.42
					startY: 10.64
					endX: 29.42
					endY: 10.64
				},
				Line {
					fill: Color.WHITE
					stroke: bind windowColorScheme.secondColor
					strokeLineCap: StrokeLineCap.BUTT
					startX: 9.42
					startY: 16.64
					endX: 21.42
					endY: 16.64
				},
				Line {
					fill: Color.WHITE
					stroke: bind windowColorScheme.secondColor
					strokeLineCap: StrokeLineCap.BUTT
					startX: 9.42
					startY: 13.64
					endX: 29.42
					endY: 13.64
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.19
					strokeLineCap: StrokeLineCap.BUTT
					content: "M37.02,28.42 C37.07,28.80 37.05,29.19 36.97,29.57 L36.84,30.04 L37.06,30.74 C37.28,31.66 37.00,32.52 36.26,33.10 C36.13,33.21 35.99,33.30 35.84,33.37 L35.81,33.60 C35.76,33.81 35.69,34.02 35.59,34.22 C35.37,34.62 35.00,34.94 34.56,35.12 L34.27,35.24 L33.96,35.32 L33.80,35.51 L33.63,35.66 C32.90,36.30 32.01,36.44 31.10,36.07 L30.88,35.98 L30.65,35.84 L30.59,35.94 C30.52,35.99 30.46,36.05 30.41,36.11 C29.36,36.88 28.09,37.00 26.88,36.47 C26.75,36.41 26.63,36.34 26.52,36.26 L26.39,36.30 L26.25,36.32 C25.43,36.49 24.59,36.34 23.88,35.90 L23.69,35.79 L23.51,35.90 C22.32,36.61 21.02,36.67 19.77,36.07 L19.51,36.17 C17.97,36.68 16.44,36.43 15.15,35.48 L14.86,35.22 L14.53,35.35 C13.12,35.82 11.81,35.47 10.84,34.39 L10.65,34.14 L10.51,33.90 L10.38,33.67 L10.29,33.61 L10.20,33.54 L10.13,33.44 C8.92,32.57 8.42,31.35 8.76,29.99 L8.92,29.67 L8.66,29.17 C8.06,27.93 8.20,26.67 9.11,25.64 L9.21,25.52 L9.06,24.99 C8.94,23.76 9.31,22.56 10.13,21.59 L10.09,21.20 C9.93,19.83 10.54,18.73 11.83,18.15 C12.38,16.78 13.53,16.12 15.05,16.34 L15.57,16.62 Q16.51,15.37 17.21,16.11 C17.65,16.58 17.59,15.54 19.53,15.54 Q20.75,15.55 21.63,16.41 L21.90,16.28 L22.14,16.18 C23.33,15.81 24.46,16.06 25.34,16.89 L25.49,17.03 L25.78,17.03 L26.05,17.06 C26.89,17.17 27.59,17.60 28.02,18.28 L28.11,18.42 L28.19,18.57 L28.57,18.64 L28.93,18.74 C30.10,19.13 30.87,19.98 31.12,21.14 L31.17,21.56 L31.41,21.65 C31.59,21.73 31.76,21.82 31.93,21.91 C32.45,22.24 32.83,22.70 33.04,23.26 L33.09,23.41 L33.11,23.58 L33.40,23.81 L33.66,24.05 C34.05,24.44 34.33,24.90 34.47,25.41 L34.54,25.70 L34.96,25.88 C36.05,26.40 36.77,27.29 37.02,28.42 L37.02,28.42 L37.02,28.42 L37.02,28.42 L37.02,28.42 L37.02,28.42 L37.02,28.42 L37.02,28.42 L37.02,28.42 L37.02,28.42 L37.02,28.42 L37.02,28.42 L37.02,28.42 L37.02,28.42 Q37.07,28.80 37.02,28.42 Q37.07,28.80 37.02,28.42 Q37.07,28.80 37.02,28.42 Q37.07,28.80 37.02,28.42 Q37.07,28.80 37.02,28.42 Q37.07,28.80 37.02,28.42 Q37.07,28.80 37.02,28.42 Q37.07,28.80 37.02,28.42 Q37.07,28.80 37.02,28.42 Q37.07,28.80 37.02,28.42 Q37.07,28.80 37.02,28.42 Q37.07,28.80 37.02,28.42 Q37.07,28.80 37.02,28.42 Q37.07,28.80 37.02,28.42 Q37.07,28.80 37.02,28.42 Q37.07,28.80 37.02,28.42 Q37.07,28.80 37.02,28.42 Q37.07,28.80 37.02,28.42 Z "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.19
					strokeLineCap: StrokeLineCap.BUTT
					content: "M11.57,20.31 Q17.50,20.92 14.59,24.78 "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.19
					strokeLineCap: StrokeLineCap.BUTT
					content: "M8.22,27.33 Q9.60,24.08 11.57,26.88 "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.19
					strokeLineCap: StrokeLineCap.BUTT
					content: "M14.54,34.55 Q14.37,29.89 18.16,30.44 "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.19
					strokeLineCap: StrokeLineCap.BUTT
					content: "M17.73,26.68 Q13.32,22.04 11.30,27.13 C9.63,31.34 13.38,31.80 14.26,31.75 "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.19
					strokeLineCap: StrokeLineCap.BUTT
					content: "M16.57,16.46 Q20.69,21.67 15.96,22.82 "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.19
					strokeLineCap: StrokeLineCap.BUTT
					content: "M18.35,20.58 Q20.96,17.53 21.79,20.72 "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.19
					strokeLineCap: StrokeLineCap.BUTT
					content: "M20.90,22.17 Q24.37,17.91 26.23,20.31 "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.19
					strokeLineCap: StrokeLineCap.BUTT
					content: "M25.41,21.67 Q31.60,18.99 30.35,25.28 "
				},
				Polyline {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.19
					strokeLineCap: StrokeLineCap.BUTT
					points: [19.09,26.73]
				},
				Polyline {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.19
					strokeLineCap: StrokeLineCap.BUTT
					points: [22.55,24.23]
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.19
					strokeLineCap: StrokeLineCap.BUTT
					content: "M16.90,27.23 Q20.63,25.83 22.72,27.78 "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.19
					strokeLineCap: StrokeLineCap.BUTT
					content: "M23.39,31.84 Q23.71,35.01 27.72,34.36 "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.19
					strokeLineCap: StrokeLineCap.BUTT
					content: "M18.66,28.69 Q17.89,32.90 23.32,32.85 "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.19
					strokeLineCap: StrokeLineCap.BUTT
					content: "M27.88,25.08 Q25.02,25.38 26.45,27.83 "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.19
					strokeLineCap: StrokeLineCap.BUTT
					content: "M35.35,28.54 Q31.56,25.23 29.31,29.69 "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.19
					strokeLineCap: StrokeLineCap.BUTT
					content: "M34.96,30.84 Q31.89,29.44 29.41,33.95 "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.19
					strokeLineCap: StrokeLineCap.BUTT
					content: "M20.85,35.60 Q18.43,34.95 20.90,32.20 "
				},
				SVGPath {
					fill: Color.WHITE
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.19
					strokeLineCap: StrokeLineCap.BUTT
					content: "M21.84,28.22 Q29.47,23.20 30.23,32.42 "
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
         AnalyseIcon{
            windowColorScheme: windowColorScheme
           layoutX: 25
            layoutY: 25
         }
         AnalyseIcon{
            windowColorScheme: windowColorScheme
            layoutX: 75
            layoutY: 25
            selected: true
         }
      ]
	}
}
}
