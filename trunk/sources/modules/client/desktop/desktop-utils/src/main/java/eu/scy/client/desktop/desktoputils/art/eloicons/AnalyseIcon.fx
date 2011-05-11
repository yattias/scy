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
				Polyline {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: null
					points: [3.97,26.83]
				},
				Polyline {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 2.28
					points: [1.32,6.92]
				},
				Line {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: bind windowColorScheme.secondColor
					strokeWidth: 4.0
					strokeLineCap: StrokeLineCap.BUTT
					startX: 1.42
					startY: 12.14
					endX: 5.42
					endY: 12.14
				},
				Line {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: bind windowColorScheme.secondColor
					strokeWidth: 4.0
					strokeLineCap: StrokeLineCap.BUTT
					startX: 1.42
					startY: 21.14
					endX: 5.42
					endY: 21.14
				},
				Line {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: bind windowColorScheme.secondColor
					strokeWidth: 4.0
					strokeLineCap: StrokeLineCap.BUTT
					startX: 1.42
					startY: 29.14
					endX: 5.42
					endY: 29.14
				},
				Line {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: bind windowColorScheme.secondColor
					strokeWidth: 4.0
					strokeLineCap: StrokeLineCap.BUTT
					startX: 1.42
					startY: 37.14
					endX: 5.42
					endY: 37.14
				},
				Line {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: bind windowColorScheme.secondColor
					strokeWidth: 3.0
					strokeLineCap: StrokeLineCap.BUTT
					startX: 10.42
					startY: 36.64
					endX: 31.42
					endY: 36.64
				},
				Line {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: bind windowColorScheme.secondColor
					strokeWidth: 4.0
					strokeLineCap: StrokeLineCap.BUTT
					startX: 1.42
					startY: 3.14
					endX: 5.42
					endY: 3.14
				},
				Line {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: bind windowColorScheme.secondColor
					strokeWidth: 3.0
					strokeLineCap: StrokeLineCap.BUTT
					startX: 10.42
					startY: 2.64
					endX: 31.42
					endY: 2.64
				},
				Line {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: bind windowColorScheme.secondColor
					strokeWidth: 3.0
					strokeLineCap: StrokeLineCap.BUTT
					startX: 10.42
					startY: 11.64
					endX: 40.42
					endY: 11.64
				},
				Line {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: bind windowColorScheme.secondColor
					strokeWidth: 3.0
					strokeLineCap: StrokeLineCap.BUTT
					startX: 10.42
					startY: 28.64
					endX: 28.42
					endY: 28.64
				},
				Line {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: bind windowColorScheme.secondColor
					strokeWidth: 3.0
					strokeLineCap: StrokeLineCap.BUTT
					startX: 10.42
					startY: 20.64
					endX: 40.42
					endY: 20.64
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeLineCap: StrokeLineCap.BUTT
					startX: 7.92
					startY: 40.14
					endX: 7.92
					endY: 0.14
				},
				Line {
					fill: null
					stroke: bind windowColorScheme.mainColor
					strokeLineCap: StrokeLineCap.BUTT
					startX: 0.42
					startY: 7.64
					endX: 40.42
					endY: 7.64
				},
				SVGPath {
					fill: bind windowColorScheme.mainColorLight
					stroke: null
					content: "M36.75,17.78 C33.39,14.35 31.40,10.33 27.17,8.65 C20.17,3.14 9.98,5.26 4.32,12.51 C-1.82,20.36 4.94,31.46 13.27,34.21 C18.92,36.08 25.60,36.46 31.49,36.13 C35.16,35.92 39.07,34.00 39.96,30.09 C41.07,25.21 40.25,21.35 36.75,17.78 Z M17.90,32.35 C9.98,31.30 1.13,22.90 6.43,14.52 C10.11,8.69 17.61,7.15 23.34,9.77 C23.42,10.32 23.77,10.82 24.42,10.92 C24.93,11.01 25.51,11.21 26.12,11.52 C26.69,11.98 27.24,12.49 27.72,13.07 C28.16,13.59 28.64,13.63 29.04,13.43 C33.02,16.57 37.31,21.89 37.75,25.15 C39.25,36.23 24.08,33.18 17.90,32.35 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.5
					strokeLineCap: StrokeLineCap.BUTT
					content: "M37.87,23.70 C37.93,24.12 37.91,24.54 37.82,24.95 L37.67,25.46 L37.92,26.22 C38.16,27.21 37.86,28.13 37.03,28.76 C36.89,28.88 36.73,28.97 36.56,29.05 L36.53,29.30 C36.48,29.53 36.40,29.75 36.29,29.97 C36.04,30.41 35.64,30.75 35.15,30.94 L34.82,31.07 L34.47,31.15 L34.30,31.36 L34.11,31.52 C33.30,32.21 32.31,32.37 31.30,31.97 L31.05,31.87 L30.80,31.72 L30.73,31.82 C30.66,31.88 30.59,31.94 30.53,32.01 C29.37,32.84 27.96,32.97 26.61,32.40 C26.47,32.33 26.34,32.26 26.21,32.17 L26.06,32.21 L25.92,32.23 C25.00,32.42 24.07,32.26 23.28,31.79 L23.06,31.66 L22.87,31.79 C21.55,32.55 20.10,32.62 18.72,31.97 L18.42,32.07 C16.72,32.62 15.01,32.36 13.58,31.33 L13.26,31.05 L12.89,31.19 C11.32,31.69 9.87,31.32 8.80,30.16 L8.58,29.89 L8.42,29.62 L8.27,29.38 L8.18,29.31 L8.08,29.23 L8.01,29.13 C6.66,28.19 6.10,26.87 6.49,25.40 L6.66,25.05 L6.36,24.52 C5.70,23.18 5.86,21.81 6.86,20.70 L6.98,20.58 L6.82,20.01 C6.67,18.67 7.09,17.38 8.01,16.34 L7.96,15.91 C7.78,14.43 8.45,13.24 9.89,12.62 C10.51,11.14 11.78,10.42 13.46,10.66 L14.04,10.96 Q15.09,9.61 15.87,10.41 C16.36,10.92 16.29,9.80 18.45,9.80 Q19.80,9.81 20.78,10.74 L21.08,10.59 L21.35,10.49 C22.66,10.09 23.93,10.36 24.90,11.25 L25.07,11.41 L25.39,11.41 L25.70,11.44 C26.63,11.56 27.40,12.03 27.88,12.76 L27.97,12.90 L28.07,13.07 L28.49,13.15 L28.89,13.25 C30.19,13.68 31.05,14.59 31.32,15.85 L31.37,16.29 L31.65,16.40 C31.84,16.48 32.03,16.58 32.22,16.68 C32.79,17.03 33.22,17.53 33.46,18.13 L33.51,18.29 L33.54,18.48 L33.86,18.72 L34.15,18.99 C34.58,19.40 34.88,19.90 35.05,20.46 L35.12,20.76 L35.59,20.96 C36.80,21.53 37.60,22.49 37.87,23.70 L37.87,23.70 L37.87,23.70 L37.87,23.70 L37.87,23.70 L37.87,23.70 L37.87,23.70 L37.87,23.70 L37.87,23.70 L37.87,23.70 L37.87,23.70 L37.87,23.70 L37.87,23.70 L37.87,23.70 Q37.93,24.12 37.87,23.70 Q37.93,24.12 37.87,23.70 Q37.93,24.12 37.87,23.70 Q37.93,24.12 37.87,23.70 Q37.93,24.12 37.87,23.70 Q37.93,24.12 37.87,23.70 Q37.93,24.12 37.87,23.70 Q37.93,24.12 37.87,23.70 Q37.93,24.12 37.87,23.70 Q37.93,24.12 37.87,23.70 Q37.93,24.12 37.87,23.70 Q37.93,24.12 37.87,23.70 Q37.93,24.12 37.87,23.70 Q37.93,24.12 37.87,23.70 Q37.93,24.12 37.87,23.70 Q37.93,24.12 37.87,23.70 Q37.93,24.12 37.87,23.70 Q37.93,24.12 37.87,23.70 Z "
				},
				SVGPath {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.5
					strokeLineCap: StrokeLineCap.BUTT
					content: "M9.61,14.95 Q16.19,15.61 12.96,19.77 "
				},
				SVGPath {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.5
					strokeLineCap: StrokeLineCap.BUTT
					content: "M5.88,22.53 Q7.41,19.02 9.61,22.05 "
				},
				SVGPath {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.5
					strokeLineCap: StrokeLineCap.BUTT
					content: "M12.90,30.33 Q12.71,25.29 16.92,25.89 "
				},
				SVGPath {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.5
					strokeLineCap: StrokeLineCap.BUTT
					content: "M16.45,21.82 Q11.54,16.82 9.30,22.32 C7.44,26.86 11.62,27.35 12.59,27.30 "
				},
				SVGPath {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.5
					strokeLineCap: StrokeLineCap.BUTT
					content: "M15.15,10.79 Q19.73,16.42 14.48,17.66 "
				},
				SVGPath {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.5
					strokeLineCap: StrokeLineCap.BUTT
					content: "M17.14,15.24 Q20.03,11.95 20.95,15.39 "
				},
				SVGPath {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.5
					strokeLineCap: StrokeLineCap.BUTT
					content: "M19.97,16.96 Q23.82,12.36 25.89,14.95 "
				},
				SVGPath {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.5
					strokeLineCap: StrokeLineCap.BUTT
					content: "M24.97,16.42 Q31.86,13.53 30.46,20.31 "
				},
				Polyline {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.5
					strokeLineCap: StrokeLineCap.BUTT
					points: [17.96,21.88]
				},
				Polyline {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.5
					strokeLineCap: StrokeLineCap.BUTT
					points: [21.80,19.18]
				},
				SVGPath {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.5
					strokeLineCap: StrokeLineCap.BUTT
					content: "M15.52,22.43 Q19.67,20.91 21.99,23.02 "
				},
				SVGPath {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.5
					strokeLineCap: StrokeLineCap.BUTT
					content: "M22.73,27.40 Q23.09,30.83 27.54,30.12 "
				},
				SVGPath {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.5
					strokeLineCap: StrokeLineCap.BUTT
					content: "M17.47,23.99 Q16.62,28.54 22.66,28.49 "
				},
				SVGPath {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.5
					strokeLineCap: StrokeLineCap.BUTT
					content: "M27.72,20.10 Q24.55,20.42 26.13,23.07 "
				},
				SVGPath {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.5
					strokeLineCap: StrokeLineCap.BUTT
					content: "M36.02,23.83 Q31.81,20.26 29.31,25.08 "
				},
				SVGPath {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.5
					strokeLineCap: StrokeLineCap.BUTT
					content: "M35.59,26.32 Q32.17,24.81 29.43,29.68 "
				},
				SVGPath {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.5
					strokeLineCap: StrokeLineCap.BUTT
					content: "M19.91,31.46 Q17.23,30.76 19.97,27.78 "
				},
				SVGPath {
					fill: bind windowColorScheme.emptyBackgroundColor
					stroke: bind windowColorScheme.mainColor
					strokeWidth: 1.5
					strokeLineCap: StrokeLineCap.BUTT
					content: "M21.01,23.49 Q29.49,18.06 30.34,28.02 "
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
